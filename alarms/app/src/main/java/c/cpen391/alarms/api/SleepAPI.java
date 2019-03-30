package c.cpen391.alarms.api;

import java.net.URL;
import java.util.List;

import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.models.Post;
import c.cpen391.alarms.models.SleepData;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;

public interface SleepAPI {
    @GET("/alarms")
    Call<List<Alarm>> getAlarms();

    @GET("/sleepdata")
    Call<List<SleepData>> getSleepData();

    @GET("/users/{id}/")
    Call<Post> getUserInfo();

    @POST("/users/")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("/profile/")
    Call<ResponseBody> profilePost(
            @Field("user") int user,
            @Field("bio") String bio,
            @Field("name") String name,
            @Field("location") String location,
            @Field("image") URL image
    );
}