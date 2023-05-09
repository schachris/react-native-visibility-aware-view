import * as React from 'react';

import { Alert, ScrollView, StyleSheet, View } from 'react-native';

import { VisibilityAwareView } from 'react-native-visibility-aware-view';

export default function App() {
  return (
    <View style={styles.container}>
      <View style={styles.topSpace} />
      <ScrollView style={styles.container}>
        <View style={styles.topScrollSpace} />
        <VisibilityAwareView
          minVisibleArea={0.5}
          ignoreAppState={false}
          onBecomeVisible={(event) => {
            Alert.alert('Visibility Visible', `${JSON.stringify(event.nativeEvent)}`);
          }}
          onBecomeInvisible={(event) => {
            Alert.alert('Visibility Invisible', `${JSON.stringify(event.nativeEvent)}`);
          }}
          style={styles.box}
        />
        <View style={styles.bottomSpace} />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
    backgroundColor: 'green',
  },
  topScrollSpace: {
    height: 300,
  },
  topSpace: {
    height: 200,
    backgroundColor: 'blue',
  },
  bottomSpace: {
    height: 800,
  },
});
