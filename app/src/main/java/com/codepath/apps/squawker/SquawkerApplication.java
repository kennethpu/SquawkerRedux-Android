package com.codepath.apps.squawker;

import android.content.Context;

public class SquawkerApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		SquawkerApplication.context = this;
	}

	public static SquawkerClient getRestClient() {
		return (SquawkerClient) SquawkerClient.getInstance(SquawkerClient.class, SquawkerApplication.context);
	}
}