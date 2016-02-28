package com.codepath.apps.squawker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.codepath.apps.squawker.Fragments.HomeTimelineFragment;
import com.codepath.apps.squawker.Fragments.MentionsTimelineFragment;

/**
 * Created by kpu on 2/20/16.
 */
public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "HOME", "MENTIONS" };
    private SparseArray<Fragment> mFragments = new SparseArray<>();

    public TimelineFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment item = mFragments.get(position);

        if (item == null) {
            if (position == 0) {
                item = new HomeTimelineFragment();
            } else if (position == 1) {
                item =  new MentionsTimelineFragment();
            }
            mFragments.put(position, item);
        }

        return item;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
