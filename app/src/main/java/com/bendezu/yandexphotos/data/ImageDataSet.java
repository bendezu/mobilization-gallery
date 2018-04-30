package com.bendezu.yandexphotos.data;

import android.database.Cursor;

public class ImageDataSet {

    private static Cursor data;

    private ImageDataSet() { }

    public static void setData(Cursor cursor) {
        data = cursor;
    }

    public static int getCount(){
        if (data == null) return 0;
        return data.getCount();
    }

    public static ImageData getImageData(int position) {

        data.moveToPosition(position);
        return new ImageData(
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_NAME),
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_PATH),
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_CREATED),
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_FILE),
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_PREVIEW),
                data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_MIME_TYPE)
        );
    }

    public static String getPreviewUrl(int position) {
        data.moveToPosition(position);
        return data.getString(GalleryDbContract.GalleryEntry.INDEX_COLUMN_PREVIEW);
    }
}
