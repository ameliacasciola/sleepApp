package c.cpen391.alarms;

import android.app.Application;

import c.cpen391.alarms.models.UserObject;

public class CustomApplication extends Application {

    private UserObject uo;
    private CustomSharedPreference shared;

    public void onCreate(){
        super.onCreate();
        shared = new CustomSharedPreference(getApplicationContext());
    }

    public UserObject getSomeVariable() {
        return uo;
    }

    public void setSomeVariable(UserObject someVariable) {
        this.uo = someVariable;
    }

    public CustomSharedPreference getShared()
    {
        return shared;
    }

}