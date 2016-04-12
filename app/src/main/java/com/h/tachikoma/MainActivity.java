package com.h.tachikoma;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.h.tachikoma.entity.Student;
import com.h.tachikoma.net.ApiService;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
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
        Call<JSONObject> android = apiService.StringRepos("Android", 10, 1);
        try {
            Response<JSONObject> execute = android.execute();
            String message = execute.message();
            Log.i(TAG, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
