package c.cpen391.alarms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.design.widget.TabLayout;

import c.cpen391.alarms.adapters.TabAdapter;
import c.cpen391.alarms.tabs.TabAlarms;
import c.cpen391.alarms.tabs.TabGames;
import c.cpen391.alarms.tabs.TabHome;
import c.cpen391.alarms.tabs.TabProfile;
import c.cpen391.alarms.tabs.TabTrends;

/**
 * Created by Grace on 2019-03-07.
 */

public class home extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_outline_home_48px,
            R.drawable.ic_outline_alarm_48px,
            R.drawable.ic_outline_insert_chart_24px,
            R.drawable.ic_outline_games_48px,
            R.drawable.ic_outline_person_48px,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new TabHome(), getString(R.string.home_tab), tabIcons[0]);
        adapter.addFragment(new TabAlarms(), getString(R.string.alarms_tab), tabIcons[1]);
        adapter.addFragment(new TabTrends(), "TRENDS", tabIcons[2]);
        adapter.addFragment(new TabGames(), getString(R.string.games_tab), tabIcons[3]);
        adapter.addFragment(new TabProfile(), getString(R.string.profile_tab), tabIcons[4]);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }
}
