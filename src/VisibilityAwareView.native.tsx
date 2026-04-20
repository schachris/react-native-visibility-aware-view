import { useRef, forwardRef, useImperativeHandle } from "react";
import { UIManager, findNodeHandle } from "react-native";
import {
  default as NativeVisibilityAwareView,
  VisibilityAwareViewComponentName,
  type VisibilityAwareViewHandle,
  type NativeProps
} from "./VisibilityAwareViewNativeComponent";
export * from "./VisibilityAwareViewNativeComponent";

type ViewManagerConfig = {
  Commands?: {
    start?: number;
    stop?: number;
    [key: string]: number | undefined;
  };
};

export const VisibilityAwareView = forwardRef<
  VisibilityAwareViewHandle,
  NativeProps
>((props, ref) => {
  const viewRef = useRef<any>(null);

  useImperativeHandle(ref, () => {
    const viewManagerConfig = UIManager.getViewManagerConfig(
      VisibilityAwareViewComponentName
    ) as ViewManagerConfig;

    const getCommand = (name: "start" | "stop") => {
      return viewManagerConfig.Commands?.[name] ?? null;
    };

    const dispatch = (command: number | null) => {
      const node = findNodeHandle(viewRef.current);
      if (node == null || command == null) return;

      UIManager.dispatchViewManagerCommand(node, command, []);
    };

    return {
      start: () => dispatch(getCommand("start")),
      stop: () => dispatch(getCommand("stop"))
    };
  }, []);

  return <NativeVisibilityAwareView {...props} ref={viewRef} />;
});
