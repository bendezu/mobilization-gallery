package com.bendezu.yandexphotos.data;

import android.accounts.AccountManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.OkHttpClientFactory;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.DiskInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageContent implements Parcelable {

    private String path;
    private String preview;

    static Parcelable.Creator CREATOR;

    public ImageContent(String path, String preview) {
        this.path = path;
        this.preview = preview;
    }

    public String getPath() {
        return path;
    }

    public String getPreview() {
        return preview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
