package c.cpen391.alarms.api;

import java.net.URL;
import java.util.List;
import java.util.Map;

import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.models.Post;
import c.cpen391.alarms.models.Profile;
import c.cpen391.alarms.models.SleepData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.Call;
import retrofit2.http.Query;

public interface SleepAPI {
    @GET("/alarms")
    Call<List<Alarm>> getAlarms(@Query("username") int userID);

    @GET("/sleepdata")
    Call<List<SleepData>> getSleepData(@Query("start_date") String start_date, @Query("end_date") String end_date, @Query("date") String date);

    @GET("/profile/{id}/")
    Call<Profile> getProfileInfo(@Path("id") String id);

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

    @POST("/alarms/")
    Call<AlarmPost> alarmPost(@Body AlarmPost post);

    @FormUrlEncoded
    @POST("/alarms/")
    Call<ResponseBody> alarmPost(@Field("description") String title,
                        @Field("alarm_time") String body,
                        @Field("game_name") String gameName,
                        @Field("volume") Integer userId,
                        @Field("active") boolean active,
                        @Field("username") Integer userid);

    @Multipart
    @POST("/profile/")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part file,
            @PartMap Map<String, RequestBody> data);

    @FormUrlEncoded
    @PATCH("/profile/{id}/")
    Call<ResponseBody> updateBio(
            @Field("bio") String bio,
            @Path("id") String id
    );
}