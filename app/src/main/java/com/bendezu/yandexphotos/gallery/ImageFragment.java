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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageFragment extends Fragment implements RequestListener<Drawable> {

    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_TOKEN = "token";

    public static ImageFragment newInstance(String url, String token) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putString(KEY_IMAGE_URL, url);
        argument.putString(KEY_TOKEN, token);
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
        String imageUrl = arguments.getString(KEY_IMAGE_URL);
        String token = arguments.getString(KEY_TOKEN);

        //image.setTransitionName(imageUrl);

        if (imageUrl != null) {
            GlideUrl request = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                    .addHeader("Authorization", "OAuth " + token)
                    .build());
            Glide.with(this)
                    .load(request)
                    .listener(this)
                    .into(image);
        } else {
            image.setImageResource(R.color.colorImagePlaceHolder);
        }

        return view;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                Target<Drawable> target, boolean isFirstResource) {
        //getParentFragment().startPostponedEnterTransition();
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                   DataSource dataSource, boolean isFirstResource) {
        //getParentFragment().startPostponedEnterTransition();
        return false;
    }
}
