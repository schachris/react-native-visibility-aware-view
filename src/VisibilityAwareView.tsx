import React, { useRef } from 'react';

import { UIManager, findNodeHandle } from 'react-native';

import {
  VisibilityAwareViewComponentName,
  VisibilityAwareViewHandle,
  type VisibilityAwareViewProps,
} from './VisibilityAwareViewNativeComponent';

const NativeVisibilityAwareView = require('./VisibilityAwareViewNativeComponent').default;

const _VisibilityAwareView: React.ForwardRefRenderFunction<VisibilityAwareViewHandle, VisibilityAwareViewProps> = (
  props,
  ref
) => {
  const viewRef = useRef(null);
  React.useImperativeHandle(ref, () => {
    const viewManagerConfig = UIManager.getViewManagerConfig(VisibilityAwareViewComponentName);
    return {
      start: () => {
        if (!viewManagerConfig.Commands) {
          return;
        }
        UIManager.dispatchViewManagerCommand(findNodeHandle(viewRef.current), viewManagerConfig.Commands.start!, []);
      },
      stop: () => {
        if (!viewManagerConfig.Commands) {
          return;
        }
        UIManager.dispatchViewManagerCommand(findNodeHandle(viewRef.current), viewManagerConfig.Commands.stop!, []);
      },
    };
  });

  return <NativeVisibilityAwareView {...props} ref={viewRef} />;
};

export const VisibilityAwareView = React.forwardRef(_VisibilityAwareView);
