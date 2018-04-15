package com.bendezu.yandexphotos.adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.view.SquareImageView;
import com.bendezu.yandexphotos.ViewHolderListenerImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    private final ArrayList<String> mPaths;
    public static ArrayList<String> mPreviews;
    private final RequestManager mRequestManager;
    private final String mToken;

    private ViewHolderListener mViewHolderListener;

    public interface ViewHolderListener {
        void onLoadCompleted(ImageView view, int adapterPosition);
        void onItemClicked(View view, int adapterPosition);
    }

    public ImageRecyclerViewAdapter(Fragment fragment, Bundle bundle) {
        mRequestManager = Glide.with(fragment);
        mViewHolderListener = new ViewHolderListenerImpl(fragment);

        mPaths = bundle.getStringArrayList("paths");
        mPreviews = bundle.getStringArrayList("previews");
        mToken = bundle.getString("token");
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        return new ImageViewHolder(view, mRequestManager, mViewHolderListener);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return mPaths.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RequestListener<Drawable> {

        public final View view;
        SquareImageView image;
        RequestManager requestManager;
        ViewHolderListener listener;
        int position;

        public ImageViewHolder(View view, RequestManager requestManager, ViewHolderListener listener) {
            super(view);
            this.view = view;
            this.requestManager = requestManager;
            this.listener = listener;

            image = view.findViewById(R.id.item_image);

            view.setOnClickListener(this);
        }

        void onBind(){
            position = getAdapterPosition();
            downloadImage(position);
            //image.setTransitionName(mPreviews.get(position));
        }

        @Override
        public void onClick(View view) {;
            mViewHolderListener.onItemClicked(view, position);
        }

        void downloadImage(int position){
            String preview = mPreviews.get(position);
            if (preview == null){
                image.setImageResource(R.drawable.ic_failed);
                return;
            }
            GlideUrl request = new GlideUrl(preview, new LazyHeaders.Builder()
                    .addHeader("Authorization", "OAuth " + mToken)
                    .build());
            mRequestManager
                    .load(request)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(this)
                    .into(image);
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                    Target<Drawable> target, boolean isFirstResource) {
            listener.onLoadCompleted(image, position);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                       DataSource dataSource, boolean isFirstResource) {
            listener.onLoadCompleted(image, position);
            return false;
        }
    }
}
