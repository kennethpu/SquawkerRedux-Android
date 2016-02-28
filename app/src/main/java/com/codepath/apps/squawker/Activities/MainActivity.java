package com.codepath.apps.squawker.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.squawker.Fragments.ComposeFragment;
import com.codepath.apps.squawker.Fragments.TimelineFragment;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.TimelineFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabsStrip;

    private TimelineFragmentPagerAdapter timelineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        timelineAdapter = new TimelineFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(timelineAdapter);

        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onComposeAction(MenuItem menuItem) {
        composeTweet("");
    }

    public void composeTweet(String preText) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeTweetDialog = ComposeFragment.newInstance(preText);
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    public void insertTweet(Tweet tweet) {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem == 0) {
            TimelineFragment timelineFragment = (TimelineFragment) timelineAdapter.getItem(viewPager.getCurrentItem());
            timelineFragment.insertTweet(tweet);
        }
    }
}
