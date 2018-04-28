package com.bendezu.yandexphotos.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class GalleryContract {

    public static final String CONTENT_AUTHORITY = "com.bendezu.yandexphotos";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_GALLERY = "gallery";

    // Inner class that defines the table contents of the gallery table
    public static final class GalleryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_GALLERY)
                .build();

        public static final String TABLE_NAME = "gallery";

        // Name of the image file
        public static final String COLUMN_NAME = "name";

        // Full path to the file
        public static final String COLUMN_PATH = "path";

        // Creation date in ISO 8601 format
        public static final String COLUMN_CREATED = "created";

        // URL for original size image
        public static final String COLUMN_FILE = "file";

        // URL for preview of image
        public static final String COLUMN_PREVIEW = "preview";

        public static final String COLUMN_MIME_TYPE = "mime_type";

        public static String[] getAllColumns() {
            return new String[]{
                    COLUMN_NAME,
                    COLUMN_PATH,
                    COLUMN_CREATED,
                    COLUMN_FILE,
                    COLUMN_PREVIEW,
                    COLUMN_MIME_TYPE
            };
        }

        public static final int INDEX_COLUMN_NAME = 0;
        public static final int INDEX_COLUMN_PATH = 1;
        public static final int INDEX_COLUMN_CREATED = 2;
        public static final int INDEX_COLUMN_FILE = 3;
        public static final int INDEX_COLUMN_PREVIEW = 4;
        public static final int INDEX_COLUMN_MIME_TYPE = 5;
    }
}
