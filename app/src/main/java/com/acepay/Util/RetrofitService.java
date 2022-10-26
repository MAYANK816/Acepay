package com.acepay.Util;

import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by pro55 on 3/5/17.
 */

public interface RetrofitService {

    @GET
    Call<ResponseBody> download(@Url String fileUrl);

    @POST
    Call<ResponseBody> callPostService(@Url String url, @Body RequestBody params);


    @POST
    Call<ResponseBody> postRawJSON(@Url String url,@Body JsonObject jsonObject);

    @GET
    Call<ResponseBody> callGetService(@Url String url);

    @Multipart
    @POST
    Call<ResponseBody> callPostWithMap(@Url String url, @PartMap HashMap<String,RequestBody> map);

    @Multipart
    @POST
    Call<ResponseBody> callMultipartService(@Url String url, @PartMap HashMap<String, RequestBody> map, @Part MultipartBody.Part part);

    @Multipart
    @POST
    Call<ResponseBody> callPostTwoImage(@Url String url, @PartMap HashMap<String,RequestBody> map,@Part MultipartBody.Part part,@Part MultipartBody.Part part2);

}

