package com.codepath.apps.squawker.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.squawker.Fragments.ComposeFragment;
import com.codepath.apps.squawker.Fragments.TimelineFragment;
import com.codepath.apps.squawker.Fragments.UserTimelineFragment;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;

public class ProfileActivity extends AppCompatActivity implements TimelineFragment.IOnReplyListener, ComposeFragment.IOnTweetListener {
    private static final String ARG_SCREEN_NAME = "ARG_SCREEN_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra(ARG_SCREEN_NAME);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    @Override
    public void onReplyTweet(Tweet tweet) {

    }

    @Override
    public void onPostTweet(Tweet tweet) {

    }
}
