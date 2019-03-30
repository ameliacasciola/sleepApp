package c.cpen391.alarms.tabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import c.cpen391.alarms.R;

public class TabGames extends Fragment {
    private ViewPager mViewPager;

    private NavigationTabStrip mTopNavigationTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.games_fragment, container, false);
        return rootview;
    }

}