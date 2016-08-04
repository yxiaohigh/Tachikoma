package com.h.tachikoma;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.h.tachikoma.base.BaseActivity;
import com.h.tachikoma.dummy.DummyContent;
import com.h.tachikoma.utli.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ItemFragment.OnListFragmentInteractionListener {


    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp)
    ViewPager vp;


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
        List<Fragment> fragmentlist = new ArrayList<>();
        List<String> titlelist = new ArrayList<>();
        ItemFragment itemFragment1 = ItemFragment.newInstance(1);
        ItemFragment itemFragment2 = ItemFragment.newInstance(1);
        ItemFragment itemFragment3 = ItemFragment.newInstance(1);
        fragmentlist.add(itemFragment1);
        fragmentlist.add(itemFragment2);
        fragmentlist.add(itemFragment3);
        titlelist.add("第一页");
        titlelist.add("第二页");
        titlelist.add("第三页");
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentlist, titlelist);
        vp.setAdapter(mainFragmentPagerAdapter);
        tab.setupWithViewPager(vp);
        tab.setTabMode(TabLayout.MODE_FIXED);
    }


    /**
     * fragment 列表点击的回调
     */
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        String id = item.id;
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
        Snackbar.make(vp, id, Snackbar.LENGTH_SHORT).show();
        //白天黑夜模式转换
        CommonUtil.SwNighAndDay(this);
    }



}
