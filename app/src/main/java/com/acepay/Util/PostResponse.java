package com.acepay.Util;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pro22 on 3/5/17.
 */

public class PostResponse
{
    @SerializedName("result")
    private JsonObject result;

    @SerializedName("message")
    private String message;

//    @SerializedName("data")
//    private JsonObject data;


    public JsonObject getResult() {
        return result;
    }

    public void setResult(JsonObject result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public JsonObject getData() {
//        return data;
//    }
//
//    public void setData(JsonObject data) {
//        this.data = data;
//    }
}
