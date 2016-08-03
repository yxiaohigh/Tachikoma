package com.h.tachikoma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.h.tachikoma.act.TestActivity;
import com.h.tachikoma.base.BaseActivity;
import com.h.tachikoma.dummy.DummyContent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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




        AsyncTask<Bitmap, String, Boolean> asyncTask = new AsyncTask<Bitmap, String, Boolean>() {

            private FileOutputStream fileOutputStream;

            @Override
            protected Boolean doInBackground(Bitmap... params) {

                try {
                    fileOutputStream = openFileOutput(getString(R.string.NIGHT_CACHE), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap param = params[0];
                boolean compress = param.compress(Bitmap.CompressFormat.PNG, 30, fileOutputStream);
                return compress;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(0, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                }
            }

        };
        View decorView = getWindow().getDecorView();

        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = getWindowManager().getDefaultDisplay();
        // 获取屏幕高
        int heigh = display.getHeight();
        int width = display.getWidth();

        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeights, width, heigh - statusBarHeights);

        asyncTask.execute(bitmap);
        decorView.destroyDrawingCache();


    }

}
