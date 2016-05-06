package com.h.tachikoma.act;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.h.tachikoma.R;
import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.ItemData;
import com.h.tachikoma.net.ApiService;
import com.h.tachikoma.net.NetClient;
import com.h.tachikoma.utli.DataUtil;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class GalleryActivity extends AppCompatActivity {


    private static final String DATA_FILE_NAME = "data_file_name";
    private static final String TAG = "GalleryActivity";
    private int state1;
    private RecyclerView rv;
    private ViewPager vp;
    File dataFile;
    private BehaviorSubject<List<ItemData>> cache;
    Gson gson = new Gson();
    private long startTime;
    private int stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        dataFile = new File(getApplication().getFilesDir(), DATA_FILE_NAME);
        initView();
        initPic();
        getNetDate();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        vp = (ViewPager) findViewById(R.id.vp);

        toolbar.inflateMenu(R.menu.main_activity_actions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);


        //小图监听
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                int top = recyclerView.getTop();
                if (state1 == 0) {
                    View childAt = recyclerView.getChildAt(0);
                    int position = (int) childAt.getTag();
                    vp.setCurrentItem(position);
                }

            }
        });

        //大图滑动监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int childCount = rv.getChildCount();
                if (rv.getScrollState() == 0) {//小图自身在滚动时不改变其位置
                    rv.smoothScrollToPosition(position + childCount - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                state1 = state;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param view
     * @param position
     * @param b        设置 true 为加载小图
     */
    private void getPic(String url, ImageView view, final int position, boolean b) {

        DrawableRequestBuilder<String> stringDrawableTypeRequest = Glide
                .with(this)
                .load(url)
                .crossFade();
        //.asBitmap();

        if (b) {
            stringDrawableTypeRequest.override(100, 100);
        } else {
            stringDrawableTypeRequest.placeholder(R.drawable.bg_image);
        }
        stringDrawableTypeRequest
                .centerCrop()
                // .listener(new StringBitmapRequestListener(position))
                //.fallback(R.drawable.bg_image)
                //.dontAnimate()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.error(R.drawable.bg_image)
                .into(view);

    }


    /**
     * 请求网络数据
     */
    private void getNetDate() {
        ApiService apiService = NetClient.getApiService();
        Observable<BasicData<ItemData>> fuliOb = apiService.getFuliOb(100, 1);
        fuliOb.subscribeOn(Schedulers.io())
                .map(new BasicDataListFunc1())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<ItemData>>() {
                    @Override
                    public void call(List<ItemData> itemDatas) {

                        if (isSaveCache(itemDatas)) {//判断数据是否变化
                            String string = gson.toJson(itemDatas);
                            DataUtil.writeData(dataFile, string);//写入硬盘
                            stat = 3;
                        } else {
                            stat = 4;
                        }
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(cache);//写入缓存
    }


    /**
     * 比较网络请求的数据是否有更新
     *
     * @param itemDatas
     * @return
     */
    public boolean isSaveCache(List<ItemData> itemDatas) {
        List<ItemData> itemDatasBak = DataUtil.ReadrFuliDatas(dataFile, gson);
        if (itemDatasBak != null) {
            ItemData itemData = itemDatas.get(0);
            ItemData itemData1 = itemDatasBak.get(0);
            if (itemData.get_id().equals(itemData1.get_id())) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 进入页面从缓存加载数据
     */
    private void initPic() {
        startTime = System.currentTimeMillis();
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<List<ItemData>>() {
                @Override
                public void call(Subscriber<? super List<ItemData>> subscriber) {
                    List<ItemData> itemDatas1 = DataUtil.ReadrFuliDatas(dataFile, gson);
                    if (itemDatas1 != null) {
                        subscriber.onNext(itemDatas1);//从硬盘写入
                        stat = 1;
                    }
                }
            }).subscribe(cache);
        } else {
            stat = 2;
        }

        cache.observeOn(AndroidSchedulers.mainThread()).subscribe(new DataObserver());
    }


    /**
     * fuli接口的 Rxjava map数据转换
     */
    private static class BasicDataListFunc1 implements Func1<BasicData<ItemData>, List<ItemData>> {
        @Override
        public List<ItemData> call(BasicData<ItemData> fuliDataBasicData) {
            return fuliDataBasicData.getResults();
        }
    }

    /**
     * recAdpter
     */
    class ImageRecAdpter extends RecyclerView.Adapter implements View.OnClickListener {

        private final Context context;
        private final List<ItemData> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public ImageRecAdpter(Context context, List<ItemData> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.gallery_item, null);
            ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ViewHolder holder1 = (ViewHolder) holder;
            holder.itemView.setTag(position);
            ImageView imageView = holder1.imageView;
            TextView image_text = holder1.image_text;

            ItemData t = data.get(position);
            String url = t.getUrl();
            getPic(url, imageView, position, true);
            image_text.setText(position + 1 + "");

        }

        @Override
        public int getItemCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView image_text;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                image_text = (TextView) itemView.findViewById(R.id.image_text);

            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (int) v.getTag());
            }
        }

        public abstract class OnRecyclerViewItemClickListener {
            public abstract void onItemClick(View view, int position);
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

    }


    /**
     * 图片下载的回调
     */
    private class StringBitmapRequestListener implements RequestListener<String, Bitmap> {
        private final int position;

        public StringBitmapRequestListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            Toast.makeText(GalleryActivity.this, "Exception>>" + position, Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    }

    /**
     * PagerAdapter
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<ItemData> itemDatas;

        public MyPagerAdapter(List<ItemData> itemDatas) {
            this.itemDatas = itemDatas;
        }

        @Override
        public int getCount() {
            return itemDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = LayoutInflater.from(GalleryActivity.this);
            View view = inflater.inflate(R.layout.gallery_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            ItemData t = itemDatas.get(position);
            String url = t.getUrl();
            getPic(url, image, position, false);

            TextView image_text = (TextView) view.findViewById(R.id.image_text);
            image_text.setText(position + 1 + "");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 网络请求的数据回调
     */
    private class DataObserver implements Observer<List<ItemData>> {



        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<ItemData> itemDatas) {
            long endTime = System.currentTimeMillis();
                vp.setAdapter(new MyPagerAdapter(itemDatas));
                ImageRecAdpter imageRecAdpter = new ImageRecAdpter(GalleryActivity.this, itemDatas);
                imageRecAdpter.setHasStableIds(true);
                imageRecAdpter.setOnItemClickListener(imageRecAdpter.new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getApplication(), position + "点击", Toast.LENGTH_SHORT).show();
                        vp.setCurrentItem(position);
                    }
                });
                rv.setAdapter(imageRecAdpter);

            showTimeToast(endTime);


        }

    }

    /**
     * @param endTime
     */
    private void showTimeToast(long endTime) {
        String str="";
        switch (stat) {
            case 1:
                str ="从硬盘缓存写入";
                break;
            case 2:
                str ="从内存缓存写入";
                break;
            case 3:
                str ="有新的内容从网络写入";
                break;
            case 4:
                str ="从网络更新";
                break;
        }
        long l = endTime - startTime;
        Toast.makeText(GalleryActivity.this, str+"耗费时间"+l , Toast.LENGTH_SHORT).show();
    }
}
