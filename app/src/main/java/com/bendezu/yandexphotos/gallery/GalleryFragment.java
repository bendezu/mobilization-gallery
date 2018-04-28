package com.bendezu.yandexphotos.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bendezu.yandexphotos.AboutActivity;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.authorization.AuthActivity;
import com.bendezu.yandexphotos.data.GalleryAsyncLoader;
import com.bendezu.yandexphotos.data.GalleryContract;
import com.bendezu.yandexphotos.util.PreferencesUtils;

import java.util.List;
import java.util.Map;

import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.NO_INTERNET_CONNECTION;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.SUCCESS;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.UNAUTHORIZED;
import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.UNEXPECTED;


public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ImageRecyclerViewAdapter mAdapter;
    private LoaderManager mLoaderManager;

    public GalleryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRecyclerView = view.findViewById(R.id.gallery_recycler_view);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((GalleryActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        int columnCount = getResources().getInteger(R.integer.galleryColumns);
        mAdapter = new ImageRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(
                R.color.swipe_refresh_1,
                R.color.swipe_refresh_2,
                R.color.swipe_refresh_3,
                R.color.swipe_refresh_4);

        prepareTransitions();
        postponeEnterTransition();

        mLoaderManager = getLoaderManager();
        mLoaderManager.restartLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
        mLoaderManager.initLoader(ID_CURSOR_LOADER, null, cursorLoaderCallbacks);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();
    }

    private static final int ID_UPDATE_LOADER = 111;
    private LoaderManager.LoaderCallbacks<String> updateLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case ID_UPDATE_LOADER:
                    return new GalleryAsyncLoader(getContext());
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            switch (data) {
                case SUCCESS:
                    Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                    break;
                case UNAUTHORIZED:
                    //reset token
                    PreferencesUtils.setAccessToken(null, getContext());
                    //launch Auth screen
                    Intent intent = new Intent(getContext(), AuthActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case NO_INTERNET_CONNECTION:
                    //show message
                    Snackbar.make(mRecyclerView, R.string.no_network_message, Snackbar.LENGTH_LONG).show();
                    break;
                case UNEXPECTED:
                    Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    throw new RuntimeException("Loader returns null status");
            }
            mSwipeRefresh.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private static final int ID_CURSOR_LOADER = 222;
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case ID_CURSOR_LOADER:
                    String[] projection = GalleryContract.GalleryEntry.getAllColumns();
                    return new CursorLoader(getContext(),
                            GalleryContract.GalleryEntry.CONTENT_URI,
                            projection, null, null, null);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            ImageRecyclerViewAdapter.mCursor = data;
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            ImageRecyclerViewAdapter.mCursor = null;
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onRefresh() {
        mLoaderManager.restartLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
    }

    private void scrollToPosition(){
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3,
                                       int i4, int i5, int i6, int i7) {
                mRecyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(GalleryActivity.currentPosition);
                if (viewAtPosition == null || layoutManager.
                        isViewPartiallyVisible(viewAtPosition, false, true)) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPosition(GalleryActivity.currentPosition);
                        }
                    });
                }
            }
        });
    }

    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.gallery_exit_transition));

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                // Locate the ViewHolder for the clicked position.
                RecyclerView.ViewHolder selectedViewHolder = mRecyclerView
                        .findViewHolderForAdapterPosition(GalleryActivity.currentPosition);
                if (selectedViewHolder == null || selectedViewHolder.itemView == null) {
                    return;
                }

                // Map the first shared element name to the child ImageView.
                sharedElements
                        .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.item_image));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}