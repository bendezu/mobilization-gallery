package com.bendezu.yandexphotos;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.bendezu.yandexphotos.fragment.AuthFragment;
import com.bendezu.yandexphotos.fragment.GalleryFragment;
import com.bendezu.yandexphotos.fragment.ImageDetailFragment;
import com.bendezu.yandexphotos.util.AuthUtils;
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
        ImageRecyclerViewAdapter.OnImageClickListener,
        AuthFragment.OnAuthorizationListener{

    private static final String LOG_TAG = "MainActivity";

    private GalleryFragment mGalleryFragment;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if  (savedInstanceState == null) {
            //Authorization
            AuthFragment authFragment = new AuthFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, authFragment)
                    .commit();
        }
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

    @Override
    public void OnAuthorizationSuccess(final String token) {
        mToken = token;
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
            mGalleryFragment = new GalleryFragment();
            mGalleryFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mGalleryFragment)
                    .commit();
        }
    }
}
