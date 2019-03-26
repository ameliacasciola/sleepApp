package c.cpen391.alarms.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

import c.cpen391.alarms.R;
import c.cpen391.alarms.adapters.SwipeRecyclerViewAdapter;
import c.cpen391.alarms.models.Alarm;

public class TabAlarms extends Fragment {

    private ArrayList<Alarm> alarmList;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.alarms_fragment, container, false);
        //initButtonList(rootview);
        initList(rootview);
        return rootview;
    }

    private void initList(View rootview){
        tvEmptyView = (TextView) rootview.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.alarm_recycler);

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        alarmList = new ArrayList<Alarm>();
        loadData();

        if (alarmList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }


        // Creating Adapter object
        SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(getContext(), alarmList);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
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

    // load initial data
    private void loadData() {
        for (int i = 0; i <= 10; i++) {
            Alarm next = new Alarm(i, "new alarm", "12:00", 10, true);
            alarmList.add(next);
        }
    }
//    private void initButtonList(View rootview){
//        FloatingActionButton add_button = rootview.findViewById(R.id.add_alarm);
//        add_button.setIcon(R.drawable.ic_baseline_add_48px);
//    }
}