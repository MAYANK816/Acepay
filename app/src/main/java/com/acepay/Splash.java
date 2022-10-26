package com.acepay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Model.TermsCondition;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Splash extends AppCompatActivity implements
        BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener , RetrofitResponse {

    TabLayout tabLayout;
    AutoScrollViewPager viewPager_welcome_screen;
    Handler handler;
    Timer timer;
    private static int SPLASH_TIME_OUT = 4000;
    SliderLayout slider;
    TextView tvSkip;
    ArrayList<TermsCondition> arrayList;
    //ArrayList<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermission();
        callmathod();
        PreferenceFile.getInstance().saveData(this, Constant.ComeFrom,"0");
        PreferenceFile.getInstance().saveData(this,Constant.CheckApp,"0");
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {

            int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
            int hasNetworkPermission = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
            int hasFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasWritePermissioncamra = checkSelfPermission(Manifest.permission.CAMERA);
            int hasReadPhonePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int hasReceiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int hasReadSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int hasSendSms = checkSelfPermission(Manifest.permission.SEND_SMS);
            int hasWifiState = checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE);
            int hasReadContact = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int hasFingerPrint = checkSelfPermission(Manifest.permission.USE_FINGERPRINT);

            ArrayList<String> permissionList = new ArrayList<String>();

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.INTERNET);

            }if (hasReadPhonePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (hasFinePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (hasCoarsePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasWritePermissioncamra != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (hasReceiveSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECEIVE_SMS);
            }
            if (hasReadSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_SMS);
            }
            if (hasSendSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.SEND_SMS);
            }
            if (hasWifiState != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
            }
            if (hasReadContact != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_CONTACTS);
            }
            if (hasFingerPrint != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.USE_FINGERPRINT);
            }
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 8);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 2:

                for (int i = 0; i < permissions.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                    }
                }

                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this, Splash.this, Constant.REQ_BEGINNER_CONTENT,
                    Constant.BEGINNER_CONTENT).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }

    }

    public void callmathod() {

        callStateService();

        viewPager_welcome_screen=(AutoScrollViewPager) findViewById(R.id.viewpager_welcome_screen);
        tabLayout=(TabLayout)findViewById(R.id.tablayout_welcome_screen);
        arrayList=new ArrayList<>();

        viewPager_welcome_screen.startAutoScroll();
        viewPager_welcome_screen.setInterval(5000);
        tvSkip = (TextView) findViewById(R.id.tvSkip);

       /*images=new ArrayList<>();
        slider = (SliderLayout) findViewById(R.id.slider);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        images.add(R.drawable.first);
        images.add(R.drawable.second);
        images.add(R.drawable.third);
        images.add(R.drawable.fouth);*/

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PreferenceFile.getInstance().getPreferenceData(Splash.this,Constant.ID)!=null){
                    finish();
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                }
                else
                {
                    finish();
                    Intent intent = new Intent(Splash.this, AfterSplash.class);
                    startActivity(intent);
                }
            }
        });

        /*for (int i = 0; i < images.size(); i++)
        {
            MyDefaultSliderView textSliderView = new MyDefaultSliderView(this);
            textSliderView
                    .image(images.get(i))
                    .setScaleType(MyDefaultSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.addSlider(textSliderView);
        }
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        slider.setDuration(3000);
        slider.addOnPageChangeListener(this);*/
    }

    @Override
    public void onResume() {

        super.onResume();


    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_BEGINNER_CONTENT:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    Log.e("response-->", result1.toString());
                    Log.e("status-->", status + " message-->" + message);

                    if (status.equals("true")) {

                        arrayList.clear();

                        JSONArray data = result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);
                            TermsCondition country = new TermsCondition();
                            country.setTerm_one(CountryObj.getString("title"));
                            country.setTerm_two(CountryObj.getString("description"));
                            arrayList.add(country);
                        }

                        viewPager_welcome_screen.setAdapter(new New_Slider(this, arrayList));
                        tabLayout.setupWithViewPager(viewPager_welcome_screen);

                    } else {

                        Constant.alertDialog(this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
