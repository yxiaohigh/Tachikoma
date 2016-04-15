package com.h.tachikoma;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    private void getpic(String url, ImageView view, final int position) {

        Glide.with(this)
                .load(url)
                .asBitmap()
                .fitCenter()
                .override(300, 300)
                .listener(new StringBitmapRequestListener(position))
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
    class ImageRecAdpter extends RecyclerView.Adapter {

        private final Context context;
        private final List<FuliData> data;

        public ImageRecAdpter(Context context, List<FuliData> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(View.inflate(context, R.layout.item_imagelist, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ViewHolder holder1 = (ViewHolder) holder;
            ImageView imageView = holder1.imageView;
            TextView image_text = holder1.image_text;

            FuliData t = data.get(position);
            String url = t.getUrl();
            getpic(url, imageView, position);
            image_text.setText(position + 1 + "/" + data.size());

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
            ImageRecAdpter imageRecAdpter = new ImageRecAdpter(MainActivity.this, fuliDatas);
            imageRecAdpter.setHasStableIds(true);
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
}
