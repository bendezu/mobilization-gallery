package com.bendezu.yandexphotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.data.GalleryContract;
import com.bendezu.yandexphotos.gallery.ImageFragment;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

    private ImageFragment mCurrentFragment;

    public ImageViewPagerAdapter(Fragment fragment) {
        super(fragment.getChildFragmentManager());
    }

    @Override
    public int getCount() {
        if (ImageRecyclerViewAdapter.mCursor == null) return 0;
        return ImageRecyclerViewAdapter.mCursor.getCount();
    }

    @Override
    public Fragment getItem(int position) {
        ImageRecyclerViewAdapter.mCursor.moveToPosition(position);
        return ImageFragment.newInstance(
                position,
                ImageRecyclerViewAdapter.mCursor.getString(GalleryContract.GalleryEntry.INDEX_COLUMN_PREVIEW),
                ImageRecyclerViewAdapter.mCursor.getString(GalleryContract.GalleryEntry.INDEX_COLUMN_FILE));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (mCurrentFragment != object) {
            mCurrentFragment = (ImageFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public ImageFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}