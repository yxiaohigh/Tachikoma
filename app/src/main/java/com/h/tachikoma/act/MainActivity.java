package com.h.tachikoma.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;

import com.h.tachikoma.ItemFragment;
import com.h.tachikoma.MainFragmentPagerAdapter;
import com.h.tachikoma.R;
import com.h.tachikoma.base.App;
import com.h.tachikoma.base.BaseActivity;
import com.h.tachikoma.dummy.DummyContent;
import com.h.tachikoma.utli.PicUtil;

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


    }


    @Override
    protected void initViews() {
        ButterKnife.bind(this);
       
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
        Snackbar.make(vp, id, Snackbar.LENGTH_SHORT).show();
        //白天黑夜模式转换
        SwNighAndDay(this);
    }


    /**
     * 切换白天黑夜模式
     *
     * @param activity 得到重启activity截图 保存截图
     *                 判断模式 切换模式
     *                 跳转SwNightActivity
     *                 取出activity截图显示
     *                 动画渐渐隐藏 关闭SwNightActivity (为了重启activity时不闪屏)
     *                 重启activity
     */
    public static void SwNighAndDay(MainActivity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Bitmap bitmap = PicUtil.getScreenBitmap(activity);
            App.getApplication().putAppArrayMap(activity.getString(R.string.night_cache), bitmap);

            int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
            switch (defaultNightMode) {
                case AppCompatDelegate.MODE_NIGHT_NO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case AppCompatDelegate.MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            }

            Intent intent = new Intent(activity, SwNightActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);


            activity.recreate();
        }
    }

}
