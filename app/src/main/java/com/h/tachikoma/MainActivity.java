package com.h.tachikoma;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.FuliData;
import com.h.tachikoma.net.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rv;
    private ListPreloader listPreloader;
    private ViewPager vp;
    private List<View> vlist = new ArrayList<>();//viewpager的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        initView();

        initNet();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        vp = (ViewPager) findViewById(R.id.vp);
        LayoutInflater inflater = LayoutInflater.from(this);
        View item1 = inflater.inflate(R.layout.item_imagelist, null);
        View item2 = inflater.inflate(R.layout.item_imagelist, null);
        View item3 = inflater.inflate(R.layout.item_imagelist, null);

        vlist.add(item1);
        vlist.add(item2);
        vlist.add(item3);

        toolbar.inflateMenu(R.menu.main_activity_actions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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
     */
    private void getPic(String url, ImageView view, final int position) {

        Glide.with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .listener(new StringBitmapRequestListener(position))
                .override(100, 100)
                //.fallback(R.drawable.bg_image)
                //.placeholder(R.drawable.bg_image)
                //.dontAnimate()
                //.centerCrop()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.error(R.drawable.bg_image)
                .into(view);

    }


    private void getBigPic(String url, ImageView view, final int position) {

        Glide.with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .listener(new StringBitmapRequestListener(position))
                // .override(100, 100)
                //.fallback(R.drawable.bg_image)
                //.placeholder(R.drawable.bg_image)
                //.dontAnimate()
                //.centerCrop()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.error(R.drawable.bg_image)
                .into(view);

    }

    private void initNet() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


        ApiService apiService = retrofit.create(ApiService.class);
        Observable<BasicData<FuliData>> fuliOb = apiService.getFuliOb(100, 1);

        fuliOb.subscribeOn(Schedulers.io())
                .map(new BasicDataListFunc1())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ListObserver());


    }

    /**
     * fuli接口的 Rxjava map数据转换
     */
    private static class BasicDataListFunc1 implements Func1<BasicData<FuliData>, List<FuliData>> {
        @Override
        public List<FuliData> call(BasicData<FuliData> fuliDataBasicData) {
            return fuliDataBasicData.getResults();
        }
    }

    /**
     * recAdpter
     */
    class ImageRecAdpter extends RecyclerView.Adapter implements View.OnClickListener {

        private final Context context;
        private final List<FuliData> data;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public ImageRecAdpter(Context context, List<FuliData> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_imagelist, null);
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

            FuliData t = data.get(position);
            String url = t.getUrl();
            getPic(url, imageView, position);
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
                mOnItemClickListener.onItemClick(v,  (int)v.getTag());
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
     * 网络请求数据的回调
     */
    private class ListObserver implements Observer<List<FuliData>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<FuliData> fuliDatas) {
            vp.setAdapter(new MyPagerAdapter(fuliDatas));

            ImageRecAdpter imageRecAdpter = new ImageRecAdpter(MainActivity.this, fuliDatas);
            imageRecAdpter.setHasStableIds(true);
            imageRecAdpter.setOnItemClickListener(imageRecAdpter.new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getApplication(), position+"", Toast.LENGTH_SHORT).show();
                    vp.setCurrentItem(position);
                }
            });
            rv.setAdapter(imageRecAdpter);
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
            Toast.makeText(MainActivity.this, "Exception>>" + position, Toast.LENGTH_SHORT).show();
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
        private List<FuliData> fuliDatas;

        public MyPagerAdapter(List<FuliData> fuliDatas) {
            this.fuliDatas = fuliDatas;
        }

        @Override
        public int getCount() {
            return fuliDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
          /*  View view = vlist.get(position % 3);
            ImageView image = (ImageView) view.findViewById(R.id.image);*/

            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.item_imagelist, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            FuliData t = fuliDatas.get(position);
            String url = t.getUrl();
            getBigPic(url, image, position);

            TextView image_text = (TextView) view.findViewById(R.id.image_text);
            image_text.setText(position + 1 + "");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //View view = vlist.get(position % 3);
            container.removeView((View) object);
        }
    }
}
