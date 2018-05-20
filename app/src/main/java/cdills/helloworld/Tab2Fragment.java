package cdills.helloworld;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cdills on 10/16/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";
    public static List dbValues = new ArrayList<>();
    public static List<result> resultList = new ArrayList<result>();
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbHelper dbHelper = new dbHelper(getContext(), "notifyDB");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                resultsDB.notifyDB._ID,
                resultsDB.notifyDB.COLUMN_NAME_TITLE,
                resultsDB.notifyDB.COLUMN_NAME_URL,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = resultsDB.notifyDB.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"My Title"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                resultsDB.notifyDB._ID + " DESC";

        Cursor cursor = db.query(
                resultsDB.notifyDB.TABLE_NAME,                     // The table to query
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
                    cursor.getColumnIndexOrThrow(resultsDB.notifyDB.COLUMN_NAME_TITLE));
            String itemURL = cursor.getString(
                    cursor.getColumnIndexOrThrow(resultsDB.notifyDB.COLUMN_NAME_URL));

            if (dbValues.contains(itemId)) {
                continue;
            }
            dbValues.add(itemId);
            result newResult = new result(itemId, itemURL);
            resultList.add(newResult);
            Log.d("item", itemId);
        }
        cursor.close();
        db.close();

        View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, dbValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                //  TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(resultList.get(position).getTitle());
                // text2.setText(resultList.get(position).getUrl());
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Object o = listView.getItemAtPosition(position);
                String u = resultList.get(position).getUrl();
                Uri uri = Uri.parse(u);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });


        return view;


    }
}
