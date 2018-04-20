package com.bendezu.yandexphotos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bendezu.yandexphotos.fragment.AuthFragment;
import com.bendezu.yandexphotos.fragment.GalleryFragment;
import com.bendezu.yandexphotos.util.AuthUtils;
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

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String accessToken = preferences.getString(getString(R.string.saved_access_token_key), null);

        token = accessToken;

        if (savedInstanceState == null) {
            if (accessToken == null){
                launchLoginScreen();
            } else {
                launchGalleryFragment();
            }
        } else {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_IMAGE_POSITION, 0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null &&
                uri.toString().startsWith(AuthUtils.REDIRECT_URI)){

            String error = UriUtils.getFragmentParameter(uri, "error");
            if (error != null) {
                Log.d(LOG_TAG, "Permission error: " + error);
                Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();

            }
            String accessToken = UriUtils.getFragmentParameter(uri, "access_token");
            if (accessToken != null) {
                Log.d(LOG_TAG, "Access token: " + accessToken);
                // save token to SharedPreferences
                getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putString(getString(R.string.saved_access_token_key), accessToken)
                        .apply();
                token = accessToken;
                //launchGalleryFragment();
                shouldLaunchgallery = true;
            }
        }
        super.onNewIntent(intent);
    }

    private boolean shouldLaunchgallery = false;

    @Override
    protected void onPostResume() {
        if (shouldLaunchgallery) launchGalleryFragment();
        shouldLaunchgallery = false;
        super.onPostResume();
    }

    public void launchGalleryFragment() {
        GalleryFragment mGalleryFragment = new GalleryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.slide_out_up)
                .replace(R.id.fragment_container, mGalleryFragment)
                .commit();
    }

    public void launchLoginScreen(){
        AuthFragment authFragment = new AuthFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_down, R.anim.nothing)
                .replace(R.id.fragment_container, authFragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_IMAGE_POSITION, currentPosition);
    }
}
