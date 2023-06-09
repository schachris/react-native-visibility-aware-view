#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import "RCTBridge.h"
#import "Utils.h"
#import "CVisibilityAwareView.h"

@interface VisibilityAwareViewManager : RCTViewManager
@end

@implementation VisibilityAwareViewManager

RCT_EXPORT_MODULE(VisibilityAwareView)

- (UIView *)view
{
  return [[CVisibilityAwareView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(onBecomeVisible, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBecomeInvisible, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(accuracy, NSNumber*)
RCT_EXPORT_VIEW_PROPERTY(minVisibleArea, NSNumber*)
RCT_EXPORT_VIEW_PROPERTY(ignoreAppState, BOOL)

RCT_EXPORT_METHOD(start:(nonnull NSNumber*) reactTag) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        CVisibilityAwareView *view = viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[CVisibilityAwareView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view start];
    }];
}

RCT_EXPORT_METHOD(stop:(nonnull NSNumber*) reactTag) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        CVisibilityAwareView *view = viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[CVisibilityAwareView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view stop];
    }];
}

@end
