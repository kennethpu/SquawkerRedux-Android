package com.codepath.apps.squawker.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.squawker.Fragments.ComposeFragment;
import com.codepath.apps.squawker.Fragments.TimelineFragment;
import com.codepath.apps.squawker.Fragments.UserTimelineFragment;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.Models.User;
import com.codepath.apps.squawker.R;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements TimelineFragment.IOnReplyListener, ComposeFragment.IOnTweetListener {
    private static final String ARG_USER = "ARG_USER";

    @Bind(R.id.ivBanner)
    ImageView ivBanner;

    @Bind(R.id.rivProfile)
    RoundedImageView rivProfile;

    @Bind(R.id.tvUserTimelineFullName)
    TextView tvUserTimelineFullName;

    @Bind(R.id.tvUserTimelineScreenName)
    TextView tvUserTimelineScreenName;

    @Bind(R.id.tvTagLine)
    TextView tvTagLine;

    @Bind(R.id.tvTweetCount)
    TextView tvTweetCount;

    @Bind(R.id.tvFollowingCount)
    TextView tvFollowingCount;

    @Bind(R.id.tvFollowersCount)
    TextView tvFollowersCount;

    private UserTimelineFragment userTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        User user = getIntent().getExtras().getParcelable(ARG_USER);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            userTimelineFragment = UserTimelineFragment.newInstance(user.getScreenName());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }

        configureViewWithUser(user);
    }

    @Override
    public void onReplyTweet(Tweet tweet) {
        String preText = "@" + tweet.getUser().getScreenName() + " ";
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeTweetDialog = ComposeFragment.newInstance(preText);
        composeTweetDialog.show(fm, "fragment_compose_tweet");
    }

    @Override
    public void onPostTweet(Tweet tweet) {
        userTimelineFragment.insertTweet(tweet);
    }

    private void configureViewWithUser(User user) {
        ivBanner.setImageResource(0);
        Glide.with(this).load(user.getProfileBannerImageUrl()).centerCrop().into(ivBanner);

        rivProfile.setImageResource(0);
        Glide.with(this).load(user.getProfileImageUrl()).into(rivProfile);

        tvUserTimelineFullName.setText(user.getFullName());
        tvUserTimelineScreenName.setText("@" + user.getScreenName());
        tvTweetCount.setText(abbrevNumString(user.getNumTweets()));
        tvFollowingCount.setText(abbrevNumString(user.getNumFollowing()));
        tvFollowersCount.setText(abbrevNumString(user.getNumFollowers()));

        if (user.getTagLine().length() > 0) {
            tvTagLine.setText(user.getTagLine());
        } else {
            tvTagLine.setVisibility(View.GONE);
        }
    }

    private String abbrevNumString(int count) {
        if (count < 1000) {
            return Integer.toString(count);
        }

        double num = (double)count;
        int exp = (int)(Math.log10(num) / Math.log10(1000));
        String[] units = {"K","M","G","T","P","E"};

        double roundedNum = Math.round(10.0 * num / Math.pow(1000.0, exp)) / 10.0;

        return String.format("%.1f%s", roundedNum, units[exp-1]);
    }
}
