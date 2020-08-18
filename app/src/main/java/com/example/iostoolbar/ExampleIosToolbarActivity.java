package com.example.iostoolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.iostoolbar.IosToolbar;

public class ExampleIosToolbarActivity extends AppCompatActivity {
    public static IosToolbar myToolbar;
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home_screen_activity);
        myToolbar = findViewById(R.id.iosToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setUpButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("IosToolbar", "Back button pressed.");
            }
        });
        transitionFragment(new SearchAndScrollFragment());
    }

    private void transitionFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, fragment).commit();
    }
}
