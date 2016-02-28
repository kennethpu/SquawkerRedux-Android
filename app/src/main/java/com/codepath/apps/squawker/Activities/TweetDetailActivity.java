package com.codepath.apps.squawker.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.squawker.Fragments.ComposeFragment;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.SquawkerApplication;
import com.codepath.apps.squawker.SquawkerClient;
import com.codepath.apps.squawker.UserStorage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetDetailActivity extends AppCompatActivity {
    private final static String ARG_TWEET = "ARG_TWEET";
    private final static String ARG_POSITION = "ARG_POSITION";
    private int mPosition;
    private Tweet mTweet;

    private SquawkerClient client;

    @Bind(R.id.rivUserImage)
    ImageView rivUserImage;

    @Bind(R.id.tvUserFullName)
    TextView tvUserFullName;

    @Bind(R.id.tvUserScreenName)
    TextView tvUserScreenName;

    @Bind(R.id.tvDetailBody)
    TextView tvDetailBody;

    @Bind(R.id.rivDetailMedia)
    RoundedImageView rivDetailMedia;

    @Bind(R.id.tvDate)
    TextView tvDate;

    @Bind(R.id.tvDetailRetweetCount)
    TextView tvDetailRetweetCount;

    @Bind(R.id.tvDetailFavoriteCount)
    TextView tvDetailFavoriteCount;

    @Bind(R.id.ibDetailReply)
    ImageButton ibDetailReply;

    @Bind(R.id.ibDetailRetweet)
    ImageButton ibDetailRetweet;

    @Bind(R.id.ibDetailLike)
    ImageButton ibDetailLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);

        client = SquawkerApplication.getRestClient();

        final Tweet tweet = getIntent().getExtras().getParcelable(ARG_TWEET);
        mPosition = getIntent().getIntExtra(ARG_POSITION, 0);

        rivUserImage.setImageResource(0);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(rivUserImage);
        tvUserFullName.setText(tweet.getUser().getFullName());
        tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        tvDate.setText(tweet.getCreatedAt());
        tvDetailBody.setText(tweet.getBody());

        // Configure media image
        if (tweet.getMediaUrl() != null) {
            rivDetailMedia.setVisibility(View.VISIBLE);
            rivDetailMedia.setImageResource(0);
            Glide.with(this).load(tweet.getMediaUrl()).into(rivDetailMedia);
        } else {
            rivDetailMedia.setVisibility(View.GONE);
        }

        // Configure reply button click action
        ibDetailReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                String preText = "@" + tweet.getUser().getScreenName() + " ";
                ComposeFragment composeTweetDialog = ComposeFragment.newInstance(preText);
                composeTweetDialog.show(fm, "fragment_compose_tweet");
            }
        });

        // Configure retweet section UI
        configureRetweetsForTweet(tweet);

        // Configure retweet button click action
        ibDetailRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ibDetailRetweet.isSelected();
                if (!isSelected) {
                    client.retweetTweet(tweet.getuId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSON(response);
                            mTweet = newTweet;
                            configureRetweetsForTweet(newTweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });

        // Configure like section UI
        configureLikesForTweet(tweet);

        // Configure like button click action
        ibDetailLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ibDetailLike.isSelected();
                if (isSelected) {
                    client.unFavoriteTweet(tweet.getuId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSON(response);
                            mTweet = newTweet;
                            configureLikesForTweet(newTweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } else {
                    client.favoriteTweet(tweet.getuId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSON(response);
                            mTweet = newTweet;
                            configureLikesForTweet(newTweet);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            }
        });
    }

    private String getTimeString(String rawJsonDat) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        return "";
    }

    private void configureLikesForTweet(Tweet tweet) {
        // Configure like button
        ibDetailLike.setSelected(tweet.isFavorited());

        // Configure like count text
        int likeCount = tweet.getFavoriteCount();
        tvDetailFavoriteCount.setText(String.valueOf(likeCount));
    }

    private void configureRetweetsForTweet(Tweet tweet) {
        // Configure retweet button
        UserStorage userStorage = new UserStorage(getApplicationContext());
        if (userStorage.getUserId() == tweet.getUser().getuId()) {
            ibDetailRetweet.setEnabled(false);
        } else {
            ibDetailRetweet.setEnabled(true);
            ibDetailRetweet.setSelected(tweet.isRetweeted());
        }

        // Configure retweet count text
        int retweetCount = tweet.getRetweetCount();
        tvDetailRetweetCount.setText(String.valueOf(retweetCount));
    }
}
