package c.cpen391.alarms.tabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.custom.TrendGraph;
import c.cpen391.alarms.models.Prediction;
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
        initPrediction(rootview);
        initUI(rootview);
        setUI(rootview);
        setIcons(rootview);
        return rootview;
    }

    private void initPrediction(View rootview){
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);

        Call<List<Prediction>> call;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        call = service.getPredictionData(dateFormat.format(getLastMonth()), dateFormat.format(getTomorrow()));
        call.enqueue(new Callback<List<Prediction>>() {
            @Override
            public void onResponse(Call<List<Prediction>> call, Response< List<Prediction>> response) {
                List<Prediction> predictionList= response.body();
                for (int i = 0; i < predictionList.size(); i++){
                    Log.i("PREDICTION", Integer.toString(predictionList.get(i).getDegree()));
                }
            }

            @Override
            public void onFailure(Call<List<Prediction>> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Date getTomorrow() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        return cal.getTime();
    }


    private Date getLastMonth() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        return cal.getTime();
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
                Bundle bundle = new Bundle();
                TrendGraph tg = new TrendGraph();
                switch (position) {
                    case 0:
                        bundle.putInt("duration", 0);
                        tg.setArguments(bundle);
                        return tg;
                    case 1:
                        bundle.putInt("duration", 1);
                        tg.setArguments(bundle);
                        return tg;
                    case 2:
                        bundle.putInt("duration", 2);
                        tg.setArguments(bundle);
                        return tg;
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