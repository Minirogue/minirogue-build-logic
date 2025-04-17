# Build Logic
This repo stores the common build logic that is shared by my personal projects.

## Usage

### Importing the build logic
This section assumes that the consuming project is a gradle project using a version catalog.

Ensure that the following is in the consuming project's `settings.gradle` file:
```groovy
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "com.github.minirogue")
                useModule("com.github.minirogue:minirogue-build-logic:$requested.version")
        }
    }
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then add the following to the version catalog:
```toml
[plugins]
minirogue-plugin = { id = "com.github.minirogue", version = "0.0.8" }
``` 
and finally add the following to the root project `build.gradle`
```groovy
plugins {
    alias(libs.plugins.minirogue.plugin) apply false
}
```

### Applying and using the plugins
Then the following plugins may be used for any gradle modules contained in the project:
- `minirogue.multiplatform.library`
- `minirogue.test.app`
- `minirogue.android.app`
- `minirogue.jvm.app`

Then you may use the `minirogue` extension in the `build.gradle` file using the plugin to configure it:
```groovy
minirogue {
    android {
        hilt()
    }
}
```

## Used by
I will add a list here of repos that I have which use the build logic defined in this repo once they are publicly viewable.

## Dependencies

This repo manages several dependencies (namely ones that have related gradle plugins) that will be inherited by any projects that consume it.
As of version 0.0.8 the following dependencies and versions are used:
- [Android Gradle Plugin](https://developer.android.com/build/releases/gradle-plugin) = 8.9.0
- [Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/whats-new-compose-1610.html) = 1.7.3
- [Detekt](https://detekt.dev/changelog) = 1.23.8
- [Hilt](https://github.com/google/dagger/releases) = 2.56
- [Java](https://www.java.com/releases/) = 17
- [Kotlin](https://kotlinlang.org/docs/releases.html#release-details) = 2.1.20
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization/releases) = 1.8.0
- [Kotlin Symbol Processing](https://github.com/google/ksp/releases) = 2.1.20-2.0.0
- [Room](https://developer.android.com/jetpack/androidx/releases/room) = 2.7.0-rc02

## TODO
- Add docs for each plugin to detail their configurations
- Share the same config for this project's detekt and and the config that it uses for consuming libraries
- Set up some tests
- Figure out test tasks for CI config given JVM apps, Android Apps, multiplatform code, etc.
