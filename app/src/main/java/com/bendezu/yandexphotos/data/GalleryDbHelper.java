package com.bendezu.yandexphotos.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bendezu.yandexphotos.data.GalleryDbContract.GalleryEntry;

public class GalleryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "gallery.db";

    private static final int DATABASE_VERSION = 2;

    public GalleryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_GALLERY_TABLE =
                "CREATE TABLE " + GalleryEntry.TABLE_NAME + " (" +
                        GalleryEntry._ID             + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GalleryEntry.COLUMN_NAME     + " TEXT NOT NULL, " +
                        GalleryEntry.COLUMN_PATH     + " TEXT NOT NULL," +
                        GalleryEntry.COLUMN_CREATED  + " TEXT NOT NULL, " +
                        GalleryEntry.COLUMN_FILE     + " TEXT NOT NULL, " +
                        GalleryEntry.COLUMN_PREVIEW  + " TEXT," +
                        GalleryEntry.COLUMN_MIME_TYPE+ " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_GALLERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GalleryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
