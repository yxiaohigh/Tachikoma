package com.h.tachikoma;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by tony on 2016/6/17.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentlist;


    /**
     * @param fm
     * @param list key: 为标题 ,map: 为fragment
     */
    public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragmentlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

}
