package com.bendezu.yandexphotos.gallery;

import android.database.Cursor;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Toast;

import com.bendezu.yandexphotos.App;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.data.ImageDataSet;
import com.bendezu.yandexphotos.util.PreferencesUtils;

import java.util.List;
import java.util.Map;

import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.NO_INTERNET_CONNECTION;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.SUCCESS;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.UNAUTHORIZED;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.UNEXPECTED;

public class GalleryFragmentPresenter implements GalleryContract.Presenter {

    private GalleryContract.View view;

    public GalleryFragmentPresenter(GalleryContract.View view) {
        this.view = view;
    }

    @Override
    public void scrollToPosition(final RecyclerView recyclerView) {

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3,
                                       int i4, int i5, int i6, int i7) {
                recyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(GalleryActivity.currentPosition);
                if (viewAtPosition == null || layoutManager.
                        isViewPartiallyVisible(viewAtPosition, false, true)) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPosition(GalleryActivity.currentPosition);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void prepareTransition(final RecyclerView recyclerView) {

        Transition exitTransition = TransitionInflater.from(App.getContext())
                .inflateTransition(R.transition.gallery_exit_transition);

        SharedElementCallback exitSharedElementCallback = new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                // Locate the ViewHolder for the clicked position.
                RecyclerView.ViewHolder selectedViewHolder = recyclerView
                        .findViewHolderForAdapterPosition(GalleryActivity.currentPosition);
                if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                    return;
                }
                // Map the first shared element name to the child ImageView.
                sharedElements
                        .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.item_image));
            }
        };

        view.setTransition(exitTransition, exitSharedElementCallback);
    }

    @Override
    public void setCursor(Cursor cursor, RecyclerView.Adapter adapter) {
        //update cursor
        ImageDataSet.setData(cursor);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void processResponseStatus(String status) {
        switch (status) {
            case SUCCESS:
                Toast.makeText(App.getContext(), status, Toast.LENGTH_SHORT).show();
                break;
            case UNAUTHORIZED:
                //reset token
                PreferencesUtils.setAccessToken(null, App.getContext());
                view.launchAuthScreen();
                break;
            case NO_INTERNET_CONNECTION:
                view.showNoConnectionMessage();
                break;
            case UNEXPECTED:
                Toast.makeText(App.getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new RuntimeException("Loader returns null status");
        }
        view.setSwipeRefreshing(false);
    }

}
