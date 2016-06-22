package com.h.tachikoma;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主页fragment adapter
 * Created by tony on 2016/6/17.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<String> titleList;
    private List<Fragment> fragmentlist;


    public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentlist, List<String> titlelist) {
        super(fm);
        this.fragmentlist = fragmentlist;
        this.titleList=titlelist;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
