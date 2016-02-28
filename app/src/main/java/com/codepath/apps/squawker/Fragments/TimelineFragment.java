package com.codepath.apps.squawker.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragment extends Fragment {
    private final static String ARG_TWEET = "ARG_TWEET";
    private final static String ARG_POSITION = "ARG_POSITION";

    @Bind(R.id.lvTweets)
    ListView lvTweets;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    protected SquawkerClient client;    // REST client for making network requests
    protected long maxId;               // Used to keep track of last tweet-id (for infinite pagination)

    private TweetsArrayAdapter tweetsArrayAdapter;
    private List<Tweet> tweets;

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
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweets, TimelineFragment.this);
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
//                populateTimeline();
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), TweetDetailActivity.class);
                i.putExtra(ARG_TWEET, tweetsArrayAdapter.getItem(position));
                i.putExtra(ARG_POSITION, position);
                getActivity().startActivity(i);
            }
        });

        // Refresh UI
        refreshTimeline();

        return view;
    }

    public void updateTweet(Tweet tweet, int position) {
        tweets.set(position, tweet);
        tweetsArrayAdapter.notifyDataSetChanged();
    }

    private void refreshTimeline() {
        maxId = 0;
        populateTimeline();
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
}
