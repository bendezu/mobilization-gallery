package com.bendezu.yandexphotos.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageViewPagerAdapter;


public class ImageDetailFragment extends Fragment implements View.OnClickListener {

    private ViewPager mViewPager;
    private ImageButton mBackButton;
    private ImageButton mShareButton;

    public ImageDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);

        mViewPager = view.findViewById(R.id.image_view_pager);
        mBackButton = view.findViewById(R.id.back_button);
        mShareButton = view.findViewById(R.id.share_button);

        mBackButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);

        mViewPager.setAdapter(new ImageViewPagerAdapter(this));
        mViewPager.setCurrentItem(GalleryActivity.currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                GalleryActivity.currentPosition = position;
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                getFragmentManager().popBackStack();
                break;
            case R.id.share_button:
                //TODO
                break;
        }
    }
}