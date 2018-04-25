package com.bendezu.yandexphotos.gallery;


import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bendezu.yandexphotos.App;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageViewPagerAdapter;

import java.util.List;
import java.util.Map;


public class ImageDetailFragment extends Fragment implements View.OnClickListener {

    public interface ImageDetailListener{
        void onTransitionEnd();
        void onBackPressed();
    }

    public static final String TAG = "ImageDetailFragment";

    private ViewPager mViewPager;
    private ImageViewPagerAdapter mViewPagerAdapter;
    private ImageButton mBackButton;
    private ImageButton mShareButton;

    public ImageDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);

        mViewPager = view.findViewById(R.id.image_view_pager);
        mBackButton = view.findViewById(R.id.back_button);
        mShareButton = view.findViewById(R.id.share_button);

        mBackButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);

        mViewPagerAdapter = new ImageViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(GalleryActivity.currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                GalleryActivity.currentPosition = position;
            }
        });

        prepareSharedElementTransition();

        if (savedInstanceState == null){
            postponeEnterTransition();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.share_button:
                //TODO
                break;
        }
    }

    public void onBackPressed() {
        mViewPagerAdapter.getCurrentFragment().onBackPressed();
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
                // Locate the image view at the primary fragment (the ImageFragment that is currently
                // visible). To locate the fragment, call instantiateItem with the selection position.
                // At this stage, the method will simply return the fragment at the position and will
                // not create a new one.
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