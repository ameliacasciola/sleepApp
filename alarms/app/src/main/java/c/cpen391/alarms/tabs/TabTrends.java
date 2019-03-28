package c.cpen391.alarms.tabs;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.custom.DateXAxisValueFormatter;
import c.cpen391.alarms.custom.TrendGraph;
import c.cpen391.alarms.models.SleepData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabTrends extends Fragment {

    private ViewPager mViewPager;
    private String[] iconLinks = {
            "https://cdn4.iconfinder.com/data/icons/material-design-4/614/3012_-_Trending_Up-512.png"
    };

    private NavigationTabStrip mTopNavigationTabStrip;
    public void printList(List<SleepData> list){
        String out;
        for (int i = 0; i < list.size(); i++){
            Log.d("DEBUG", list.get(i).getDay());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.trend_fragment, container, false);

        initUI(rootview);
        setUI(rootview);
        setIcons(rootview);
        return rootview;
    }

    private void setIcons(View rootview){
        ImageView oxygenIcon = (ImageView) rootview.findViewById(R.id.oxygen_icon);
        Picasso.get()
                .load(R.drawable.areagraph)
                .into(oxygenIcon);

        ImageView heartRateIcon = (ImageView) rootview.findViewById(R.id.heartrate_icon);
        Picasso.get()
                .load(R.drawable.bargraph)
                .into(heartRateIcon);

        ImageView peaksIcon = (ImageView) rootview.findViewById(R.id.peak_icon);
        Picasso.get()
                .load(R.drawable.mailgraph)
                .into(peaksIcon);
    }

    private void initUI(View rootview) {
        mViewPager = (ViewPager) rootview.findViewById(R.id.vp);
        mTopNavigationTabStrip = (NavigationTabStrip) rootview.findViewById(R.id.nts_top);
    }

    private void setUI(View rootview) {
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()){
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new TrendGraph();
                    case 1:
                        return new TrendGraph();
                    case 2:
                        return new TrendGraph();
                    default: return new TabHome();
                }
            }

        });


        mTopNavigationTabStrip.setViewPager(mViewPager);

        mTopNavigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTopNavigationTabStrip.setStripColor(R.color.emerald_green);
        mTopNavigationTabStrip.setActiveColor(R.color.emerald_green);
    }
}