package br.com.chicobentojr.androidpushmessage.models;

import android.util.Log;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.chicobentojr.androidpushmessage.utils.ApiRoutes;
import br.com.chicobentojr.androidpushmessage.utils.AppController;

/**
 * Created by Francisco on 01/04/2016.
 */
public class Message {

    public String Title;
    public String Content;
    public User User;

    public Message(String title, String content, User user) {
        this.Title = title;
        this.Content = content;
        this.User = user;
    }

    public interface ApiListener {
        void onSuccess(Message message);

        void onError(VolleyError error);
    }

    public static void send(final Message message, final ApiListener listener) {
        final Gson gson = new Gson();

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    ApiRoutes.MESSAGE.Send(message.User.Login),
                    new JSONObject(gson.toJson(message)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onSuccess(message);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof ParseError) {
                                listener.onSuccess(message);
                            } else {
                                listener.onError(error);
                            }
                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("LOG", "The Message parse to JSONObject throws an error");
        }
    }

    public static void sendAll(final Message message, final ApiListener listener) {
        final Gson gson = new Gson();

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    ApiRoutes.MESSAGE.SEND_ALL,
                    new JSONObject(gson.toJson(message)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onSuccess(message);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof ParseError) {
                                listener.onSuccess(message);
                            } else {
                                listener.onError(error);
                            }
                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(request);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("LOG", "The Message parse to JSONObject throws an error");
        }
    }
}
