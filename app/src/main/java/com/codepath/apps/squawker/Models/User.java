package com.codepath.apps.squawker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kpu on 2/20/16.
 */
public class User implements Parcelable {
    private long uId;
    private String fullName;
    private String screenName;
    private String profileImageUrl;
    private String profileBannerImageUrl;
    private String tagLine;
    private int numTweets;
    private int numFollowing;
    private int numFollowers;

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.uId = jsonObject.getLong("id");
            user.fullName = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.profileBannerImageUrl = jsonObject.getString("profile_banner_url");
            user.tagLine = jsonObject.getString("description");
            user.numTweets = Integer.parseInt(jsonObject.getString("statuses_count"));
            user.numFollowing = Integer.parseInt(jsonObject.getString("friends_count"));
            user.numFollowers = Integer.parseInt(jsonObject.getString("followers_count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public long getuId() {
        return uId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBannerImageUrl() {
        return profileBannerImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getNumTweets() {
        return numTweets;
    }

    public int getNumFollowing() {
        return numFollowing;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uId);
        dest.writeString(this.fullName);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.profileBannerImageUrl);
        dest.writeString(this.tagLine);
        dest.writeInt(this.numTweets);
        dest.writeInt(this.numFollowing);
        dest.writeInt(this.numFollowers);
    }

    private User(Parcel in) {
        this.uId = in.readLong();
        this.fullName = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.profileBannerImageUrl = in.readString();
        this.tagLine = in.readString();
        this.numTweets = in.readInt();
        this.numFollowing = in.readInt();
        this.numFollowers = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
