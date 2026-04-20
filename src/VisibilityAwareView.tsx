import { View } from "react-native";
import type { NativeProps } from "./VisibilityAwareViewNativeComponent";

type Props = NativeProps;

export function VisibilityAwareView({ style, ...rest }: Props) {
  return <View {...rest} style={style} />;
}
