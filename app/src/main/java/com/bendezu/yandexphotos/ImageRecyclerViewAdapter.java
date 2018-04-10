package com.bendezu.yandexphotos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;


public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    private final String LOG_TAG = "RecyclerViewAdapter";

    private final ArrayList<String> mPaths;
    private final ArrayList<String> mPreviews;
    private final RequestManager mRequestManager;
    private final String mToken;

    private OnImageClickListener mClickHandler;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);
    }

    public ImageRecyclerViewAdapter(Fragment fragment, Bundle bundle, OnImageClickListener clickHandler) {
        mRequestManager = Glide.with(fragment);
        mPaths = bundle.getStringArrayList("paths");
        mPreviews = bundle.getStringArrayList("previews");
        mToken = bundle.getString("token");
        mClickHandler = clickHandler;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        holder.position = position;
        //load Image
        String preview = mPreviews.get(position);
        GlideUrl request = new GlideUrl(preview, new LazyHeaders.Builder()
                .addHeader("Authorization", "OAuth " + mToken)
                .build());
        mRequestManager
                .load(request)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mPaths.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public int position;
        SquareImageView image;

        public ImageViewHolder(View view) {
            super(view);
            this.view = view;
            image = view.findViewById(R.id.item_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {;
            mClickHandler.onImageSelected(position);
        }
    }
}
