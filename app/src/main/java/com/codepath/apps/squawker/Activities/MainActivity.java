package com.codepath.apps.squawker.Activities;

import android.content.Intent;
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
import com.codepath.apps.squawker.Models.User;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.SquawkerApplication;
import com.codepath.apps.squawker.TimelineFragmentPagerAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TimelineFragment.IOnReplyListener, ComposeFragment.IOnTweetListener {
    private static final String ARG_USER = "ARG_USER";

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

    @Override
    public void onReplyTweet(Tweet tweet) {
        String preText = "@" + tweet.getUser().getScreenName() + " ";
        composeTweet(preText);
    }

    @Override
    public void onPostTweet(Tweet tweet) {
        TimelineFragment timelineFragment = (TimelineFragment) timelineAdapter.getItem(viewPager.getCurrentItem());
        timelineFragment.insertTweet(tweet);
    }

    public void onComposeAction(MenuItem menuItem) {
        composeTweet("");
    }

    public void onProfileAction(MenuItem menuItem) {
        SquawkerApplication.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJSON(response);

                if (user != null) {
                    Intent i = new Intent(getBaseContext(), ProfileActivity.class);
                    i.putExtra(ARG_USER, user);
                    startActivity(i);
                }
            }
        });
    }

    public void composeTweet(String preText) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeTweetDialog = ComposeFragment.newInstance(preText);
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }
}
