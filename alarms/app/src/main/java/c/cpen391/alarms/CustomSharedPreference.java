package c.cpen391.alarms;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class CustomSharedPreference {

    private SharedPreferences sharedPref;

    public CustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
    }

    public SharedPreferences getInstanceOfSharedPreference()
    {
        return sharedPref;
    }

    // alarm edit or view
    public void setScore(int score)
    {
        sharedPref.edit().putInt("SCORE", score).apply();
    }

    public int getScore()
    {
        return sharedPref.getInt("SCORE", -1);
    }

    // alarm edit or view
    public void setAlarmFlag(int flag)
    {
        sharedPref.edit().putInt("ALARMFLAG", flag).apply();
    }

    public int getAlarmFlag()
    {
        return sharedPref.getInt("ALARMFLAG", -1);
    }

    // alarm id
    public void setAlarmID(int id)
    {
        sharedPref.edit().putInt("ALARMID", id).apply();
    }

    public int getAlarmID()
    {
        return sharedPref.getInt("ALARMID", -1);
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

    public Bitmap getPic() {
        if(sharedPref.getString("USERPIC", "NONE") == "NONE") {
            return null;
        } else {
            return decodeBase64(sharedPref.getString("USERPIC", "NONE"));
        }
    }

    public void setPic(Bitmap bm) {
        sharedPref.edit().putString("USERPIC", encodeTobase64(bm)).commit();
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

}