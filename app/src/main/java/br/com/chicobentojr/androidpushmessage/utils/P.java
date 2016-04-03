package br.com.chicobentojr.androidpushmessage.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.chicobentojr.androidpushmessage.models.User;

/**
 * Created by Francisco on 02/04/2016.
 */
public class P {

    public static User user;

    public static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getContext());
    public static String USER_ID = "br.com.chicobentojr.androidpushmessage.USER_ID";
    public static String USER_NAME = "br.com.chicobentojr.androidpushmessage.USER_NAME";
    public static String USER_LOGIN = "br.com.chicobentojr.androidpushmessage.USER_LOGIN";
    public static String USER_REGISTRATION_ID = "br.com.chicobentojr.androidpushmessage.USER_REGISTRATION_ID";

    public static final String SENDER_ID = "201269282865";

    public static User getUser() {
        if (user == null) {
            user = new User();
            user.Id = prefs.getInt(USER_ID, 0);
            user.Name = prefs.getString(USER_NAME, "");
            user.Login = prefs.getString(USER_LOGIN, "");
            user.RegistrationId = prefs.getString(USER_REGISTRATION_ID, "");
        }
        return user;
    }

    public static void setUser(User user) {
        P.user = user;
        prefs.edit()
                .putInt(USER_ID, user.Id)
                .putString(USER_NAME, user.Name)
                .putString(USER_LOGIN, user.Login)
                .putString(USER_REGISTRATION_ID, user.RegistrationId)
                .apply();
    }
}
