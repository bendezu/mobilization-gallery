package com.bendezu.yandexphotos;


import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.URI;


public class AuthFragment extends Fragment {

    public static final String LOG_TAG = "AuthFragment";

    public AuthFragment() {
        // Required empty public constructor
    }

    private ProgressBar mLoadingIndicator;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        mLoadingIndicator = view.findViewById(R.id.loading_indicator);
        mWebView = view.findViewById(R.id.auth_webview);

        //Request focus for the webview
        mWebView.requestFocus(View.FOCUS_DOWN);

        //Show loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                //Remove loading indicator if is visible
                if (mLoadingIndicator != null &&
                        mLoadingIndicator.getVisibility() == View.VISIBLE) {
                    //mWebView.setVisibility(View.VISIBLE);
                    mLoadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String stringUri = uri.toString();
                Log.d(LOG_TAG, "shouldOverrideUrlLoading: " + uri);

                if (stringUri.startsWith(AuthUtils.CALLBACK_URL)){
                    //Access was denied or grant
                    String error = UriUtils.getFragmentParameter(uri, "error");
                    if (error != null) {
                        Log.d(LOG_TAG, "Permission error: " + error);
                        mWebView.loadUrl(AuthUtils.getForceAuthUrl());
                    }
                    String accessToken = UriUtils.getFragmentParameter(uri, "access_token");
                    if (accessToken != null) {
                        Log.d(LOG_TAG, "Access token: " + accessToken);
                        //Handle token

                        return true;
                    }
                }

                return false;
            }
        });

        //mWebView.loadUrl(AuthUtils.getAuthUrl());
        mWebView.loadUrl(AuthUtils.getForceAuthUrl());

        return view;

    }

}
