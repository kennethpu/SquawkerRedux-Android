package com.codepath.apps.squawker.Fragments;

import android.util.Log;

import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.UserStorage;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by kpu on 2/27/16.
 */
public class MentionsTimelineFragment extends TimelineFragment {

    @Override
    protected void populateTimeline() {
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

    @Override
    public void insertTweet(Tweet tweet) {
        String body = tweet.getBody();
        String screenName = "@" + new UserStorage(getContext()).getScreenName();
        if (body.contains(screenName)) {
            super.insertTweet(tweet);
        }
    }
}
