package br.com.chicobentojr.androidpushmessage.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import br.com.chicobentojr.androidpushmessage.models.Message;
import de.greenrobot.event.EventBus;

/**
 * Created by Francisco on 01/04/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String LOG = "LOG";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from, data);

        Message message = new Gson().fromJson(data.getString("message"), Message.class);

        EventBus.getDefault().post(message);
    }
}
