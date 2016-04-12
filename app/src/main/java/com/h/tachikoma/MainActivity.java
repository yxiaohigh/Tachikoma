package com.h.tachikoma;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.h.tachikoma.entity.Student;
import com.h.tachikoma.net.ApiService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Student student = new Student();
        student.setName("name");
        student.setAddr("addr");
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setVariable(com.h.tachikoma.BR.stu, student);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.PATH)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> android = apiService.getRepos("Android", 10, 1);
        android.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                try {
                    String string = body.string();
                    Log.i(TAG, "sssssssssssssssssonResponse: "+string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = response.toString();
                Log.i(TAG, "sssssssssssssssssonResponse: "+body);
                Log.i(TAG, "sssssssssssssssssonResponse: "+s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
