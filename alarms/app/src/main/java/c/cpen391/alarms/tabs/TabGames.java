package c.cpen391.alarms.tabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.squareup.picasso.Picasso;

import c.cpen391.alarms.R;

public class TabGames extends Fragment {
    private ViewPager mViewPager;

    private NavigationTabStrip mTopNavigationTabStrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.games_fragment, container, false);

        initUI(rootview);
        setUI(rootview);
        return rootview;
    }

    private void initUI(View rootview) {
        mViewPager = (ViewPager) rootview.findViewById(R.id.vp);
        mTopNavigationTabStrip = (NavigationTabStrip) rootview.findViewById(R.id.nts_top);
    }



    private void setUI(View rootview) {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = new View(getContext());
                container.addView(view);
                return view;
            }
        });

        mTopNavigationTabStrip.setTabIndex(1, true);

    }
}