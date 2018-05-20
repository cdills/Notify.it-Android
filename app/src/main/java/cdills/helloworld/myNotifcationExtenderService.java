package cdills.helloworld;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by cdills on 10/18/2017.
 */

public class myNotifcationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read properties from result.
        String notifTitle = receivedResult.payload.title;
        String notifBody = receivedResult.payload.body;

        dbHelper dbHelper = new dbHelper(getApplicationContext(), "notifyDB");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(resultsDB.notifyDB.COLUMN_NAME_TITLE, notifTitle);
        values.put(resultsDB.notifyDB.COLUMN_NAME_URL, notifBody);
        long newRowId = db.insert(resultsDB.notifyDB.TABLE_NAME, null, values);
        db.close();

        Log.d("DIDGET", notifTitle);
        // Return true to stop the notification from displaying.
        return false;
    }
}