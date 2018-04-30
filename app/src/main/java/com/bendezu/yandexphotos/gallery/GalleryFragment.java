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
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.AboutActivity;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.authorization.AuthActivity;
import com.bendezu.yandexphotos.data.GalleryAsyncLoader;
import com.bendezu.yandexphotos.data.GalleryDbContract;


public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        GalleryContract.View {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ImageRecyclerViewAdapter recyclerViewAdapter;
    private LoaderManager loaderManager;

    private GalleryContract.Presenter presenter;

    public GalleryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((GalleryActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        presenter = new GalleryFragmentPresenter(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int columnCount = getResources().getInteger(R.integer.galleryColumns);
        recyclerViewAdapter = new ImageRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        recyclerView.setHasFixedSize(true);

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(
                R.color.swipe_refresh_1,
                R.color.swipe_refresh_2,
                R.color.swipe_refresh_3,
                R.color.swipe_refresh_4);

        presenter.prepareTransition(recyclerView);
        postponeEnterTransition();

        loaderManager = getLoaderManager();
        loaderManager.restartLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
        loaderManager.initLoader(ID_CURSOR_LOADER, null, cursorLoaderCallbacks);

        presenter.scrollToPosition(recyclerView);
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
            presenter.processResponseStatus(data);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) { }
    };

    private static final int ID_CURSOR_LOADER = 222;
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case ID_CURSOR_LOADER:
                    String[] projection = GalleryDbContract.GalleryEntry.getAllColumns();
                    return new CursorLoader(getContext(),
                            GalleryDbContract.GalleryEntry.CONTENT_URI,
                            projection, null, null, null);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            presenter.setCursor(data, recyclerViewAdapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            presenter.setCursor(null, recyclerViewAdapter);
        }
    };

    @Override
    public void onRefresh() {
        loaderManager.restartLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
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

    @Override
    public void setTransition(Transition transition, SharedElementCallback callback) {
        setExitTransition(transition);
        setExitSharedElementCallback(callback);
    }

    @Override
    public void showNoConnectionMessage() {
        Snackbar.make(recyclerView, R.string.no_network_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void launchAuthScreen() {
        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setSwipeRefreshing(boolean refreshing) {
        swipeRefresh.setRefreshing(refreshing);
    }
}