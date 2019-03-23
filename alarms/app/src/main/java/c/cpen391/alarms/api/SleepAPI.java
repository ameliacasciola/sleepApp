package c.cpen391.alarms.api;

import java.util.List;

import c.cpen391.alarms.models.Alarm;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

public interface SleepAPI {
    @GET("/alarms")
    Call<List<Alarm>> getAlarms();

}