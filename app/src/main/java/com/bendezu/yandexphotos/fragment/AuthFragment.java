package com.bendezu.yandexphotos.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.util.AuthUtils;


public class AuthFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = "AuthFragment";

    private View mLogInButton;

    public AuthFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "OnAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        mLogInButton = view.findViewById(R.id.btn_log_in);
        mLogInButton.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(AuthUtils.buildAuthUrl()));
        startActivity(intent);
    }

}
