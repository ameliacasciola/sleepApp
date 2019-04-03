package c.cpen391.alarms.tabs;

import android.graphics.Color;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.custom.DateXAxisValueFormatter;
import c.cpen391.alarms.custom.TrendGraph;
import c.cpen391.alarms.models.Point;
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
        final View view = rootview;

        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);

        Call<Prediction> call;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        call = service.getPredictionData(dateFormat.format(getLastMonth()), dateFormat.format(getTomorrow()));
        Log.i("PREDICTION", call.request().toString());
        call.enqueue(new Callback<Prediction>() {
            @Override
            public void onResponse(Call<Prediction> call, Response<Prediction> response) {
                Prediction pred = response.body();
                Log.i("PREDICTION", Integer.toString(response.body().getDegree()));
                initPredictionsGraph(pred, view);
            }

            @Override
            public void onFailure(Call<Prediction> call, Throwable t) {
                Log.e("TREND", t.getMessage());
                Toast.makeText(getActivity(), "Trend error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPredictionsGraph (Prediction pred, View rootview){
        LineChart pChart = (LineChart) rootview.findViewById(R.id.prediction_chart);
        TextView suggestionText = rootview.findViewById(R.id.suggestions);

        Log.i("TREND", Integer.toString(pred.getDegree()));
        Log.i("TREND", Boolean.toString(pred.getDegree() == 3));

        Integer degree = pred.getDegree();
        if (degree == 1){
            suggestionText.setText("A downwards slope is a sign that your metabolism is working overtime. Avoid late meals and late workouts to avoid waking up feeling unrefreshed.");
        } else if (degree == 2){
            suggestionText.setText("You have an optimal heart rate curve. The time of your lowest heart rate coincides with the midpoint of sleep. Keep up the good work!");
        } else if (degree == 3){
            suggestionText.setText("Your heart rate generally increases right after you fall asleep, and it may be a sign that you're too tired for bed. Try maintaining a steady sleep routine.");
        } else{
            suggestionText.setText("Not enough data to make accurate predictions.");
        }

        pChart.getDescription().setEnabled(false);
        pChart.setDrawBorders(false);
        pChart.setDrawGridBackground(false);

        setData(pChart, pred.getPoints());
        pChart.invalidate(); // refresh
    }

    private void setData(LineChart chart, List<Point> pointsList){
        List<Entry> entries = new ArrayList<Entry>();
        if (pointsList != null) {
            for (int i = 0; i < pointsList.size(); i++) {
                Point p = pointsList.get(i);
                entries.add(new Entry(p.getX(), p.getY()));
            }
        }

        LineDataSet functionDS = new LineDataSet(entries, "Predicted Sleep Trend");

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setTextColor(R.color.dark_gray);
        legend.setDrawInside(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        functionDS.setDrawIcons(false);
        functionDS.setColor(R.color.goog_yellow);
        functionDS.setDrawCircles(false);
        functionDS.setLineWidth(0);
        functionDS.setDrawCircleHole(false);
        functionDS.setValueTextSize(0);
        functionDS.setDrawFilled(true);
        functionDS.setFormLineWidth(0);
        functionDS.disableDashedLine();
        functionDS.disableDashedHighlightLine();
        functionDS.setHighLightColor(R.color.goog_yellow);

        functionDS.setFillColor(R.color.goog_yellow);
        functionDS.setFillAlpha(8);

        LineData chartData = new LineData();
        chartData.addDataSet(functionDS);
        chart.setData(chartData);
    }

    private Date getTomorrow() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +2);
        return cal.getTime();
    }


    private Date getLastMonth() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Log.i("DATES", cal.getTime().toString());
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