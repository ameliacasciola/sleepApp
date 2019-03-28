package c.cpen391.alarms.api;

import java.util.List;

import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.models.SleepData;
import c.cpen391.alarms.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);
}