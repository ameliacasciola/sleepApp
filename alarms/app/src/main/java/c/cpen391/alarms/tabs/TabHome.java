package c.cpen391.alarms.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;


import java.util.List;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.R;
import c.cpen391.alarms.adapters.RecyclerViewAdapter;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.api.WeatherService;
import c.cpen391.alarms.custom.WeatherCard;
import c.cpen391.alarms.games.GraphicsActivity;
import c.cpen391.alarms.games.WalkingStepsGame;
import c.cpen391.alarms.home;
import c.cpen391.alarms.login;
import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.models.UserObject;
import c.cpen391.alarms.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabHome extends Fragment {
    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "d759bba2b2a5a9470634fd12aaba0ffd";
    public static String lat = "49.25";
    public static String lon = "-123.12";
    private int currentTempMin;
    private int currentTempMax;

    private String[] cardUrls = {
            "http://crowdmedia.co/wp-content/uploads/2018/04/download-wallpaper-black-and-white-city-red-houses-skyline-ages-monochrome-wallpapers-widescreen-dark-single-c.jpg",
            "http://www.4usky.com/data/out/61/164540853-minimal-wallpapers.jpg",
            "https://png.pngtree.com/thumb_back/fw800/back_pic/04/09/63/575815dcbddd47e.jpg",
            "https://www.lescartons.fr/img/search-banner/search-banner-1-sm.jpg"
    };

    private String[] gamesUrls = {
            "https://images.ctfassets.net/8mn0755ou4sj/6ebMFEbgFUGig84iowaCqW/f59614c4f2ccba1c09dbe1aa48c3d583/pirates_passage.png",
            "https://images.ctfassets.net/8mn0755ou4sj/oU3j9ofTYO6SWKqcKksyy/2f8b51ec7b9efa12011577e77ccfaef1/braingames_lumosity.png",
            "https://mms.businesswire.com/media/20180424005040/en/652796/5/tot_PR_TrainWorld.jpg",
            "https://asset.lumosity.com/resources/landing_page_templates/857/studies_mobile.png"
    };

    private String[] gamesNames = {
            "Egg Run",
            "Get Eggy With It",
            "The Egg Did It",
            "Where did the Egg Go"
    };

    private String[] gamesType = {
            "Physical Exercise",
            "Planning",
            "Speed",
            "Just get the Egg"
    };

    TextClock clock;
    ImageView alarmImage;

    private MaterialSearchBar searchBar;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private CardView weatherCard;
    private ImageView weatherImage;
    private CardView toGraphics;
    private TextView greetings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context c = getActivity().getApplicationContext();
        final View rootview = inflater.inflate(R.layout.home_page, container, false);

        greetings = rootview.findViewById(R.id.greeting);
        UserObject mUserObject = ((CustomApplication) c).getSomeVariable();
        greetings.setText("Welcome Back, " + mUserObject.getUsername());
        getCurrentData();

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<List<Alarm>> call = service.getAlarms();
        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response< List<Alarm>> response) {
                progressDoalog.dismiss();
                List<Alarm> alarmList = response.body();
                generateDataList(alarmList, rootview);
                displayNextAlarm(alarmList.get(0), rootview);
            }

            @Override
            public void onFailure(Call<List<Alarm>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        searchBar = (MaterialSearchBar) rootview.findViewById(R.id.search_bar);
        searchBar.setHint("Search");
        searchBar.setSpeechMode(true);
        searchBar.setCardViewElevation(0);
        searchBar.setPlaceHolderColor(Color.parseColor("#A9A9A9"));


        WeatherCard mCustomLayout = (WeatherCard) rootview.findViewById(R.id.weatherCard_bg);
        Picasso.get().load(cardUrls[2]).into(mCustomLayout);

        int[] gamesCardId = {
                R.id.games_card,
                R.id.games_card2,
                R.id.games_card3,
                R.id.games_card4
        };

        displayForecast(rootview);
        initGamesScroll(rootview, gamesCardId);
        initAlarmsList(rootview);
        return rootview;
    }

    private void initAlarmsList(View rootview){

        Button viewAllbtn = rootview.findViewById(R.id.view_all_btn);
        viewAllbtn.setText("View All (10+)");
        viewAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((home)getActivity()).selectTab(1);
            }
        });

    }

    private void displayNextAlarm(Alarm nextAlarm, View rootview){
        ImageView nextAlarmHeader = (ImageView) rootview.findViewById(R.id.nextAlarmImage);
        Picasso.get()
                .load(cardUrls[3])
                .placeholder(R.drawable.blue_plane)
                .into(nextAlarmHeader);

        TextView alarmDescription = (TextView) rootview.findViewById(R.id.alarm_description);
        alarmDescription.setText(nextAlarm.getAlarmDescription());

        CardView nextAlarmCard = (CardView) rootview.findViewById(R.id.next_alarm);
        nextAlarmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalkingStepsGame.class);
                startActivity(intent);
            }
        });
    }

    private void initGamesScroll(View rootview, int[] gamesCardId){

        toGraphics = (CardView) rootview.findViewById(R.id.games_card2);

        for (int i = 0; i < gamesCardId.length; i++){
            View gamesCardView = rootview.findViewById(gamesCardId[i]);
            ImageView gamesImg = gamesCardView.findViewById(R.id.gameImg);
            TextView gamesName = gamesCardView.findViewById(R.id.game_name);
            gamesName.setText(gamesNames[i]);

            //go to dumb bubble game
            if(i==1) {
                //set button functionality to take you to the graphics exercise
                toGraphics.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), GraphicsActivity.class);
                        startActivity(intent);
                    }
                });
            }

            if(i == 0){
                gamesCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WalkingStepsGame.class);
                        startActivity(intent);
                    }
                });
            }

            TextView gameType = gamesCardView.findViewById(R.id.game_type);
            gameType.setText(gamesType[i]);

            Picasso.get()
                    .load(gamesUrls[i])
                    .placeholder(R.drawable.blue_plane)
                    .into(gamesImg);
        }
    }

    private void displayForecast(View rootview){
        View outerBox = rootview.findViewById(R.id.weather_box1);
        ImageView weather_box1 = (ImageView) outerBox.findViewById(R.id.weather_box_icon);
        weather_box1.setImageResource(R.mipmap.cloudsun);
        TextView date1 = outerBox.findViewById(R.id.date);
        date1.setText("THU");

        View outerBox2 = rootview.findViewById(R.id.weather_box2);
        ImageView weather_box2 = (ImageView) outerBox2.findViewById(R.id.weather_box_icon);
        weather_box2.setImageResource(R.mipmap.cloudsun);
        TextView date2 = outerBox2.findViewById(R.id.date);
        date2.setText("FRI");
        TextView temp_high_2 =  outerBox2.findViewById(R.id.temp_high);
        TextView temp_low_2 = outerBox2.findViewById(R.id.temp_low);


        View outerBox3 = rootview.findViewById(R.id.weather_box3);
        ImageView weather_box3 = (ImageView) outerBox3.findViewById(R.id.weather_box_icon);
        weather_box3.setImageResource(R.mipmap.cloudsun);
        TextView date3 = outerBox3.findViewById(R.id.date);
        date3.setText("SAT");
        TextView temp_high_3 =  outerBox3.findViewById(R.id.temp_high);
        TextView temp_low_3 = outerBox3.findViewById(R.id.temp_low);

        View outerBox4 = rootview.findViewById(R.id.weather_box4);
        ImageView weather_box4 = (ImageView) outerBox4.findViewById(R.id.weather_box_icon);
        weather_box4.setImageResource(R.mipmap.cloudsun);
        TextView date4 = outerBox4.findViewById(R.id.date);
        date4.setText("SUN");
        TextView temp_high_4 =  outerBox4.findViewById(R.id.temp_high);
        TextView temp_low_4 = outerBox4.findViewById(R.id.temp_low);
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Alarm> alarmList, View rootview) {
        recyclerView = rootview.findViewById(R.id.alarm_list);
        adapter = new RecyclerViewAdapter(getActivity(),alarmList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    // initialize Retrofit to call the weather service. The following code snippet will help us to call the service.

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    currentTempMin = (int)(weatherResponse.main.temp_min - 273.15f);
                    currentTempMax = (int)(weatherResponse.main.temp_max - 273.15f);

                    View outerBox = getView().findViewById(R.id.weather_box1);
                    // set high and low temp
                    TextView temp_high_1 = (TextView) outerBox.findViewById(R.id.temp_high);
                    temp_high_1.setText(String.valueOf(currentTempMax));

                    TextView temp_low_1 = (TextView) outerBox.findViewById(R.id.temp_low);
                    temp_low_1.setText(String.valueOf(currentTempMin));
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Fail to Get Weather", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
