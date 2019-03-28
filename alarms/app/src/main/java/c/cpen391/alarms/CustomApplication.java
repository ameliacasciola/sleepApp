package c.cpen391.alarms;

import android.app.Application;

import c.cpen391.alarms.models.UserObject;

public class CustomApplication extends Application {

    private UserObject uo;

    public void onCreate(){
        super.onCreate();
    }

    public UserObject getSomeVariable() {
        return uo;
    }

    public void setSomeVariable(UserObject someVariable) {
        this.uo = someVariable;
    }

}