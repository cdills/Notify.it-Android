package cdills.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by cdills on 10/18/2017.
 */

public class dataRefresher {

    public static void dataRefresh(Context mcon, ListView listView, String DB) {
        //reload content
        dbHelper dbHelper = new dbHelper(mcon, DB);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // listView.invalidateViews();
        //Tab2Fragment.dbValues.clear();


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

            if (Tab2Fragment.dbValues.contains(itemId)) {
                continue;
            }

            Tab2Fragment.dbValues.add(itemId);
            result newResult = new result(itemId, itemURL);
            Tab2Fragment.resultList.add(newResult);

        }
        cursor.close();
        db.close();

        //View view = inflater.inflate(R.layout.tab2_fragment, container, false);
        //listView = (ListView) view.findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mcon, android.R.layout.simple_list_item_1, android.R.id.text1, Tab2Fragment.dbValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);


                text1.setText(Tab2Fragment.resultList.get(position).getTitle());
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.refreshDrawableState();
    }


}
