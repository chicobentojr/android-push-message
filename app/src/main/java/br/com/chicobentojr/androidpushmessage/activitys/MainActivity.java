package br.com.chicobentojr.androidpushmessage.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.adapters.UserAdapter;
import br.com.chicobentojr.androidpushmessage.models.Message;
import br.com.chicobentojr.androidpushmessage.models.User;
import br.com.chicobentojr.androidpushmessage.utils.ItemClickSupport;
import br.com.chicobentojr.androidpushmessage.utils.P;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ProgressDialog progressDialog;
    private ArrayList<User> usersList;

    private boolean sendToAllUsers = false;

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
            this.onUserClick();
        }

        Message message = (Message) getIntent().getSerializableExtra("message");

        if (message != null) {
            this.onEvent(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void loadUsers() {
        progressDialog.setMessage(getString(R.string.load_users_progress_message));
        progressDialog.show();
        User.listAll(new User.ApiListListener() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                usersList = users;
                adapter = new UserAdapter(usersList);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(error.getMessage())
                        .setPositiveButton("Ok", null)
                        .create().show();
                progressDialog.dismiss();
            }
        });

    }

    public void onUserClick() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                User to = usersList.get(position);
                openSendDialog(to);
            }
        });
    }

    public void openSendDialog(final User to) {
        View v = getLayoutInflater().inflate(R.layout.dialog_send_message, null);

        final AlertDialog sendDialog = new AlertDialog.Builder(this)
                .setTitle("Send Message")
                .setView(v)
                .setPositiveButton("Send", null)
                .setNegativeButton("Cancel", null)
                .create();

        sendDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                sendDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSendClickDialog(sendDialog, to);
                    }
                });
            }
        });

        sendDialog.show();
    }

    public void onSendClickDialog(final AlertDialog dialog, User to) {
        EditText txtTitle = (EditText) dialog.findViewById(R.id.txtTitle);
        EditText txtContent = (EditText) dialog.findViewById(R.id.txtContent);

        String title = txtTitle.getText().toString();
        String content = txtContent.getText().toString();

        Message message = new Message(title, content, to);

        if (message.Title.isEmpty()) {
            txtTitle.setError(getString(R.string.title_empty_error));
            txtTitle.requestFocus();
        } else if (message.Content.isEmpty()) {
            txtContent.setError(getString(R.string.content_empty_error));
            txtContent.requestFocus();
        } else {
            progressDialog.setMessage(getString(R.string.send_message_progress_message));
            progressDialog.show();

            Message.ApiListener listener = new Message.ApiListener() {
                @Override
                public void onSuccess(Message message) {
                    progressDialog.dismiss();
                    dialog.dismiss();

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(R.string.message_sent_dialog_title)
                            .setPositiveButton("Ok", null)
                            .create().show();

                }

                @Override
                public void onError(VolleyError error) {
                    progressDialog.dismiss();
                    dialog.dismiss();

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.error_dialog_title)
                            .setMessage(error.getMessage())
                            .setPositiveButton("Ok", null)
                            .create().show();
                }
            };

            if (sendToAllUsers) {
                Message.sendAll(message, listener);
                sendToAllUsers = false;

            } else {
                Message.send(message, listener);
            }
        }
    }

    public void onEvent(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String title = message.User.Name + " says:\n" + message.Title;

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(title)
                        .setMessage(message.Content)
                        .setPositiveButton("Ok", null)
                        .create().show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                logout();
                return true;
            case R.id.act_all_users:
                sendToAllUsers = true;
                openSendDialog(null);
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
