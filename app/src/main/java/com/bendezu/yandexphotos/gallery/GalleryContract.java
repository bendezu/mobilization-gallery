package com.bendezu.yandexphotos.gallery;

import android.database.Cursor;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;


public interface GalleryContract {

    interface View {

        void setTransition(Transition transition, SharedElementCallback callback);

        void showNoConnectionMessage();

        void launchAuthScreen();

        void setSwipeRefreshing(boolean refreshing);

    }

    interface Presenter {

        void scrollToPosition(RecyclerView recyclerView);

        void prepareTransition(RecyclerView recyclerView);

        void setCursor(Cursor cursor, RecyclerView.Adapter adapter);

        void processResponseStatus(String status);

    }
}
