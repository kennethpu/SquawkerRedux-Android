package com.codepath.apps.squawker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kpu on 2/21/16.
 */
public class UserStorage {
    private SharedPreferences prefs;
    private final String USER_PREFS = "UserPrefs";
    private final String KEY_ID = "KEY_ID";
    private final String KEY_FULL_NAME = "KEY_FULL_NAME";
    private final String KEY_SCREEN_NAME = "KEY_SCREEN_NAME";
    private final String KEY_IMAGE_URL = "KEY_IMAGE_URL";

    public UserStorage(Context context) {
        prefs = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
    }

    public void saveUserData(long userId, String fullName, String screenName, String imageUrl) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putLong(KEY_ID, userId);
        prefsEditor.putString(KEY_FULL_NAME, fullName);
        prefsEditor.putString(KEY_SCREEN_NAME, screenName);
        prefsEditor.putString(KEY_IMAGE_URL, imageUrl);
        prefsEditor.commit();
    }

    public long getUserId() {
        return prefs.getLong(KEY_ID, 0);
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, null);
    }

    public String getScreenName() {
        return prefs.getString(KEY_SCREEN_NAME, null);
    }

    public String getImageUrl() {
        return prefs.getString(KEY_IMAGE_URL, null);
    }
}
