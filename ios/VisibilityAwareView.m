//
//  VisibilityAwareView.m
//  react-native-visibility-aware-view
//
//  Created by Christian Schaffrath on 09.05.23.
//

#import "VisibilityAwareView.h"
#import "Utils.h"

NSTimeInterval VISIBILITY_TIMER_ACCURACY = 250;

@interface VisibilityAwareView()

@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic) BOOL appInBackground;

@end

@implementation VisibilityAwareView
{
    BOOL _wasDisplayedOnce;
    BOOL _isViewVisible;
    BOOL _ignoreAppState;
    NSTimeInterval _timer_accuracy;
    CGFloat _minVisibileArea;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        _timer_accuracy = VISIBILITY_TIMER_ACCURACY;
        _isViewVisible = NO;
        _wasDisplayedOnce = NO;

        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(willResignActive) name: UIApplicationWillResignActiveNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(willBecomeActive) name: UIApplicationDidBecomeActiveNotification object:nil];
        
        [self addObserver:self forKeyPath:@"frame" options:0 context:@selector(onUpdatePositionOrSize)];
        [self addObserver:self forKeyPath:@"bounds" options:0 context:@selector(onUpdatePositionOrSize)];
        
        _ignoreAppState = NO;
        _appInBackground = NO;
        _isViewVisible = NO;
        _minVisibileArea = 0.01;
        
        [self updateVisibilityIfNeeded];
    }
    return self;
}

- (void) start {
    [self startIntervalTrackingIfOnWindow];
}

- (void) stop {
    [self stopIntervalForVisibilityCheck];
}


- (void) setAccuracy:(NSNumber *) accuracy {
    if(accuracy != nil){
        if([accuracy longValue]>0){
            _timer_accuracy = [accuracy longValue];
            [self stopIntervalForVisibilityCheck];
            [self startIntervalTrackingIfOnWindow];
        }
    }
}

- (void) setMinVisibleArea:(NSNumber *) minVisibleArea {
    if ([minVisibleArea floatValue] != _minVisibileArea){
        _minVisibileArea = [minVisibleArea floatValue];
//        [self updateVisibilityIfNeeded];
    }
}


- (void) setIgnoreAppState:(BOOL) ignoreAppState {
    if(ignoreAppState != _ignoreAppState){
        _ignoreAppState = ignoreAppState;
//        [self updateVisibilityIfNeeded];
    }
}

- (NSTimeInterval) getTimerAccuracy {
    if(_timer_accuracy > 0) {
        return _timer_accuracy / 1000;
    }
    return VISIBILITY_TIMER_ACCURACY;
}

- (void)startIntervalForVisibilityCheck {
    if (self.timer != nil){
        [self.timer invalidate];
    }
    self.timer = [NSTimer scheduledTimerWithTimeInterval:[self getTimerAccuracy] target:self selector: @selector(updateVisibilityIfNeeded) userInfo:nil repeats:YES];
}

- (void) startIntervalTrackingIfNotRunningAlready {
    if (!self.timer || ![self.timer isValid]) {
        [self startIntervalForVisibilityCheck];
    }
}

- (void) startIntervalTrackingIfOnWindow {
    if (self.window != nil){
        [self startIntervalTrackingIfNotRunningAlready];
    }
}

- (void)stopIntervalForVisibilityCheck {
    if(self.timer != nil){
        [self.timer invalidate];
    }
    self.timer = nil;
}

- (BOOL) isViewVisible {
    return _isViewVisible;
}


- (BOOL) isOnWindow {
    CGRect bounds = self.bounds;
    CGRect frameInWindow = [self convertRect: bounds toView:nil];
    CGRect frameOnScreen = [self.window convertRect:frameInWindow toWindow:UIApplication.sharedApplication.keyWindow];
    
    BOOL visible = [Utils intersects:frameOnScreen andRect:self.window.frame minPercentage:_minVisibileArea];
//    BOOL visible = CGRectIntersectsRect(frameOnScreen, self.window.frame);
    return visible;
}

- (BOOL) isViewFrameVisibleToUser {
    if (self.window == nil)
        return NO;
    
    BOOL isOnWindow = [self isOnWindow];
    if (!isOnWindow){
        return NO;
    }
    
    BOOL visible = [Utils isVisible:self inView:self.superview minPercentage:_minVisibileArea];
    if (!visible) {
        return NO;
    }
    
    return YES;
}


//// Only override drawRect: if you perform custom drawing.
//// An empty implementation adversely affects performance during animation.
//- (void)drawRect:(CGRect)rect {
//    // Drawing code
//    [super drawRect:rect];
//    [self checkVisibility];
//}
//
//-(void)layoutSubviews {
//    [super layoutSubviews];
//    [self checkVisibility];
//}
#pragma mark private state changer
- (void) updateVisibility: (BOOL) newVisibility withFrameVisible: (BOOL) frameVisible {
    BOOL oldVisibility = self.isViewVisible;
    if (oldVisibility != newVisibility) {
        _isViewVisible = newVisibility;
        if(self.isViewVisible){
            _wasDisplayedOnce = YES;
            
            [self startIntervalTrackingIfNotRunningAlready];
            
            if(self.onBecomeVisible){
                self.onBecomeVisible(@{
                    @"app_open": @(!self.appInBackground),
                    @"frame_visible": @(frameVisible)
                });
            }
        }else{
            if(self.onBecomeInvisible){
                self.onBecomeInvisible(@{
                    @"app_closed": @(self.appInBackground),
                    @"frame_visible": @(frameVisible)
                });
            }
        }
    }
}


- (void) updateVisibilityIfNeeded {
    BOOL visibleToUser =  [self isViewFrameVisibleToUser];
    BOOL nextState;
    if(_ignoreAppState){
        nextState = visibleToUser;
    }else{
        nextState = !self.appInBackground && visibleToUser;
    }
     
    if (self.isViewVisible != nextState) {
        [self updateVisibility:nextState withFrameVisible:visibleToUser];
    }
}

#pragma mark Visibility state changers
//- (void)willMoveToWindow:(UIWindow *)newWindow {
//    [super willMoveToWindow:newWindow];
//
////    [self checkVisibility];
//    [self updateVisibilityIfNeeded];
//}

//- (void)willMoveToSuperview:(UIView *)newSuperview {
//    [super willMoveToSuperview:newSuperview];

////    [self checkVisibility];
//    [self updateVisibilityIfNeeded];
//}

- (void)didMoveToSuperview {
    [super didMoveToSuperview];

    [self updateVisibilityIfNeeded];
    if (self.window == nil){
        [self stopIntervalForVisibilityCheck];
    }else{
        [self startIntervalTrackingIfOnWindow];
    }
}

- (void)didMoveToWindow {
    [super didMoveToWindow];

    [self updateVisibilityIfNeeded];
    if (self.window == nil){
        [self stopIntervalForVisibilityCheck];
    }else{
        [self startIntervalTrackingIfOnWindow];
    }
}

- (void) willResignActive {
    self.appInBackground = YES;
    [self stopIntervalForVisibilityCheck];
    [self updateVisibilityIfNeeded];
}

- (void) willBecomeActive {
    self.appInBackground = NO;
    [self startIntervalTrackingIfNotRunningAlready];
    [self updateVisibilityIfNeeded];
}

- (void) onUpdatePositionOrSize {
    [self updateVisibilityIfNeeded];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context{
    // make sure we don't interfere with other observed props
    // and check the context param
    if (context == @selector(onUpdatePositionOrSize)) {
        [self onUpdatePositionOrSize];
    }
}

@end
