package com.h.tachikoma.act;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h.tachikoma.R;
import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.ItemData;
import com.h.tachikoma.net.ApiService;
import com.h.tachikoma.net.NetClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {


    @BindView(R.id.f_bt)
    FloatingActionButton fBt;
    @BindView(R.id.tb)
    Toolbar tb;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initViews() {

        //隐藏状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int statusBarHeight = getStatusBarHeight(this);
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tb.getLayoutParams();
//            params.topMargin = statusBarHeight;
//            tb.setLayoutParams(params);
        }
        tb.setTitle("ssssssssssss");

        LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layout);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

        rvList.setAdapter(new MyAdapter(null));


        ApiService apiService = NetClient.getApiService();
        Observable<BasicData<ItemData>> allOb = apiService.getAllOb(10, 5);
        allOb.subscribeOn(Schedulers.io())
                .map(new Func1<BasicData<ItemData>, List<ItemData>>() {
                    @Override
                    public List<ItemData> call(BasicData<ItemData> itemDataBasicData) {
                        return itemDataBasicData.getResults();
                    }
                })
                .subscribe(new Observer<List<ItemData>>() {


                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final List<ItemData> itemDatas) {

                    }
                });

    }


    @OnClick(R.id.f_bt)
    public void onClick() {
        Snackbar.make(fBt, "snackbar", Snackbar.LENGTH_SHORT).show();
    }



    private class ViewHolder extends RecyclerView.ViewHolder{

       TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {
        List<ItemData> itemDatas;

        public MyAdapter(List<ItemData> itemDatas) {
            this.itemDatas = itemDatas;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.main_item, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
           // ItemData itemData = itemDatas.get(position);
            //String desc = itemData.getDesc();
            ((ViewHolder) holder).text.setText("ssssssss"+position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}

