package c.cpen391.alarms.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import c.cpen391.alarms.R;
import c.cpen391.alarms.adapters.LeaderboardRecyclerViewAdapter;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.games.BurpeesGame;
import c.cpen391.alarms.games.ColorSequenceGame;
import c.cpen391.alarms.games.GraphicsActivity;
import c.cpen391.alarms.games.JumpingJacksGame;
import c.cpen391.alarms.games.MainSpellingActivity;
import c.cpen391.alarms.games.SquatGame;
import c.cpen391.alarms.games.WalkingStepsGame;
import c.cpen391.alarms.models.HighScore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabGames extends Fragment {
    private ViewPager mViewPager;
    public SlidingUpPanelLayout leaderboard;
    private NavigationTabStrip mTopNavigationTabStrip;
    private CardView toGraphics;
    private CardView toSpelling;
    private CardView toColourSequence;
    private CardView toSteps;
    private CardView toJump;
    private CardView toSquat;
    private CardView toBurpees;

    private View baseview;
    private RecyclerView leaderList;
    ProgressDialog progressDoalog;
    private LeaderboardRecyclerViewAdapter adapter;

    // Physical games list
    int[] gamesCardId = {
            R.id.games_card,
            R.id.games_card2,
            R.id.games_card3,
            R.id.games_card4
    };

    // Mental games list
    int[] mgameCardId = {
            R.id.mgame_card,
            R.id.mgame_card2,
            R.id.mgame_card3,
            R.id.mgame_card4
    };

    private String[] gamesUrls = {
            "https://cdn.dribbble.com/users/1061799/screenshots/4125959/ball-icons.png",
            "https://cdn.dribbble.com/users/36143/screenshots/3439603/pop_2x.png",
            "https://cdn.dribbble.com/users/156486/screenshots/5059471/ping-pan-trick.jpg",
            "https://cdn.dribbble.com/users/146798/screenshots/6052932/ping-pong-dribbble_4x.jpg"
    };

    private String[] mgamesUrls = {
            "https://cdn.dribbble.com/users/31752/screenshots/5840325/walking-.png",
            "https://cdn.dribbble.com/users/1008875/screenshots/4246359/push-it-4.png",
            "https://cdn.dribbble.com/users/1053699/screenshots/4876659/squats.png",
            "https://cdn.dribbble.com/users/337606/screenshots/2874587/concept1av2.jpg"
    };

    private String[] gamesNames = {
            "Colour Sequence",
            "Bubble Pop",
            "Eggcellent Spelling",
            "Where did the Egg Go"
    };

    private String[] mgamesNames = {
            "Eggs-cercise",
            "Jumping Jacks",
            "Squats",
            "Burpees"
    };

    private String[] gamesType = {
            "Memorization",
            "Just get the Egg",
            "Speed",
            "Planning"
    };

    private String[] mgamesType = {
            "Walking around",
            "Cardio exercise",
            "Strengthening muscles",
            "Cardio exercise"
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.games_fragment, container, false);
        initLeaderboardPage(rootview);
        baseview = rootview;

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        generateUserList(rootview);
        initGamesScroll(rootview, gamesCardId);

        return rootview;
    }

    public SlidingUpPanelLayout getLeaderboard(){
        return this.leaderboard;
    }


    private void initGamesScroll(View rootview, int[] gamesCardId){
        toColourSequence = (CardView) rootview.findViewById(R.id.games_card);
        toGraphics = (CardView) rootview.findViewById(R.id.games_card2);
        toSpelling = (CardView) rootview.findViewById(R.id.games_card3);

        toSteps = (CardView) rootview.findViewById(R.id.mgame_card);
        toJump = (CardView) rootview.findViewById(R.id.mgame_card2);
        toSquat = (CardView) rootview.findViewById(R.id.mgame_card3);
        toBurpees = (CardView) rootview.findViewById(R.id.mgame_card4);

        for (int i = 0; i < gamesCardId.length; i++){
            View gamesCardView = rootview.findViewById(gamesCardId[i]);
            ImageView gamesImg = gamesCardView.findViewById(R.id.gameImg);
            TextView gamesName = gamesCardView.findViewById(R.id.game_name);
            gamesName.setText(gamesNames[i]);

            // Mental
            if(i == 0){
                gamesCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ColorSequenceGame.class);
                        startActivity(intent);
                    }
                });
            }

            if(i==1) {
                toGraphics.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), GraphicsActivity.class);
                        startActivity(intent);
                    }
                });
            }

            if(i==2) {
                toSpelling.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainSpellingActivity.class);
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


        // physical games
        for (int i = 0; i < mgameCardId.length; i++) {
            View gamesCardView = rootview.findViewById(mgameCardId[i]);
            ImageView gamesImg = gamesCardView.findViewById(R.id.gameImg);
            TextView gamesName = gamesCardView.findViewById(R.id.game_name);
            gamesName.setText(mgamesNames[i]);
            TextView gameType = gamesCardView.findViewById(R.id.game_type);
            gameType.setText(mgamesType[i]);

            //go to color sequence game
            if(i==0) {
                gamesCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WalkingStepsGame.class);
                        startActivity(intent);
                    }
                });
            }

            if(i==1) {
                toJump.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), JumpingJacksGame.class);
                        startActivity(intent);
                    }
                });
            }

            if(i==2) {
                toSquat.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SquatGame.class);
                        startActivity(intent);
                    }
                });
            }

            if(i==3) {
                toBurpees.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BurpeesGame.class);
                        startActivity(intent);
                    }
                });
            }

            Picasso.get()
                    .load(mgamesUrls[i])
                    .placeholder(R.drawable.blue_plane)
                    .into(gamesImg);
        }
    }

    private void generateUserList(View rootview){
        final View view = rootview;
        /*Create handle for the RetrofitInstance interface*/
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);

        Call<List<HighScore>> call = service.getHighScores();
        call.enqueue(new Callback<List<HighScore>>() {
            @Override
            public void onResponse(Call<List<HighScore>> call, Response< List<HighScore>> response) {
                progressDoalog.dismiss();
                List<HighScore> highScoreList= response.body();
                generateDataList(highScoreList);
            }

            @Override
            public void onFailure(Call<List<HighScore>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<HighScore> highScoreList){
        View rootview = baseview;
        leaderList = rootview.findViewById(R.id.user_list);
        Integer size = highScoreList.size();
        Log.e("SIZE", Integer.toString(size));
        if (size > 1){
            adapter = new LeaderboardRecyclerViewAdapter(getActivity(), highScoreList.subList(1, size-1));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            leaderList.setLayoutManager(layoutManager);
            leaderList.setAdapter(adapter);
        }

        TextView winner = rootview.findViewById(R.id.first_place_user);
        TextView score = rootview.findViewById(R.id.user_score);
        if (size > 0){
            winner.setText(highScoreList.get(0).getUsername());
            Integer highest_score = highScoreList.get(0).getScore();
            if (highest_score != null) {
                if (highest_score > 10000){
                    highest_score /= 1000;
                    String result = Integer.toString(highest_score);
                    result = result.concat("K");
                    score.setText(result);
                }
                else{
                    score.setText(Integer.toString(highScoreList.get(0).getScore()));
                }
            }
        }
    }

    private void initLeaderboardPage(View rootview){
        leaderboard = (SlidingUpPanelLayout) rootview.findViewById(R.id.leaderboard);
//        leaderboard.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//                Log.i("LEADERBOARD", "onPanelSlide, offset " + slideOffset);
//            }
//
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                Log.i("LEADERBOARD", "onPanelStateChanged " + newState);
//            }
//        });
        leaderboard.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboard.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        ImageView trophyPic = rootview.findViewById(R.id.trophy_img);
        Picasso.get()
                .load("https://cdn.dribbble.com/users/910078/screenshots/2544835/cup.jpg")
                .into(trophyPic);
    }

}