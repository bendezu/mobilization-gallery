package com.bendezu.yandexphotos.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.FileProvider;

import com.bendezu.yandexphotos.util.NetworkUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;

// Loader loads image from URL into file and returns URI of that file
public class ShareTaskLoader extends AsyncTaskLoader<Uri> {

    private final String url;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public ShareTaskLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Uri loadInBackground() {
        File file;
        try {
            file = NetworkUtils.loadImageToFile(getContext(), url);
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
        return FileProvider.getUriForFile(getContext(), "com.bendezu.yandexphotos.fileprovider", file);
    }
}
