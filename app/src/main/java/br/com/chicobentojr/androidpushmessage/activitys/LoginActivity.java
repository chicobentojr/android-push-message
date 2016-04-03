package br.com.chicobentojr.androidpushmessage.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.gcm.RegistrationIntentService;
import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.P;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {

    public User user;

    public EditText txtName;
    public EditText txtLogin;

    public ProgressDialog progressDialog;

    @Override
    protected void onNewIntent(Intent intent) {
        txtName.setText("");
        txtLogin.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);

        txtName = (EditText) findViewById(R.id.txtName);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        user = P.getUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void attemptLogin(View view) {
        user.Name = txtName.getText().toString();
        user.Login = txtLogin.getText().toString();

        if (user.Name.isEmpty()) {
            txtName.setError(getString(R.string.name_empty_error));
            txtName.requestFocus();
        } else if (user.Login.isEmpty()) {
            txtLogin.setError(getString(R.string.login_empty_error));
            txtLogin.requestFocus();
        } else {

            progressDialog.setMessage(getString(R.string.login_progress_message));
            progressDialog.show();

            P.setUser(user);
            startService(new Intent(this, RegistrationIntentService.class));
        }
    }

    public void onEvent(User user) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
