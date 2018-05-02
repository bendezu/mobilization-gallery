package com.bendezu.yandexphotos.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.imagedetail.ImageDetailFragment;

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
        GalleryFragment galleryFragment = new GalleryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, galleryFragment, GalleryFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment imageDetailFragment = getSupportFragmentManager().findFragmentByTag(ImageDetailFragment.TAG);
        if (imageDetailFragment != null){
            ((ImageDetailFragment)imageDetailFragment).getCurrentFragment().onBackPressed();
            Fragment galleryFragment = getSupportFragmentManager().findFragmentByTag(GalleryFragment.TAG);
            ((GalleryFragment)galleryFragment).onResumeFromDetail();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_IMAGE_POSITION, currentPosition);
    }
}
