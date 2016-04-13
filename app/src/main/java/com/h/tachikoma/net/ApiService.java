package com.h.tachikoma.net;

import com.h.tachikoma.entity.AndroidData;
import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.Fuli;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tony on 2016/4/12.
 * 请求接口
 * http://gank.io/api/data/Android/10/1
 */


public interface ApiService {
    String PATH="http://gank.io/api/data/";

    @GET("{type}/{amount}/{page}")
    Call<ResponseBody> getRepos(@Path("type") String user, @Path("amount") int amount, @Path("page") int page);

    @GET("Android/{amount}/{page}")
    Call<BasicData<AndroidData>> getAndroid( @Path("amount") int amount, @Path("page") int page);

    @GET("Fuli/{amount}/{page}")
    Call<BasicData<Fuli>> getFuli(@Path("amount") int amount, @Path("page") int page);

}
