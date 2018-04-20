package com.bendezu.yandexphotos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bendezu.yandexphotos.App;
import com.bendezu.yandexphotos.data.GalleryContract;
import com.bendezu.yandexphotos.gallery.ImageFragment;
import com.bendezu.yandexphotos.util.PreferencesUtils;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

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
                ImageRecyclerViewAdapter.mCursor.getString(GalleryContract.GalleryEntry.INDEX_COLUMN_PREVIEW),
                ImageRecyclerViewAdapter.mCursor.getString(GalleryContract.GalleryEntry.INDEX_COLUMN_FILE) );
    }
}