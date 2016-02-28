package com.codepath.apps.squawker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.squawker.Fragments.TimelineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "HOME", "MENTIONS" };
    private List<TimelineFragment> fragments;

    public TimelineFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.size() <= position) {
            fragments.add(TimelineFragment.newInstance(position + 1));
        }
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
