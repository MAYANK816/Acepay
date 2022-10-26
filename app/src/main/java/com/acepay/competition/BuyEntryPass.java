package com.acepay.competition;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.CompetitionAdapter;
import com.acepay.Adapter.PlansAdapter;
import com.acepay.Dashboard;
import com.acepay.DepositTransaction;
import com.acepay.Model.CompetitionModel;
import com.acepay.Model.PlansModel;
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
import java.util.concurrent.TimeUnit;
import okhttp3.ResponseBody;
import retrofit2.Response;

//public class BuyEntryPass extends AppCompatActivity implements RetrofitResponse, View.OnClickListener, PaymentStatusListener {
public class BuyEntryPass extends AppCompatActivity
        implements RetrofitResponse, View.OnClickListener {

    RecyclerView recyclerCompetition,recyclerPlans;

    ArrayList<CompetitionModel> copetitionList=new ArrayList<>();
    ArrayList<PlansModel> plansList=new ArrayList<>();

    TextView txSellRate,tvBuyrate;
    NumberFormat formatter;

    RelativeLayout rlUpi;

    public static String competition_id="";
    public static String plan_id="",plan_price="";

    ImageView ivarrow;
    TextView tvNoData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_entry_pass);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
       getPreferenceData(BuyEntryPass.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

       findIds();

       callService();
//     showSuccessPopup("Buy Subscrption succesfully","buy");

    }

    public void findIds()
    {
        recyclerCompetition=(RecyclerView)findViewById(R.id.recyclerCompetition);
        recyclerPlans=(RecyclerView)findViewById(R.id.recyclerPlans);
        txSellRate=(TextView) findViewById(R.id.txSellRate);
        tvBuyrate=(TextView) findViewById(R.id.tvBuyrate);
        tvWinnerLabel=(TextView) findViewById(R.id.tvWinnerLabel);
        ivarrow=(ImageView) findViewById(R.id.ivarrow);
        tvNoData=(TextView) findViewById(R.id.tvNoData);
        rlUpi=(RelativeLayout) findViewById(R.id.rlUpi);
        rlUpi.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        recyclerCompetition.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerPlans.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setCompetitionAdapter()
    {
        CompetitionAdapter competitionAdapter=new CompetitionAdapter(this,copetitionList);
        recyclerCompetition.setAdapter(competitionAdapter);
    }

    public void setPlansAdapter()
    {
        PlansAdapter plansAdapter=new PlansAdapter(this,plansList);
        recyclerPlans.setAdapter(plansAdapter);
    }

    private void callService()
    {
        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this,
                    BuyEntryPass.this, Constant.REQ_BTC_RATE,
                    Constant.BTC_RATE).callService(true);
        }
        else
        {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public boolean checkValidations()
    {
        if (competition_id.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Choose your competition",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (plan_id.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Choose your  plan",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    private void callCompetitionPlans()
    {
        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this, BuyEntryPass.this,
                    Constant.REQ_COMPETITION_PLANS, Constant.COMPETITION_PLANS).
                    callService(true);
        }
        else
        {
            Constant.alertDialog(this, getResources().
                    getString(R.string.check_connection));
        }
    }

    public void callUpiData()
    {
        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this, BuyEntryPass.this,
                    Constant.REQ_UPI_DETAILS, Constant.UPI_DETAILS).callService(true);
        }
        else
        {
            Constant.alertDialog(this,
                    getResources().getString(R.string.check_connection));
        }
    }

    public void openUPIApp(String upi_id,String name,String note)
    {
        Log.e("UpiId ",upi_id);
        Log.e("name ",name);
        Log.e("note ",note);
        Log.e("plan_price ",plan_price);

//     makePayment(plan_price,upi_id,name,note,"64734635645");
        payUsingUpi(plan_price,upi_id,name,note);
//        payUsingUpi("1",upi_id,name,note);
    }

    //https://web.archive.org/web/20200921110005/https://www.npci.org.in/sites/default/files/UPI%20Linking%20Specs_ver%201.6.pdf


    void payUsingUpi(String amount, String upiId, String name, String note)
    {

        /*Uri uri =
    new Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", "test@okbizaxis")
        .appendQueryParameter("pn", "Test")
        .appendQueryParameter("mc", "1234")
        .appendQueryParameter("tr", "1234")
        .appendQueryParameter("tn", "test")
        .appendQueryParameter("am", "10")
        .appendQueryParameter("cu", "INR")
        .appendQueryParameter("url", "")
        .build();
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setData(uri);
intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
startActivityForResult(intent, TEZ_REQUEST_CODE);*/

//https://www.blueappsoftware.com/google-pay-error-bank-limit-exceeded/
//8267836652-1@okbizaxis
//        Uri uri = Uri.parse("upi://pay").buildUpon()
////                .appendQueryParameter("pa", upiId)
//                .appendQueryParameter("pa", "8267836652-1@okbizaxis")
////                .appendQueryParameter("pa", "9897010981@okbizaxis")
////                .appendQueryParameter("pn", name)
//                .appendQueryParameter("pn", "NextGen Group")
//                .appendQueryParameter("tn", note)
//                .appendQueryParameter("am", "1.00")
//                .appendQueryParameter("cu", "INR")
////               .appendQueryParameter("mc", "BCR2DN6TTPUIPVZH")
//                .appendQueryParameter("mc", "BCR2DN4TTDMIFTJK")
//                .appendQueryParameter("tr", "xyzshdfhsd")
//                .appendQueryParameter("url", "https://test.merchant.website")
//                .build();

//                      Uri uri = new Uri.Builder()
//                        .scheme("upi")
//                        .authority("pay")
//                        .appendQueryParameter("pa", "9897010981@okbizaxis")
//                        .appendQueryParameter("pn", "NextGen Group")
//                        .appendQueryParameter("mc", "1234")
//                        .appendQueryParameter("tr", "123456789")
//                        .appendQueryParameter("tn", "test transaction note")
//                        .appendQueryParameter("am", "1.00")
//                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "https://test.merchant.website")
//                        .build();


                 Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "q474063887@ybl")
                .appendQueryParameter("pn", "Tradebridge")
                .appendQueryParameter("mc", "1234")
                .appendQueryParameter("tr", "123456789")
                .appendQueryParameter("tn", "test transaction note")
                .appendQueryParameter("am", "1.00")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", "https://test.merchant.website")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser, UPI_PAYMENT);
        }
        else
        {
            Toast.makeText(BuyEntryPass.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    final int UPI_PAYMENT = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case UPI_PAYMENT:
                Log.e("resultCode ", resultCode+"");
                Log.e("dataaa ", data+"");

                if ((RESULT_OK == resultCode) || (resultCode == 11))
                {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult:IF " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult:Else " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }
                else
                {
                    Log.e("UPI", "onActivityResult:empty " + "no data"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }

                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(BuyEntryPass.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                    {
                        status = equalStr[1].toLowerCase();
                        Log.e("status ", status);
//                        Toast.makeText(this, "Stauts: "+status, Toast.LENGTH_SHORT).show();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                        Log.e("approvalRefNo ", approvalRefNo);
//                        Toast.makeText(this, "approvalRefNo: "+approvalRefNo, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                    Log.e("paymentCancel ", paymentCancel);
//                    Toast.makeText(this, "paymentCancel: "+paymentCancel, Toast.LENGTH_SHORT).show();

                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
//                Toast.makeText(BuyEntryPass.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "responseStr: "+approvalRefNo);
                transaction_id=approvalRefNo;
                Log.e("transaction_id ", "transaction_id: "+transaction_id);
//                Toast.makeText(this, "transaction_id: "+transaction_id, Toast.LENGTH_SHORT).show();
                callBuySubscription();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
//                Toast.makeText(BuyEntryPass.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
//                Toast.makeText(BuyEntryPass.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(BuyEntryPass.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    String showing_time="";
    String start_date="";
    String start_time="";
    String end_date="";
    String final_end_date="";
    String dateNow="";
    String end_time="";
    TextView tvWinnerLabel;

    public void callWinnersAnnouncement()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, BuyEntryPass.this, Constant.REQ_WINNERS_ANNOUNCEMENT,
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

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        switch (requestCode)
        {

            case Constant.REQ_BTC_RATE:
            try {

                if (response.isSuccessful()) {

                    JSONObject result = new JSONObject(response.body().string());

                    Log.e("dashbardres",result.toString());


                    String status = result.getString("response");
                    String message = result.getString("message");

                    callCompetitionPlans();
                    callWinnersAnnouncement();

                    if (status.equals("true")) {

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

                            tvBuyrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY)))+
                                    " "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)+" ");
                            txSellRate.setText(" "+formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL)))
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

            case Constant.REQ_COMPETITION_PLANS:

                try {

                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("Competitionnns ",result.toString());

                        if (result.getString("response").equalsIgnoreCase("true"))
                        {
                            copetitionList.clear();
                            plansList.clear();
                            JSONArray Competition=result.getJSONArray("Competition");

                            if (Competition.length()>0)
                            {
                                for (int i = 0; i <Competition.length() ; i++)
                                {
                                    JSONObject jsonObject=Competition.getJSONObject(i);
                                    CompetitionModel competitionModel=new CompetitionModel();
                                    competitionModel.setId(jsonObject.getString("id"));
                                    competitionModel.setName(jsonObject.getString("name"));
                                    competitionModel.setStart_date(jsonObject.getString("start_date"));
                                    competitionModel.setStart_time(jsonObject.getString("start_time"));
                                    competitionModel.setEnd_date(jsonObject.getString("end_date"));
                                    competitionModel.setEnd_time(jsonObject.getString("end_time"));
                                    competitionModel.setTick(false);
                                    copetitionList.add(competitionModel);
                                }
                                if (copetitionList.size()>0)
                                {
                                    tvNoData.setVisibility(View.GONE);
                                    recyclerPlans.setVisibility(View.VISIBLE);
                                    recyclerCompetition.setVisibility(View.VISIBLE);
                                    setCompetitionAdapter();
                                }
                                else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    recyclerPlans.setVisibility(View.GONE);
                                    recyclerCompetition.setVisibility(View.GONE);
                                }
                            }

                            JSONArray Plans=result.getJSONArray("Plans");

                            if (Plans.length()>0)
                            {
                                for (int i = 0; i <Plans.length() ; i++)
                                {
                                    JSONObject jsonObject=Plans.getJSONObject(i);

                                    PlansModel plansModel=new PlansModel();

                                    plansModel.setId(jsonObject.getString("id"));
                                    plansModel.setName(jsonObject.getString("name"));
                                    plansModel.setPrice(jsonObject.getString("entry_fee"));
                                    plansModel.setDescription(jsonObject.getString("benefits"));
                                    plansModel.setFlag(false);

                                    plansList.add(plansModel);
                                }

                                if (plansList.size()>0)
                                {
                                    tvNoData.setVisibility(View.GONE);
                                    recyclerPlans.setVisibility(View.VISIBLE);
                                    recyclerCompetition.setVisibility(View.VISIBLE);
                                    setPlansAdapter();
                                }
                                else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    recyclerPlans.setVisibility(View.GONE);
                                    recyclerCompetition.setVisibility(View.GONE);
                                }
                            }
                        }
                        else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerPlans.setVisibility(View.GONE);
                            recyclerCompetition.setVisibility(View.GONE);
                        }
                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                  /*      tvNoData.setVisibility(View.VISIBLE);
                        recyclerPlans.setVisibility(View.GONE);
                        recyclerCompetition.setVisibility(View.GONE);*/
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;

                case Constant.REQ_UPI_DETAILS:

                try {

                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("Result ",result.toString());
                        JSONObject upidetails=result.getJSONObject("upidetails");
                        Log.e("UPIDetails ",upidetails.toString());

                        upi_id=upidetails.getString("upi_id");
                        name=upidetails.getString("name");
                        note=upidetails.getString("note");

                        openUPIApp(upi_id,name,note);

                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_BUY_SUBSCRIPTION:

                try
                {
                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("Result ",result.toString());

                        if (result.getString("response").equalsIgnoreCase("true"))
                        {
//                            Toast.makeText(this, result.getString("message"), Toast.LENGTH_SHORT).show();
                            showSuccessPopup(result.getString("message"),"buy");
                        }
                        else {
                            Toast.makeText(this, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(BuyEntryPass.this, "inside Catch", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }

                break;

                case Constant.REQ_CHECK_PLAN:

                try
                {
                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("Result ",result.toString());
//                        Toast.makeText(this, "BuyResponse "+result.toString(), Toast.LENGTH_SHORT).show();

                        if (result.getString("response").equalsIgnoreCase("true"))
                        {
//                            callBuySubscription();
                            callUpiData();
                        }
                        else {
//                            Toast.makeText(this, result.getString("message"), Toast.LENGTH_SHORT).show();
                            showSuccessPopup(result.getString("message"),"check");
                        }
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
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

                    } else
                    {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void stopCountdown() {
        if (countdownHandler != null) {
            countdownHandler.removeCallbacks(updateCountdown);
            countdownHandler = null;
        }
    }

    private void startCountdown() {
        stopCountdown();
        countdownHandler = new Handler();
        updateCountdown.run();
    }

    private Runnable updateCountdown = new Runnable() {
        @Override
        public void run() {
            try {
                tvWinnerLabel.setText("Winners announce in: "+getCountDownText());
            }
            finally {
                countdownHandler.postDelayed(updateCountdown, COUNTDOWN_UPDATE_INTERVAL);
            }
        }
    };

    int finalSeconds=0;
    int inum=0;
    long remain=0;
    Date dateNow11=null;
    Date end =null;
    private static final int COUNTDOWN_UPDATE_INTERVAL = 500;

    private Handler countdownHandler;

    int time = 1 * 20 * 1000; //20 seconds
    int interval = 1000; // 1 second

    public  String getCountDownText()
    {
        String countDownTextValue="";


        StringBuilder countdownText = new StringBuilder();

        remain = end.getTime() - dateNow11.getTime();
        Log.e("remainValue ",remain+"");
        Log.e("remainValueDate ",new Date().getTime()+"");
        if (remain > 0) {
            Resources resources = getResources();

            int days = (int) TimeUnit.MILLISECONDS.toDays(remain);
            remain = TimeUnit.DAYS.toMillis(days);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(remain);
            remain = TimeUnit.HOURS.toMillis(hours);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(remain);
            remain = TimeUnit.MINUTES.toMillis(minutes);
            int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(remain);
            Log.e("days ",days+"");
            Log.e("hours ",hours+"");
            Log.e("mins ",minutes+"");
            Log.e("seconds ",seconds+"");

            /* E/days: 3
2020-05-24 03:36:46.926 11908-11908/com.tigerpay E/hours: 72
2020-05-24 03:36:46.926 11908-11908/com.tigerpay E/mins: 4320
2020-05-24 03:36:46.926 11908-11908/com.tigerpay E/seconds: 259200*/



            // For each time unit, add the quantity string to the output, with a space.
            if (days > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.days, days, days));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.hours, hours, hours));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.seconds, seconds, seconds));
                countdownText.append(" ");
            }
            countDownTextValue=String.valueOf(countDownTextValue);
            Log.e("CountdownText ",countDownTextValue);

        }

        return countDownTextValue;
    }

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

            }
            public void onFinish()
            {
                tvWinnerLabel.setText("Winner Announce in: 0"+" days 00:00:00");
            }
        }.start();
    }

    public String timeCalculate(long ttime)
    {
        long  daysuuu,hoursuuu, minutesuuu, secondsuuu;
        String daysT = "", restT = "";



        daysuuu = (Math.round(ttime) / 86400);

        if(daysuuu==1) daysT = String.format("%d day ", daysuuu);
        if(daysuuu>1) daysT = String.format("%d days ", daysuuu);


        hoursuuu = (Math.round(ttime) / 3600) - (daysuuu * 24);
        minutesuuu = (Math.round(ttime) / 60) - (daysuuu * 1440) - (hoursuuu * 60);
        secondsuuu = Math.round(ttime) % 60;




        restT = String.format("%02d:%02d:%02d", hoursuuu, minutesuuu, secondsuuu);

        return daysT + restT;
    }

/*var millisUntilFinished:Long = millisUntilFinished
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
                Locale.getDefault(),
                "%02d day: %02d hour: %02d min: %02d sec",
                days,hours, minutes,seconds
        )*/

    public String ExactTimeCalculate(Long remain)
    {
        String excatTime="";
        return excatTime;
    }

    public void showSuccessPopup(String message,final String key)
    {
        /*Toast.makeText(this, "InsidePopup  "+message.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "key  "+key.toString(), Toast.LENGTH_SHORT).show();*/

        final Dialog dialog=new Dialog(BuyEntryPass.this, R.style.StatisticsDialog);
        dialog.setTitle("TigerPay");
        dialog.setCancelable(true);

        LayoutInflater li = LayoutInflater.from(BuyEntryPass.this);
        View promptsView2 = li.inflate(R.layout.deposit_transaction_admin_popup, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(false);


        Button btnokk=(Button)promptsView2.findViewById(R.id.btnokk);
        TextView tvReason=(TextView) promptsView2.findViewById(R.id.tvReason);

        tvReason.setText(message);

        btnokk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Toast.makeText(BuyEntryPass.this, "okayCLick "+key.toString(), Toast.LENGTH_SHORT).show();
                if (key.equalsIgnoreCase("check"))
                {
                    dialog.dismiss();
                }
                else
                {
                    Intent intent=new Intent(BuyEntryPass.this, Dashboard.class);
                    intent.putExtra("key","buy");
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public String transaction_id="";

    public void callBuySubscription()
    {
        try
        {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("competition_id",competition_id);
            jsonObject.put("plan_id",plan_id);
            jsonObject.put("transaction_id",transaction_id);
//          jsonObject.put("transaction_id","013123405672");
            jsonObject.put("user_id",PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));

//         Toast.makeText(this, "SendParams "+jsonObject.toString(), Toast.LENGTH_SHORT).show();

            if (Constant.isConnectingToInternet(BuyEntryPass.this))
            {
                Log.e("depositconnect--->", "2");
                new Retrofit2(BuyEntryPass.this, BuyEntryPass.this, jsonObject,
                        Constant.REQ_BUY_SUBSCRIPTION, Constant.BUY_SUBSCRIPTION, "3").callService(true);
            }
            else
            {
                Log.e("depositconnect--->", "no");
                Constant.alertDialog(BuyEntryPass.this, getResources().getString(R.string.check_connection));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void callCHeckALreadySubscription()
    {
        try
        {
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("competition_id",competition_id);
            jsonObject.put("plan_id",plan_id);
            jsonObject.put("user_id",
                    PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));

            Log.e("CheckPlansParams ",jsonObject.toString());

            if (Constant.isConnectingToInternet(BuyEntryPass.this))
            {
                new Retrofit2(BuyEntryPass.this, BuyEntryPass.this, jsonObject,
                        Constant.REQ_CHECK_PLAN, Constant.CHECK_PLAN, "3").callService(true);
            }
            else
            {
                Constant.alertDialog(BuyEntryPass.this,
                        getResources().getString(R.string.check_connection));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    String id="",upi_id="",name="",note="";

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rlUpi:

                if (checkValidations())
                {
                    callUpiData();
//                    callCHeckALreadySubscription();
                }

                break;

                case R.id.ivarrow:
                    Intent intent = new Intent(BuyEntryPass.this, DepositTransaction.class);
                    startActivity(intent);
                break;
        }
    }

//    private void makePayment(String amount, String upi, String name, String desc, String transactionId) {
//        // on below line we are calling an easy payment method and passing
//        // all parameters to it such as upi id,name, description and others.
//        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
//                .with(this)
//                // on below line we are adding upi id.
//                .setPayeeVpa(upi)
//                // on below line we are setting name to which we are making oayment.
//                .setPayeeName(name)
//                // on below line we are passing transaction id.
//                .setTransactionId(transactionId)
//                // on below line we are passing transaction ref id.
//                .setTransactionRefId(transactionId)
//                // on below line we are adding description to payment.
//                .setDescription(desc)
//                // on below line we are passing amount which is being paid.
//                .setAmount(amount)
//                // on below line we are calling a build method to build this ui.
//                .build();
//        // on below line we are calling a start
//        // payment method to start a payment.
//        easyUpiPayment.startPayment();
//        // on below line we are calling a set payment
//        // status listener method to call other payment methods.
//        easyUpiPayment.setPaymentStatusListener(this);
//    }

//    @Override
//    public void onTransactionCompleted(TransactionDetails transactionDetails) {
//        // on below line we are getting details about transaction when completed.
//        String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
//        Log.e("Transactioncompleted: ",transcDetails);
//    }
//
//    @Override
//    public void onTransactionSuccess() {
//        // this method is called when transaction is successful and we are displaying a toast message.
//        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onTransactionSubmitted() {
//        // this method is called when transaction is done
//        // but it may be successful or failure.
//        Log.e("TAG", "TRANSACTION SUBMIT");
//    }
//
//    @Override
//    public void onTransactionFailed() {
//        // this method is called when transaction is failure.
//        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onTransactionCancelled() {
//        // this method is called when transaction is cancelled.
//        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onAppNotFound() {
//        // this method is called when the users device is not having any app installed for making payment.
//        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
//    }
}
