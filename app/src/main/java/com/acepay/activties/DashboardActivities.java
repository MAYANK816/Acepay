package com.acepay.activties;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.ActivitiesAdapter;
import com.acepay.Model.ActivitiesModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Util.UtilClass;


import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class DashboardActivities extends AppCompatActivity implements RetrofitResponse,
        View.OnClickListener
{

    TextView txSellRate,tvBuyrate,tvGifts,tvTop,NoData;
    NumberFormat formatter;
    ImageView ivarrow;
    RecyclerView recyclerActivities;
    ArrayList<ActivitiesModel> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activities);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(DashboardActivities.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        findIds();
        callService();
        callActivitiesService();
        callWinnersAnnouncement();
        callCHeckPremiumUser();

//        setAdapter();
    }

    public void callActivitiesService()
    {
        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DashboardActivities.this, Constant.REQ_ACTIVITIES, Constant.ACTIVITIES).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void findIds()
    {
        txSellRate=(TextView) findViewById(R.id.txSellRate);
        NoData=(TextView) findViewById(R.id.NoData);
        tvBuyrate=(TextView) findViewById(R.id.tvBuyrate);
        tvGifts=(TextView) findViewById(R.id.tvGifts);
        tvTop=(TextView) findViewById(R.id.tvTop);
        tvWinnerLabel=(TextView) findViewById(R.id.tvWinnerLabel);
        ivarrow=(ImageView) findViewById(R.id.ivarrow);
        recyclerActivities=(RecyclerView) findViewById(R.id.recyclerActivities);

        tvGifts.setOnClickListener(this);
        tvTop.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
    }

    String showing_time="";
    String start_date="";
    String start_time="";
    String end_date="";
    String end_time="";
    TextView tvWinnerLabel;

    public void callCHeckPremiumUser()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, DashboardActivities.this, Constant.REQ_CHECK_PREMIUM_USERS,
                        Constant.CHECK_PREMIUM_USERS+"/"+PreferenceFile.getInstance().getPreferenceData(getApplication(), Constant.ID))
                        .callService(false);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void callWinnersAnnouncement()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, DashboardActivities.this, Constant.REQ_WINNERS_ANNOUNCEMENT,
                        Constant.WINNERS_ANNOUNCEMENT)
                        .callService(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void callService()
    {
        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this, DashboardActivities.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else
        {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }



    long remain=0;
    Date dateNow11=null;
    Date end =null;
    String final_end_date="";
    String dateNow="";



    public void showPopUp()
    {
        final Dialog dialog=new Dialog(DashboardActivities.this, R.style.StatisticsDialog);
        dialog.setTitle("TigerPay");
        dialog.setCancelable(true);

        LayoutInflater li = LayoutInflater.from(DashboardActivities.this);
        View promptsView2 = li.inflate(R.layout.deposit_transaction_admin_popup, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(false);
        Button btnokk=(Button)promptsView2.findViewById(R.id.btnokk);
        TextView tvReason=(TextView) promptsView2.findViewById(R.id.tvReason);

        tvReason.setText("You havn't Subscribed yet");

        btnokk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              dialog.dismiss();
            }
        });
        dialog.show();
    }

int finalSeconds=0;
    int inum=0;
    String keyValue="0";
    public void callTimer()
    {

//        duration=81200000; //6 hours
        Log.e("finalSeconds ",finalSeconds+"");
        Log.e("iNum ",inum+"");
        new CountDownTimer(finalSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes,elapsedSeconds);
                tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+yy);
            }

            public void onFinish() {

                tvWinnerLabel.setText("Winner Announce in:"+inum+" days 00:00:00");

            }
        }.start();
/*


        // int time=Integer.parseInt(showing_time);
// downTimer=   new CountDownTimer(time, interval)
        Log.e("FinalSeconds ",finalSeconds+"");
        downTimer=   new CountDownTimer(finalSeconds, interval)
        {
            public void onTick(long millisUntilFinished)
            {
                tvWinnerLabel.setVisibility(View.VISIBLE);
//                tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+showing_time);
                tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+getDateFromMillis(millisUntilFinished));
            }

            public void onFinish()
            {
                tvWinnerLabel.setText("Winner Announce in 0 day 00:00:00");

            }
        };
        downTimer.start();*/
    }


    public void setAdapter()
    {
        recyclerActivities.setLayoutManager(new LinearLayoutManager(this));
        ActivitiesAdapter activitiesAdapter=new ActivitiesAdapter(this,list);
        recyclerActivities.setAdapter(activitiesAdapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvTop:

                if (keyValue.equalsIgnoreCase("0"))
                {
                    showPopUp();
                }else {
                    tvTop.setBackground(getResources().getDrawable(R.drawable.sell_dashboard_activebg));
                    tvGifts.setBackground(getResources().getDrawable(R.drawable.buy_dashboard_inactivebg));

                    tvTop.setTextColor(getResources().getColor(R.color.white));
                    tvGifts.setTextColor(getResources().getColor(R.color.gradient_1));

                    Intent intent1=new Intent(DashboardActivities.this,Top10List.class);
                    startActivity(intent1);
                }



                break;

            case R.id.tvGifts:

                if (keyValue.equalsIgnoreCase("0"))
                {
                    showPopUp();
                }else
                    {
                    tvGifts.setBackground(getResources().getDrawable(R.drawable.blue_bg_gradient));
                    tvTop.setBackground(getResources().getDrawable(R.drawable.sell_dashboard_inactivebg));

                    tvGifts.setTextColor(getResources().getColor(R.color.white));
                    tvTop.setTextColor(getResources().getColor(R.color.gradient_1));
                    Intent  intent=new Intent(DashboardActivities.this,GiftsRewards.class);
                    startActivity(intent);
                }




                break;


                case R.id.ivarrow:
                onBackPressed();

                break;
        }


    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode)
        {
            case Constant.REQ_BTC_RATE:
                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("dashbardres ",result.toString());


                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            JSONObject data = result.getJSONObject("data");
                            double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
                            BigDecimal d = new BigDecimal(calcul);

                            tvBuyrate.setText( formatter.format(Double.valueOf(data.getString("buy")))+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)+" ");
                            txSellRate.setText(  formatter.format(Double.valueOf(data.getString("sell")))+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this, Constant.BUY, data.getString("buy"));
                            PreferenceFile.getInstance().saveData(this, Constant.SELL, data.getString("sell"));
                            PreferenceFile.getInstance().saveData(this, Constant.BUYRATE, tvBuyrate.getText().toString());
                            PreferenceFile.getInstance().saveData(this, Constant.SELLRATE, txSellRate.getText().toString());

                            Double buy_rate = Double.parseDouble(String.valueOf(d)) * Double.parseDouble(data.getString("buy"));

                            // tvINR.setText(String.valueOf(buy_rate));

                       /* if (buy_rate > 0) {

                            tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                        } else {
                            tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                        }*/


                        } else {
                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY) != null) {
                                double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));

                                BigDecimal d = new BigDecimal(calcul);

                                tvBuyrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,
                                        Constant.BUY)))+
                                        " "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)+" ");
                                txSellRate.setText(" "+formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,
                                        Constant.SELL)))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                PreferenceFile.getInstance().saveData(this, Constant.BUYRATE, tvBuyrate.getText().toString());
                                PreferenceFile.getInstance().saveData(this, Constant.SELLRATE, txSellRate.getText().toString());
                                Double buy_rate = Double.parseDouble(String.valueOf(d)) *
                                        Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL));

                           /* if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                            }*/
                            }
                        }

                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.REQ_ACTIVITIES:

                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("ActivitiesData ",result.toString());


                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            list.clear();
                            NoData.setVisibility(View.GONE);
                            recyclerActivities.setVisibility(View.VISIBLE);
                            JSONArray buyBitcoin=result.getJSONArray("activity");

                            Log.e("Lengthhh ",buyBitcoin.length()+"");


                            if (buyBitcoin.length()>0)
                            {
                                for (int i = 0; i < buyBitcoin.length(); i++)
                                {

                                    ActivitiesModel activitiesModel=new ActivitiesModel();

                                    JSONObject jsonObject=buyBitcoin.getJSONObject(i);

                                    Log.e("JSONObject ",jsonObject.toString());


                                    activitiesModel.setId(jsonObject.getString("id"));


                                    //todo for buy btc
                                    if (jsonObject.getString("type_activity").equalsIgnoreCase("buy_btc"))
                                    {
                                        activitiesModel.setBtc_rate(jsonObject.getString("rate"));
                                        activitiesModel.setBtc(jsonObject.getString("amount"));

                                        if (jsonObject.getString("transaction_by").equalsIgnoreCase("2"))
                                        {
                                            activitiesModel.setImage("admin");
                                            activitiesModel.setName("By Admin");
                                        }

                                        else {

                                            if (jsonObject.has("Users"))
                                            {
                                                Object o=jsonObject.get("Users");

                                                if (o instanceof JSONObject)
                                                {
                                                    JSONObject Users=jsonObject.getJSONObject("Users");
                                                    activitiesModel.setImage(Users.getString("image"));
                                                    activitiesModel.setName(Users.getString("first_name")+Users.getString("last_name"));
                                                }
                                                else
                                                {
                                                    activitiesModel.setImage("admin");
                                                    activitiesModel.setName("NA");
                                                }
                                            }


                                        }

                                    }

                                    //todo for sell btc
                                    else if (jsonObject.getString("type_activity").equalsIgnoreCase("sell_btc"))
                                    {
                                        activitiesModel.setBtc_rate(jsonObject.getString("sell_rate"));
                                        activitiesModel.setBtc(jsonObject.getString("sell_amount"));

                                        if (jsonObject.getString("transaction_by").equalsIgnoreCase("2"))
                                        {
                                            activitiesModel.setImage("admin");
                                            activitiesModel.setName("By Admin");
                                        }

                                        else {

                                            if (jsonObject.has("Users"))
                                            {
                                                Object o=jsonObject.get("Users");
                                                if (o instanceof JSONObject)
                                                {
                                                    JSONObject Users=jsonObject.getJSONObject("Users");
                                                    activitiesModel.setImage(Users.getString("image"));
                                                    activitiesModel.setName(Users.getString("first_name")+Users.getString("last_name"));
                                                }
                                                else
                                                {
                                                    activitiesModel.setImage("admin");
                                                    activitiesModel.setName("NA");
                                                }
                                            }


                                        }

                                    }

                                    //todo for transfer receive
                                    else if (jsonObject.getString("type_activity").equalsIgnoreCase("transfer_btc"))
                                    {
//                                        activitiesModel.setBtc_rate(jsonObject.getString("sell_rate"));
                                        activitiesModel.setBtc(jsonObject.getString("amount_withdrawn"));

                                        if (jsonObject.getString("transaction_by").equalsIgnoreCase("2"))
                                        {
                                            activitiesModel.setImage("admin");
                                            activitiesModel.setName("By Admin");
                                        }

                                        else {

                                            if (jsonObject.has("receiver"))
                                            {
                                                Object o=jsonObject.get("receiver");

                                                if (o instanceof JSONObject)
                                                {
                                                    JSONObject Users=jsonObject.getJSONObject("receiver");
                                                    activitiesModel.setImage(Users.getString("image"));
                                                    activitiesModel.setName(Users.getString("first_name")+Users.getString("last_name"));
                                                }
                                                else
                                                {
                                                    activitiesModel.setImage("admin");
                                                    activitiesModel.setName("NA");
                                                }
                                            }
                                        }
                                    }

                                    activitiesModel.setDate_time(jsonObject.getString("created"));
                                    activitiesModel.setType(jsonObject.getString("type_activity"));


                                    list.add(activitiesModel);


                                }

                                if (list.size()>0)
                                {
                                    setAdapter();
                                }
                            }
                        }
                        else
                        {
                            NoData.setVisibility(View.VISIBLE);
                            recyclerActivities.setVisibility(View.GONE);
                        }

                    } else
                    {
//                            NoData.setVisibility(View.VISIBLE);
//                            recyclerActivities.setVisibility(View.GONE);
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case Constant.REQ_WINNERS_ANNOUNCEMENT:
                Log.e("tOP10lISTrESPONSE  ",response.body().toString()+"");

                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("tOP10lISTrESPONSE ",result.toString());


                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            JSONObject data=result.getJSONObject("data");

                            start_date=data.getString("start_date");
                            end_date=data.getString("end_date");
                            final_end_date=data.getString("final_end_date");
                            start_time=data.getString("start_time");
                            end_time=data.getString("end_time");

                            String finalCurrentDate="";
                            String finalEndDate="";
                            String finalStartDate="";
                            String finalEndTime="";
                            String finalStartTime="";
                            String finalCurrentTime="";
                            String currentTimeformat="";

                            SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date dt11=format2.parse(end_date);
                                Date dtStart=format2.parse(start_date);
                                DateFormat formatSd=new SimpleDateFormat("dd-MM-yyyy");
                                finalEndDate=formatSd.format(dt11);
                                finalStartDate=formatSd.format(dtStart);
                                Log.e("finalEndDate ",finalEndDate);
                                Log.e("finalStartDate ",finalStartDate);
                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }



                            finalCurrentDate= UtilClass.getCurrentDate();
                            Log.e("finalCurrentDate ",finalCurrentDate);


                            long diffDatesSTartCurrent= UtilClass.difference2Dates(finalStartDate,finalCurrentDate);
                            Log.e("diffDatesSTartCurrent ",diffDatesSTartCurrent+"");

                            long diffDatesEndCurrent=UtilClass.difference2Dates(finalEndDate,finalCurrentDate);
                            Log.e("diffDatesEndCurrent ",diffDatesEndCurrent+"");


                            int  inumStart = (int)diffDatesSTartCurrent;
                            Log.e("DiffeSTartCurrent ",inumStart+"");

                            inum = (int)diffDatesEndCurrent;
                            Log.e("DiffeEndCurrent ",inum+"");





                            SimpleDateFormat format21=new SimpleDateFormat("hh:mm aa");
                            try {
                                Date dt311=format21.parse(end_time);
                                Date dt3Start=format21.parse(start_time);
                                DateFormat formatSd=new SimpleDateFormat("HH:mm");
                                finalEndTime=formatSd.format(dt311);
                                finalStartTime=formatSd.format(dt3Start);

                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                            finalCurrentTime=UtilClass.getCurrentTime();
                            Log.e("finalCurrentTime ",finalCurrentTime+"");
                            Log.e("finalEndTime ",finalEndTime+"");
                            Log.e("finalStartTime ",finalStartTime);

                            String current12="";
                            try
                            {


                                SimpleDateFormat input=new SimpleDateFormat("hh:mm");
                                SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");

                                Date dt3bfffh11=input.parse(finalCurrentTime);
                                current12=output.format(dt3bfffh11);
                                Log.e("current12 ",current12+"");
                                Log.e("StartTime ",finalStartTime+"");

                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }



                            String timeeeGet="";
                            //todo for time
                            SimpleDateFormat format2123=new SimpleDateFormat("HH:mm aa");
                            try {
                                Date dt311=format2123.parse(start_time);
                                Date dt311Curr=format2123.parse(current12);
                                SimpleDateFormat formatSd=new SimpleDateFormat("HH:mm aa");
//                                timeeeGet=formatSd.format(dt311);
                                currentTimeformat=formatSd.format(dt311Curr);
//                                Log.e("timeeeGet ",timeeeGet);
                                Log.e("currentTimeformat ",currentTimeformat);
                                Log.e("start_time ",start_time);
                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }


                            long dtMili = System.currentTimeMillis();
                            dateNow11 = new Date(dtMili);


                            dateNow=String.valueOf(dateNow11);

                            Log.e("DateVaueee ",dateNow+"");

                            Log.e("final_end_date ",final_end_date);
                            String enddFormat="";
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                                Date date = inputFormat.parse(final_end_date);
                                enddFormat = outputFormat.format(date);
                                Log.e("EndFormatsss ",enddFormat);
                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                            try{
                                SimpleDateFormat EndoutputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                                end = EndoutputFormat.parse(enddFormat);


                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }



                            long diffDates=UtilClass.difference2Dates(finalEndDate,finalCurrentDate);
                            Log.e("diffDates ",diffDates+"");

                            inum = (int)diffDates;


                            Log.e("difference ",inum+"");

                            finalStartTime=UtilClass.getCurrentTime();
                            Log.e("finalStartTime ",finalStartTime+"");
                            Log.e("finalEndTime ",finalEndTime+"");

                            long timeee=UtilClass.diff2Times(finalEndTime,finalStartTime);
                            Log.e("timeee ",timeee+"");

                            int iTime = (int)timeee;
                            Log.e("iTime ",iTime+"");

                            int seconds=(iTime)/1000;
                            Log.e("seconds ",seconds+"");

                            finalSeconds=iTime;

                            String compareDate="",compareDatewithEnd="";
                            compareDate=UtilClass.compareDates(finalStartDate,finalCurrentDate);
                            compareDatewithEnd=UtilClass.compareDates(finalStartDate,finalStartDate);

                            String compareTime="";
//                                compareTime=UtilClass.compareTiminng(finalStartTime,finalCurrentTime);
                            compareTime=UtilClass.compareTiminng24(start_time,currentTimeformat);

                            Log.e("compareDateClasss ",compareDate);
                            Log.e("compareTimeClasss ",compareTime);

                            if (compareDate.equalsIgnoreCase("equal")||(compareDate.equalsIgnoreCase("before")))
                            {
                                Log.e("StartCurrentDateEquals ","equall");
                                if ( compareDate.equalsIgnoreCase("before"))
                                {
//                                        startCountdown();
                                    callTimer();
                                }
                                else {
                                    if (compareTime.equalsIgnoreCase("equal")||(compareTime.equalsIgnoreCase("before")))
                                    {
                                        Log.e("StartCurrentTimeEquals ","equals");

//                                            startCountdown();
                                        callTimer();//when competiton started
                                    }
                                    else {
                                        Log.e("StartCurrentTimeEquals ","after");
                                        //no timer
                                        tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");
                                    }
                                }


                            }
                            else
                            {
                                Log.e("StartCurrentDateEquals ","notEqual");
                                tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");
                            }



/*
                                if ((inum==0 && seconds==0)||(inum<0 && seconds==0))
                                {
                                    tvWinnerLabel.setText("Winner Announce in:0 days "+showing_time);

                                }

                                else {
                                    tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+showing_time);


                                    callTimer();
                                }


*/



                        }
                        else
                        {
                            tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");
                        }

                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_CHECK_PREMIUM_USERS:

                try {
                    if (response.isSuccessful()) {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("REQ_CHECK_PREMIUM_USERS", result.toString());

                        String status = result.getString("response");

                        if (status.equals("true"))
                        {
                            keyValue="1";

                        } else
                        {
                            keyValue="0";
                        }

                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;

        }
    }
}
