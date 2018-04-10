package com.bendezu.yandexphotos.data;

import android.accounts.AccountManager;
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


public class ImageContent {

    private static final String LOG_TAG = "ImageContent";
    private static final int COUNT = 25;
    private static String TOKEN;

    public static List<ImageData> getImageData(String token){
        TOKEN = token;

        List<ImageData> images = new ArrayList<>(25);
        for (int i = 1; i <= COUNT; i++) {
            images.add(fetchImageData(i));
        }
        return images;
    }

    private static ImageData fetchImageData(int position) {
        String id = String.valueOf(position);
        String content = "Item " + position;
        return new ImageData(id, content);
    }


    public static class ImageData {

        private final String id;
        private final String content;

        public ImageData(String id, String content) {
            this.id = id;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
