package com.codepath.apps.squawker.Fragments;

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

import com.codepath.apps.squawker.Activities.TweetDetailActivity;
import com.codepath.apps.squawker.EndlessScrollListener;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.SquawkerApplication;
import com.codepath.apps.squawker.SquawkerClient;
import com.codepath.apps.squawker.TweetsArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private final static String ARG_TWEET = "ARG_TWEET";
    private final static String ARG_POSITION = "ARG_POSITION";

    private int mPage;

    @Bind(R.id.lvTweets)
    ListView lvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private SquawkerClient client;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private List<Tweet> tweets;

    private long maxId;

    public static TimelineFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);

        // Create adapter and link it to the list view
        tweets = new ArrayList<Tweet>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getContext(), tweets, TimelineFragment.this);
        lvTweets.setAdapter(tweetsArrayAdapter);

        // Set up pull-to-refresh
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
                populateTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "tet");
                Intent i = new Intent(getContext(), TweetDetailActivity.class);
                i.putExtra(ARG_TWEET, tweetsArrayAdapter.getItem(position));
                i.putExtra(ARG_POSITION, position);
                getActivity().startActivity(i);
            }
        });

        // Initialize our REST client
        client = SquawkerApplication.getRestClient();

        // Refresh UI
        refreshTimeline();

        return view;
    }

    public void insertTweet(Tweet tweet) {
        switch (mPage) {
            case 1:
                tweetsArrayAdapter.insert(tweet, 0);
                break;
            default:
                break;
        }
    }

    public void updateTweet(Tweet tweet, int position) {
        tweets.set(position, tweet);
        tweetsArrayAdapter.notifyDataSetChanged();
    }

    private void refreshTimeline() {
        maxId = 0;
        populateTimeline();
    }

    private void populateTimeline() {
        switch (mPage) {
            case 1:
                fetchHomeTimeline();
                break;
            case 2:
                fetchMentionsTimeline();
                break;
            default:
                break;
        }
    }

    private void fetchHomeTimeline() {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleTimelineFetch(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void fetchMentionsTimeline() {
        client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handleTimelineFetch(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void handleTimelineFetch(ArrayList<Tweet> tweets) {
        tweetsArrayAdapter.addAll(tweets);
        if (tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).getuId();
        }
    }

    public void addAll(List<Tweet> tweets) {
        tweetsArrayAdapter.addAll(tweets);
    }
}
