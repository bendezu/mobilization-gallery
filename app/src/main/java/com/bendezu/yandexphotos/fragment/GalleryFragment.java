package com.bendezu.yandexphotos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.data.ImageContent;
import com.bendezu.yandexphotos.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.R;

import java.util.List;


public class GalleryFragment extends Fragment {

    private final String LOG_TAG = "GalleryFragment";
    private int mColumnCount;
    private ImageRecyclerViewAdapter.OnImageClickListener mActivity;
    private RecyclerView mRecyclerView;

    public GalleryFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(LOG_TAG, "OnAttach");

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mActivity = (ImageRecyclerViewAdapter.OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.gallery_recycler_view);

        mColumnCount = getResources().getInteger(R.integer.galleryColumns);
        Log.d(LOG_TAG, "set column count to " + mColumnCount);

        mRecyclerView.setAdapter(new ImageRecyclerViewAdapter(this, getArguments(), mActivity));
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        return view;
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "OnDetach");

        mActivity = null;
        super.onDetach();
    }
}
