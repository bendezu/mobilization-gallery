package com.bendezu.yandexphotos.gallery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.util.NetworkUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageFragment extends Fragment implements RequestListener<Drawable> {

    private static final String KEY_POSITION = "position";
    private static final String KEY_THUMBNAIL_URL = "thumbnailUrl";
    private static final String KEY_IMAGE_URL = "imageUrl";

    public static ImageFragment newInstance(int position, String thumbnailUrl, String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putInt(KEY_POSITION, position);
        argument.putString(KEY_THUMBNAIL_URL, thumbnailUrl);
        argument.putString(KEY_IMAGE_URL, url);
        fragment.setArguments(argument);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ImageView image = view.findViewById(R.id.iv_image);

        Bundle arguments = getArguments();
        int position = arguments.getInt(KEY_POSITION);
        String thumbnailUrl = arguments.getString(KEY_THUMBNAIL_URL);
        String imageUrl = arguments.getString(KEY_IMAGE_URL);

        image.setTransitionName(String.valueOf(position));

        NetworkUtils.loadImageDetail(this, imageUrl, thumbnailUrl, image,
                this, this);

        return view;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                Target<Drawable> target, boolean isFirstResource) {
        getParentFragment().startPostponedEnterTransition();
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                   DataSource dataSource, boolean isFirstResource) {
        getParentFragment().startPostponedEnterTransition();
        return false;
    }

}
