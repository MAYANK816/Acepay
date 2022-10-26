package com.acepay.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
//import com.crashlytics.android.Crashlytics;
//import com.facebook.FacebookSdk;

import java.io.File;

//import io.fabric.sdk.android.Fabric;
import java.util.AbstractMap;


public class App extends Application
{
    public static String ENQUIRIES = null, QUOTATIONS = null;
    public static App mInstance;
    public static Context mcontext;
    public static String latitude = "", longitude = "";
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate()
    {
        super.onCreate();
//        Fabric.with(this, new Crashlytics());
        mInstance = this;
        mcontext = getApplicationContext();




//        FacebookSdk.sdkInitialize(getApplicationContext());

//        Fabric.with(this, new Crashlytics());
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Fonts/DroidSans.ttf");
//
//        ZendeskConfig.INSTANCE.init(this, "https://metapay.zendesk.com",
//                "7b5b2197c0b75ab55e87b61e60b8fbf8e1ad2f48302c8bab", "mobile_sdk_client_3f693ea4959e1206d347");
//       // Identity identity = new AnonymousIdentity.Builder().build();
//
//        Identity identity;
//        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)!=null) {
//             identity = new AnonymousIdentity.Builder()
//                    .withNameIdentifier(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username))
//                    .withEmailIdentifier(PreferenceFile.getInstance().getPreferenceData(this,Constant.Email))
//                    .build();
//        }else
//        {
//             identity = new AnonymousIdentity.Builder().build();
//        }
//        ZendeskConfig.INSTANCE.setIdentity(identity);

        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + "TigerPay");
            if (!directory.exists())
                directory.mkdirs();
        }
        else {
            directory = getApplicationContext().getDir("TigerPay", Context.MODE_PRIVATE);
            if (!directory.exists())
                directory.mkdirs();
        }

        if (directory != null) {
            File books = new File(directory + File.separator + "Quotations");
            File video = new File(directory + File.separator + "Enquiries");

            if (!books.exists())
                books.mkdirs();

            if (!video.exists())
                video.mkdirs();

            QUOTATIONS = directory + File.separator + "Quotations";
            ENQUIRIES = directory + File.separator + "Enquiries";
        }
    }

    public static SharedPreferences getGlobalPefs()
    {
        return getContext().getSharedPreferences("MetaAPay", 0);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mcontext;
    }

    public static SharedPreferences getIdPref() {

         return getContext().getSharedPreferences("MetaAPay", 0);
    }

    public static String getQUOTATIONS() {
        return QUOTATIONS;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}

