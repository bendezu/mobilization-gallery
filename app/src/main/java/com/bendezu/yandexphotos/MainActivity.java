package com.bendezu.yandexphotos;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bendezu.yandexphotos.fragment.AuthFragment;
import com.bendezu.yandexphotos.fragment.GalleryFragment;
import com.bendezu.yandexphotos.fragment.ImageDetailFragment;
import com.bendezu.yandexphotos.util.AuthUtils;
import com.bendezu.yandexphotos.util.UriUtils;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.DownloadListener;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;
import com.yandex.disk.rest.util.ResourcePath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ImageRecyclerViewAdapter.OnImageClickListener {

    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_CODE_ACCOUNT_MANAGER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String accessToken = preferences.getString(getString(R.string.saved_access_token_key), null);
        if (accessToken == null) {
            if  (savedInstanceState == null) {
                //Authorization
                AuthFragment authFragment = new AuthFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, authFragment)
                        .commit();
            }
        } else {
            OnAuthorizationSuccess(accessToken);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null &&
                uri.toString().startsWith(AuthUtils.REDIRECT_URI)){

            String error = UriUtils.getFragmentParameter(uri, "error");
            if (error != null) {
                Log.d(LOG_TAG, "Permission error: " + error);
                Toast.makeText(this, "DAY DOSTUP PIDOR", Toast.LENGTH_SHORT).show();

            }
            String accessToken = UriUtils.getFragmentParameter(uri, "access_token");
            if (accessToken != null) {
                Log.d(LOG_TAG, "Access token: " + accessToken);
                //Handle token
                // save to SharedPreferences
                getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putString(getString(R.string.saved_access_token_key), accessToken)
                        .apply();

                OnAuthorizationSuccess(accessToken);
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this, "Click to Image: " + position, Toast.LENGTH_SHORT).show();

        ImageDetailFragment detailImageFragment = new ImageDetailFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, detailImageFragment)
                .addToBackStack(ImageDetailFragment.class.getSimpleName())
                .commit();
    }

    public void OnAuthorizationSuccess(final String token) {
        new AsyncTaskRunner().execute(token);
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, Bundle> {

        @Override
        protected Bundle doInBackground(String... strings) {
            String token = strings[0];
            ArrayList<String> paths = new ArrayList<>();
            ArrayList<String> previews = new ArrayList<>();
            Credentials credentials = new Credentials("user", token);
            RestClient client = new RestClient(credentials);
            try {
                ResourcesArgs args = new ResourcesArgs.Builder()
                        .setMediaType("image")
                        .setLimit(Integer.MAX_VALUE)
                        //.setPreviewSize("S")
                        .setPreviewCrop(true)
                        .build();
                ResourceList resources = client.getLastUploadedResources(args);
                List<Resource> items = resources.getItems();
                for (Resource item : items) {
                    paths.add(item.getPath().getPath());
                    previews.add(item.getPreview());
                }

            } catch (IOException | ServerException e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            bundle.putStringArrayList("paths", paths);
            bundle.putStringArrayList("previews", previews);
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            GalleryFragment mGalleryFragment = new GalleryFragment();
            mGalleryFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mGalleryFragment)
                    .commit();
        }
    }
}
