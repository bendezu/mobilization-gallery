package com.bendezu.yandexphotos.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bendezu.yandexphotos.MainActivity;
import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.data.GalleryAsyncLoader;
import com.bendezu.yandexphotos.data.GalleryContract;

import static com.bendezu.yandexphotos.data.GalleryAsyncLoader.*;


public class GalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = "GalleryFragment";
    private int mColumnCount;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ImageRecyclerViewAdapter mAdapter;
    private LoaderManager mLoaderManager;

    public GalleryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.gallery_recycler_view);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        mColumnCount = getResources().getInteger(R.integer.galleryColumns);
        mAdapter = new ImageRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefresh.setOnRefreshListener(this);

        //prepareTransitions();
        //postponeEnterTransition();

        mLoaderManager = getLoaderManager();
        if (savedInstanceState == null) {
            mLoaderManager.initLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
            mLoaderManager.initLoader(ID_CURSOR_LOADER, null, cursorLoaderCallbacks);
        }

        return view;
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
                    //nothing to do
                    break;
                case UNAUTHORIZED:
                    //launch Auth screen
                    AuthFragment authFragment = new AuthFragment();
                    FragmentTransaction transaction = getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_down, R.anim.nothing)
                            .replace(R.id.fragment_container, authFragment);
                    if (!isStateSaved())
                        transaction.commitAllowingStateLoss();
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
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null)
            scrollToPosition();
    }

    private void scrollToPosition(){
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3,
                                       int i4, int i5, int i6, int i7) {
                mRecyclerView.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
                if (viewAtPosition == null || layoutManager.
                        isViewPartiallyVisible(viewAtPosition, false, true)) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPosition(MainActivity.currentPosition);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mLoaderManager.restartLoader(ID_UPDATE_LOADER, null, updateLoaderCallbacks);
    }
}