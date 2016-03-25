# GameAnalytics Plugin for Game Closure

This is GameAnalytics wrapper plugin for Game Closure devkit.

## Notes
* [Google Play Games](https://github.com/hashcube/gameplay) plugin is required to work on Android.

## License
MIT

## Installation
Clone this repo into addons folder inside devkit.

In manifest, add "gameanalytics" to "addons" and add "gameanalyticsGameKey" and "gameanalyticsSecretKey" to ios/android section.

for eg.
```
{
    "android": {
        "gameanalyticsGameKey": "mygamekey",
        "gameanalyticsSecretKey": "mysecretkey"
    },
    "ios": {
        "gameanalyticsGameKey": "mygamekey",
        "gameanalyticsSecretKey": "mysecretkey"
    },
    "addons": {
        "gameanalytics"
    }
}
```
