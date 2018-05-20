package cdills.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by cdills on 10/16/2017.
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab1_fragment, container, false);

        btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //TODO Filter
                EditText subText = (EditText) view.findViewById(R.id.subText);
                final String subInput = subText.getText().toString().replaceAll(" ", "").toLowerCase();

                EditText searchText = (EditText) view.findViewById(R.id.searchText);
                final String searchInput = searchText.getText().toString().trim().toLowerCase();
                final String trimTest = searchInput.trim();
                Log.d("trim", searchInput);

                Context context = getActivity().getApplicationContext();
                CharSequence text = "Now monitoring r/" + subInput + " for \"" + searchInput + "\"";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


                //TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                final String androidID = Secure.getString(getContext().getContentResolver(),
                        Secure.ANDROID_ID);

                searchDBHelper sdbHelper = new searchDBHelper(context);
                SQLiteDatabase db = sdbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(activeSearchDB.searchDB.COLUMN_NAME_SUB, subInput);
                values.put(activeSearchDB.searchDB.COLUMN_NAME_QUERY, searchInput);
                long newRowId = db.insert(activeSearchDB.searchDB.TABLE_NAME, null, values);
                db.close();


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // all networking code
                        // Create URL
                        // Have to catch this because java is dumb
                        try {
                            URL monitorEndpoint = new URL("http://dillscody.pythonanywhere.com/CreateMonitor");
                            // Create connection
                            HttpURLConnection myConnection =
                                    (HttpURLConnection) monitorEndpoint.openConnection();
                            myConnection.setRequestMethod("POST");
                            String userDATA = "sub=" + subInput + "&query=" + searchInput + "&udid=" + androidID;


                            myConnection.setDoOutput(true);
                            myConnection.getOutputStream().write(userDATA.getBytes());

                            if (myConnection.getResponseCode() == 200) {
                                // Success
                                // Further processing here
                                myConnection.disconnect();
                                Log.d("Response", "OK");
                            } else {
                                // Error handling code goes here
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            }
        });

        return view;
    }
}


