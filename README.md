# Ios-Toolbar-For-Android
A custom toolbar for Android Studio that looks and behaves like the IosNavigationController top bar.

# Download
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
	        implementation 'com.github.sahailee:Ios-Toolbar-For-Android:v1.1.0'
	}
```

Set the theme to NoActionBar in your AndroidManifest.xml
```
<application
        ...
        android:theme="@style/Theme.AppCompat.Light.NoActionBar">
```

# How To Use

In a Fragment or Activity add the following to its xml:
The four attributes are optional.
```
 <com.android.iostoolbar.IosToolbar
        ...
        app:show_search_bar="true" 
        app:show_up_button="false"
        app:title="Home Screen"
        app:up_button_text="Back" />
```
Or you can use the design tab and drag in the IosToolbar.

In your activity's on Create, set the support action bar to be the iosToolbar:
```
@Override
    public void onCreate(Bundle savedInstance) {
    			...
			setSupportActionBar(iosToolbar);
			
	}

```
# Displaying the Up Button
I recommend configuring the up button with however you are managing navigation.
```
        iosToolbar.setTitle("Large Title"); 	// Sets the main title of the toolbar.
	
	iosToolbar.setUpButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButton();  // Define this or try something else to get the previous screen.
            }
        });
    	iosToolbar.setUpButtonText("Previous Screen");
    	iosToolbar.enableUpButton();
	// iosToolbar.disableUpButton(); // This will hide the up button
```

# Scroll Views, Recycler Views, and Search Bars
Enable the scrollview functionality in the OnResume Function.
Enable the search bar if you need it as well.
```
@Override
    public void onResume() {
        super.onResume();
	...
        iosToolbar.enableSearchBar();
        iosToolbar.setScrollingView(view.findViewById(R.id.scroll));
        iosToolbar.setupScrollingView();
	...
    }
```

Disable them in the OnPause:
```
@Override
    public void onPause() {
        super.onPause();
	...
	iosToolbar.disableSearchBar();
        iosToolbar.disableScrollingView();
	...
    }
```

# Configuring the Search Bar
To make the search bar work with a Recycler View, pass in it's adapter into the IosToolbar.configureSearchView(final T...adapters) function:
(The adapter must extend Filterable in order to properly search through the lists)
```
@Override
    public void onResume() {
        super.onResume();
	...
        iosToolbar.enableSearchBar();
	iosToolbar.configureSearchView(mAdapter); // Able to add multiple adapters in case you have multiple searchable lists on one Fragment
	...
    }
```

