# android-convention

A collection of Gradle convention plugins for Android projects, designed to keep a single source of truth for common module configurations.

This approach is based on:
- [Herding Elephants – Square Engineering](https://developer.squareup.com/blog/herding-elephants/)
- [Idiomatic Gradle – Johannes Hüsing](https://github.com/jjohannes/idiomatic-gradle)

Convention plugins avoid duplicated build script setup and messy `subprojects` blocks, without the pitfalls of `buildSrc`. They are **additive** and **composable** — each plugin has a single responsibility, and modules pick only what they need. One-off logic that isn't shared should stay directly in the module's `build.gradle.kts`.

## Structure

```
android-convention/
├── convention/                  # Convention plugins module
│   ├── build.gradle.kts
│   └── src/main/kotlin/
│       ├── android-application-conventions.gradle.kts
│       ├── android-library-conventions.gradle.kts
│       ├── android-feature-conventions.gradle.kts
│       ├── android-compose-application-conventions.gradle.kts
│       ├── android-compose-library-conventions.gradle.kts
│       ├── android-compose-feature-conventions.gradle.kts
│       ├── android-project-conventions.gradle.kts
│       └── extension/           # Shared Kotlin helpers for plugins
├── gradle/
│   └── libraries.versions.toml  # Version catalog
└── settings.gradle.kts
```

## Setup

### 1. Add as a Git submodule

From your project's root directory:

```sh
git submodule add git@github.com:raxden/android-convention.git
git mv android-convention build-logic
```

### 2. Configure `settings.gradle.kts`

```kotlin
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("build-logic/gradle/libraries.versions.toml"))
        }
    }
}
```

> The `settings.gradle.kts` inside `build-logic` will automatically look for the version catalog first in the parent project's `gradle/` folder, and fall back to its own `gradle/` folder.

## Plugins

### Base plugins

| Plugin | Description |
|--------|-------------|
| `android-application-conventions` | Android application with signing configs, build types, proguard, and KSP |
| `android-library-conventions` | Android library with build types and coverage |
| `android-feature-conventions` | Extends `android-library-conventions` |
| `android-project-conventions` | Root project: coverage reports via `rootCoverage` |

### Compose plugins

| Plugin | Description |
|--------|-------------|
| `android-compose-application-conventions` | Extends `android-application-conventions` with Compose + serialization |
| `android-compose-library-conventions` | Extends `android-library-conventions` with Compose |
| `android-compose-feature-conventions` | Extends `android-compose-library-conventions` |

---

### `android-application-conventions`

For the `:app` module. Includes:
- `com.android.application`, `ksp`, `kotlin-parcelize`
- Signing configs (`debug` / `release`)
- `debug` build type with unit test and Android test coverage enabled
- `release` build type with minify and resource shrinking

```kotlin
plugins {
    id("android-application-conventions")
}
```

### `android-library-conventions`

For standalone library modules. Includes:
- `com.android.library`, `ksp`, `kotlin-parcelize`
- `debug` build type with coverage enabled

```kotlin
plugins {
    id("android-library-conventions")
}
```

### `android-feature-conventions`

For feature modules. Thin wrapper around `android-library-conventions`.

```kotlin
plugins {
    id("android-feature-conventions")
}
```

### `android-compose-application-conventions`

For the `:app` module with Jetpack Compose. Extends `android-application-conventions` and adds:
- `org.jetbrains.kotlin.plugin.compose`
- `org.jetbrains.kotlin.plugin.serialization`
- `kotlinx-serialization-json` dependency

```kotlin
plugins {
    id("android-compose-application-conventions")
}
```

### `android-compose-library-conventions`

For library modules with Compose. Extends `android-library-conventions` and enables:
- `org.jetbrains.kotlin.plugin.compose`
- `buildFeatures.compose = true`

```kotlin
plugins {
    id("android-compose-library-conventions")
}
```

### `android-compose-feature-conventions`

For feature modules with Compose. Thin wrapper around `android-compose-library-conventions`.

```kotlin
plugins {
    id("android-compose-feature-conventions")
}
```

### `android-project-conventions`

For the root project. Configures:
- Code coverage aggregation via [`rootcoverage`](https://github.com/NeoTech-Software/Android-Root-Coverage-Plugin)
- `project-report` plugin on all subprojects
- `DownloadGradleDependencies` task

```kotlin
plugins {
    id("android-project-conventions")
}
```

## Signing

The `android-application-conventions` plugin expects:
- **Debug**: `config/debug.keystore` at the root of your project (standard Android debug keystore).
- **Release**: a `signing.release.properties` file (or equivalent) at the root of your project with the following keys:

```properties
storeFile=path/to/release.keystore
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```
