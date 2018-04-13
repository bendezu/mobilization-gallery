package com.bendezu.yandexphotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bendezu.yandexphotos.MainActivity;
import com.bendezu.yandexphotos.fragment.ImageFragment;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

    public ImageViewPagerAdapter(Fragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    @Override
    public int getCount() {
        return ImageRecyclerViewAdapter.mPreviews.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(ImageRecyclerViewAdapter.mPreviews.get(position),
                MainActivity.token);
    }
}
