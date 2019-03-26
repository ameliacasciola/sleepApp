package c.cpen391.alarms.models;


import android.net.Uri;

public class UserObject {

    private String username;
    private String email;
    private String password;
    private volatile Uri uri;

    public UserObject(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPassword()
    {
        return password;
    }
    public Uri getUri() {
        return uri;
    }
    public void putUri(Uri i) {
        this.uri = i;
    }

}