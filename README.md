# matrix-android-sdk2-sample

This is an example project for using the matrix-android-sdk2


## Gradle

In your top level build.gradle file, you should have at least:

```
buildscript {
    ext.kotlin_version = "1.4.10"
    repositories {
        google()
        jcenter()
        maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.1"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
        maven {
        // This is necessary for now, will be removed soon
            url "https://github.com/vector-im/jitsi_libre_maven/raw/master/android-sdk-2.9.3"
        }
    }
}
```

And for your app module build.gradle you should at least include:

```
 implementation 'com.github.matrix-org:matrix-android-sdk2:v1.0.9'
 implementation('org.jitsi.react:jitsi-meet-sdk:2.9.3') { transitive = true }
```

## AndroidManifest.xml file

Your application should at least contains the provider "WorkManagerInitializer" with tools:node="remove" as below

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
            android:name="androidx.work.impl.WorkManagerInitializer"
            android:authorities="${applicationId}.workmanager-init"
            tools:node="remove"
            tools:ignore="MissingClass" />

    </application>
    
```

## Code

Please have a look in the following files to know how to start using the sdk: 

[SampleApp](app/src/main/java/org/matrix/android/sdk/sample/SampleApp.kt)

[Login](/app/src/main/java/org/matrix/android/sdk/sample/ui/SimpleLoginFragment.kt)

[RoomList](/app/src/main/java/org/matrix/android/sdk/sample/ui/RoomListFragment.kt)

[RoomDetail](/app/src/main/java/org/matrix/android/sdk/sample/ui/RoomDetailFragment.kt)

