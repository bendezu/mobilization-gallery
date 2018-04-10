package com.bendezu.yandexphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bendezu.yandexphotos.util.UriUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;


public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    private final String LOG_TAG = "RecyclerViewAdapter";

    private final ArrayList<String> mPaths;
    private final ArrayList<String> mPreviews;
    private final RequestManager mRequestManager;

    private OnImageClickListener mClickHandler;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(int position);
    }

    public ImageRecyclerViewAdapter(Fragment fragment, Bundle bundle, OnImageClickListener clickHandler) {
        mRequestManager = Glide.with(fragment);
        mPaths = bundle.getStringArrayList("paths");
        mPreviews = bundle.getStringArrayList("previews");
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
        mRequestManager.load("http://i.imgur.com/zuG2bGQ.jpg").into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mPaths.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View view;
        public int position;
        SquareImageView image;
        WebView webView;


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
