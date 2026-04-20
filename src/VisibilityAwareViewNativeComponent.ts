import {
  codegenNativeComponent,
  type CodegenTypes,
  type ViewProps
} from "react-native";

export const VisibilityAwareViewComponentName = "VisibilityAwareView";

type BecomeVisibleEvent = Readonly<{
  app_open: boolean;
  frame_visible: boolean;
}>;

type BecomeInvisibleEvent = Readonly<{
  app_closed: boolean;
  frame_visible: boolean;
}>;

export interface NativeProps extends ViewProps {
  accuracy?: CodegenTypes.WithDefault<CodegenTypes.Float, 250>;
  /**
   * Percentage between {0,1} on how much the view must be visible to the user the be "visible"
   */
  minVisibleArea?: CodegenTypes.WithDefault<CodegenTypes.Float, 0.01>;
  /**
   * Usually a view is not visible when the app is in background.
   * When setting to true it will disable this feature.
   * @default: false
   */
  ignoreAppState?: CodegenTypes.WithDefault<boolean, false>;
  onBecomeVisible?: CodegenTypes.BubblingEventHandler<
    Readonly<BecomeVisibleEvent>
  >;
  onBecomeInvisible?: CodegenTypes.BubblingEventHandler<
    Readonly<BecomeInvisibleEvent>
  >;
}

export type VisibilityAwareViewHandle = {
  start: () => void;
  stop: () => void;
};

export default codegenNativeComponent<NativeProps>("VisibilityAwareView");
