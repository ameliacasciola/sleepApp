package c.cpen391.alarms.custom;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.SleepData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrendGraph extends Fragment {

    ProgressDialog progressDoalog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.trends_graph, container, false);

        int duration = getArguments().getInt("duration");

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Call<List<SleepData>> call;
        Date date = new Date();

        if (duration == 0){
            call = service.getSleepData(dateFormat.format(date), dateFormat.format(getTomorrow()), null);
        } else if (duration == 1){
            call = service.getSleepData(dateFormat.format(getLastWeek()), dateFormat.format(getTomorrow()), null);
        } else {
            call = service.getSleepData(null, null, null);
        }

        call.enqueue(new Callback<List<SleepData>>() {
            @Override
            public void onResponse(Call<List<SleepData>> call, Response< List<SleepData>> response) {
                progressDoalog.dismiss();
                List<SleepData> sleepDataList= response.body();
                initTrendsGraph(sleepDataList, rootview);
            }

            @Override
            public void onFailure(Call<List<SleepData>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootview;
    }

    private Date getLastWeek() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    private Date getTomorrow() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        return cal.getTime();
    }

    private void setData(LineChart chart, List<SleepData> sleepDataList){
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> HRentries = new ArrayList<Entry>();

        List<String> dates = new ArrayList<String>();

        for (int i = 0; i < sleepDataList.size(); i++){
            SleepData sd = sleepDataList.get(i);
            HRentries.add(new Entry(i, sd.heart_rate));
            entries.add(new Entry(i, sd.oxygen_level));
            dates.add(sd.getTime());
        }
        LineDataSet oxygenDS = new LineDataSet(entries, "Oxygen");
        LineDataSet heartRateDS = new LineDataSet(HRentries, "Heart Rate");

        String[] dateArr = Arrays.copyOf(dates.toArray(), dates.toArray().length, String[].class);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateXAxisValueFormatter(dateArr));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setTextColor(R.color.dark_gray);
        legend.setDrawInside(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        oxygenDS.setDrawIcons(false);
        oxygenDS.setColor(R.color.teal);
        oxygenDS.setDrawCircles(false);
        oxygenDS.setLineWidth(0);
        oxygenDS.setDrawCircleHole(false);
        oxygenDS.setValueTextSize(0);
        oxygenDS.setDrawFilled(true);
        oxygenDS.setFormLineWidth(0);
        oxygenDS.disableDashedLine();
        oxygenDS.disableDashedHighlightLine();
        oxygenDS.setHighLightColor(R.color.teal);

        oxygenDS.setFillColor(R.color.teal);
        oxygenDS.setFillAlpha(8);

        heartRateDS.setDrawIcons(false);
        heartRateDS.setColor(R.color.white);
        heartRateDS.setDrawCircles(false);
        heartRateDS.setLineWidth(0);
        heartRateDS.setDrawCircleHole(false);
        heartRateDS.setValueTextSize(0);
        heartRateDS.setDrawFilled(true);
        heartRateDS.setFormLineWidth(0);
        heartRateDS.disableDashedLine();
        heartRateDS.disableDashedHighlightLine();
        heartRateDS.setHighLightColor(R.color.white);

        heartRateDS.setFillColor(Color.MAGENTA);
        heartRateDS.setFillAlpha(8);

        LineData chartData = new LineData();
        chartData.addDataSet(oxygenDS);
        chartData.addDataSet(heartRateDS);
        chart.setData(chartData);
    }

    private void initTrendsGraph(List<SleepData> sleepDataList, View rootview){
        LineChart sdChart = (LineChart) rootview.findViewById(R.id.chart);

        sdChart.getDescription().setEnabled(false);
        sdChart.setDrawBorders(false);
        sdChart.setDrawGridBackground(false);

        setData(sdChart, sleepDataList);
        sdChart.invalidate(); // refresh
    }


}
