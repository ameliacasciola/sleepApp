package c.cpen391.alarms.custom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import c.cpen391.alarms.AlarmReceiver;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.AlarmPost;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.games.GraphicsActivity;
import c.cpen391.alarms.games.MainSpellingActivity;
import c.cpen391.alarms.games.WalkingStepsGame;
import c.cpen391.alarms.home;
import c.cpen391.alarms.models.Alarm;
import c.cpen391.alarms.tabs.CreateAlarm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.ContentApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SpotifySlidePageFragment extends Fragment {

    private String[] spotifySongIds = {"spotify:image:b25864800cff4456b32a216de0f7f2d016088936",
                                        "spotify:image:5a0c73915586db4a6acf0a92eb7c503877f1c9a4",
                                        "spotify:image:fe9a6270d2c1e8b0a62945623da0c6a98f392b0a",
                                        "spotify:image:8de53fac20477d01cc2e77a513ee417c3c5253dd",
                                        "spotify:image:680f63e0199dc160f414b5be084b3d91493c67a6"};

    private String[] spotifyTrackURI = {
            "spotify:track:4VUwkH455At9kENOfzTqmF",
            "spotify:track:1rqqCSm0Qe4I9rUvWncaom",
            "spotify:track:4DMKwE2E2iYDKY01C335Uw",
            "spotify:track:5I9zIwGB6f0edpjO5oX2b9",
            "spotify:track:4TTV7EcfroSLWzXRY6gLv6",
    };

    private int[] spotiftCardViews = {
            R.id.spotify_card0,
            R.id.spotify_card1,
            R.id.spotify_card2,
            R.id.spotify_card3,
            R.id.spotify_card4
    };

    private String[] gamesNameList= {"Eggs-cercise", "Bubble Pop", "Eggcellent Spelling", "Jumping Jacks", "Color Sequences", "Burpees", "Squats"};
    private String[] gamesDescriptionList = {"Walk at least 100 steps.",
                                                "Pop some bubbles in a X amount of time.",
                                                "Figure out the correct word without making X mistakes.",
                                                "Complete at least 20 jumping jacks.",
                                                "Memorize the color sequence and repeat it back!",
                                                "Complete at least 5 burpees.",
                                                "Complete at least 20 squats"};
    private SwipeItem[] swipeItemList;
    private Alarm newAlarm;
    private CardView submitBtn;
    protected static CustomSharedPreference mPref;
    private  SwipeSelector swipeSelector;
    private SleepAPI sleepAPI;

    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotify_screen_slide, container, false);
        initGamesSwipeSelector(rootView);
        newAlarm = ((CreateAlarm)getActivity()).getAlarm();
        setSubmitBtn(rootView);

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
                        //Log.e("SPOTIFY REMOTE", "Success, Onconnected" + mSpotifyAppRemote.isConnected());

                        // Now you can start interacting with App Remote
                        connected(rootView);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        //Log.e("SPOTIFY REMOTE", "Failure, Onconnected");

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void connected(View rootview) {
        // Play a playlist

        initSpotifyCards(rootview);
        //mSpotifyAppRemote.getPlayerApi().play("spotify:track:4VUwkH455At9kENOfzTqmF");
//        mSpotifyAppRemote.getPlayerApi().play("spotify:track:4TTV7EcfroSLWzXRY6gLv6");
//        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(
//                new Subscription.EventCallback<PlayerState>() {
//                    @Override
//                    public void onEvent(PlayerState playerState) {
//                        final Track track = playerState.track;
//                        Log.i("SPOTIFY A: ", track.imageUri.raw);
//                        if (track != null) {
//                            Log.i("IMAGE URI", track.imageUri.raw);
//                        }
//                    }
//                });
    }


    private void initSpotifyCards(View rootview) {
        final View baseview = rootview;
        for (int i = 0; i < spotiftCardViews.length; i++) {
            final CardView spotifyCardView = rootview.findViewById(spotiftCardViews[i]);

            spotifyCardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for (int i = 0; i < spotiftCardViews.length; i++){
                            CardView tmp = baseview.findViewById(spotiftCardViews[i]);
                            if (tmp != null) {
                                tmp.setCardBackgroundColor(getResources().getColor(R.color.white));
                            }

                            if (spotiftCardViews[i] == spotifyCardView.getId()){
                                ((CardView)v).setCardBackgroundColor(getResources().getColor(R.color.dark_gray));
                                newAlarm.setSpotifyURI(spotifyTrackURI[i]);
                            }
                    }
                }
            });

            final ImageView spotifyImg = spotifyCardView.findViewById(R.id.spotifyImg);
            ImageUri imgURI = new ImageUri(spotifySongIds[i]);
//
//            if (i % 2 == 0) {
//                mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(
//                        new Subscription.EventCallback<PlayerState>() {
//                            @Override
//                            public void onEvent(PlayerState playerState) {
//                                final Track track = playerState.track;
//                                Log.i("SPOTIFY A: ", track.imageUri.raw);
//                                if (track != null) {
//                                    mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(
//                                            new CallResult.ResultCallback<Bitmap>() {
//                                                @Override
//                                                public void onResult(Bitmap bitmap) {
//                                                    spotifyImg.setImageBitmap(bitmap);
//                                                }
//                                            });
//                                }
//                            }
//                        });
//
//            }else {
                mSpotifyAppRemote.getImagesApi().getImage(imgURI).setResultCallback(new CallResult.ResultCallback<Bitmap>() {
                @Override
                public void onResult(Bitmap bitmap) {
                    spotifyImg.setImageBitmap(bitmap);
                }
            });
//            }
        }
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

                    try {
                        startAlarm(newAlarm.getTiffanyDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
                        , mPref.getAlarmID()
                        , newAlarm.getSpotifyURI());

        service.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    //Log.e("POST", "alarm edit success" + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("POST", "Unable to edit alarm");
            }
        });
    }


    public void sendPost() {
        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
        AlarmPost post = new AlarmPost(newAlarm.getAlarmDescription(), newAlarm.getAlarmTime(),  newAlarm.getVolume(), newAlarm.getGame(),true, mPref.getUserID(), newAlarm.getSpotifyURI());
        Call<ResponseBody> alarmCall = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class).alarmPost(
                newAlarm.getAlarmDescription(), newAlarm.getAlarmTime(),  newAlarm.getGame(), newAlarm.getVolume(), true, mPref.getUserID(), newAlarm.getSpotifyURI()
        );
        alarmCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    //Log.e("POST", "post submitted to API." + response.body().toString());
                } else{
                    //Log.e("POST", call.request().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("POST", "Unable to submit post to API.");
            }
        });
    }

    private void startAlarm(String time) throws ParseException {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("Game", newAlarm.getGame());
        intent.putExtra("URI", newAlarm.spotify_uri);
        intent.putExtra("Volume", newAlarm.volume);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), newAlarm.alarmDescription.hashCode() + mPref.getUserID(), intent, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = sdf.parse(time);
        long millis = date.getTime();

        long total_millis = millis - System.currentTimeMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,  millis, pendingIntent);
    }
}
