package com.bendezu.yandexphotos.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.authorization.AuthActivity;
import com.bendezu.yandexphotos.util.AuthUtils;
import com.bendezu.yandexphotos.util.PreferencesUtils;
import com.bendezu.yandexphotos.util.UriUtils;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final String KEY_CURRENT_IMAGE_POSITION = "currentImagePosition";

    public static int currentPosition;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = PreferencesUtils.getAccessToken(this);

        if (savedInstanceState == null) {
            launchGalleryFragment();
        } else {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_IMAGE_POSITION, 0);
        }
    }

    public void launchGalleryFragment() {
        GalleryFragment mGalleryFragment = new GalleryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mGalleryFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_IMAGE_POSITION, currentPosition);
    }
}
