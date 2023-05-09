//
//  VisibilityAwareView.h
//  react-native-visibility-aware-view
//
//  Created by Christian Schaffrath on 09.05.23.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

NS_ASSUME_NONNULL_BEGIN

@interface VisibilityAwareView : UIView

@property (nonatomic, copy) RCTBubblingEventBlock onBecomeVisible;
@property (nonatomic, copy) RCTBubblingEventBlock onBecomeInvisible;

- (void) setAccuracy:(NSNumber *) accuracy;
- (void) setMinVisibleArea:(NSNumber *) minVisibleArea;
- (void) setIgnoreAppState:(BOOL) ignoreAppState;

- (void) start;
- (void) stop;

@end

NS_ASSUME_NONNULL_END
