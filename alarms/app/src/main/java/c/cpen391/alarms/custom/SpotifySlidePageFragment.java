package c.cpen391.alarms.custom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class SpotifySlidePageFragment extends Fragment {

    private String[] spotifySongIds = {"4JuZQeSRYJfLCqBgBIxxrR"};

    private String[] gamesNameList= {"Run", "Bubble Pop", "Guess the Word"};
    private String[] gamesDescriptionList = {"Walk at least 100 steps.",
                                                "Pop some bubbles in a X amount of time.",
                                                "Figure out the correct word wtihout making X mistakes."};
    private SwipeItem[] swipeItemList;
    private Alarm newAlarm;
    private CardView submitBtn;
    private ImageView spotifyImg;
    protected static CustomSharedPreference mPref;
    private  SwipeSelector swipeSelector;
    private SleepAPI sleepAPI;

    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotify_screen_slide, container, false);
        initGamesSwipeSelector(rootView);
        newAlarm = ((CreateAlarm)getActivity()).getAlarm();
        setSubmitBtn(rootView);
        spotifyImg = rootView.findViewById(R.id.spotifyImg);

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.e("SPOTIFY REMOTE", "Success, Onconnected" + mSpotifyAppRemote.isConnected());

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("SPOTIFY REMOTE", "Failure, Onconnected");

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void connected() {
        // Play a playlist

        mSpotifyAppRemote.getPlayerApi().play("spotify:track:4VUwkH455At9kENOfzTqmF");
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(
                new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.i("SPOTIFY", "GOT IMAGE");
                            mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(new CallResult.ResultCallback<Bitmap>() {
                                @Override
                                public void onResult(Bitmap bitmap) {
                                    spotifyImg.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }
        );

    }

    @Override
    public void onStop() {
        super.onStop();
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
                mSpotifyAppRemote.getPlayerApi().pause();
                SpotifyAppRemote.disconnect(mSpotifyAppRemote);

                String game = swipeSelector.getSelectedItem().title;
                newAlarm.setGameName(game);

                mPref = ((CustomApplication)getContext().getApplicationContext()).getShared();
                int alarmFlag = mPref.getAlarmFlag();

                if (newAlarm.getAlarmTime() == null){
                    Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if(alarmFlag == 1) { // edit, view flag
                    editPost();
                    mPref.setAlarmFlag(0);

                    // refresh, jump
                    Intent refresh = new Intent(getContext(), home.class);
                    getContext().startActivity(refresh);
                } else{
                    sendPost();
                    ((CreateAlarm)getActivity()).closeAlarm();

                    // refresh, jump
                    Intent refresh = new Intent(getContext(), home.class);
                    getContext().startActivity(refresh);
                }
            }
        });
    }

    public void editPost() {
        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
        Call<ResponseBody> service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .alarmEdit(newAlarm.getAlarmDescription()
                        , newAlarm.getAlarmTime()
                        , newAlarm.getVolume()
                        , true
                        , newAlarm.getGame()
                        , mPref.getAlarmID());

        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("POST", "alarm edit success" + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("POST", "Unable to edit alarm");
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
