package com.example.taller_1_2p;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    private FeedReaderContract() {
    }

    public static final class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "persona";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_APELLIDO = "apellido";

        public static final String nameTable = TABLE_NAME;
        public static final String name_Table = TABLE_NAME;
        public static final String column1 = COLUMN_NAME_NOMBRE;
        public static final String column2 = COLUMN_NAME_APELLIDO;
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FeedEntry.COLUMN_NAME_NOMBRE + " TEXT NOT NULL, " +
                    FeedEntry.COLUMN_NAME_APELLIDO + " TEXT NOT NULL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
