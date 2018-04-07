package com.bendezu.yandexphotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yandex.disk.rest.RestClient;

public class MainActivity extends AppCompatActivity implements ImageRecyclerViewAdapter.OnImageClickListener {

    private GalleryFragment mGalleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if  (savedInstanceState == null) {
            mGalleryFragment = new GalleryFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, mGalleryFragment)
                    .commit();
        }

    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Click to Image: " + position, Toast.LENGTH_SHORT).show();

        ImageDetailFragment detailImageFragment = new ImageDetailFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, detailImageFragment)
                .addToBackStack(ImageDetailFragment.class.getSimpleName())
                .commit();
    }

}
