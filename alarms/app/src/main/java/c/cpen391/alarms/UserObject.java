package c.cpen391.alarms;


public class UserObject {

    private String username;
    private String email;
    private String password;
    private String address;
    private String phone;
    private boolean loginOption;

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

}