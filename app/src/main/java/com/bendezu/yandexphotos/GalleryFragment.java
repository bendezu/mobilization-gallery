package com.bendezu.yandexphotos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.dummy.DummyContent;


public class GalleryFragment extends Fragment {

    private int mColumnCount = 2;

    public GalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.gallery_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(new ImageRecyclerViewAdapter(DummyContent.ITEMS));
        return view;
    }

}