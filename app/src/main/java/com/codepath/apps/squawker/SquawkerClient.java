package com.codepath.apps.squawker;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public class SquawkerClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "aZ6hIO9Y1rVlf9hw6vGx8ccm7";
    public static final String REST_CONSUMER_SECRET = "gzdON6cBesmQeTiwJT4jHCOQ6kIBCArx2IUSkOg7zqmrhjwCsw";
    public static final String REST_CALLBACK_URL = "oauth://codepathtweets";

    public SquawkerClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void geUserCredentials(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        if (maxId != 0) {
            params.put("max_id", maxId-1);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if (maxId != 0) {
            params.put("max_id", maxId-1);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void retweetTweet(long tweetId, AsyncHttpResponseHandler handler) {
        String extension = String.format("statuses/retweet/%d.json", tweetId);
        String apiUrl = getApiUrl(extension);
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    public void favoriteTweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void unFavoriteTweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().post(apiUrl, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }
}