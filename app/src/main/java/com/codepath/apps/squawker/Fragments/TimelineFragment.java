package com.codepath.apps.squawker.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.squawker.Activities.TweetDetailActivity;
import com.codepath.apps.squawker.EndlessScrollListener;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.SquawkerApplication;
import com.codepath.apps.squawker.SquawkerClient;
import com.codepath.apps.squawker.TweetsArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragment extends Fragment implements TweetsArrayAdapter.ITweetActionsListener {
    private final static String ARG_TWEET = "ARG_TWEET";
    private final static String ARG_POSITION = "ARG_POSITION";

    private final static int REQUEST_CODE = 42;

    @Bind(R.id.lvTweets)
    ListView lvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    protected SquawkerClient client;    // REST client for making network requests
    protected long maxId;               // Used to keep track of last tweet-id (for infinite pagination)

    private TweetsArrayAdapter tweetsArrayAdapter;
    private List<Tweet> tweets;

    private IOnReplyListener mListener;

    public interface IOnReplyListener {
        void onReplyTweet(Tweet tweet);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = SquawkerApplication.getRestClient();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);

        // Create adapter and link it to the list view
        tweets = new ArrayList<>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweets, this);
        lvTweets.setAdapter(tweetsArrayAdapter);

        // Set up pull-to-refresh
        swipeContainer.setColorSchemeResources(R.color.twitter_blue);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweetsArrayAdapter.clear();
                refreshTimeline();
            }
        });

        // Set up infinite scrolling
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int totalItemsCount) {
//                populateTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), TweetDetailActivity.class);
                i.putExtra(ARG_TWEET, tweetsArrayAdapter.getItem(position));
                i.putExtra(ARG_POSITION, position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        // Refresh UI
        refreshTimeline();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IOnReplyListener) {
            mListener = (IOnReplyListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement TimelineFragment.IOnReplyListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = data.getExtras().getParcelable(ARG_TWEET);
            if (tweet == null) {
                return;
            }

            int position = data.getIntExtra(ARG_POSITION, 0);
            if (position < 0) {
                insertTweet(tweet);
            } else {
                updateTweet(tweet, position);
            }
        }
    }

    @Override
    public void replyTweet(int position) {
        Tweet tweet = tweetsArrayAdapter.getItem(position);
        mListener.onReplyTweet(tweet);
    }

    @Override
    public void retweetTweet(final int position) {
        final Tweet tweet = tweetsArrayAdapter.getItem(position);
        client.retweetTweet(tweet.getuId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = tweet;
                newTweet.setRetweeted(true);
                newTweet.setRetweetCount(tweet.getRetweetCount() + 1);
                updateTweet(newTweet, position);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getContext(), "Retweet Failed!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void favoriteTweet(final int position) {
        final Tweet tweet = tweetsArrayAdapter.getItem(position);
        client.favoriteTweet(tweet.getuId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSON(response);
                updateTweet(newTweet, position);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getContext(), "Favorite Failed!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void unFavoriteTweet(final int position) {
        final Tweet tweet = tweetsArrayAdapter.getItem(position);
        client.unFavoriteTweet(tweet.getuId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSON(response);
                updateTweet(newTweet, position);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Toast.makeText(getContext(), "UnFavorite Failed!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void insertTweet(Tweet tweet) {
        tweetsArrayAdapter.insert(tweet, 0);
    }

    protected void populateTimeline() {
        // TO BE OVERRIDDEN IN SUB-CLASSES
    }

    protected void handleTimelineFetch(ArrayList<Tweet> tweets) {
        tweetsArrayAdapter.addAll(tweets);
        if (tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).getuId();
        }
    }

    private void updateTweet(Tweet tweet, int position) {
        tweets.set(position, tweet);
        tweetsArrayAdapter.notifyDataSetChanged();
    }

    private void refreshTimeline() {
        maxId = 0;
        populateTimeline();
    }
}
