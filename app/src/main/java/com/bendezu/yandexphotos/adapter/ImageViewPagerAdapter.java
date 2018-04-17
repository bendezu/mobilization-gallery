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
        if (ImageRecyclerViewAdapter.mResources == null) return 0;
        return ImageRecyclerViewAdapter.mResources.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(ImageRecyclerViewAdapter.mResources.get(position).getFile(),
                MainActivity.token);
    }
}