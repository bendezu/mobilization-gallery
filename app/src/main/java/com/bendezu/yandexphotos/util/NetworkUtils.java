package com.bendezu.yandexphotos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bendezu.yandexphotos.App;
import com.bendezu.yandexphotos.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.request.target.Target;

public class NetworkUtils {

    public static final String PASSWORD = "f9e3d8ad13624c0985b2da84af91b310";

    //OAuth URL constants
    public static final String OAUTH_URL = "https://oauth.yandex.ru/authorize";
    public static final String QUESTION_MARK = "?";
    public static final String RESPONSE_TYPE = "token";
    public static final String AMPERSAND = "&";
    public static final String CLIENT_ID = "27b1814298ef4a65907b9384adceafc4";
    public static final String REDIRECT_URI = "yandexphotos://token";
    public static final String FORCE_CONFIRM = "true";

    public static String buildAuthUrl(){
        return OAUTH_URL + QUESTION_MARK
                + "response_type=" + RESPONSE_TYPE +
                AMPERSAND + "client_id=" + CLIENT_ID +
                AMPERSAND + "redirect_uri=" + REDIRECT_URI;
    }

    //Forces authorization even if user has already authorized
    public static String buildForceAuthUrl(){
        return buildAuthUrl() + AMPERSAND + "force_confirm=" + FORCE_CONFIRM;
    }


    private static GlideUrl getGlideUrl(String imageUrl){
        return new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("Authorization",
                        "OAuth " + PreferencesUtils.getAccessToken(App.getContext()))
                .build());
    }

    public static void loadImage(RequestManager requestManager, String imageUrl,
                                 ImageView view, RequestListener<Drawable> listener){
        if (imageUrl == null) {
            view.setImageResource(R.drawable.no_image);
            return;
        }
        requestManager
                .load(getGlideUrl(imageUrl))
                .apply(new RequestOptions().dontTransform().override(Target.SIZE_ORIGINAL))
                .listener(listener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }

    public static void loadImageDetail(Fragment fragment, String imageUrl, String thumbnailUrl,
                                       ImageView view, RequestListener<Drawable> listener,
                                       RequestListener<Drawable> thumbnailListener){
        if (thumbnailUrl == null) {
            view.setImageResource(R.drawable.no_image);
            return;
        }
//        RequestBuilder<Drawable> thumbnailRequest = Glide
//                .with(fragment)
//                .load(thumbnailUrl)
//                .apply(new RequestOptions().dontTransform())
//                .listener(thumbnailListener);
        Glide.with(fragment)
                .load(getGlideUrl(thumbnailUrl))
                .apply(new RequestOptions().dontTransform().override(Target.SIZE_ORIGINAL))
                //.thumbnail(thumbnailRequest)
                .listener(listener)
                .into(view);
    }

}
