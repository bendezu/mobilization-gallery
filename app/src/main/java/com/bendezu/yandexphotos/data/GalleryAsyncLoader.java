package com.bendezu.yandexphotos.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.bendezu.yandexphotos.MainActivity;
import com.bendezu.yandexphotos.rest.Resource;
import com.bendezu.yandexphotos.rest.ResourceList;
import com.bendezu.yandexphotos.rest.ResourcesArgs;
import com.bendezu.yandexphotos.rest.RestClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/* Loader fetches image data from the server, then clears database table
*  and inserts fresh data into that table.
*  Returns status of response from server.
*/
public class GalleryAsyncLoader extends AsyncTaskLoader<String> {

    public static final String NO_INTERNET_CONNECTION = "no_internet_connection";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String SUCCESS = "success";
    public static final String UNEXPECTED = "unexpected";

    private String responseStatus = null;

    public GalleryAsyncLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (responseStatus != null) {
            deliverResult(responseStatus);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {

        String token = MainActivity.token;
        String status;
        RestClient client = new RestClient(token);
        ResourcesArgs args = new ResourcesArgs.Builder()
                .setMediaType("image")
                .setLimit(Integer.MAX_VALUE)
                .setPreviewSize("M")
                .build();
        Call<ResourceList> call = client.getLastUploadedResources(args);
        try {
            Response<ResourceList> response = call.execute();
            if (response.isSuccessful()) {
                status = SUCCESS;
                List<Resource> resources = response.body().getItems();
                truncateTable();
                insertData(resources);
            } else {
                switch (response.code()) {
                    case 401:
                        status = UNAUTHORIZED;
                        break;
                    default:
                        status = UNEXPECTED;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            status = NO_INTERNET_CONNECTION;
        }
        return status;
    }

    private void truncateTable() {
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.delete(GalleryContract.GalleryEntry.CONTENT_URI,
                null, null);
    }

    private void insertData(List<Resource> resources) {

        ContentValues[] values = new ContentValues[resources.size()];
        for (int i = 0; i < values.length; i++) {

            Resource resource = resources.get(i);
            String name = resource.getName();
            String path = resource.getPath();
            String created = resource.getCreated();
            String file = resource.getFile();
            String preview = resource.getPreview();

            ContentValues cv = new ContentValues();
            cv.put(GalleryContract.GalleryEntry.COLUMN_NAME, name);
            cv.put(GalleryContract.GalleryEntry.COLUMN_PATH, path);
            cv.put(GalleryContract.GalleryEntry.COLUMN_CREATED, created);
            cv.put(GalleryContract.GalleryEntry.COLUMN_FILE, file);
            cv.put(GalleryContract.GalleryEntry.COLUMN_PREVIEW, preview);
            values[i] = cv;
        }
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.bulkInsert(GalleryContract.GalleryEntry.CONTENT_URI, values);
    }

    @Override
    public void deliverResult(String data) {
        responseStatus = data;
        super.deliverResult(data);
    }

}
