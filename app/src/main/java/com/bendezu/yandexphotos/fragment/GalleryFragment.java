package com.bendezu.yandexphotos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.MainActivity;
import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.R;


public class GalleryFragment extends Fragment {

    private final String LOG_TAG = "GalleryFragment";
    private int mColumnCount;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    public GalleryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.gallery_recycler_view);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        mColumnCount = getResources().getInteger(R.integer.galleryColumns);
        mRecyclerView.setAdapter(new ImageRecyclerViewAdapter(this, getArguments()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        //mSwipeRefresh.setOnRefreshListener(this);

        //prepareTransitions();
        //postponeEnterTransition();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null)
            scrollToPosition();
    }

    private void scrollToPosition(){
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3,
                                       int i4, int i5, int i6, int i7) {
                mRecyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
                if (viewAtPosition == null || layoutManager.
                        isViewPartiallyVisible(viewAtPosition, false, true)) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPosition(MainActivity.currentPosition);
                        }
                    });
                }
            }
        });
    }
}
