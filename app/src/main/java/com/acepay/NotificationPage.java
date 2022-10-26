package com.acepay;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.NewAapterScreen;
import com.acepay.Adapter.NotificationAdapter;
import com.acepay.Model.AdvertismentImages;
import com.acepay.Model.NotificationModel;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class NotificationPage extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

    RecyclerView recyclerView;
    ImageView ivarrow,imNoti,imAlert,imMessage;
    TextView txName,tvdescription,txtext,tvText,tvwallet,txBitcoin,tvSellrate,tvINR,tvBitcoin,currentsymbol,txMessage;
    Toolbar toolbar;
    TabLayout tabLayout;
    String key,amount="";
    AutoScrollViewPager viewPager_welcome_screen;
    NewAapterScreen newAapterScreen;
    Double finacals,bit;
    LinearLayout lnNotification,lnAlerts,lnMessage,lnLeaner;
    TextView txNotification,txAlert;
    NotificationAdapter adapter;
    int flag=1;
    Handler handler;
    Timer timer;
    boolean doubleBackToExitPressedOnce = false;
    DecimalFormat formatter;
    Typeface tfArchitectsDaughter;
    ArrayList<NotificationModel> AllUserModel;
    ArrayList<NotificationModel> notiCount=new ArrayList<>();
    ArrayList<NotificationModel> alertCount=new ArrayList<>();
    ArrayList<NotificationModel> offerCount=new ArrayList<>();
    ArrayList<AdvertismentImages> advertismentImages;
    RelativeLayout rlCountt,rlCounttAlert,rlCounttOffers;
    TextView tvCountOffers,tvCountAlert,tvCount;

    String value="0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

        AllUserModel=new ArrayList<>();
        advertismentImages=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        txtext = (TextView) findViewById(R.id.txtext);
        txNotification = (TextView) findViewById(R.id.txNotification);
        tvdescription = (TextView) findViewById(R.id.tvdescription);
        imNoti = (ImageView) findViewById(R.id.imNoti);
        txAlert = (TextView) findViewById(R.id.txAlert);


        tvCountOffers = (TextView) findViewById(R.id.tvCountOffers);
        tvCountAlert = (TextView) findViewById(R.id.tvCountAlert);
        tvCount = (TextView) findViewById(R.id.tvCount);


        rlCountt = (RelativeLayout) findViewById(R.id.rlCountt);
        rlCounttAlert = (RelativeLayout) findViewById(R.id.rlCounttAlert);
        rlCounttOffers = (RelativeLayout) findViewById(R.id.rlCounttOffers);

        txMessage = (TextView) findViewById(R.id.txMessage);
        imMessage = (ImageView) findViewById(R.id.imMessage);
        imAlert = (ImageView) findViewById(R.id.imAlert);
        currentsymbol = (TextView) findViewById(R.id.currentsymbol);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        tvINR = (TextView) findViewById(R.id.tvINR);
        txBitcoin = (TextView) findViewById(R.id.txBitcoin);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
        lnNotification = (LinearLayout) findViewById(R.id.lnNotification);
        lnAlerts = (LinearLayout) findViewById(R.id.lnAlerts);
        lnMessage = (LinearLayout) findViewById(R.id.lnMessage);
        lnLeaner = (LinearLayout) findViewById(R.id.lnLeaner);
        formatter = new DecimalFormat("#,##,###.00");

        callservice();

        viewPager_welcome_screen=(AutoScrollViewPager) findViewById(R.id.viewpager_welcome_screen);
        tabLayout=(TabLayout) findViewById(R.id.tablayout_welcome_screen);

        viewPager_welcome_screen.startAutoScroll();
        viewPager_welcome_screen.setInterval(5000);
//
        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        txName= (TextView) findViewById(R.id.txName);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setVisibility(View.GONE);

        lnMessage.setOnClickListener(this);
        lnAlerts.setOnClickListener(this);
        lnNotification.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Notification");

        if(getIntent().hasExtra("type")){

            Log.e("data-->",getIntent().getStringExtra("type"));

            if(getIntent().getStringExtra("type").equalsIgnoreCase("Alert")){

                lnLeaner.setVisibility(View.GONE);

                flag=2;
                Drawable bell = getResources().getDrawable(R.drawable.bell);

                imNoti.setBackground(bell);

                Drawable neds = getResources().getDrawable(R.drawable.alert);

                imAlert.setBackground(neds);

                Drawable dfd = getResources().getDrawable(R.drawable.update);

                imMessage.setBackground(dfd);

                callservice();

                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.gradient_1));
                txNotification.setTextColor(getResources().getColor(R.color.blue));

                txAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));


            }
            else if(getIntent().getStringExtra("type").equalsIgnoreCase("Notification")){
                lnLeaner.setVisibility(View.GONE);
                flag=1;

                Drawable msg = getResources().getDrawable(R.drawable.bell);

                imNoti.setBackground(msg);
                callservice();

                Drawable tv = getResources().getDrawable(R.drawable.alert);

                imAlert.setBackground(tv);

                Drawable mes = getResources().getDrawable(R.drawable.update);

                imMessage.setBackground(mes);

                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.gradient_1));

                txAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));


            }
            else {

                lnLeaner.setVisibility(View.GONE);

                flag=3;

                Drawable dn = getResources().getDrawable(R.drawable.bell);

                imNoti.setBackground(dn);

                Drawable alr = getResources().getDrawable(R.drawable.alert);

                imAlert.setBackground(alr);

                Drawable ffer = getResources().getDrawable(R.drawable.update);

                imMessage.setBackground(ffer);

                txMessage.setTextColor(getResources().getColor(R.color.gradient_1));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.blue));


                txAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                callservice();
            }
        }
        oninit();
    }

    private void oninit() {
        lnLeaner.setVisibility(View.GONE);
        flag=1;

        Drawable msg = getResources().getDrawable(R.drawable.bell);
//                int imf = Color.argb( 255,208,120,228);
//                msg.setColorFilter(imf, PorterDuff.Mode.MULTIPLY);
        imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
        imNoti.setBackground(msg);

        value="1";
        callservice();

        Drawable tv = getResources().getDrawable(R.drawable.alert);
        imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

        imAlert.setBackground(tv);

        Drawable mes = getResources().getDrawable(R.drawable.update);
        imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

        imMessage.setBackground(mes);

        txMessage.setTextColor(getResources().getColor(R.color.blue));
        txAlert.setTextColor(getResources().getColor(R.color.blue));
        txNotification.setTextColor(getResources().getColor(R.color.gradient_1));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // callservice();
    }

    private void callService(){



        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, NotificationPage.this, Constant.REQ_ADVERTISEMAENT, Constant.ADVERTISEMAENT).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    public void callservice() {

        if (Constant.isConnectingToInternet(this)) {
            //  new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_AddedAddresslist,Constant.AddedAddresslist+"56").callService(true);
            new Retrofit2(this, NotificationPage.this, Constant.REQ_USER_NOTIFICATION,
                    Constant.USER_NOTIFICATION+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.lnNotification:
                lnLeaner.setVisibility(View.GONE);
                flag=1;

                Drawable msg = getResources().getDrawable(R.drawable.bell);
//                int imf = Color.argb( 255,208,120,228);
//                msg.setColorFilter(imf, PorterDuff.Mode.MULTIPLY);
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
                imNoti.setBackground(msg);

                value="1";
                callservice();

                Drawable tv = getResources().getDrawable(R.drawable.alert);
                 imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                imAlert.setBackground(tv);

                Drawable mes = getResources().getDrawable(R.drawable.update);
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                imMessage.setBackground(mes);

                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.gradient_1));


                break;

            case R.id.lnMessage:

                lnLeaner.setVisibility(View.GONE);

                flag=3;

                Drawable dn = getResources().getDrawable(R.drawable.bell);
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                imNoti.setBackground(dn);

                Drawable alr = getResources().getDrawable(R.drawable.alert);
                imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                imAlert.setBackground(alr);


                Drawable ffer = getResources().getDrawable(R.drawable.update);
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

                imMessage.setBackground(ffer);


                txMessage.setTextColor(getResources().getColor(R.color.gradient_1));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.blue));

                value="1";
                callservice();

                break;

            case R.id.lnAlerts:

                lnLeaner.setVisibility(View.GONE);

                flag=2;
                Drawable bell = getResources().getDrawable(R.drawable.bell);
//                int inb = Color.argb( 255,208,120,228);
//                bell.setColorFilter(inb, PorterDuff.Mode.MULTIPLY);
                imNoti.setBackground(bell);
                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                Drawable neds = getResources().getDrawable(R.drawable.alert);
                imAlert.setBackground(neds);
                imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

                Drawable dfd = getResources().getDrawable(R.drawable.update);
//                int ds = Color.argb( 255,208,120,228);
//                dfd.setColorFilter(ds, PorterDuff.Mode.MULTIPLY);
                imMessage.setBackground(dfd);
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                value="1";
                callservice();

                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.gradient_1));
                txNotification.setTextColor(getResources().getColor(R.color.blue));

                break;


            case R.id.tvQrScanner:
                Log.e("tvQrScanner-->","tvQrScanner");
                break;

        }
    }

    @Override
    public void onBackPressed() {

        /*if (!doubleBackToExitPressedOnce) {
            finish();
            Log.e("Biding","once");
        }*/

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
//            super.onBackPressed();
            Log.e("Biding","once");
//            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Intent intent=new Intent(NotificationPage.this,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_BTC_RATE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        callservice();

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))*Double.parseDouble(data.getString("sell"));

                            if(buy_rate>0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                            else
                            {
                                tvINR.setText("0" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                            Log.e("data--->", data.toString());

                        } else
                        {
                            Constant.alertDialog(this, message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;


            case Constant.REQ_USER_NOTIFICATION:

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Log.e("NotificationResulr ",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        AllUserModel.clear();
                        notiCount.clear();
                        alertCount.clear();
                        offerCount.clear();
                        JSONArray data = result.getJSONArray("data");

                        for (int x = 0; x < data.length(); x++) {

                            JSONObject obj = data.getJSONObject(x);

                            if (obj.getString("notification_type").equalsIgnoreCase("Notification"))
                            {
                                NotificationModel notificationModel=new NotificationModel();
                                if (obj.getString("view_status").equalsIgnoreCase("Open"))
                                {
                                    notificationModel.setNotification_id(obj.getString("id"));
                                    notiCount.add(notificationModel);
                                }
                            }
                            Log.e("notiCount ",notiCount.size()+"");
                            if (notiCount.size()>0)
                            {
                                rlCountt.setVisibility(View.VISIBLE);
                                tvCount.setText(String.valueOf(notiCount.size()));
                            }
                            else {
                                rlCountt.setVisibility(View.GONE);
                            }

                            if (obj.getString("notification_type").equalsIgnoreCase("Alert"))
                            {
                                if (obj.getString("view_status").equalsIgnoreCase("Open"))
                                {
                                    NotificationModel notificationModel1=new NotificationModel();
                                    notificationModel1.setNotification_id(obj.getString("id"));
                                    alertCount.add(notificationModel1);
                                }
                            }

                            if (alertCount.size()>0)
                            {
                                rlCounttAlert.setVisibility(View.VISIBLE);
                                tvCountAlert.setText(String.valueOf(alertCount.size()));
                            }
                            else {
                                rlCounttAlert.setVisibility(View.GONE);
                            }

                            Log.e("alertCount ",alertCount.size()+"");


                            if (obj.getString("notification_type").equalsIgnoreCase("Update"))
                            {
                                if (obj.getString("view_status").equalsIgnoreCase("Open"))
                                {
                                    NotificationModel notificationModel2=new NotificationModel();
                                    notificationModel2.setNotification_id(obj.getString("id"));
                                    offerCount.add(notificationModel2);
                                }
                            }

                            if (offerCount.size()>0)
                            {
                                rlCounttOffers.setVisibility(View.VISIBLE);
                                tvCountOffers.setText(String.valueOf(offerCount.size()));
                            }
                            else {
                                rlCounttOffers.setVisibility(View.GONE);
                            }



                            Log.e("offerCount ",offerCount.size()+"");



                            if(flag==1) {

                                if (obj.getString("notification_type").equalsIgnoreCase("Notification"))
                                {
                                    NotificationModel allUserModel = new NotificationModel();
                                    allUserModel.setNotification_id(obj.getString("id"));
                                    allUserModel.setNotification_title(obj.getString("title"));
                                    allUserModel.setNotification_description(obj.getString("message"));
//                                    allUserModel.setNotification_date(obj.getString("created"));
                                    allUserModel.setNotification_date(obj.getString("updated"));
                                    allUserModel.setNotification_type(obj.getString("notification_type"));
                                    allUserModel.setRead_status(obj.getString("view_status"));
                                    AllUserModel.add(allUserModel);
                                }
                            }

                            if(flag==2) {

                                if (obj.getString("notification_type").equalsIgnoreCase("Alert")) {
                                    NotificationModel allUserModel = new NotificationModel();
                                    allUserModel.setNotification_id(obj.getString("id"));
                                    allUserModel.setNotification_title(obj.getString("title"));
                                    allUserModel.setNotification_description(obj.getString("message"));
//                                    allUserModel.setNotification_date(obj.getString("created"));
                                    allUserModel.setNotification_date(obj.getString("updated"));
                                    allUserModel.setNotification_type(obj.getString("notification_type"));
                                    allUserModel.setRead_status(obj.getString("view_status"));
                                    AllUserModel.add(allUserModel);
                                }
                            }

                            if(flag==3) {

                                if (obj.getString("notification_type").equalsIgnoreCase("Update")) {
                                    NotificationModel allUserModel = new NotificationModel();
                                    allUserModel.setNotification_id(obj.getString("id"));
                                    allUserModel.setNotification_title(obj.getString("title"));
                                    allUserModel.setNotification_description(obj.getString("message"));
//                                    allUserModel.setNotification_date(obj.getString("created"));
                                    allUserModel.setNotification_date(obj.getString("updated"));
                                    allUserModel.setNotification_type(obj.getString("notification_type"));
                                    allUserModel.setRead_status(obj.getString("view_status"));
                                    AllUserModel.add(allUserModel);
                                }
                            }

                        }

                        if (value.equalsIgnoreCase("1"))
                        {
                            if(AllUserModel.size()>0) {

                                recyclerView.setVisibility(View.VISIBLE);
                                txtext.setVisibility(View.GONE);
                                adapter = new NotificationAdapter(this, AllUserModel);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                recyclerView.setVisibility(View.GONE);
                                txtext.setVisibility(View.VISIBLE);

                                if(flag==1){
                                    txtext.setText("No Notifications Found");
                                }
                                if(flag==2){
                                    txtext.setText("No Alerts Found");
                                }
                                if(flag==3){
                                    txtext.setText("No Updates Found");
                                }
                            }
                        }


                    }
                    else {

                        Constant.alertWithIntent(this,message,Dashboard.class);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_ADVERTISEMAENT:

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Log.e("AdvertisementResultt-->",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        advertismentImages.clear();
                        JSONArray data = result.getJSONArray("data");

                        for (int x = 0; x < data.length(); x++) {

                            JSONObject obj = data.getJSONObject(x);

                            AdvertismentImages allUserModel = new AdvertismentImages();
                            allUserModel.setTitle(obj.getString("title"));
                            allUserModel.setDescription(obj.getString("description"));
                            allUserModel.setImage(obj.getString("image"));
                            allUserModel.setId(obj.getString("id"));
                            allUserModel.setAdvertisement_url(obj.getString("advertisement_url"));
                            advertismentImages.add(allUserModel);

                        }


                        if(advertismentImages.size()>0) {

                            methodcall();
                        }
                        else {
                            recyclerView.setVisibility(View.GONE);
                            txtext.setVisibility(View.VISIBLE);
                            txtext.setText("No Advertisement Found");
                        }
                    }
                    else {

                        Constant.alertWithIntent(this,message,Dashboard.class);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    public void  methodcall(){

        viewPager_welcome_screen.setAdapter(new NewAapterScreen(this,advertismentImages));
        // tabLayout.setupWithViewPager(viewPager_welcome_screen);

    }
}
