package cdills.helloworld;

import android.provider.BaseColumns;

/**
 * Created by cdills on 10/17/2017.
 */

public final class resultsDB {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + notifyDB.TABLE_NAME + " (" +
                    notifyDB._ID + " INTEGER PRIMARY KEY," +
                    notifyDB.COLUMN_NAME_TITLE + " TEXT," +
                    notifyDB.COLUMN_NAME_URL + " TEXT)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + resultsDB.notifyDB.TABLE_NAME;


    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private resultsDB() {
    }

    /* Inner class that defines the table contents */
    public static class notifyDB implements BaseColumns {
        public static final String TABLE_NAME = "results";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_URL = "url";
    }


}
