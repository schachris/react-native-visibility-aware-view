# `react-native-visibility-aware-view`

![Supports iOS and Android][support-badge] ![npm][npm-badge]

A view which is aware of its visibility. It also tracks itself when the view is scrolled or moved.

Starting from version `0.6.0` only the new arch architecture is supported.

## Installation

```sh
yarn add react-native-visibility-aware-view
```

## Usage

```js
import { VisibilityAwareView } from "react-native-visibility-aware-view";

// ...

<VisibilityAwareView
  minVisibleArea={0.5}
  ignoreAppState={ignoreAppState}
  onBecomeVisible={(event) => {}}
  onBecomeInvisible={(event) => {}}
  style={styles.box}
/>;
```

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

[npm-badge]: https://img.shields.io/npm/v/react-native-visibility-aware-view.svg?style=flat-square
[support-badge]: https://img.shields.io/badge/platforms-android%20%7C%20ios-lightgrey.svg?style=flat-square
