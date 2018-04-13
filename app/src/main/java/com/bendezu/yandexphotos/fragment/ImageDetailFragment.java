package com.bendezu.yandexphotos.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bendezu.yandexphotos.MainActivity;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageViewPagerAdapter;


public class ImageDetailFragment extends Fragment {

    private ViewPager mViewPager;

    public ImageDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);

        mViewPager = view.findViewById(R.id.image_view_pager);
        mViewPager.setAdapter(new ImageViewPagerAdapter(this));
        mViewPager.setCurrentItem(MainActivity.currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                MainActivity.currentPosition = position;
            }
        });
        return view;
    }

}