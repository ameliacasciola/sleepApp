package c.cpen391.alarms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import c.cpen391.alarms.models.Alarm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHome extends Fragment {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context c = getActivity().getApplicationContext();
        final View rootview = inflater.inflate(R.layout.home_page, container, false);


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
        return rootview;
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
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });
    }

    private void initGamesScroll(View rootview, int[] gamesCardId){

        for (int i = 0; i < gamesCardId.length; i++){
            View gamesCardView = rootview.findViewById(gamesCardId[i]);
            ImageView gamesImg = gamesCardView.findViewById(R.id.gameImg);
            TextView gamesName = gamesCardView.findViewById(R.id.game_name);
            gamesName.setText(gamesNames[i]);

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

        View outerBox2 = rootview.findViewById(R.id.weather_box2);
        ImageView weather_box2 = (ImageView) outerBox2.findViewById(R.id.weather_box_icon);
        weather_box2.setImageResource(R.mipmap.cloudsun);

        View outerBox3 = rootview.findViewById(R.id.weather_box3);
        ImageView weather_box3 = (ImageView) outerBox3.findViewById(R.id.weather_box_icon);
        weather_box3.setImageResource(R.mipmap.cloudsun);

        View outerBox4 = rootview.findViewById(R.id.weather_box4);
        ImageView weather_box4 = (ImageView) outerBox4.findViewById(R.id.weather_box_icon);
        weather_box4.setImageResource(R.mipmap.cloudsun);
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Alarm> alarmList, View rootview) {
        recyclerView = rootview.findViewById(R.id.alarm_list);
        adapter = new RecyclerViewAdapter(getActivity(),alarmList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
 }
