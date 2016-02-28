package com.codepath.apps.squawker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kpu on 2/20/16.
 */
public class Tweet implements Parcelable {
    private long uId;
    private String body;
    private User user;
    private String createdAt;
    private boolean retweeted;
    private int retweetCount;
    private boolean favorited;
    private int favoriteCount;
    private String mediaUrl;

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.uId = jsonObject.getLong("id");
            tweet.body = jsonObject.getString("text");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
            JSONArray mediaArray = jsonObject.getJSONObject("entities").getJSONArray("media");
            if (mediaArray.length() > 0) {
                tweet.mediaUrl = mediaArray.getJSONObject(0).getString("media_url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public long getuId() {
        return uId;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public Tweet() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uId);
        dest.writeString(this.body);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.createdAt);
        dest.writeByte(retweeted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.retweetCount);
        dest.writeByte(favorited ? (byte) 1 : (byte) 0);
        dest.writeInt(this.favoriteCount);
        dest.writeString(this.mediaUrl);
    }

    private Tweet(Parcel in) {
        this.uId = in.readLong();
        this.body = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.retweeted = in.readByte() != 0;
        this.retweetCount = in.readInt();
        this.favorited = in.readByte() != 0;
        this.favoriteCount = in.readInt();
        this.mediaUrl = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
