package com.bendezu.yandexphotos.gallery;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageViewPagerAdapter;
import com.bendezu.yandexphotos.util.DimUtils;

import java.util.List;
import java.util.Map;


public class ImageDetailFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ImageDetailFragment";

    private ViewPager mViewPager;
    private ImageViewPagerAdapter mViewPagerAdapter;
    private ImageButton mBackButton;
    private ImageButton mShareButton;
    private FrameLayout mToolbar;

    public ImageDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);

        mViewPager = view.findViewById(R.id.image_view_pager);
        mBackButton = view.findViewById(R.id.back_button);
        mShareButton = view.findViewById(R.id.share_button);
        mToolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBackButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);

        mViewPager.setPageTransformer(true, new ParallaxPageTransformer());
        mViewPager.setPageMargin(Math.round(DimUtils.dpToPx(getContext(), 16)));
        mViewPagerAdapter = new ImageViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(GalleryActivity.currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mViewPagerAdapter.getCurrentFragment().resetImageZoom();
                GalleryActivity.currentPosition = position;
            }
        });

        prepareSharedElementTransition();

        if (savedInstanceState == null){
            postponeEnterTransition();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.share_button:
                showProcessDialog();
                break;
        }
    }

    public void onBackPressed() {
        mViewPagerAdapter.getCurrentFragment().onBackPressed();
    }

    private void showProcessDialog(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.progress_title));
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.show();
    }

    public void onImageClicked() {
        if  (mToolbar.getVisibility() == View.VISIBLE){
            mToolbar.animate()
                    .translationY(-mToolbar.getHeight())
                    .setListener(new AnimatorListenerAdapter(){
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mToolbar.setVisibility(View.GONE);
                        }
                    });
        } else {
            mToolbar.animate().setListener(null);
            mToolbar.setVisibility(View.VISIBLE);
            mToolbar.animate()
                    .translationY(0f);
        }
    }

    private void prepareSharedElementTransition() {

        Transition enterTransition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        enterTransition.addListener(new SimpleTransitionListener(){
            @Override
            public void onTransitionEnd(Transition transition) {
                mViewPagerAdapter.getCurrentFragment().onTransitionEnd();
            }
        });

        Transition returnTransition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);

        setSharedElementEnterTransition(enterTransition);
        setSharedElementReturnTransition(returnTransition);
        setEnterTransition(new Fade());

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

                Fragment currentFragment = (Fragment) mViewPager.getAdapter()
                        .instantiateItem(mViewPager, GalleryActivity.currentPosition);
                View view = currentFragment.getView();
                if (view == null) {
                    return;
                }

                // Map the first shared element name to the child ImageView.
                View sharedView = view.findViewById(R.id.iv_image);
                sharedElements.put(names.get(0), sharedView);
            }
        });
    }
}