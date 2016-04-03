package br.com.chicobentojr.androidpushmessage.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.P;
import de.greenrobot.event.EventBus;

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
        User user = P.getUser();

        synchronized (LOG) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                if (user.RegistrationId.isEmpty()) {

                    String token = instanceID.getToken(P.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                    Log.i(LOG, "TOKEN: " + token);

                    user.RegistrationId = token;

                    registerUser(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser(User user) {
        User.register(user, new User.ApiListener() {
            @Override
            public void onSuccess(User user) {
                P.setUser(user);
                Log.i(LOG, "USER: " + user.Name);
                Log.i(LOG, "TOKEN: " + user.RegistrationId);
                EventBus.getDefault().post(user);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(LOG, "Happened an error with the registerUser method");
                EventBus.getDefault().post(error);
            }
        });
    }
}
