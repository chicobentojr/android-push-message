package br.com.chicobentojr.androidpushmessage.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Francisco on 01/04/2016.
 */
public class RegistrationIntentService extends IntentService {
    public static final String LOG = "LOG";

    public RegistrationIntentService() {
        super(LOG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = preferences.getBoolean("status", false);

        synchronized (LOG) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                if (!status) {

                    String token = instanceID.getToken("201269282865", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    Log.i(LOG, "TOKEN: " + token);

                    preferences.edit().putBoolean("status", token != null && token.trim().length() > 0).apply();

                    sendRegistrationID(token);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRegistrationID(String token) {

        // TODO: Send the token to the webservice for save in a user from database
    }
}
