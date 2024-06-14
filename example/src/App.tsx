import * as React from "react";

import {
  Alert,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Switch,
  Text,
  View
} from "react-native";

import { VisibilityAwareView } from "react-native-visibility-aware-view";

function ViewWithVisibility() {
  const [visible, setVisible] = React.useState(false);
  const txt = visible ? "Visible" : "Not visible";
  return (
    <VisibilityAwareView
      minVisibleArea={0.5}
      onBecomeVisible={(event) => {
        console.log("VISIBLE", event.nativeEvent);
        setVisible(true);
      }}
      onBecomeInvisible={(event) => {
        console.log("INVISIBLE", event.nativeEvent);
        setVisible(false);
      }}
      style={styles.borderedBox}
    >
      <Text>{txt}</Text>
      <Text>{txt}</Text>
      <Text>{txt}</Text>
      <Text>{txt}</Text>
      <Text>{txt}</Text>
    </VisibilityAwareView>
  );
}

// @ts-ignore
const uiManager = global?.nativeFabricUIManager ? "Fabric" : "Paper";

export default function App() {
  const [ignoreAppState, setIgnoreAppState] = React.useState(false);
  return (
    <View style={styles.container}>
      <View style={styles.topSpace}>
        <SafeAreaView>
          <Text>hallo {uiManager}</Text>
        </SafeAreaView>
      </View>
      <Text>ignoreAppState</Text>
      <Switch onValueChange={setIgnoreAppState} value={ignoreAppState} />
      <ScrollView style={styles.container}>
        <View style={styles.topScrollSpace} />
        <VisibilityAwareView
          minVisibleArea={0.5}
          ignoreAppState={ignoreAppState}
          onBecomeVisible={(event) => {
            Alert.alert(
              "Visibility Visible",
              `${JSON.stringify(event.nativeEvent)}`
            );
          }}
          onBecomeInvisible={(event) => {
            Alert.alert(
              "Visibility Invisible",
              `${JSON.stringify(event.nativeEvent)}`
            );
          }}
          style={styles.box}
        />
        <View style={styles.bottomSpace} />
        <ViewWithVisibility />
        <View style={styles.bottomSpace} />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
    backgroundColor: "green"
  },
  borderedBox: {
    padding: 10,
    borderWidth: 2,
    borderColor: "purple"
  },
  topScrollSpace: {
    height: 300
  },
  topSpace: {
    height: 200,
    backgroundColor: "blue"
  },
  bottomSpace: {
    height: 800
  }
});
