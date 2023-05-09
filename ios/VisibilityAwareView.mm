#ifdef RCT_NEW_ARCH_ENABLED
#import "VisibilityAwareView.h"

#import <react/renderer/components/RNVisibilityAwareViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/EventEmitters.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/Props.h>
#import <react/renderer/components/RNVisibilityAwareViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "Utils.h"

using namespace facebook::react;

@interface VisibilityAwareView () <RCTVisibilityAwareViewProtocol>

@end

@implementation VisibilityAwareView {
    UIView * _view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<VisibilityAwareViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const VisibilityAwareViewProps>();
    _props = defaultProps;

    _view = [[UIView alloc] init];

    self.contentView = _view;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &oldViewProps = *std::static_pointer_cast<VisibilityAwareViewProps const>(_props);
    const auto &newViewProps = *std::static_pointer_cast<VisibilityAwareViewProps const>(props);

    if (oldViewProps.color != newViewProps.color) {
        NSString * colorToConvert = [[NSString alloc] initWithUTF8String: newViewProps.color.c_str()];
        [_view setBackgroundColor: [Utils hexStringToColor:colorToConvert]];
    }

    [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> VisibilityAwareViewCls(void)
{
    return VisibilityAwareView.class;
}

@end
#endif
