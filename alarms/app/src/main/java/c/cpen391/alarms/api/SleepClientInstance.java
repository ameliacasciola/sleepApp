package c.cpen391.alarms.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SleepClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://django-env.qgbzkwbhei.us-west-1.elasticbeanstalk.com";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
