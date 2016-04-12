package com.h.tachikoma;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.h.tachikoma.entity.Student;
import com.h.tachikoma.net.ApiService;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<JSONArray> android1 = apiService.getJson("Android", 10, 1);
        android1.enqueue(new Callback<JSONArray>() {

            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {

                try {
                    Object o = response.body().get(1);
                    Log.i(TAG, "onResponse: "+o.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {

            }
        });


    }

}
