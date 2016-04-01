package br.com.chicobentojr.androidpushmessage.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.gcm.RegistrationIntentService;
import br.com.chicobentojr.androidpushmessage.models.PushMessage;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String SERVER_API_KEY = "AIzaSyCp2sD0kQMpg-ZRq5-K1eM1XGfh0lc9lqU";
    private static final String SENDER_ID = "201269282865";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TextView txtTitle;
    private TextView txtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("LOG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void onEvent(final PushMessage pushMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTitle.setText(pushMessage.title);
                txtMessage.setText(pushMessage.message);
            }
        });
    }
}
