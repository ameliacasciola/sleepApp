package c.cpen391.alarms.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snow {

    @SerializedName("1h")
    @Expose
    private float _1h;

    public float get1h() {
        return _1h;
    }

    public void set1h(float _1h) {
        this._1h = _1h;
    }

}