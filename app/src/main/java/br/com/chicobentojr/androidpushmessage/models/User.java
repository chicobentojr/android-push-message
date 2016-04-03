package br.com.chicobentojr.androidpushmessage.models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import br.com.chicobentojr.androidpushmessage.utils.ApiRoutes;
import br.com.chicobentojr.androidpushmessage.utils.AppController;

/**
 * Created by Francisco on 02/04/2016.
 */
public class User implements Serializable {

    public int Id;
    public String Name;
    public String Login;
    public String RegistrationId;

    public interface ApiListener {
        void OnSuccess(User user);

        void OnError(VolleyError error);
    }

    public static void register(User user, final ApiListener listener) {

        final Gson gson = new Gson();

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    ApiRoutes.USER.REGISTER,
                    new JSONObject(gson.toJson(user)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            User user = gson.fromJson(response.toString(), User.class);
                            listener.OnSuccess(user);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.OnError(error);
                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("LOG", "The User parse to JSONObject throws an error");
        }
    }
}
