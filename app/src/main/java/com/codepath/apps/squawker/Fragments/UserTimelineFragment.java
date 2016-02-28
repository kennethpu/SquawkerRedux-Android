package com.codepath.apps.squawker.Fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.squawker.Models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by kpu on 2/28/16.
 */
public class UserTimelineFragment extends TimelineFragment {
    private static final String ARG_SCREEN_NAME = "ARG_SCREEN_NAME";

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment frag = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCREEN_NAME, screenName);
        frag.setArguments(args);

        return frag;
    }

    @Override
    protected void populateTimeline() {
        String screenName = getArguments().getString(ARG_SCREEN_NAME);
        client.getUserTimeline(maxId, screenName, new JsonHttpResponseHandler() {
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

    @Override
    public void insertTweet(Tweet tweet) {
        // Only insert tweet if author is the current timeline's user
        String screenName = tweet.getUser().getScreenName();
        if (screenName.equals(getArguments().getString(ARG_SCREEN_NAME))) {
            super.insertTweet(tweet);
        }
    }
}
