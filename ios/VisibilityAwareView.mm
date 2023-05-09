#ifdef RCT_NEW_ARCH_ENABLED
#import "VisibilityAwareView.h"

#import <react/renderer/components/RNVisibilityAwareViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/EventEmitters.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/Props.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "Utils.h"

using namespace facebook::react;

@interface VisibilityAwareView () <RCTVisibilityAwareViewViewProtocol>

@end

@implementation VisibilityAwareView {
    CVisibilityAwareView * _view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<VisibilityAwareViewComponentDescriptor>();
}

- (void) viewDidLeaveVisibleArea:(CVisibilityAwareView *)customView withFrameInvisible:(BOOL)frameInvisible andAppInBackground:(BOOL)appInBackground {
    std::dynamic_pointer_cast<const VisibilityAwareViewEventEmitter>(_eventEmitter)
    ->onBecomeInvisible(VisibilityAwareViewEventEmitter::OnBecomeInvisible{
        .app_closed = static_cast<bool>(appInBackground),
        .frame_visible = static_cast<bool>(!frameInvisible)
    });
}

- (void) viewDidEnterVisibleArea:(CVisibilityAwareView *)customView withFrameVisible:(BOOL)frameVisible andAppInForeground:(BOOL)appInForeground {
    std::dynamic_pointer_cast<const VisibilityAwareViewEventEmitter>(_eventEmitter)
    ->onBecomeVisible(VisibilityAwareViewEventEmitter::OnBecomeVisible{
        .app_open = static_cast<bool>(appInForeground),
        .frame_visible = static_cast<bool>(frameVisible)
    });
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const VisibilityAwareViewProps>();
    _props = defaultProps;

    _view = [[CVisibilityAwareView alloc] init];
    _view.delegate = self;
      
    self.contentView = _view;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &oldViewProps = *std::static_pointer_cast<VisibilityAwareViewProps const>(_props);
    const auto &newViewProps = *std::static_pointer_cast<VisibilityAwareViewProps const>(props);

    if (oldViewProps.accuracy != newViewProps.accuracy) {
        [_view setAccuracy:[NSNumber numberWithFloat:newViewProps.accuracy]];
    }else if (oldViewProps.minVisibleArea != newViewProps.minVisibleArea) {
        [_view setMinVisibleArea:[NSNumber numberWithFloat:newViewProps.minVisibleArea]];
    }else if (oldViewProps.ignoreAppState != newViewProps.ignoreAppState) {
        [_view setIgnoreAppState:newViewProps.ignoreAppState];
    }

    [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> VisibilityAwareViewCls(void)
{
    return VisibilityAwareView.class;
}

- (void) start {
    [_view start];
}
- (void) stop {
    [_view stop];
}

@end
#endif
