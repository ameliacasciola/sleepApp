package c.cpen391.alarms.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.daimajia.swipe.util.Attributes;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.adapters.SwipeRecyclerViewAdapter;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.Alarm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabAlarms extends Fragment {

    private ArrayList<Alarm> alarmList;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    ProgressDialog progressDoalog;
    protected static CustomSharedPreference mPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview =  inflater.inflate(R.layout.alarms_fragment, container, false);
        //initButtonList(rootview);


        mPref = ((CustomApplication)getActivity().getApplicationContext()).getShared();
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        // get alarms from database
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<List<Alarm>> call = service.getAlarms(mPref.getUserID());
        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response< List<Alarm>> response) {
                progressDoalog.dismiss();
                List<Alarm> alarmList = response.body();
                initList(rootview, alarmList);
            }

            @Override
            public void onFailure(Call<List<Alarm>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        initButtonList(rootview);

        View decorView = getActivity().getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        return rootview;
    }

    private void onRefresh(View rootview){
        final View base_view = rootview;
        /*Create handle for the RetrofitInstance interface*/
        // get alarms from database
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<List<Alarm>> call = service.getAlarms(mPref.getUserID());
        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response< List<Alarm>> response) {
                progressDoalog.dismiss();
                List<Alarm> alarmList = response.body();
                initList(base_view, alarmList);
            }

            @Override
            public void onFailure(Call<List<Alarm>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initList(View rootview, List<Alarm> alarmList){
        tvEmptyView = (TextView) rootview.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.alarm_recycler);

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (alarmList == null || alarmList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
        // Creating Adapter object
        SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(getContext(), alarmList);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Multiple to reveal bottom Views for multile items in List
        mAdapter.setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initButtonList(View rootview){
        FloatingActionButton add_button = rootview.findViewById(R.id.add_alarm);
        add_button.setIcon(R.drawable.baseline_add_white_18dp);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAlarm.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}