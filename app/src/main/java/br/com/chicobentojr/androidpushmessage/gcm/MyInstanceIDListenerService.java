package br.com.chicobentojr.androidpushmessage.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.P;

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

        User user = P.getUser();
        user.RegistrationId = "";
        P.setUser(user);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
