package br.com.chicobentojr.androidpushmessage.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.gcm.RegistrationIntentService;
import br.com.chicobentojr.androidpushmessage.models.Message;
import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.P;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (P.getUser().RegistrationId.isEmpty()) {
            logout();
        }

        EventBus.getDefault().register(this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        if (checkPlayServices()) {
            User user = P.getUser();
            user.Id = 1;
            user.Name = "Gilberto Gil";
            user.Login = "gilberto";
            user.RegistrationId = "eooQaEB-nSs:APA91bFiUk_dxCwU9HJwhds_4PjuRuhgiEk-cVkkry9OLwsdb999ArkO0h7v6pL19nRRwA80NEgug83M9xb-GIMD6uK4naA2lXt19DLTcmV225ERBNFyz1fpHsqhC5MIaMJIzrVSX6b9";

            P.setUser(user);


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

    public void onEvent(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtTitle.setText(message.Title);
                txtMessage.setText(message.Content);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                logout();
                return true;
        }
        return false;
    }

    public void logout() {
        P.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
