package com.bendezu.yandexphotos.adapter;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.gallery.GalleryActivity;
import com.bendezu.yandexphotos.gallery.ImageDetailFragment;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.bendezu.yandexphotos.App.getContext;

public class ViewHolderListenerImpl implements ImageRecyclerViewAdapter.ViewHolderListener {

    private Fragment fragment;
    private AtomicBoolean enterTransitionStarted;

    public ViewHolderListenerImpl(Fragment fragment) {
        this.fragment = fragment;
        enterTransitionStarted = new AtomicBoolean();
    }

    @Override
    public void onLoadCompleted(ImageView view, int position) {
        // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
        if (GalleryActivity.currentPosition != position) {
            return;
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return;
        }
        fragment.startPostponedEnterTransition();
    }

    /**
     * Handles a view click by setting the current position to the given {@code position} and
     * starting a {@link  ImageDetailFragment} which displays the image at the position.
     *
     * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
     * GridFragment's SharedElementCallback)
     * @param position the selected view position
     */
    @Override
    public void onItemClicked(View view, int position) {

        GalleryActivity.currentPosition = position;

        // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
        // instead of fading out with the rest to prevent an overlapping animation of fade and move).
        ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

        ImageView transitioningView = view.findViewById(R.id.item_image);

        this.fragment.getFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true) // Optimize for shared element transition
                .addSharedElement(transitioningView, transitioningView.getTransitionName())
                .replace(R.id.fragment_container, new ImageDetailFragment())
                .addToBackStack(null)
                .commit();
    }
}