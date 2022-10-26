package com.acepay;


import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.FeesAdapter;
import com.acepay.Model.FeesChargeModet;
import com.acepay.Util.Constant;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Fees_Type extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    public View view;
    FrameLayout frame;
    DrawerLayout mydrawer;
    NavigationView navigationView;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    FeesAdapter feesAdapter;
    RecyclerView rec_fee;

    @Override
    public void setContentView(int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_fees__type, null);
        frame = (FrameLayout) view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);

        mydrawer = (DrawerLayout) findViewById(R.id.mydrawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        rec_fee=(RecyclerView)findViewById(R.id.rec_fee);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rec_fee.setLayoutManager(mLayoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("");
        toolbar.setSubtitle("");

        setSupportActionBar(toolbar);
        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Fees_Type.this, Constant.REQ_BTCCHARGE, Constant.BTCCHARGE).callService(true);
            //   new Retrofit2(this, MyTickets.this, Constant.REQ_MYTICKET,PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }


        drawerToggle = new ActionBarDrawerToggle(this, mydrawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };


        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {


        switch (requestCode) {
            case Constant.REQ_BTCCHARGE:
                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        Log.e("statusfgsta", status);
                        if (status.equals("true")) {
                            Log.e("status", status);
                            JSONArray mydata = result.getJSONArray("data");

                            for (int i = 0; i < mydata.length(); i++)

                            {
                                Log.e("status", status);
                                JSONObject myobj = mydata.getJSONObject(i);

                                String from = myobj.getString("btc_from");
                                String charge = myobj.getString("charge");
                                String to = myobj.getString("btc_to");
                                double d=Double.parseDouble(charge);
                                double fromd=Double.parseDouble(from);
                                double tod=Double.parseDouble(to);
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(d).toPlainString());
                                FeesChargeModet model = new FeesChargeModet();
                                model.setFees((String.format("%.8f", finacal)));
                                model.setFrom((String.format("%.8f", fromd)));
                                model.setTo(to);
                             //   model.setFrom(from);
                               // model.setTo(to);
                                list.add(model);
                                Log.e("njsc", "" + list.size());

                            }
                            feesAdapter = new FeesAdapter(this, list);
                            rec_fee.setAdapter(feesAdapter);

                        } else {
                            Constant.alertDialog(this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

        }
    }
}


