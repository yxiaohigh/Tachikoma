package com.h.tachikoma.net;

import com.h.tachikoma.entity.BasicData;
import com.h.tachikoma.entity.ItemData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tony on 2016/4/12.
 * 请求接口
 * http://gank.io/api/data/Android/10/1
 */


public interface NetApi {
    String PATH="http://gank.io/api/data/";

    @GET("{type}/{amount}/{page}")
    Call<ResponseBody> getRepos(@Path("type") String user, @Path("amount") int amount, @Path("page") int page);

    @GET("福利/{amount}/{page}")
    Observable<BasicData<ItemData>> getFuliOb(@Path("amount") int amount, @Path("page") int page);

    @GET("all/{amount}/{page}")
    Observable<BasicData<ItemData>> getAllOb(@Path("amount") int amount, @Path("page") int page);

    @GET("Android/{amount}/{page}")
    Observable<BasicData<ItemData>> getAndroidOb(@Path("amount") int amount, @Path("page") int page);

    @GET("ios/{amount}/{page}")
    Observable<BasicData<ItemData>> getIosOb(@Path("amount") int amount, @Path("page") int page);

}
