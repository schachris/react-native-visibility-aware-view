import type { ViewProps } from 'react-native';

import type { BubblingEventHandler, Float, WithDefault } from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

export const VisibilityAwareViewComponentName = 'VisibilityAwareView';

export interface VisibilityAwareViewProps extends ViewProps {
  accuracy?: WithDefault<Float, 250>;
  /**
   * Percentage between {0,1} on how much the view must be visible to the user the be "visible"
   */
  minVisibleArea?: WithDefault<Float, 0.01>;
  /**
   * Usually a view is not visible when the app is in background.
   * When setting to true it will disable this feature.
   * @default: false
   */
  ignoreAppState?: WithDefault<boolean, false>;
  onBecomeVisible?: BubblingEventHandler<Readonly<{ app_open: boolean; frame_visible: boolean }>>;
  onBecomeInvisible?: BubblingEventHandler<Readonly<{ app_closed: boolean; frame_visible: boolean }>>;
}

export type VisibilityAwareViewHandle = {
  start: () => void;
  stop: () => void;
};

export default codegenNativeComponent<VisibilityAwareViewProps>('VisibilityAwareView');
