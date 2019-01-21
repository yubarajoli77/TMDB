package com.amniltech.filmstack.View;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.amniltech.filmstack.View.CustomAdapter.ViewPagerAdapter;
import com.amniltech.filmstack.R;

public class LandingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_circle_love_white,
            R.drawable.ic_box_star_white,
            R.drawable.ic_picture_frame_white,
            R.drawable.ic_calender_white
    };
    private String[] tabTitles ;
    private SearchView svSearchMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        tabTitles = getResources().getStringArray(R.array.movie_tabs);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        svSearchMovie = findViewById(R.id.sv_search_box);
        svSearchMovie.setQueryHint(getResources().getString(R.string.search_movie));

        svSearchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String movieName) {
                startActivity(new Intent(LandingActivity.this,SearchResultActivity.class)
                        .putExtra(SearchResultActivity.MOVIE_SEARCH_WORD,movieName));
                svSearchMovie.setQuery("",false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        setupTabIcons();
    }

    private void setupTabIcons() {

        TextView tabPopular = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabPopular.setText(tabTitles[0]);
        tabPopular.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabPopular);

        TextView tabLatest = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabLatest.setText(tabTitles[1]);
        tabLatest.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabLatest);

        TextView tabShowing = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabShowing.setText(tabTitles[2]);
        tabShowing.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabShowing);

        TextView tabUpcoming = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabUpcoming.setText(tabTitles[3]);
        tabUpcoming.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabUpcoming);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PopularFragment(), tabTitles[0]);
        adapter.addFrag(new TopRatedFragment(), tabTitles[1]);
        adapter.addFrag(new NowShowingFragment(), tabTitles[2]);
        adapter.addFrag(new UpcomingFragment(), tabTitles[3]);

        viewPager.setAdapter(adapter);
    }
}
