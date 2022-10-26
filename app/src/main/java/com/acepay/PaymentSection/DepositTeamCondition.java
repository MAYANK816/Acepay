package com.acepay.PaymentSection;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.acepay.Adapter.Welcome_Screen_Viwpager_Adapter;
import com.acepay.Model.TermsCondition;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DepositTeamCondition extends AppCompatActivity implements RetrofitResponse {

    TabLayout tabLayout;
//    AutoScrollViewPager viewPager_welcome_screen;
    ViewPager viewPager_welcome_screen;
    Handler handler;
    TextView tvSkip;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<TermsCondition> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_team_condition);
        arrayList=new ArrayList<>();
        getIds();
        callStateService();

    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositTeamCondition.this, Constant.REQ_Terms_conditiona, Constant.Terms_conditiona).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    public void getIds()
    {
        viewPager_welcome_screen=(ViewPager) findViewById(R.id.viewpager_welcome_screen);
        tabLayout=(TabLayout)findViewById(R.id.tablayout_welcome_screen);
        tvSkip=(TextView) findViewById(R.id.tvSkip);

//        viewPager_welcome_screen.startNestedScroll();
//        viewPager_welcome_screen.setInterval(5000);

        handler=new Handler();

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getIntent().getStringExtra("key").equals("normal")) {
                    Log.e("amount deposit-->", getIntent().getStringExtra("amount"));

                    Intent intent = new Intent(DepositTeamCondition.this, NormalPaymentWay.class);
                    intent.putExtra("amount", getIntent().getStringExtra("amount"));
                    intent.putExtra("ifsc", getIntent().getStringExtra("ifsc"));
                    intent.putExtra("brach_name", getIntent().getStringExtra("brach_name"));
                    intent.putExtra("account_holder_name", getIntent().getStringExtra("account_holder_name"));
                    intent.putExtra("bank_name", getIntent().getStringExtra("bank_name"));
                    intent.putExtra("bank_id", getIntent().getStringExtra("bank_id"));
                    intent.putExtra("account_number", getIntent().getStringExtra("account_number"));
                    startActivity(intent);
                }

                if(getIntent().getStringExtra("key").equals("payu")) {


                }


            }
        });

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
                finish();
            }
        }, 1000);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_Terms_conditiona:
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
                            country.setTerm_one(CountryObj.getString("term_one"));
                            country.setTerm_two(CountryObj.getString("term_two"));
                            country.setTerm_three(CountryObj.getString("term_three"));
                            arrayList.add(country);
                        }

                        viewPager_welcome_screen.setAdapter(new Welcome_Screen_Viwpager_Adapter(this,arrayList));
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
