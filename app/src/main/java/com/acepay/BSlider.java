package com.acepay;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Adapter.TransformerAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;


public class BSlider extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

   private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bslider);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        checkPermission();

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
     /* file_maps.put("Hannibal",R.drawable.intro1);
        file_maps.put("Big Bang Theory",R.drawable.intro2);
        file_maps.put("House of Cards",R.drawable.intro3); */

        file_maps.put("Hannibal",R.drawable.s1);
        file_maps.put("Big Bang Theory",R.drawable.s2);
        file_maps.put("House of Cards",R.drawable.s3);

        for(String name : file_maps.keySet())
       {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView.description(name)
            .image(file_maps.get(name))
            .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);
        mDemoSlider.addOnPageChangeListener(this);
        ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
//                Toast.makeText(BSlider.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {

            int hasInternetPermission = checkSelfPermission(android.Manifest.permission.INTERNET);
            int hasNetworkPermission = checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
//            int hasFinePermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
//            int hasCoarsePermission = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasReadPermission = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasWritePermissioncamra = checkSelfPermission(android.Manifest.permission.CAMERA);
           // int hasReadPhonePermission = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
            //int hasReceiveSms = checkSelfPermission(android.Manifest.permission.RECEIVE_SMS);
            //int hasReadSms = checkSelfPermission(android.Manifest.permission.READ_SMS);
            //int hasSendSms = checkSelfPermission(android.Manifest.permission.SEND_SMS);
            int hasWifiState = checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE);
            //int hasReadContact = checkSelfPermission(android.Manifest.permission.READ_CONTACTS);
            int hasFingerPrint = checkSelfPermission(android.Manifest.permission.USE_FINGERPRINT);

            ArrayList<String> permissionList = new ArrayList<String>();

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.INTERNET);

            }/*if (hasReadPhonePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
            }*/
            if (hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
            }
//            if (hasFinePermission != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (hasCoarsePermission != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasWritePermissioncamra != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.CAMERA);
            }
            /*if (hasReceiveSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.RECEIVE_SMS);
            }
            if (hasReadSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_SMS);
            }
            if (hasSendSms != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.SEND_SMS);
            }*/
            if (hasWifiState != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.ACCESS_WIFI_STATE);
            }
           /* if (hasReadContact != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.READ_CONTACTS);
            }*/
            if (hasFingerPrint != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(android.Manifest.permission.USE_FINGERPRINT);
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


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
//     Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(this, AfterSplash.class);
       startActivity(intent);
    }

   /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

}
