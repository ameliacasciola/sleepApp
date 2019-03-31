package c.cpen391.alarms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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
        return sharedPref.getString("USERNAME", "NONE");
    }

    public Uri getPic() {
        if(sharedPref.getString("USERPIC", "NONE") == "NONE") {
            return null;
        } else {
            return Uri.parse(sharedPref.getString("USERPIC", "NONE"));
        }
    }

    public void setPic(Uri pic) {
        sharedPref.edit().putString("USERPIC", pic.toString()).apply();
    }

}