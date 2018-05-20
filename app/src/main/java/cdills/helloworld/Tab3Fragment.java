package cdills.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import static com.google.android.gms.internal.zzahf.runOnUiThread;



public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";
    public static List sdbValues = new ArrayList<>();
    public static List<search> searchList = new ArrayList<search>();
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String androidID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        View view = inflater.inflate(R.layout.tab3_fragment, container, false);

        searchDBHelper sdbHelper = new searchDBHelper(getContext());
        SQLiteDatabase db = sdbHelper.getReadableDatabase();

        String[] projection = {
                activeSearchDB.searchDB._ID,
                activeSearchDB.searchDB.COLUMN_NAME_SUB,
                activeSearchDB.searchDB.COLUMN_NAME_QUERY,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = resultsDB.notifyDB.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"My Title"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                resultsDB.notifyDB._ID + " DESC";

        Cursor cursor = db.query(
                activeSearchDB.searchDB.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        //public List dbValues = new ArrayList<>();

        while (cursor.moveToNext()) {

            String itemId = cursor.getString(
                    cursor.getColumnIndexOrThrow(activeSearchDB.searchDB.COLUMN_NAME_QUERY));
            String subId = cursor.getString(
                    cursor.getColumnIndexOrThrow(activeSearchDB.searchDB.COLUMN_NAME_SUB));

            if (sdbValues.contains(itemId)) {
                continue;
            }
            search newSearch = new search(androidID, subId, itemId);
            searchList.add(newSearch);
            sdbValues.add(itemId);
            Log.d("item", itemId);
        }
        cursor.close();
        db.close();


        listView = (ListView) view.findViewById(R.id.slist);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, sdbValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                //  TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(searchList.get(position).getQuery());
                // text2.setText(resultList.get(position).getUrl());
                return view;
            }
        };


        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                final String id = searchList.get(position).getID();
                final String sub = searchList.get(position).getSub();
                final String query = searchList.get(position).getQuery();

                final int fPosition = position;

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Cancel Search");
                alert.setMessage("Cancel this search?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                // all networking code
                                // Create URL
                                // Have to catch this because java is dumb
                                try {
                                    URL monitorEndpoint = new URL("http://dillscody.pythonanywhere.com/ClearSingleSearch");
                                    // Create connection
                                    HttpURLConnection myConnection =
                                            (HttpURLConnection) monitorEndpoint.openConnection();
                                    myConnection.setRequestMethod("POST");
                                    String userDATA = "udid=" + id + "&sub=" + sub + "&query=" + query;

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

                        searchDBHelper sdbHelper = new searchDBHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = sdbHelper.getWritableDatabase();
                        db.execSQL("delete from " + activeSearchDB.searchDB.TABLE_NAME + " where " + activeSearchDB.searchDB.COLUMN_NAME_SUB + " = '" + sub + "' and " + activeSearchDB.searchDB.COLUMN_NAME_QUERY + " = '" + query + "'");
                        db.close();
                        sdbValues.remove(fPosition);
                        searchList.remove(fPosition);
                        if (listView != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //reload content
                                    arrayAdapter.notifyDataSetChanged();
                                    listView.invalidateViews();
                                    listView.refreshDrawableState();

                                }
                            });
                        }

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });
        return view;
    }
}
