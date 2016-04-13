package com.h.tachikoma;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
    private ImageView image_main;
    private Button bt;
    private String url;
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
        image_main = (ImageView) findViewById(R.id.image_main);
        rv = (RecyclerView) findViewById(R.id.rv);
        bt = (Button) findViewById(R.id.bt);


        rv.setAdapter(new ImageRecAdpter());

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNet();
            }
        });

    }

    private void getpic() {
        Glide.with(MainActivity.this)
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                //.centerCrop()
                //.error(R.drawable.error)
                //.override(100, 100)
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
                .into(new BitmapImageViewTarget(image_main) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    /*    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        image_main.setImageDrawable(roundedBitmapDrawable);*/
                    }
                });
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
        android.enqueue(new Callback<BasicData<AndroidData>>() {
            @Override
            public void onResponse(Call<BasicData<AndroidData>> call, Response<BasicData<AndroidData>> response) {
                BasicData body = response.body();
                body.isError();
                List<AndroidData> results = body.getResults();
                for (AndroidData result : results) {
                    String s = result.toString();
                }
            }

            @Override
            public void onFailure(Call<BasicData<AndroidData>> call, Throwable t) {

            }
        });
        fuli.enqueue(new Callback<BasicData<FuliData>>() {
            @Override
            public void onResponse(Call<BasicData<FuliData>> call, Response<BasicData<FuliData>> response) {
                List<FuliData> results = response.body().getResults();
                FuliData fuliData1 = results.get(2);
                url = fuliData1.getUrl();
                getpic();
            }

            @Override
            public void onFailure(Call<BasicData<FuliData>> call, Throwable t) {

            }
        });
    }

    class ImageRecAdpter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
