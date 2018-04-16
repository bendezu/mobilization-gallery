package com.bendezu.yandexphotos.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class GalleryProvider extends ContentProvider {

    public static final int CODE_GALLERY = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private GalleryDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GalleryContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, GalleryContract.PATH_GALLERY, CODE_GALLERY);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new GalleryDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {

            case CODE_GALLERY:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(GalleryContract.GalleryEntry.TABLE_NAME, null, value);
                        if (_id != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case CODE_GALLERY: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        GalleryContract.GalleryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_GALLERY:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GalleryContract.GalleryEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("getType is not implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new RuntimeException("Insert is not implemented");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Update is not implemented");
    }

}
