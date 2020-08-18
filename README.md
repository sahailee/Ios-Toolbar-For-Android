# Ios-Toolbar-For-Android
A custom toolbar for Android Studio that looks and behaves like the IosNavigationController top bar.

# Download and Install Dependency
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency to your app build.gradle:
```
dependencies {
	        implementation 'com.github.sahailee:Ios-Toolbar-For-Android:v1.0'
	}
```

Set the theme to NoActionBar in your AndroidManifest.xml
```
<application
        ...
        android:theme="@style/Theme.AppCompat.Light.NoActionBar">
```

# How To Use
