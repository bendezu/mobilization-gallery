package com.bendezu.yandexphotos.gallery;

import android.support.v4.app.Fragment;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ImageView;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.imagedetail.ImageDetailFragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewHolderListenerImpl implements ImageRecyclerViewAdapter.ViewHolderListener {

    private final Fragment fragment;
    private final AtomicBoolean enterTransitionStarted;

    public ViewHolderListenerImpl(Fragment fragment) {
        this.fragment = fragment;
        enterTransitionStarted = new AtomicBoolean();
    }

    @Override
    public void onLoadCompleted(ImageView view, int position) {
        // Call startPostponedEnterTransition only when the selected image loading is completed.
        if (GalleryActivity.currentPosition != position) {
            return;
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return;
        }
        fragment.startPostponedEnterTransition();
    }

    /*
    Handles a view click by setting the current position to the given position and
    starting a ImageDetailFragment which displays the image at the position.
    */
    @Override
    public void onItemClicked(View view, int position) {

        GalleryActivity.currentPosition = position;

        // Exclude the clicked card from the exit transition.
        ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

        ImageView transitioningView = view.findViewById(R.id.item_image);

        this.fragment.getFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(transitioningView, transitioningView.getTransitionName())
                .replace(R.id.fragment_container, new ImageDetailFragment(), ImageDetailFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}