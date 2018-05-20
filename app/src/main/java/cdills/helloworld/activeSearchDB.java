package cdills.helloworld;

import android.provider.BaseColumns;

/**
 * Created by cdills on 10/17/2017.
 */

public final class activeSearchDB {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + searchDB.TABLE_NAME + " (" +
                    searchDB._ID + " INTEGER PRIMARY KEY," +
                    searchDB.COLUMN_NAME_SUB + " TEXT," +
                    searchDB.COLUMN_NAME_QUERY + " TEXT)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + searchDB.TABLE_NAME;


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private activeSearchDB() {
    }

    /* Inner class that defines the table contents */
    public static class searchDB implements BaseColumns {
        public static final String TABLE_NAME = "active_searches";
        public static final String COLUMN_NAME_SUB = "sub";
        public static final String COLUMN_NAME_QUERY = "query";
    }

}
