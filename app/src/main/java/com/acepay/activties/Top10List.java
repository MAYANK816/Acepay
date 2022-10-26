package com.acepay.activties;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.Top10Adapter;
import com.acepay.Adapter.WinnersAdapter;
import com.acepay.Model.Top10ListModel;
import com.acepay.Model.WinnerModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Util.UtilClass;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Top10List extends AppCompatActivity implements RetrofitResponse
{
    TextView tvWinnerLabel,tvNoData,tvLast,tvNoWinner;
    RecyclerView recycler_winners,recycler_top;
    ImageView ivarrow;
    String start_date="",end_date="",start_time="",end_time="",showing_date="",showing_time="";
    long remain=0;
    Date dateNow11=null;
    Date end =null;
    String final_end_date="";
    String dateNow="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_list);
        findIds();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void findIds()
    {
        tvWinnerLabel=(TextView)findViewById(R.id.tvWinnerLabel);
        tvNoData=(TextView)findViewById(R.id.tvNoData);
        tvLast=(TextView)findViewById(R.id.tvLast);
        tvNoWinner=(TextView)findViewById(R.id.tvNoWinner);
        recycler_winners=(RecyclerView)findViewById(R.id.recycler_winners);
        recycler_top=(RecyclerView)findViewById(R.id.recycler_top);
        ivarrow=(ImageView) findViewById(R.id.ivarrow);

        callTop10();
        callWinnersAnnouncement();

        ivarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        tvWinnerLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                callSubmitCompetition();
//            }
//        });


//        setTop10Adapter();
//        setWinnersAdapter();
    }

    public void callTop10()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, Top10List.this, Constant.REQ_TOP_10_LIST,
                        Constant.TOP_10_LIST+"/"+ PreferenceFile.getInstance().getPreferenceData(getApplication(), Constant.ID))
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

    public void callWinnersAnnouncement()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, Top10List.this, Constant.REQ_WINNERS_ANNOUNCEMENT,
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

    public void callSubmitCompetition()
    {
        try
        {
            JSONObject jsonObject=new JSONObject();

//            jsonObject.put("user_id",PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));

            JSONArray jsonArray=new JSONArray();
            for (int i = 0; i <winnerList.size() ; i++)
            {
                JSONObject jo=new JSONObject();
                jo.put("user_id",winnerList.get(i).getId());
                jsonArray.put(i,jo);
            }

            jsonObject.put("data",jsonArray);
            jsonObject.put("competition_id",competition_id);
            Log.e("SubmitCompetitionParams ",jsonObject.toString());
            if (Constant.isConnectingToInternet(Top10List.this))
            {
                new Retrofit2(Top10List.this, Top10List.this, jsonObject,
                        Constant.REQ_WINNERS_SUBMIT, Constant.WINNERS_SUBMIT, "3").callService(true);
            }
            else
            {
                Constant.alertDialog(Top10List.this, getResources().getString(R.string.check_connection));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setWinnersAdapter()
    {
        recycler_winners.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        WinnersAdapter winnersAdapter=new WinnersAdapter(this,winnerList);
//        WinnersAdapter winnersAdapter=new WinnersAdapter(this);
        recycler_winners.setAdapter(winnersAdapter);

    }

    public void setTop10Adapter()
    {
        recycler_top.setLayoutManager(new LinearLayoutManager(this));
        Top10Adapter top10Adapter=new Top10Adapter(this,list);
        recycler_top.setAdapter(top10Adapter);
    }

    //69960tvWinnerLabel
    int time = 1 * 20 * 1000;//20 seconds
    int interval = 1000; // 1 second
    CountDownTimer downTimer=null;

    public void callTimer()
    {
//        duration=81200000; //6 hours
        Log.e("finalSeconds ",finalSeconds+"");
        Log.e("iNum ",inum+"");
        new CountDownTimer(finalSeconds, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
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
                tvLast.setVisibility(View.VISIBLE);
                tvNoWinner.setVisibility(View.VISIBLE);
                recycler_winners.setVisibility(View.GONE);
            }
            public void onFinish()
            {
                tvWinnerLabel.setText("Winner Announce in: 0"+" days 00:00:00");
                tvLast.setVisibility(View.VISIBLE);
                tvNoWinner.setVisibility(View.GONE);
                recycler_winners.setVisibility(View.VISIBLE);
                callSubmitCompetition();
            }
        }.start();
    }
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




    public static String getDateFromMillis(long d)
    {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    ArrayList<Top10ListModel> list=new ArrayList<>();
    ArrayList<WinnerModel> winnerList=new ArrayList<>();

    String competition_id="";

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        switch (requestCode)
        {
            case Constant.REQ_TOP_10_LIST:
                Log.e("tOP10lISTrESPONSE  ",response.body().toString()+"");

                try
                {
                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("tOP10lISTrESPONSE ",result.toString());

                        list.clear();
                        winnerList.clear();
                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            tvNoData.setVisibility(View.GONE);
                            recycler_top.setVisibility(View.VISIBLE);
                            JSONArray topList=result.getJSONArray("topList");


                            if (topList.length()>0)
                            {
                                for (int i = 0; i <topList.length() ; i++)
                                {
                                    JSONObject jsonObject=topList.getJSONObject(i);
                                    Top10ListModel top10ListModel=new Top10ListModel();

                                    top10ListModel.setId(jsonObject.getString("id"));
                                    top10ListModel.setImage(jsonObject.getString("image"));
                                    top10ListModel.setBtc_amount(jsonObject.getString("btc_amount"));
                                    top10ListModel.setDate(jsonObject.getString("active_date"));
                                    top10ListModel.setTrade(jsonObject.getString("number_of_trade"));
                                    top10ListModel.setName(jsonObject.getString("first_name")+" "+jsonObject.getString("last_name"));

                                    list.add(top10ListModel);
                                }

                                if (list.size()>0)
                                {
                                    setTop10Adapter();
                                }
                            }

                            JSONArray winner=result.getJSONArray("winner");

                            if (winner.length()>0)
                            {
                                for (int j = 0; j <winner.length() ; j++)
                                {
                                    JSONObject jsonObject=winner.getJSONObject(j);
                                    WinnerModel winnerModel=new WinnerModel();
                                    winnerModel.setId(jsonObject.getString("id"));
                                    winnerModel.setName(jsonObject.getString("first_name")+" "+jsonObject.getString("last_name"));
                                    winnerModel.setImage(jsonObject.getString("image"));
                                    winnerModel.setHigh_Score(jsonObject.getString("btc_amount"));
                                    winnerList.add(winnerModel);
                                }
                               /* if (winnerList.size()>0)
                                {
                                    setWinnersAdapter();
                                }*/
                            }
                        }
                        else
                        {
                            tvNoData.setVisibility(View.VISIBLE);
                            recycler_top.setVisibility(View.GONE);
                        }

                    } else {
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

                            competition_id=data.getString("id");
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


                            long diffDatesSTartCurrent=UtilClass.difference2Dates(finalStartDate,finalCurrentDate);
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
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                            try{
                                SimpleDateFormat EndoutputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                                end = EndoutputFormat.parse(enddFormat);
                            }
                            catch (Exception ex)
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
//                                    tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+showing_time);
                                    tvNoWinner.setVisibility(View.VISIBLE);
                                    recycler_winners.setVisibility(View.GONE);
                                    callTimer();
                                }
                                else {
                                    if (compareTime.equalsIgnoreCase("equal")||(compareTime.equalsIgnoreCase("before")))
                                    {
                                        Log.e("StartCurrentTimeEquals ","equals");

//                                            startCountdown();
//                                        tvWinnerLabel.setText("Winner Announce in:"+inum+" days "+showing_time);
                                        tvNoWinner.setVisibility(View.VISIBLE);
                                        recycler_winners.setVisibility(View.GONE);
                                        callTimer();
                                    }
                                    else {
                                        Log.e("StartCurrentTimeEquals ","after");
                                        //no timer
//                                        tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");

                                        tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");
                                        if (winnerList.size()>0)
                                        {
                                            tvNoWinner.setVisibility(View.GONE);
                                            recycler_winners.setVisibility(View.VISIBLE);
                                            setWinnersAdapter();
                                        }
                                        else
                                        {
                                            tvNoWinner.setVisibility(View.VISIBLE);
                                            recycler_winners.setVisibility(View.GONE);
                                        }
                                    }
                                }


                            }
                            else
                            {
                                Log.e("StartCurrentDateEquals ","notEqual");
                                tvWinnerLabel.setText("Winner Announce in:0 days 00:00:00");
                                if (winnerList.size()>0)
                                {
                                    tvNoWinner.setVisibility(View.GONE);
                                    recycler_winners.setVisibility(View.VISIBLE);
                                    setWinnersAdapter();
                                }
                                else
                                {
                                    tvNoWinner.setVisibility(View.VISIBLE);
                                    recycler_winners.setVisibility(View.GONE);
                                }
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

            case Constant.REQ_WINNERS_SUBMIT:
                Log.e("REQ_WINNERS_SUBMIT  ",response.body().toString()+"");

                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("REQ_WINNERS_SUBMITResponse ",result.toString());


                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            tvLast.setVisibility(View.VISIBLE);
                            tvNoWinner.setVisibility(View.GONE);
                            if (winnerList.size()>0)
                            {
                                setWinnersAdapter();
                            }
                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;



        }

    }

    String keyValue="0";

    int finalSeconds=0;
    int inum=0;
}
