package com.health.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CircularFragmentPagerAdapter extends FragmentPagerAdapter {
    /**
     * 首末位扩展后的fragments
     */
    private List<Fragment> broadenFragments;

    /**
     * @param fragments 扩展后的fragements
     */
    public CircularFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        initDatas(fragments);
    }

    private void initDatas(List<Fragment> fragments) {
        if (fragments == null) {
            broadenFragments = new ArrayList<Fragment>();
        } else {
            broadenFragments = fragments;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return broadenFragments.get(position);
    }

    @Override
    public int getCount() {
        return broadenFragments.size();
    }

}
