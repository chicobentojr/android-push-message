package br.com.chicobentojr.androidpushmessage.models;

/**
 * Created by Francisco on 01/04/2016.
 */
public class Message {

    public String Title;
    public String Content;
    public User User;

    public Message(String title, String message){
        this.Title = title;
        this.Content = message;
    }
}
