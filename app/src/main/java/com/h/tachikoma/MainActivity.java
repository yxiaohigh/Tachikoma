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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.h.tachikoma.entity.AndroidData;
import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.FuliData;
import com.h.tachikoma.net.ApiService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private Observer<List<FuliData>> observer;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    Student student = new Student();
        student.setName("name");
        student.setAddr("addr");
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setVariable(com.h.tachikoma.BR.stu, student);*/
        setContentView(R.layout.activity_main1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ttt");
        toolbar.inflateMenu(R.menu.main_activity_actions);
        observer = new Observer<List<FuliData>>() {

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

        };
        initNet();

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void getpic(String url, ImageView view, final int position) {

        Glide.with(this)
                .load(url)
                .asBitmap()
                //.fallback(R.drawable.bg_image)
                //.placeholder(R.drawable.bg_image)
                //.dontAnimate()
                //.centerCrop()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                //.error(R.drawable.bg_image)
                .override(100, 100)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "Exception>>" + position, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
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
        Call<BasicData<AndroidData>> android = apiService.getAndroid(10, 1);
        final Call<BasicData<FuliData>> fuli = apiService.getFuli(100, 1);

        Observable<BasicData<FuliData>> fuliOb = apiService.getFuliOb(100, 1);

        fuliOb
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BasicData<FuliData>, List<FuliData>>() {
                    @Override
                    public List<FuliData> call(BasicData<FuliData> fuliDataBasicData) {
                        return fuliDataBasicData.getResults();
                    }
                })
                .subscribe(observer);

        fuli.enqueue(new Callback<BasicData<FuliData>>() {
            @Override
            public void onResponse(Call<BasicData<FuliData>> call, Response<BasicData<FuliData>> response) {
              /*  List<FuliData> results = response.body().getResults();
                ImageRecAdpter imageRecAdpter = new ImageRecAdpter(MainActivity.this, results);
                imageRecAdpter.setHasStableIds(true);
                rv.setAdapter(imageRecAdpter);*/
            }

            @Override
            public void onFailure(Call<BasicData<FuliData>> call, Throwable t) {

            }
        });
    }


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

}
