package br.com.chicobentojr.androidpushmessage.utils;

/**
 * Created by Francisco on 02/04/2016.
 */
public class ApiRoutes {

    private static final String BASE_URL = "http://androidpushmessage.apphb.com/";

    public static class USER {
        public static final String LIST_ALL = BASE_URL + "api/users";
        public static final String REGISTER = BASE_URL + "api/user/register";
    }

    public static class MESSAGE {
        public static final String SEND_ALL = BASE_URL + "api/message/sendall";

        public static final String Send(String login){
            return  BASE_URL + "api/message/send/" +login;
        }

    }
}
