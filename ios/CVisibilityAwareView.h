//
//  CVisibilityAwareView.h
//  react-native-visibility-aware-view
//
//  Created by Christian Schaffrath on 09.05.23.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

NS_ASSUME_NONNULL_BEGIN
@protocol CVisibilityAwareViewDelegate;

@interface CVisibilityAwareView : UIView

@property (nonatomic, weak) id<CVisibilityAwareViewDelegate> delegate;
@property (nonatomic, copy) RCTBubblingEventBlock onBecomeVisible;
@property (nonatomic, copy) RCTBubblingEventBlock onBecomeInvisible;

- (void) setAccuracy:(NSNumber *) accuracy;
- (void) setMinVisibleArea:(NSNumber *) minVisibleArea;
- (void) setIgnoreAppState:(BOOL) ignoreAppState;

- (void) start;
- (void) stop;

@end


@protocol CVisibilityAwareViewDelegate <NSObject>


- (void)viewDidEnterVisibleArea:(CVisibilityAwareView *)customView withFrameVisible: (BOOL) frameVisible andAppInForeground: (BOOL) appInForeground;
- (void)viewDidLeaveVisibleArea:(CVisibilityAwareView *)customView withFrameInvisible: (BOOL) frameInvisible andAppInBackground: (BOOL) appInBackground;

@end

NS_ASSUME_NONNULL_END
