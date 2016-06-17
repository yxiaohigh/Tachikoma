package com.h.tachikoma;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.h.tachikoma.base.BaseFragmentActivity;
import com.h.tachikoma.dummy.DummyContent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseFragmentActivity implements ItemFragment.OnListFragmentInteractionListener {


    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;

    private GoogleApiClient client;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {


    }

    @Override
    protected void initData() {
        HashMap<String, Fragment> map = new HashMap<>();
        ItemFragment itemFragment1 = ItemFragment.newInstance(1);
        ItemFragment itemFragment2 = ItemFragment.newInstance(1);
        ItemFragment itemFragment3 = ItemFragment.newInstance(1);
        map.put("第1页",itemFragment1);
        map.put("第2页",itemFragment2);
        map.put("第3页",itemFragment3);
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this.getSupportFragmentManager(), map);
  /*      List<String> titlelist = mainFragmentPagerAdapter.getTitlelist();
        for (String s : titlelist) {
            tab.addTab(tab.newTab().setText(s));
        }
        tab.addTab(tab.newTab().setText("dsfsdf"));*/
        vp.setAdapter(mainFragmentPagerAdapter);
        tab.setTabMode(TabLayout.MODE_FIXED);
        tab.setupWithViewPager(vp);
    }


    /**
     * fragment 列表点击的回调
     */
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        String id = item.id;
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
        Snackbar.make(vp, id, Snackbar.LENGTH_SHORT).show();
    }

}
