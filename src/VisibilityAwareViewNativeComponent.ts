import type { NativeSyntheticEvent, ViewProps } from 'react-native';

import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

export const VisibilityAwareViewComponentName = 'VisibilityAwareView';

export interface VisibilityAwareViewProps extends ViewProps {
  accuracy?: number;
  /**
   * Percentage between {0,1} on how much the view must be visible to the user the be "visible"
   */
  minVisibleArea?: number;
  /**
   * Usually a view is not visible when the app is in background.
   * When setting to true it will disable this feature.
   * @default: false
   */
  ignoreAppState?: boolean;
  onBecomeVisible?: (event: NativeSyntheticEvent<{ duration: number }>) => void;
  onBecomeInvisible?: (event: NativeSyntheticEvent<{ duration: number }>) => void;
}

export type VisibilityAwareViewHandle = {
  start: () => void;
  stop: () => void;
};

export const NativeVisibilityAwareView = codegenNativeComponent<VisibilityAwareViewProps>(
  VisibilityAwareViewComponentName
);
