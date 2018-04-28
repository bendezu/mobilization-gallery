package com.bendezu.yandexphotos.gallery;

import android.graphics.Matrix;
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
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment extends Fragment implements ImageDetailFragment.ImageDetailListener {

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

    private int position;
    String thumbnailUrl;
    private String imageUrl;
    ImageView transitionImage;
    PhotoView fullImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);

        transitionImage = view.findViewById(R.id.iv_image);
        fullImage = view.findViewById(R.id.iv_fullsize);

        Bundle arguments = getArguments();
        position = arguments.getInt(KEY_POSITION);
        thumbnailUrl = arguments.getString(KEY_THUMBNAIL_URL);
        imageUrl = arguments.getString(KEY_IMAGE_URL);



        transitionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ImageDetailFragment)getParentFragment()).onImageClicked();
            }
        });
        fullImage.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((ImageDetailFragment)getParentFragment()).onImageClicked();
            }
        });




        transitionImage.setTransitionName(String.valueOf(position));

        if (savedInstanceState == null)
            showTransitionImage();

        NetworkUtils.loadImageTransition(this, thumbnailUrl, transitionImage,
            new RequestListener<Drawable>() {
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
            });

        if (position != GalleryActivity.currentPosition)
            showFullImage();

        NetworkUtils.loadFullsizeImage(this, imageUrl, this.fullImage, null);
        return view;
    }

    @Override
    public void onTransitionEnd() {
        showFullImage();
    }

    @Override
    public void onBackPressed() {
        showTransitionImage();
        getParentFragment().getFragmentManager().popBackStack();
    }

    private void showFullImage(){
        fullImage.setVisibility(View.VISIBLE);
        //transitionImage.setVisibility(View.INVISIBLE);
    }

    private void showTransitionImage(){
        //transitionImage.setVisibility(View.VISIBLE);
        fullImage.setVisibility(View.INVISIBLE);
    }

    public void resetImageZoom(){
        fullImage.setDisplayMatrix(new Matrix());
        fullImage.setSuppMatrix(new Matrix());
    }

}
