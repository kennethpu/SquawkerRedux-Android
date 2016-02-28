package com.codepath.apps.squawker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.squawker.Fragments.HomeTimelineFragment;
import com.codepath.apps.squawker.Fragments.MentionsTimelineFragment;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "HOME", "MENTIONS" };

    public TimelineFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeTimelineFragment();
        } else if (position == 1) {
            return new MentionsTimelineFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
