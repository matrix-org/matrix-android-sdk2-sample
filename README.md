# matrix-android-sdk2-sample

This is an example project for using the [matrix-android-sdk2](https://github.com/matrix-org/matrix-android-sdk2)


## Gradle

In your top level build.gradle file, you should have at least:

```
buildscript {
    ext.kotlin_version = "1.6.0"
    repositories {
        google()
        jcenter()
        maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:7.0.3"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

And for your app module build.gradle you should at least include:

```
 implementation 'org.matrix.android:matrix-android-sdk2:x.y.z'
```

replace `x.y.z by the latest version: ![Latest version](https://img.shields.io/maven-central/v/org.matrix.android/matrix-android-sdk2)

## AndroidManifest.xml file

Your application should at least contains the provider "InitializationProvider" to remove initialiwation of the WorkManager as below

```
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:name=".SampleApp"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        
        [...]

        // This is required as the WorkManager is already initialized by the SDK
        <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false"
            tools:ignore="MissingClass"
            tools:node="merge">
            <meta-data
                android:name="androidx.work.WorkManagerInitializer"
                android:value="androidx.startup"
                tools:node="remove" />
        </provider>

    </application>
    
```

## Code

Please have a look in the following files to know how to start using the sdk: 

[SampleApp](app/src/main/java/org/matrix/android/sdk/sample/SampleApp.kt)

[Login](/app/src/main/java/org/matrix/android/sdk/sample/ui/SimpleLoginFragment.kt)

[RoomList](/app/src/main/java/org/matrix/android/sdk/sample/ui/RoomListFragment.kt)

[RoomDetail](/app/src/main/java/org/matrix/android/sdk/sample/ui/RoomDetailFragment.kt)

