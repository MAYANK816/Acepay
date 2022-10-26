package com.acepay.Retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pro22 on 10/11/17.
 */

public interface Endpoints {
    @GET("api/vendor/all_feature_list.json")//define path of the URL
    Call<RequestBody> getProduct();//call the query for response
}

