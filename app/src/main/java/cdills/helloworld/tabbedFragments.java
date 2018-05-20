package cdills.helloworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static cdills.helloworld.resultsDB.notifyDB.TABLE_NAME;

public class tabbedFragments extends AppCompatActivity {

    private static final String TAG = "tabbedFragments";
    ListView listView;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_fragments);
        getSupportActionBar().setTitle("Notify.it Reddit Notifications");

        //ADMOB
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4583114006273156~1496304023");
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().
               // addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
                build();
        Log.d("test", String.valueOf(request.isTestDevice(getBaseContext())));
        adView.loadAd(request);

        //dbHelper dbHelper = new dbHelper(getApplicationContext());
        // SQLiteDatabase db = dbHelper.getWritableDatabase();

        //  db.close();


        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // set up the viewPager with sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Notify");
        adapter.addFragment(new Tab2Fragment(), "RESULTS");
        adapter.addFragment(new Tab3Fragment(), "SEARCHES");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabbed_fragments, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                OneSignal.clearOneSignalNotifications();
                listView = (ListView) findViewById(R.id.list);
                Log.d("did", "refresh");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //reload content
                        dbHelper dbHelper = new dbHelper(getBaseContext(), "notifyDB");
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        dataRefresher.dataRefresh(getBaseContext(), listView, "notifyDB");

                    }
                });
                Toast mtoast = Toast.makeText(getApplicationContext(), "Done refreshing", Toast.LENGTH_SHORT);
                mtoast.show();
                return true;

            case R.id.action_deleteResults:
                listView = (ListView) findViewById(R.id.list);
                dbHelper dbHelper = new dbHelper(getApplicationContext(), "notifyDB");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("delete from " + TABLE_NAME);
                db.close();
                Tab2Fragment.dbValues.clear();
                Tab2Fragment.resultList.clear();
                dataRefresher.dataRefresh(getApplicationContext(), listView, "notifyDB");
                //   listView = (ListView) findViewById(R.id.list);
                //  listView.invalidateViews();
                Context context = getApplicationContext();
                CharSequence text = "All results deleted";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;

            case R.id.menu_clearAll:

                Toast rtoast = Toast.makeText(getApplicationContext(), "All searches cancelled", Toast.LENGTH_SHORT);
                rtoast.show();


                final String androidID = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // all networking code
                        // Create URL
                        // Have to catch this because java is dumb
                        try {
                            URL monitorEndpoint = new URL("http://dillscody.pythonanywhere.com/ClearSearch");
                            // Create connection
                            HttpURLConnection myConnection =
                                    (HttpURLConnection) monitorEndpoint.openConnection();
                            myConnection.setRequestMethod("POST");
                            String userDATA = "udid=" + androidID;

                            myConnection.setDoOutput(true);
                            myConnection.getOutputStream().write(userDATA.getBytes());

                            if (myConnection.getResponseCode() == 200) {
                                // Success
                                // Further processing here
                                listView = (ListView) findViewById(R.id.slist);
                                searchDBHelper sdbHelper = new searchDBHelper(getApplicationContext());
                                SQLiteDatabase db = sdbHelper.getWritableDatabase();
                                db.execSQL("delete from " + activeSearchDB.searchDB.TABLE_NAME);
                                db.close();
                                Tab3Fragment.sdbValues.clear();
                                Tab3Fragment.searchList.clear();
                                if (listView != null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //reload content

                                            listView.invalidateViews();
                                            listView.refreshDrawableState();

                                        }
                                    });
                                }


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


            default:
                return super.onOptionsItemSelected(item);
        }

    }

}