package c.cpen391.alarms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TabHome extends Fragment {
    ImageView weatherCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.home_fragment, container, false);
        setWeatherCard(rootview);
        return rootview;
    }

    public void setWeatherCard(View rootview){
        weatherCard = (ImageView) rootview.findViewById(R.id.backdrop);
        weatherCard.setImageResource(R.drawable.blue_plane);
    }

}