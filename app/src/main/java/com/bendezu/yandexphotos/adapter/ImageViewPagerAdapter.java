package com.bendezu.yandexphotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.data.ImageDataSet;
import com.bendezu.yandexphotos.imagedetail.ImageFragment;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

    private ImageFragment currentFragment;

    public ImageViewPagerAdapter(Fragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    @Override
    public int getCount() {
        return ImageDataSet.getCount();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (currentFragment != object) {
            currentFragment = (ImageFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public ImageFragment getCurrentFragment() {
        return currentFragment;
    }
}