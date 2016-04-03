package br.com.chicobentojr.androidpushmessage.utils;

/**
 * Created by Francisco on 02/04/2016.
 */
public class ApiRoutes {

    private static final String BASE_URL = "http://androidpushmessage.apphb.com/";

    public static class USER {
        public static final String USERS = BASE_URL + "api/users";
        public static final String REGISTER = BASE_URL + "api/user/register";
    }
}
