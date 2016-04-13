package com.h.tachikoma;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    Student student = new Student();
        student.setName("name");
        student.setAddr("addr");
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setVariable(com.h.tachikoma.BR.stu, student);*/
        setContentView(R.layout.activity_main1);

        initNet();

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);


    }

    private void getpic(String url,ImageView view) {
        Glide.with(getApplicationContext())
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.error)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                //.override(200, 200)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
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
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.PATH)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<BasicData<AndroidData>> android = apiService.getAndroid(10, 1);
        final Call<BasicData<FuliData>> fuli = apiService.getFuli(10, 1);

        fuli.enqueue(new Callback<BasicData<FuliData>>() {
            @Override
            public void onResponse(Call<BasicData<FuliData>> call, Response<BasicData<FuliData>> response) {
                List<FuliData> results = response.body().getResults();
                rv.setAdapter(new ImageRecAdpter(MainActivity.this,results));
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

            ViewHolder holder = new ViewHolder(View.inflate(MainActivity.this, R.layout.item_imagelist, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ViewHolder holder1 = (ViewHolder) holder;
            ImageView imageView = holder1.imageView;
            FuliData t = data.get(position);
            String url = t.getUrl();
            getpic(url,imageView);

        }

        @Override
        public int getItemCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);

            }
        }

    }

}
