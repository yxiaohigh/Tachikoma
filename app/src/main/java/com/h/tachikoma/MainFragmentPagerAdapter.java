package com.h.tachikoma;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tony on 2016/6/17.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentlist;
    List<String> titlelist;


    /**
     * @param fm
     * @param map key: 为标题 ,map: 为fragment
     */
    public MainFragmentPagerAdapter(FragmentManager fm, HashMap<String, Fragment> map) {
        super(fm);
        fragmentlist = new ArrayList<>();
        titlelist = new ArrayList<>();
        Set<Map.Entry<String, Fragment>> entries = map.entrySet();
        for (Map.Entry<String, Fragment> entry : entries) {
            fragmentlist.add(entry.getValue());
            titlelist.add(entry.getKey());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    public String getTitle(int position) {
        return titlelist.get(position % titlelist.size());

    }
    public List<String> getTitlelist(){
        return titlelist;
    }
}
