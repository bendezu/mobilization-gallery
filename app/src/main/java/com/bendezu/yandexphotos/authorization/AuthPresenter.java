package com.bendezu.yandexphotos.authorization;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bendezu.yandexphotos.util.NetworkUtils;
import com.bendezu.yandexphotos.util.PreferencesUtils;
import com.bendezu.yandexphotos.util.UriUtils;

public class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View view;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
    }

    @Override
    public void startAuthProcess(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(NetworkUtils.buildAuthUrl()));
        context.startActivity(intent);
    }

    @Override
    public void verifyAccess(Intent intent, Context context) {
        Uri uri = intent.getData();
        if (uri != null &&
                uri.toString().startsWith(NetworkUtils.REDIRECT_URI)){

            String error = UriUtils.getFragmentParameter(uri, "error");
            if (error != null) {
                view.showErrorToast();
            }
            String accessToken = UriUtils.getFragmentParameter(uri, "access_token");
            if (accessToken != null) {
                PreferencesUtils.setAccessToken(accessToken, context);
                view.launchGallery();
            }
        }
    }

    @Override
    public String getAccessToken(Context context) {
        return PreferencesUtils.getAccessToken(context);
    }
}
