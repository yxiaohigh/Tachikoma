package com.h.tachikoma.net;

import com.h.tachikoma.entity.AndroidData;

import org.json.JSONObject;

import java.util.List;

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
    List<AndroidData> listRepos(@Path("type") String user,@Path("amount") int amount,@Path("page") int page);
    @GET("{type}/{amount}/{page}")
    Call<JSONObject> StringRepos(@Path("type") String user, @Path("amount") int amount, @Path("page") int page);
}
