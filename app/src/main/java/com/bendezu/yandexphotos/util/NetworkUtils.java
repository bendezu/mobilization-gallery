package com.bendezu.yandexphotos.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bendezu.yandexphotos.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    //OAuth URL constants
    private static final String OAUTH_URL = "https://oauth.yandex.ru/authorize";
    private static final String QUESTION_MARK = "?";
    private static final String RESPONSE_TYPE = "token";
    private static final String AMPERSAND = "&";
    private static final String CLIENT_ID = "27b1814298ef4a65907b9384adceafc4";
    public static final String REDIRECT_URI = "yandexphotos://token";

    public static String buildAuthUrl() {
        return OAUTH_URL + QUESTION_MARK
                + "response_type=" + RESPONSE_TYPE +
                AMPERSAND + "client_id=" + CLIENT_ID +
                AMPERSAND + "redirect_uri=" + REDIRECT_URI;
    }


    private static GlideUrl getGlideUrl(String imageUrl) {
        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization",
                        "OAuth " + PreferencesUtils.getAccessToken())
                .build());
    }

    private static void loadPlaceholder(RequestManager requestManager, ImageView view,
                                        RequestListener<Drawable> listener) {
        requestManager
                .load(R.mipmap.no_image)
                .listener(listener)
                .apply(new RequestOptions().dontTransform().override(Target.SIZE_ORIGINAL))
                .into(view);
    }

    public static void loadImageItem(RequestManager requestManager, String imageUrl,
                                     ImageView view, RequestListener<Drawable> listener) {
        if (imageUrl == null) {
            loadPlaceholder(requestManager, view, listener);
            return;
        }
        requestManager
                .load(getGlideUrl(imageUrl))
                .apply(new RequestOptions().dontTransform().override(Target.SIZE_ORIGINAL))
                .listener(listener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }

    public static void loadImageTransition(Fragment fragment, String thumbnailUrl,
                                           ImageView view, RequestListener<Drawable> listener) {
        if (thumbnailUrl == null) {
            loadPlaceholder(Glide.with(fragment), view, listener);
            return;
        }
        Glide.with(fragment)
                .load(getGlideUrl(thumbnailUrl))
                .apply(new RequestOptions().dontTransform().override(Target.SIZE_ORIGINAL))
                .listener(listener)
                .into(view);
    }

    public static void loadFullsizeImage(Fragment fragment, String imageUrl,
                                    ImageView view, RequestListener<Drawable> listener) {
        Glide.with(fragment)
                .load(getGlideUrl(imageUrl))
                .listener(listener)
                .into(view);
    }

    // Get file from url for sharing
    public static File loadImageToFile(Context context, String imageUrl) throws ExecutionException, InterruptedException {
        return Glide.with(context)
                .downloadOnly()
                .load(imageUrl)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

}
