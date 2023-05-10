# `react-native-visibility-aware-view`

![Supports iOS and Android and Web][support-badge] ![npm][npm-badge]

A view which is aware of its visibility. It also tracks itself when the view is scrolled or moved.

This library supports both architectures. The old one and fabric (new arch).

## Installation

```sh
npm install react-native-visibility-aware-view
```

## Usage

```js
import { VisibilityAwareView } from 'react-native-visibility-aware-view';

// ...

<VisibilityAwareView
  minVisibleArea={0.5}
  ignoreAppState={ignoreAppState}
  onBecomeVisible={(event) => {}}
  onBecomeInvisible={(event) => {}}
  style={styles.box}
/>;
```

## Testing

### New Arch

#### iOS

Navigate to example/package.json and set _RCT_NEW_ARCH_ENABLED=1_ in pods script

then run

```sh
yarn clean
yarn
# and then
yarn example ios
```

#### Android

Navigate to example/android/gradle.properties and set _newArchEnabled=true_
then run

```sh
yarn clean
yarn
# and then
yarn example android
```

##### generate Artifacts

```sh
cd ./example/android
./gradlew generateCodegenArtifactsFromSchema
cd ../../
yarn example android

# or
cd ./example/android && ./gradlew generateCodegenArtifactsFromSchema && cd ../../ && yarn example android
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

[npm-badge]: https://img.shields.io/npm/v/react-native-visibility-aware-view.svg?style=flat-square
[support-badge]: https://img.shields.io/badge/platforms-android%20|%20ios%20|%20web-lightgrey.svg?style=flat-square
