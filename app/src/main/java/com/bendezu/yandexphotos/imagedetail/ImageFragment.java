package com.bendezu.yandexphotos.imagedetail;

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
import com.bendezu.yandexphotos.data.ImageData;
import com.bendezu.yandexphotos.data.ImageDataSet;
import com.bendezu.yandexphotos.gallery.GalleryActivity;
import com.bendezu.yandexphotos.util.NetworkUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment extends Fragment {

    private static final String KEY_POSITION = "position";

    public static ImageFragment newInstance(int position) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putInt(KEY_POSITION, position);
        fragment.setArguments(argument);
        return fragment;
    }

    private int position;
    private ImageData imageData;

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
        imageData = ImageDataSet.getImageData(position);

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

        if (savedInstanceState == null) {
            showTransitionImage();
        }

        NetworkUtils.loadImageTransition(this, imageData.getPreview(), transitionImage,
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

        if (position != GalleryActivity.currentPosition) {
            showFullImage();
        }

        NetworkUtils.loadFullsizeImage(this, imageData.getFile(), this.fullImage, null);
        return view;
    }

    public void onTransitionEnd() {
        showFullImage();
    }

    public void onBackPressed() {
        showTransitionImage();
        getParentFragment().getFragmentManager().popBackStack();
    }

    public void resetImageZoom(){
        fullImage.setDisplayMatrix(new Matrix());
        fullImage.setSuppMatrix(new Matrix());
    }

    private void showFullImage(){
        fullImage.setVisibility(View.VISIBLE);
        //transitionImage.setVisibility(View.INVISIBLE);
    }

    private void showTransitionImage(){
        //transitionImage.setVisibility(View.VISIBLE);
        fullImage.setVisibility(View.INVISIBLE);
    }

}
