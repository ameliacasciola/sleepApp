package c.cpen391.alarms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.List;

import c.cpen391.alarms.models.Alarm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHome extends Fragment {
    private String[] cardUrls = {
            "http://crowdmedia.co/wp-content/uploads/2018/04/download-wallpaper-black-and-white-city-red-houses-skyline-ages-monochrome-wallpapers-widescreen-dark-single-c.jpg",
            "http://www.4usky.com/data/out/61/164540853-minimal-wallpapers.jpg",
            "https://png.pngtree.com/thumb_back/fw800/back_pic/04/09/63/575815dcbddd47e.jpg"
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
        //setWeatherCard(rootview);
        //clock = rootview.findViewById(R.id.textClock);

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<List<Alarm>> call = service.getAlarms();
        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response<List<Alarm>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body(), rootview);
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



        ImageView imageView = (ImageView) rootview.findViewById(R.id.location_icon);
        imageView.setImageResource(R.drawable.ic_outline_location_on_24px);


        WeatherCard mCustomLayout = (WeatherCard) rootview.findViewById(R.id.weatherCard_bg);
        Picasso.get().load(cardUrls[2]).into(mCustomLayout);
        //weatherImage = (ImageView) rootview.findViewById(R.id.weatherImage);
//        Picasso.get()
//                .load(cardUrls[0])
//                .placeholder(R.drawable.blue_plane)
//                .into(weatherImage);

        displayForecast(rootview);

        return rootview;
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
    private void generateDataList(List<Alarm> photoList, View rootview) {
        recyclerView = rootview.findViewById(R.id.alarm_list);
        adapter = new RecyclerViewAdapter(getActivity(),photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
 }
