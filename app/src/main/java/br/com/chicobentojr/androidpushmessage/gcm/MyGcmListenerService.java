package br.com.chicobentojr.androidpushmessage.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.chicobentojr.androidpushmessage.models.PushMessage;
import de.greenrobot.event.EventBus;

/**
 * Created by Francisco on 01/04/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String LOG = "LOG";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from, data);

        String title = data.getString("Title");
        String message = data.getString("Content");

        EventBus.getDefault().post(new PushMessage(title, message));
    }
}
