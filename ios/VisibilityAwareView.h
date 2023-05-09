// This guard prevent this file to be compiled in the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "CVisibilityAwareView.h"

#ifndef VisibilityAwareViewNativeComponent_h
#define VisibilityAwareViewNativeComponent_h

NS_ASSUME_NONNULL_BEGIN

@interface VisibilityAwareView : RCTViewComponentView <CVisibilityAwareViewDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onBecomeVisible;
@property (nonatomic, copy) RCTBubblingEventBlock onBecomeInvisible;

@property (nonatomic, assign) float accuracy;
@property (nonatomic, assign) float minVisibleArea;
@property (nonatomic, assign) bool ignoreAppState;

- (void) start;
- (void) stop;

@end

NS_ASSUME_NONNULL_END

#endif /* VisibilityAwareViewNativeComponent_h */
#endif /* RCT_NEW_ARCH_ENABLED */
