package c.cpen391.alarms.custom;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.AlarmPost;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.home;
import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.tabs.CreateAlarm;
import c.cpen391.alarms.tabs.TabAlarms;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotifySlidePageFragment extends Fragment {

    private String[] gamesNameList= {"Run", "Bubble Pop", "Guess the Word"};
    private String[] gamesDescriptionList = {"Walk at least 100 steps.",
                                                "Pop some bubbles in a X amount of time.",
                                                "Figure out the correct word wtihout making X mistakes."};
    private SwipeItem[] swipeItemList;
    private Alarm newAlarm;
    private CardView submitBtn;
    protected static CustomSharedPreference mPref;
    private  SwipeSelector swipeSelector;
    private SleepAPI sleepAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotify_screen_slide, container, false);
        initGamesSwipeSelector(rootView);
        newAlarm = ((CreateAlarm)getActivity()).getAlarm();
        setSubmitBtn(rootView);
        return rootView;
    }

    private void initGamesSwipeSelector(View rootview){
        swipeSelector = (SwipeSelector) rootview.findViewById(R.id.swipeSelector);
        swipeItemList = new SwipeItem[gamesNameList.length];

        for (int i = 0; i < gamesNameList.length; i++){
            swipeItemList[i] = new SwipeItem(i, gamesNameList[i], gamesDescriptionList[i]);
        }

        swipeSelector.setItems(
                swipeItemList
        );
    }

    private void setSubmitBtn(View rootview){
        submitBtn = rootview.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String game = swipeSelector.getSelectedItem().title;
                newAlarm.setGameName(game);
                if (newAlarm.getAlarmTime() == null){
                    Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else{
                    sendPost();
                    ((CreateAlarm)getActivity()).closeAlarm();

                    Intent refresh = new Intent(getContext(), home.class);
                    getContext().startActivity(refresh);
                }
            }
        });
    }

    public void sendPost() {
        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
        AlarmPost post = new AlarmPost(newAlarm.getAlarmDescription(), newAlarm.getAlarmTime(),  newAlarm.getVolume(), newAlarm.getGame(),true, mPref.getUserID());
        Call<AlarmPost> alarmCall = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class).alarmPost(post);

        alarmCall.enqueue(new Callback<AlarmPost>() {
            @Override
            public void onResponse(Call<AlarmPost> call, Response<AlarmPost> response) {
                if(response.isSuccessful()) {
                    Log.e("POST", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<AlarmPost> call, Throwable t) {
                Log.e("POST", "Unable to submit post to API.");
            }
        });
    }
}
