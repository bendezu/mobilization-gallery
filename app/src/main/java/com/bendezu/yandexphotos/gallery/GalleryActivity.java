package com.bendezu.yandexphotos.gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bendezu.yandexphotos.R;

public class GalleryActivity extends AppCompatActivity {

    private static final String KEY_CURRENT_IMAGE_POSITION = "currentImagePosition";

    public static int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

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