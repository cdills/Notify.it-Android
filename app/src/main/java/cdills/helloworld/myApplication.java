package cdills.helloworld;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.Settings;
import android.util.Log;

import com.onesignal.OneSignal;



public class myApplication extends Application {
    SharedPreferences prefs = null;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        final String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        OneSignal.sendTag("uuid", androidID);

        OneSignal.clearOneSignalNotifications();

        prefs = getSharedPreferences(this.getString(R.string.app_name), MODE_PRIVATE);
      /*  if(!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)){
            createShortCut();
            getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).commit();
        }*/

        if (prefs.getBoolean("firstrun", true)) {
            createShortCut();
            prefs.edit().putBoolean("firstrun", false).commit();
        }



        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);
    }


    private void createShortCut() {

     /*   ShortcutManager mShortcutManager =
                getApplicationContext().getSystemService(ShortcutManager.class);

        if (mShortcutManager.isRequestPinShortcutSupported()) {
            Context context = getApplicationContext();
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, "my-shortcut")
                    .setShortLabel(getString(R.string.app_name))
                    .setLongLabel(getString(R.string.app_extended))
                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com")))
                    .build();
            Intent intent = new Intent();
            mShortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());



        }*/
        Log.d("CreateShortCut", "didBegin.");
        Intent shortcutIntent = new Intent(getApplicationContext(),tabbedFragments.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher_round));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        this.sendBroadcast(intent);
        Log.d("CreateShortCut", "didEnd.");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}



