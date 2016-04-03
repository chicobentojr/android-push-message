package br.com.chicobentojr.androidpushmessage.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.adapters.UserAdapter;
import br.com.chicobentojr.androidpushmessage.models.Message;
import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.P;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ProgressDialog progressDialog;
    private ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (P.getUser().RegistrationId.isEmpty()) {
            logout();
        } else {
            EventBus.getDefault().register(this);

            usersList = new ArrayList<>();

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);


            this.loadUsers();
        }
    }

    public void loadUsers() {
        progressDialog.setMessage(getString(R.string.load_users_progress_message));
        progressDialog.show();
        User.listAll(new User.ApiListListener() {
            @Override
            public void OnSuccess(ArrayList<User> users) {
                usersList = users;
                adapter = new UserAdapter(usersList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void OnError(VolleyError error) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(error.getMessage())
                        .setNeutralButton("Ok", null)
                        .create().show();
                progressDialog.dismiss();
            }
        });

    }

    public void onEvent(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String title = message.User.Name + ": " + message.Title;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(title)
                        .setMessage(message.Content)
                        .setNeutralButton("Ok", null)
                        .create().show();
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
