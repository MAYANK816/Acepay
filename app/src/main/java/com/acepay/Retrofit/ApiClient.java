package com.acepay.Retrofit;

import com.acepay.Util.Retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pro22 on 10/11/17.
 */

public class ApiClient {

    public static Retrofit retrofit=null;

    public static Retrofit getClient()
    {
        if (retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(" https://ifsc.razorpay.com/").
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}