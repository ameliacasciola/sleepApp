package c.cpen391.alarms;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreference {

    private SharedPreferences sharedPref;

    public CustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
    }

    public SharedPreferences getInstanceOfSharedPreference()
    {
        return sharedPref;
    }

    //Save user information
    public void setUserID(int userID)
    {
        sharedPref.edit().putInt("USERID", userID).apply();
    }

    public int getUserID()
    {
        return sharedPref.getInt("USERID", -1);
    }

    public void setUserName(String userName)
    {
        sharedPref.edit().putString("USERNAME", userName).apply();
    }

    public String getUserName()
    {
        return sharedPref.getString("USERNAME", "");
    }

}