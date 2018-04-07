package com.bendezu.yandexphotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ImageRecyclerViewAdapter.OnImageClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if  (savedInstanceState == null) {
            GalleryFragment galleryFragment = new GalleryFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, galleryFragment)
                    .commit();
        }

    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Click to Image: " + position, Toast.LENGTH_SHORT).show();
    }
}
