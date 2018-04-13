package com.bendezu.yandexphotos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bendezu.yandexphotos.adapter.ImageRecyclerViewAdapter;
import com.bendezu.yandexphotos.fragment.AuthFragment;
import com.bendezu.yandexphotos.fragment.GalleryFragment;
import com.bendezu.yandexphotos.fragment.ImageDetailFragment;
import com.bendezu.yandexphotos.util.AuthUtils;
import com.bendezu.yandexphotos.util.UriUtils;
import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.exceptions.http.HttpCodeException;
import com.yandex.disk.rest.exceptions.http.UnauthorizedException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final String KEY_CURRENT_IMAGE_POSITION = "currentImagePosition";

    public static int currentPosition;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String accessToken = preferences.getString(getString(R.string.saved_access_token_key), null);

        token = accessToken;

        if (savedInstanceState == null) {
            if (accessToken == null){
                //Authorization
                AuthFragment authFragment = new AuthFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, authFragment)
                        .commit();
            } else {
                OnAuthorizationSuccess(accessToken);
            }
        } else {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_IMAGE_POSITION, 0);
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
                Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();

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

            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.activity_container),
                        "NO INTERNET CONNECTION", Snackbar.LENGTH_LONG).show();
            } catch (UnauthorizedException e) {
                //token expired
                e.printStackTrace();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(AuthUtils.getAuthUrl()));
                startActivity(intent);
            }
            catch (HttpCodeException e) {
                e.printStackTrace();
            }
            catch (ServerException e) {
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
                    .setCustomAnimations(R.anim.nothing, R.anim.slide_out_up)
                    .replace(R.id.fragment_container, mGalleryFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_IMAGE_POSITION, currentPosition);

    }
}
