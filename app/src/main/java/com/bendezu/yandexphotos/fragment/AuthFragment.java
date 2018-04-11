package com.bendezu.yandexphotos.fragment;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.util.AuthUtils;
import com.bendezu.yandexphotos.util.UriUtils;


public class AuthFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = "AuthFragment";

    private Button mLogInButton;

    public AuthFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "OnAttach");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        mLogInButton = view.findViewById(R.id.btn_log_in);
        mLogInButton.setOnClickListener(this);

        /*

            @Override
            public void onPageFinished(WebView view, String url) {
                //Remove loading indicator if is visible
                if (mLoadingIndicator != null &&
                        mLoadingIndicator.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                    mLoadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                return shouldOverrideUrlLoading(view, uri);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                return shouldOverrideUrlLoading(view, uri);
            }

            private boolean shouldOverrideUrlLoading(WebView view, Uri uri){
                String stringUri = uri.toString();
                Log.d(LOG_TAG, "shouldOverrideUrlLoading: " + uri);

                if (stringUri.startsWith(AuthUtils.CALLBACK_URL)){
                    //Access was denied or grant
                    String error = UriUtils.getFragmentParameter(uri, "error");
                    if (error != null) {
                        Log.d(LOG_TAG, "Permission error: " + error);
                        view.loadUrl(AuthUtils.getForceAuthUrl());
                    }
                    String accessToken = UriUtils.getFragmentParameter(uri, "access_token");
                    if (accessToken != null) {
                        Log.d(LOG_TAG, "Access token: " + accessToken);
                        //Handle token
                        mActivity.OnAuthorizationSuccess(accessToken);
                        return true;
                    }
                }
                return false;
            }
        });

        mWebView.loadUrl(AuthUtils.getAuthUrl());
        //mWebView.loadUrl(AuthUtils.getForceAuthUrl());
        */
        return view;

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(AuthUtils.getAuthUrl()));
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        Log.d(LOG_TAG, "OnDetach");
        super.onDetach();
    }
}
