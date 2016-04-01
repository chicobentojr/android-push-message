package br.com.chicobentojr.androidpushmessage.gcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Francisco on 01/04/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String LOG = "LOG";

    @Override
    public void onTokenRefresh() {
        /**
         * This Code is for update the token in the webservice
         * Apparently, this method is called by the Android
         * so, you don't know when this was executed
         */

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("status", false).apply();

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
