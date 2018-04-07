package com.bendezu.yandexphotos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.dummy.DummyContent;


public class GalleryFragment extends Fragment {

    private final String LOG_TAG = "GalleryFragment";
    private int mColumnCount;
    private ImageRecyclerViewAdapter.OnImageClickListener mActivity;

    public GalleryFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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

        RecyclerView recyclerView = view.findViewById(R.id.gallery_recycler_view);
        mColumnCount = getResources().getInteger(R.integer.galleryColumns);

        Log.d(LOG_TAG, "SET COLUMN COUNT TO " + mColumnCount);

        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(new ImageRecyclerViewAdapter(DummyContent.ITEMS, mActivity));

        return view;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
