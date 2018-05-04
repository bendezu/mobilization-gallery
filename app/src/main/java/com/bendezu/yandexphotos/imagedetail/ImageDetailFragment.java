package com.bendezu.yandexphotos.imagedetail;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageViewPagerAdapter;
import com.bendezu.yandexphotos.data.ImageData;
import com.bendezu.yandexphotos.data.ShareTaskLoader;
import com.bendezu.yandexphotos.gallery.GalleryActivity;
import com.bendezu.yandexphotos.util.DimUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ImageDetailFragment extends Fragment {

    public static final String TAG = "ImageDetailFragment";
    private final int ID_SHARE_LOADER = 333;

    @BindView(R.id.image_view_pager) ViewPager viewPager;
    @BindView(R.id.back_button) ImageButton backButton;
    @BindView(R.id.share_button) ImageButton shareButton;
    @BindView(R.id.toolbar) FrameLayout toolbar;
    @BindView(R.id.image_counter) TextView counter;

    private ImageViewPagerAdapter viewPagerAdapter;
    private Unbinder unbinder;

    public ImageDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setPageTransformer(true, new ParallaxPageTransformer());
        viewPager.setPageMargin(Math.round(DimUtils.dpToPx(getContext(), 16)));
        viewPagerAdapter = new ImageViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(GalleryActivity.currentPosition);
        setCounter(GalleryActivity.currentPosition + 1);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                viewPagerAdapter.getCurrentFragment().resetImageZoom();
                GalleryActivity.currentPosition = position;
                setCounter(position + 1);
            }
        });

        prepareSharedElementTransition();
        if (savedInstanceState == null){
            postponeEnterTransition();
        }
    }

    @OnClick(R.id.back_button)
    public void onBackPressed() {
        getActivity().onBackPressed();
    }
    public ImageFragment getCurrentFragment(){
        return viewPagerAdapter.getCurrentFragment();
    }

    @OnClick(R.id.share_button)
    public void shareImage(){

        final ImageData imageData = viewPagerAdapter.getCurrentFragment().getImageData();
        final ProgressDialog progressDialog  = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.progress_title));
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                getLoaderManager().destroyLoader(ID_SHARE_LOADER);
            }
        });
        progressDialog.show();

        getLoaderManager().restartLoader(ID_SHARE_LOADER, null, new LoaderManager.LoaderCallbacks<Uri>() {
            @Override
            public Loader<Uri> onCreateLoader(int id, Bundle args) {
                switch (id) {
                    case ID_SHARE_LOADER:
                        return new ShareTaskLoader(getContext(), imageData.getFile());
                    default:
                        throw new RuntimeException("Loader Not Implemented: " + id);
                }
            }
            @Override
            public void onLoadFinished(Loader<Uri> loader, Uri uri) {
                if (uri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType(imageData.getMimeType());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, imageData.getName());
                    shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    getLoaderManager().destroyLoader(ID_SHARE_LOADER);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image_chooser_title)));
                }
            }
            @Override
            public void onLoaderReset(Loader<Uri> loader) { }
        });
    }

    public void onImageClicked() {
        if  (toolbar.getVisibility() == View.VISIBLE){
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .setListener(new AnimatorListenerAdapter(){
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            toolbar.setVisibility(View.GONE);
                        }
                    });
        } else {
            toolbar.animate().setListener(null);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.animate()
                    .translationY(0f);
        }
    }

    private void setCounter(int position) {
        String strCounter = getString(R.string.image_counter);
        counter.setText(String.format(strCounter, position, viewPagerAdapter.getCount()));
    }

    private void prepareSharedElementTransition() {

        Transition enterTransition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.image_shared_element_transition);
        enterTransition.addListener(new SimpleTransitionListener(){
            @Override
            public void onTransitionEnd(Transition transition) {
                viewPagerAdapter.getCurrentFragment().onTransitionEnd();
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

                Fragment currentFragment = (Fragment) viewPager.getAdapter()
                        .instantiateItem(viewPager, GalleryActivity.currentPosition);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}