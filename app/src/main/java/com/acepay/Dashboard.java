package com.acepay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.CustomMySubscriptionsAdapter;
import com.acepay.Adapter.ServicesAdapter;
import com.acepay.Model.FeesChargeModet;
import com.acepay.Model.MySubscriptionModel;
import com.acepay.Model.NotificationModel;
import com.acepay.Model.ServiceModel;
import com.acepay.TransationSaction.TransationBitcoinType;
import com.acepay.TransationSaction.TransationINRtype;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Util.RetrofitUPI;
import com.acepay.Util.UtilClass;
import com.acepay.upi.IRetrofit;
import com.acepay.upi.ServiceGenerator;
import com.acepay.upi.UpiScan;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Drawer implements View.OnClickListener, RetrofitResponse
{
    ImageView ivarrow,ivPremium,ivUpi;
    Bitmap bitmap;
    boolean doubleBackToExitPressedOnce = false;
//    String vibrate = "", sound = "";
    Typeface tfArchitectsDaughter;
    static public int flag = 0;
    NumberFormat formatter;
    TextView txName, txSelling,tvCount, txBuy, tvBuyrate, txSellRate, tvBitcoin, tvINR, tvwallet, currentsymbol;
    LinearLayout lnBidding, lnINR, lnAsk, lntransaction, lnDeposit, lnFlight, lnPan,lnRateChart, lnMovies, lnMobileRecharge, lnTransfer, lnReceiver, lnBank, lnRece, lnSend;
    Handler handler;
    Timer timer;
    RelativeLayout rlCountt;
    String key="0";
    LinearLayout ll_bottom, lnrefresh;
    private IntentIntegrator qrScan;
    String SendUpiAmount="";
    String SendUpiValue="";
    String sellPrice="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        setContentView(R.layout.dashboard);
//        if(PreferenceFile.getInstance().getPreferenceData(this,"biddingamount")==null)
//        {
          //  PreferenceFile.getInstance().saveData(this,"biddingamount","0");
//        }
     //   Log.e("biddingamount", String.valueOf(Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Dashboard.this, "biddingamount"))));
//        PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_TYPE,"Saving Account");
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        ivPremium = (ImageView) findViewById(R.id.ivPremium);
        ivUpi = (ImageView) findViewById(R.id.ivUpi);
        txName = (TextView) findViewById(R.id.txName);
        lnBidding=(LinearLayout) findViewById(R.id.llbid);
        lnAsk=(LinearLayout) findViewById(R.id.llask);
        //ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        rlCountt = (RelativeLayout) findViewById(R.id.rlCountt);
        txBuy = (TextView) findViewById(R.id.txBuy);

        tvCount = (TextView) findViewById(R.id.tvCount);
        lnrefresh = (LinearLayout) findViewById(R.id.lnrefresh);
//        lnrefresh = (ConstraintLayout) findViewById(R.id.lnrefresh);
        tvBuyrate = (TextView) findViewById(R.id.tvBuyrate);

        txSellRate = (TextView) findViewById(R.id.txSellRate);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        tvINR = (TextView) findViewById(R.id.tvINR);

        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        txSelling = (TextView) findViewById(R.id.txSelling);
        lnSend = (LinearLayout) findViewById(R.id.lnSend);

        lnBank = (LinearLayout) findViewById(R.id.lnBank);
        lnDeposit = (LinearLayout) findViewById(R.id.lnDeposit);
        lntransaction = (LinearLayout) findViewById(R.id.lntransaction);

        lnTransfer = (LinearLayout) findViewById(R.id.lnTransfer);
        lnINR = (LinearLayout) findViewById(R.id.lnINR);
        lnReceiver = (LinearLayout) findViewById(R.id.lnReceiver);

        lnRece = (LinearLayout) findViewById(R.id.lnRece);
        lnRateChart = (LinearLayout) findViewById(R.id.lnRateChart);
//        lnSettings = (LinearLayout) findViewById(R.id.lnSettings);
//
//        lnSettings.setOnClickListener(this);
        lnRateChart.setOnClickListener(this);
        ivPremium.setOnClickListener(this);
        ivUpi.setOnClickListener(this);
        lnBidding.setOnClickListener(this);
        lnAsk.setOnClickListener(this);
       // ll_bottom.setOnClickListener(this);
        ivarrow.setVisibility(View.GONE);
        flag = 0;

        rlMain.setVisibility(View.GONE);
        handler=new Handler();
        timer = new Timer();

        qrScan = new IntentIntegrator(this);


        if ((2.9671177849035E11)> (602000.0))
        {
            Log.e("resuttt ","gretaer");
        }
        else if ((2.9671177849035E11)<( 602000.0))
        {
            Log.e("resuttt ","less");
        }
        if ((2.9671177849035E11)==( 602000.0))
        {
            Log.e("resuttt ","equal");
            Log.e("resutttValuee ","equal");
        }
        startService(new Intent(Dashboard.this, BackGroundService.class));

        callService();

        if(PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.NORMAL_DEP_AMT)!=null)
        {
            Log.e("InsideNORMAL_DEP_AMT ","inside");

            final TimerTask doAsynchronousTask1 = new TimerTask()
            {
                @Override
                public void run()
                {
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            checkDepositStatus();
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask1, 0, 10000);
        }
        else
        {

        }

        lntransaction.setOnClickListener(this);
        lnBank.setOnClickListener(this);
        lnINR.setOnClickListener(this);

        lnSend.setOnClickListener(this);
        lnDeposit.setOnClickListener(this);

        lnTransfer.setOnClickListener(this);
        txSelling.setOnClickListener(this);
        txBuy.setOnClickListener(this);
        lnReceiver.setOnClickListener(this);
        lnRece.setOnClickListener(this);
        lnrefresh.setOnClickListener(this);

//       formatter = new DecimalFormat("#,##,###");


        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(Dashboard.this,Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        Double bit = Double.parseDouble(PreferenceFile.getInstance().
                getPreferenceData(this, Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        String symbol = UtilClass.getCurrencySymbol(PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Code));

        PreferenceFile.getInstance().saveData(this, Constant.Currency_Symbol, symbol);
        Log.e("symbol-->", symbol);

        //Double val = 1.0;

        if (bit > 0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        Double inr = Double.parseDouble(PreferenceFile.getInstance().
                getPreferenceData(this, Constant.Inr_Amount));

        BigDecimal d = new BigDecimal(inr);
        Log.e("newcal-->", "d -->" + d);

        if (inr > 0)
        {  Log.e("newcal-->", "d if-->" + d);

            tvwallet.setText(" "+String.format("%.0f", d)+" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        } else {
            tvwallet.setText(" 0 "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        txName.setText("Dashboard");

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice1, new IntentFilter("refresh_wallet_balance"));

    }

    private File profileIMG;

    public void popup()
    {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null)
        {
            try {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                profileIMG = new File(dir, Long.toString(System.currentTimeMillis()) + ".jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileIMG));
                startActivityForResult(cameraIntent, 33);
                saveImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private BroadcastReceiver onNotice = new BroadcastReceiver()
    {

    @Override
    public void onReceive(Context context, Intent intent)
    {
    String buy = intent.getStringExtra("buy");
    String sell = intent.getStringExtra("sell");

   Log.e("dashboard>>>", buy + "sender id-->" + sell);

   double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.BTC_amount));

   BigDecimal d = new BigDecimal(calcul);

   tvBuyrate.setText( formatter.format(Double.valueOf(buy)) +" "+PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.Currency_Symbol)+" ");
   txSellRate.setText(" "+formatter.format(Double.valueOf(sell))+" "+PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.Currency_Symbol));

   PreferenceFile.getInstance().saveData(Dashboard.this, Constant.BUYRATE, tvBuyrate.getText().toString());
   PreferenceFile.getInstance().saveData(Dashboard.this, Constant.SELLRATE, txSellRate.getText().toString());
   Double buy_rate = Double.parseDouble(String.valueOf(d)) * Double.parseDouble(buy);

   if (buy_rate > 0)
   {
   tvINR.setText(" "+formatter.format(buy_rate)+" "+PreferenceFile.getInstance().getPreferenceData(Dashboard.this,
   Constant.Currency_Symbol));
   PreferenceFile.getInstance().saveData(Dashboard.this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
  }
   else
  {
  tvINR.setText("0.00 "+" "+PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.Currency_Symbol));
  PreferenceFile.getInstance().saveData(Dashboard.this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
  }
  }
  };

    private BroadcastReceiver onNotice1 = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            callStateService();
        }
    };

    private void callStateService() {
        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Dashboard.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh +
                    PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void checkDepositStatus()
    {

        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, Dashboard.this, Constant.REQ_CHECK_DEPOSIT_STATUS,
                        Constant.CHECK_DEPOSIT_STATUS+"/"+PreferenceFile.getInstance().getPreferenceData(getApplication(), Constant.ID))
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

    private void callService()
    {
        if (Constant.isConnectingToInternet(this))
        {
            new Retrofit2(this, Dashboard.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else
        {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void callCHeckPremiumUser()
    {
        if (Constant.isConnectingToInternet(this))
        {
            try {
                new Retrofit2(this, Dashboard.this, Constant.REQ_CHECK_PREMIUM_USERS,
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

    public void getNotiifcationCount()
    {
        if (Constant.isConnectingToInternet(this)) {
        //yaha change for loading
            //  new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_AddedAddresslist,Constant.AddedAddresslist+"56").callService(true);
            new Retrofit2(this, Dashboard.this, Constant.REQ_USER_NOTIFICATION,
                    Constant.USER_NOTIFICATION+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(false);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try
        {
             if (requestCode==12) {

//            Log.e("DashboardData request", requestCode + "");
//            Log.e("DashboardData result", resultCode + "");
//            Log.e("DashboardData dataValue", data.getData() + "");

            if (data != null)
            {

                if (data == null) {
                    Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.e("UPiDataTry---", data.getData() + "");
                        if (data.getStringExtra("ValueUpi") != null) {
                            String receiveraddress = data.getStringExtra("ValueUpi");
                            Log.e("ValueUpiDashboard--", receiveraddress + "");

                            String resultUpi = "";

                            resultUpi=receiveraddress.substring(receiveraddress.indexOf('=')+1,
                                    receiveraddress.indexOf("&"));

                            Log.e("resultUpiValue ",resultUpi);

                            alertDialogPay(resultUpi,"Nexgen","abc@gmail.com");
                        }  else {
                            Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Log.e("UPiDataCatch---", "Catchhh");
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null)
                {
                    //if qrcode has nothing in it
                    if (result.getContents() == null) {
                        Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject obj = new JSONObject(result.getContents());

                            Log.e("result--->", obj.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException--->", e.toString());
                            //if control comes here
                            //that means the encoded format not matches
                            //in this case you can display whatever data is available on the qrcode
                            //to a toast
                            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                        }

                        if (result.getContents() != null) {

                            if (result.getContents().contains("address")) {

                                String CurrentString = result.getContents();
                                String[] separated = CurrentString.split("address");
                                String address = separated[1];
                                String[] newAddress = address.split("Phone");
                                String properaddress = newAddress[0];
                                //  edAddress.setText(properaddress);

                                String newPhone = newAddress[1];

                                Log.e("newAddress-->", address);
                                Log.e("phone-->", newPhone);

                                String[] phn = newPhone.split("name");

//                        edNumber.setText(phn[0]);
//                        edname.setText(phn[1]);


                                Log.e("number split-->", address);

                            } else {
                                Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
                            }
                        }
                        Log.e("JSONException--->", result.getContents());

                    }
                }
                else
                {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private static final int BARCODE = 12;
     int amount=0;
     Dialog upiDialog;
    public void alertDialogPay(final String addr, String name, String number) {
//        upiDialog = new Dialog(Dashboard.this, R.style.StatisticsDialog);
//
//        upiDialog.setCancelable(false);
//        LayoutInflater li = LayoutInflater.from(this);
//        View promptsView2 = li.inflate(R.layout.sendtoupi, null);
//        upiDialog.setContentView(promptsView2);
//        upiDialog.setCanceledOnTouchOutside(true);
//        upiDialog.show();
//
//        TextView tvName,tvSendUPI;
//        final EditText etAmount;
//        ImageView ivCross;
//
//        tvName = (TextView) promptsView2.findViewById(R.id.tvName);
//        tvSendUPI = (TextView) promptsView2.findViewById(R.id.tvSendUPI);
//        etAmount = (EditText) promptsView2.findViewById(R.id.etAmount);
//        ivCross = (ImageView) promptsView2.findViewById(R.id.ivCross);
//
//        tvName.setText(addr);
//
//        ivCross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upiDialog.cancel();
//            }
//        });
//
// //       amount=Integer.parseInt(etAmount.getText().toString().trim());
//
//        tvSendUPI.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//        //       Log.e("AmountValue--",amount+"");
//
//               Log.e("ClickSendButton","Clickk");
//
//               if (etAmount.getText().toString().trim().isEmpty())
//               {
//                   Constant.alertDialog(Dashboard.this,"Enter amount to send");
//                 //  Toast.makeText(Dashboard.this, "Enter amount to send", Toast.LENGTH_SHORT).show();
//
//               }
//               else if (Integer.parseInt(etAmount.getText().toString().trim())< 100)
//               {
//                   Constant.alertDialog(Dashboard.this,"Amount must be  at least 100");
//                //   Toast.makeText(Dashboard.this, "Amount must be  at least 100", Toast.LENGTH_SHORT).show();
//               }
//               else
//               {
//                   SendUpiAmount=etAmount.getText().toString().trim();
//                   SendUpiValue=addr;
//                   Log.e("amount",SendUpiAmount);
//                   Log.e("address",SendUpiValue);
//                   callNetworkFee();
//
////                   callPayOutApi(addr,etAmount.getText().toString().trim(),"Nexgen",
////                           "8699202650");
//
//               }
//           }
//       });
        Intent newintent=new Intent(Dashboard.this,UpiPayment.class);
        newintent.putExtra("addr",addr);
        newintent.putExtra("sellPrice",sellPrice.trim());
        startActivity(newintent);

    }






    public void callSecurePin(String amount,String upi,String name,String number)
    {

        Log.e("BTCConvertedValue--",BTCConvertedValue);

        Intent intent1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent1 = new Intent(Dashboard.this, CheckSecurePin.class);
        }
        intent1.putExtra("key", "sell");
        intent1.putExtra("type", "upi");
        intent1.putExtra("amount",amount);
        intent1.putExtra("upi",upi);
        intent1.putExtra("name",name);
        intent1.putExtra("number",number);
        intent1.putExtra("sellPrice",sellPrice);
        intent1.putExtra("charge",charges.toString());
        intent1.putExtra("gst",gst.toString());
        intent1.putExtra("fee",total.toString());
        intent1.putExtra("btcConverted",BTCConvertedValue);
//      intent1.putExtra("btcConverted","1.81521147");
        startActivity(intent1);

    }

//    public void callPayOutApi(String upi,String amount,String name,String phone)
//    {
//
//
//     Random r = new Random();
//     int   transaction = (100000 + r.nextInt(1000000));
//     Log.e("TransactionID",transaction+"");
//
//
//        try {
//
//            JsonObject jsonObject=new JsonObject();
//            jsonObject.addProperty("AccountNo",upi);
//            jsonObject.addProperty("AmountR",amount);
//            jsonObject.addProperty("SenderMobile",PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
//            jsonObject.addProperty("SenderName",PreferenceFile.getInstance().getPreferenceData(this, Constant.Username));
//            jsonObject.addProperty("SenderEmail",PreferenceFile.getInstance().getPreferenceData(this, Constant.Email));
//
//
//
////            jsonObject.addProperty("UserID","11577");
////            jsonObject.addProperty("Token","0f1ab4d41ee4e308f46ba3d2b8817e75");
////            jsonObject.addProperty("OutletID","31467");
////
////            JsonObject object=new JsonObject();
////
////            object.addProperty("AccountNo",upi);
////            object.addProperty("AmountR",amount);
////            object.addProperty("BankID","2546");
////            object.addProperty("IFSC","");
////            object.addProperty("SenderMobile",PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
////            object.addProperty("SenderName",PreferenceFile.getInstance().getPreferenceData(this, Constant.Username));
////            object.addProperty("SenderEmail",PreferenceFile.getInstance().getPreferenceData(this, Constant.Email));
////            object.addProperty("BeneName",name);
////            object.addProperty("BeneMobile",phone);
////            object.addProperty("APIRequestID",transaction);
////            object.addProperty("SPKey","UPI");
////
////            jsonObject.add("PayoutRequest",object);
//
//
//            Log.e("postparamUPI--->", jsonObject.toString());
//
//            if (Constant.isConnectingToInternet(Dashboard.this)) {
//                new RetrofitUPI(Dashboard.this, Dashboard.this, jsonObject,
//                        Constant.REQ_UPI_PAYOUT_API,
//                        Constant.UPI_PAYOUT_API, "7").callService(true);
//            }
//            else {
//                Constant.alertDialog(Dashboard.this, getResources().getString(R.string.check_connection));
//            }
//
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        Intent intent;

        switch (v.getId()) {

            case R.id.lnRateChart:
//                intent = new Intent(Dashboard.this, RateChart.class);
//                intent = new Intent(Dashboard.this, DashboardActivities.class);
//                startActivity(intent);

//                intent = new Intent(Dashboard.this, BitcoinAddressAddedList.class);
//                intent.putExtra("key","drawer");
//                startActivity(intent);
                Double bit = Double.parseDouble(PreferenceFile.getInstance().
                        getPreferenceData(this, Constant.Inr_Amount));
                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                //getting value
                Log.e("upi click","bete");
                Log.e("btcamount ",finacal+"");


                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null) {

                    Constant.alertDialog(this, "Wait for the verification");
                }
                else
                {
                    if (finacal.equals(0.0))
                    {
                        Constant.alertDialog(this, "Insufficient Balance");
                    }
                    else
                    {
                        Intent i=new Intent(this, UpiScan.class);
                        startActivityForResult(i,BARCODE);
                    }
                }

                break;

                case R.id.ivPremium:
                    Log.e("ListSizeClick ",list.size()+"");
                    showMySubscriptions();

                break;

             case R.id.ivUpi:

//                 Double bit = Double.parseDouble(PreferenceFile.getInstance().
//                         getPreferenceData(this, Constant.BTC_amount));
//                 Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
//
//                 Log.e("btcamount ",finacal+"");
//
//
//                 if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null) {
//
//                     Constant.alertDialog(this, "Wait for the verification");
//                 }
//                 else
//                 {
//                     if (finacal.equals(0.0))
//                     {
//                         Constant.alertDialog(this, "Insufficient Balance");
//                     }
//                     else
//                     {
//                         Intent i=new Intent(this, UpiScan.class);
//                         startActivityForResult(i,BARCODE);
//                     }
//                 }

                 break;

//                case R.id.ll_bottom:
//                    Log.e("serviceList ",serviceList.size()+"");
//                    showExtraServices();
//                break;

//                case R.id.lnSettings:
//
//                    intent = new Intent(Dashboard.this, NotificationPage.class);
//                    startActivity(intent);
//
//                break;

                case R.id.lnrefresh:

                callService();

                break;

            case R.id.lnReceiver:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null)
                {
                    Constant.alertDialog(this, "Wait for the verification");
                }
                else
                {
                    intent = new Intent(Dashboard.this, BitcoinAddress.class);
                    intent.putExtra("key", "receive");
                    startActivity(intent);
                }

                break;

            case R.id.lnRece:
            {
                intent = new Intent(Dashboard.this, ReceiveAmount.class);
                startActivity(intent);
            }
            break;

            case R.id.lntransaction:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null)
                {

                    Constant.alertDialog(this, "Wait for the verification");
                }
                else {
                    intent = new Intent(Dashboard.this, TransationBitcoinType.class);
                    intent.putExtra("key", "btc");
                    startActivity(intent);
                }
                break;

            case R.id.lnINR:
                intent = new Intent(Dashboard.this, TransationINRtype.class);
                intent.putExtra("key", "inr");
                startActivity(intent);
                break;

            case R.id.lnDeposit:
                intent = new Intent(Dashboard.this, DepositTransaction.class);
                startActivity(intent);
                break;

            case R.id.lnTransfer:

                if (isRunning.equalsIgnoreCase("1"))
                {
                    showPopUp("restriction");
                }
                else
                {
                    intent = new Intent(Dashboard.this, MoneyTranfer.class);
                    startActivity(intent);
                }

                break;

            case R.id.lnBank:

                if (PreferenceFile.getInstance().getPreferenceData
                        (this, Constant.VERIFY_BANK).equals("Pending") ||
                        PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_PAN).equals("Pending")
                        || PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("Pending")) {

                    Constant.alertDialog(this, "Please verify your account.");
                } else {


                    intent = new Intent(Dashboard.this, Withdrow.class);
                    startActivity(intent);
                }

                break;

            case R.id.lnSend:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null) {

                    Constant.alertDialog(this, "Wait for the verification");
                } else {

                    if (isRunning.equalsIgnoreCase("1"))
                    {
                        showPopUp("restriction");
                    }
                    else {
                        intent = new Intent(Dashboard.this, SendBitcoin.class);
                        startActivity(intent);
                    }


                }

                break;

            case R.id.txSelling:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null) {

                    Constant.alertDialog(this, "Wait for the verification");
                } else {

                    txSelling.setBackground(getResources().getDrawable(R.drawable.sell_dashboard_active_blue_bg));
                    txSelling.setTextColor(getResources().getColor(R.color.white));
                    txBuy.setBackground(getResources().getDrawable(R.drawable.buy_dashboard_inactivebg));
                    txBuy.setTextColor(getResources().getColor(R.color.text_grey_color));


                    intent = new Intent(Dashboard.this, SellBitcoin.class);
                    startActivity(intent);
                }

                break;

            case R.id.txBuy:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) == null) {

                    Constant.alertDialog(this, " Wait for the verification");
                } else {

                    txBuy.setBackground(getResources().getDrawable(R.drawable.blue_active_left_corner));
                    txBuy.setTextColor(getResources().getColor(R.color.white));
                    txSelling.setBackground(getResources().getDrawable(R.drawable.sell_dashboard_inactivebg));
                    txSelling.setTextColor(getResources().getColor(R.color.text_grey_color));


                    intent = new Intent(Dashboard.this, BuyBitcoin.class);
                    startActivity(intent);
                }

                break;
            case R.id.llbid:
                intent = new Intent(Dashboard.this,Biding.class);
                startActivity(intent);
                break;
            case R.id.llask:
                intent = new Intent(Dashboard.this, Ask.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        /*super.onBackPressed();
        finishAffinity();*/
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }



    ArrayList<FeesChargeModet> NetFeeslist = new ArrayList<>();

    Double price=0.00;
    Double charges=0.00;
    Double finacals,bit;
    Double gst=0.00;
    Double min=0.00,max=0.00;
    Double buy_rate;
    Double total=0.00;

    String BTCConvertedValue="";


    public void setCovertedValue(String setAmount)
    {
        if(setAmount.length()>0) {

            if((!setAmount.equals(".")) || (!setAmount.equals(""))) {

                if (!PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.BTC_amount).equals(null) ||
                        (!PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.BTC_amount).equals("")))
                {

                    for (int x=0;x<NetFeeslist.size();x++)
                    {
                        if(Double.parseDouble(setAmount)
                                >= Double.parseDouble(NetFeeslist.get(x).getFrom().trim())
                                && Double.parseDouble(setAmount)
                                <= Double.parseDouble(NetFeeslist.get(x).getTo().trim())){

                            charges=Double.parseDouble(NetFeeslist.get(x).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(x).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;

                            Log.e("gstDashboard",gst+" changes "+charges+" total "+total+"sellPrice "+getSellPrice);
                            double calcul = ((Double.parseDouble(setAmount)) / getSellPrice);

                            BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);

                            break;

                        }

                        else if(Double.parseDouble(setAmount) >=
                                Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getFrom().trim())){
                            charges=Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;

                            Log.e("gst",gst+" changes "+charges+" total "+total+"GetSellPrice "+getSellPrice);
                            double calcul = ((Double.parseDouble(setAmount)) / getSellPrice);

                           BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);
                            break;

                        }
                        else {
                            double calcul = Double.parseDouble(setAmount) / getSellPrice;

                            BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);

                            charges=Double.parseDouble(NetFeeslist.get(x).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(x).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;

                        }
                    }

                    if(NetFeeslist.size()<0) {


                        double calcul = Double.parseDouble(setAmount) / getSellPrice;

                        BTCConvertedValue=(String.format("%.8f", calcul));
                        Log.e("BTCConvertedValue-",BTCConvertedValue);
                    }
                }
            }

        }

        else {
            BTCConvertedValue="0.00";
            Log.e("BTCConvertedValue-",BTCConvertedValue);
        }

        callSecurePin(SendUpiAmount,SendUpiValue,"Nexgen","869920265");

    }

    Double getSellPrice=0.00;



    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        super.onServiceResponse(requestCode, response);

        switch (requestCode)
        {

//            case Constant.REQ_NET_FEES:
//                Log.e("netFees","done");
//                if (response.isSuccessful())
//                {
//                    try {
//                        JSONObject result = new JSONObject(response.body().string());
//                        Log.e("result-->", result.toString());
//                        String status = result.getString("response");
//                        String message = result.getString("message");
//                        Log.e("statusfgsta", status);
//
//                        if (status.equals("true"))
//                        {
//                            NetFeeslist.clear();
//                            Log.e("status", status);
//                            JSONArray mydata = result.getJSONArray("data");
//
//                            for (int i = 0; i < mydata.length(); i++)
//                            {
//                                Log.e("status", status);
//                                JSONObject myobj = mydata.getJSONObject(i);
//
//                                FeesChargeModet model = new FeesChargeModet();
//                                model.setFees(myobj.getString("net_fee"));
//                                model.setFrom(myobj.getString("from_amount"));
//                                model.setTo((myobj.getString("to_amount")));
//                                model.setGst((myobj.getString("gst")));
//
//                                NetFeeslist.add(model);
//                                Log.e("njsc", "" + NetFeeslist.size());
//                            }
//
//                            Log.e("listsize-->",NetFeeslist.size()+"");
//
//
//                            setCovertedValue(SendUpiAmount);
////                            callSecurePin(SendUpiAmount,SendUpiValue,"Nexgen","869920265");
//
//
//                        }
//                        else
//                        {
//                            Constant.alertDialog(this, message);
//                        }
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    catch (IOException e) {
//                            e.printStackTrace();
//                    }
//                }
//                else
//                {
//                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
//                }
//
//                break;


            case Constant.REQ_CHECK_DEPOSIT_STATUS:

                if (response.isSuccessful())
                {
                    try
                    {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("result", result1.toString());
                        String data = result1.getString("data");
                        String message = result1.getString("message");
                        String amount = result1.getString("amount");
                        String response1 = result1.getString("response");

                        if (response1.equals("true"))
                        {
                            if (data.equalsIgnoreCase("no"))
                            {//pending
                                PreferenceFile.getInstance().saveData(Dashboard.this, Constant.NORMAL_DEP_AMT, amount);
                            }
                            else if (data.equalsIgnoreCase("Yes"))
                            {//Approved
                                PreferenceFile.getInstance().saveData(Dashboard.this, Constant.NORMAL_DEP_AMT, null);
                                timer.cancel();
                            } else if (data.equalsIgnoreCase("Rejected"))
                            {//Rejected
                                PreferenceFile.getInstance().saveData(Dashboard.this, Constant.NORMAL_DEP_AMT, null);
                                timer.cancel();
                            }
                        }
                        else
                        {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;


            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    callCHeckPremiumUser();

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")) {

                            // PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"0");
                            JSONObject result = result1.getJSONObject("data");

                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                            PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                            PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                            Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                            Double inr = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));

                            BigDecimal ff = new BigDecimal(inr);

                            tvwallet.setText(formatter.format(ff) + " " +
                                    PreferenceFile.getInstance().getPreferenceData(Dashboard.this, Constant.Currency_Symbol));
                            // tvwallet.setText(String.format(formatter, ff)+" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            // tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            if (bit > 0)
                            {
                                tvBitcoin.setText(String.format("%.8f", finacal));
                            } else {
                                tvBitcoin.setText("0.00000000");
                            }

                            if (!result.getString("first_name").equals("null"))
                            {
                                PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name") + " " + result.getString("last_name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                PreferenceFile.getInstance().saveData(this, Constant.email_verification, result.getString("verification"));
                                PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));
                                PreferenceFile.getInstance().saveData(this, Constant.City_name, result.getString("city"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_PAN, result.getString("verify_pan"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_BANK, result.getString("verify_bank"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_Adhaar, result.getString("verify_add"));

                                if (!result.isNull("block_address"))
                                {
                                    PreferenceFile.getInstance().saveData(this, Constant.BITCOIN_ADDRESS, result.getString("block_address"));
                                }

                                JSONObject StateName = result.getJSONObject("StateName");
                                PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));

                                JSONObject CountryName = result.getJSONObject("CountryName");
                                PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                if (!result.isNull("BankName"))
                                {

                                    JSONObject BankName = result.getJSONObject("BankName");
                                    PreferenceFile.getInstance().saveData(this, Constant.BANK_NAME, BankName.getString("bank_name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_TYPE, BankName.getString("account_type"));
                                    PreferenceFile.getInstance().saveData(this, Constant.BRANCH, BankName.getString("branch"));
                                    PreferenceFile.getInstance().saveData(this, Constant.PASSBOOK_IMAGE, BankName.getString("passbook_image"));
                                    PreferenceFile.getInstance().saveData(this, Constant.IFSC, BankName.getString("ifsc"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_HOLDER, BankName.getString("account_holder"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_NUMBER, BankName.getString("account_number"));
                                }

                                if (!result.isNull("AddName")) {

                                    JSONObject AddName = result.getJSONObject("AddName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_image, AddName.getString("aadhar"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_image_back, AddName.getString("aadhar_back"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_number, AddName.getString("aadhar_number"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_line1, AddName.getString("line1"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_line2, AddName.getString("line2"));
                                    PreferenceFile.getInstance().saveData(this, Constant.LANDMARK, AddName.getString("landmark"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_city, AddName.getString("city"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_state, AddName.getString("state"));

                                    JSONObject state = AddName.getJSONObject("StateName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_state, state.getString("name"));

                                }
                                if (!result.isNull("PanName")) {

                                    JSONObject PanName = result.getJSONObject("PanName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_name, PanName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_last, PanName.getString("last_name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_image, PanName.getString("image"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_number, PanName.getString("pan_number"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_dob, PanName.getString("dob"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_gender, PanName.getString("gender"));
                                }
                            }

                            // PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                        } else {
                            Constant.alertWithIntent(this, "Account Blocked", BlockScreen.class);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                break;

            case Constant.REQ_CHECK_PREMIUM_USERS:

                try {
                    if (response.isSuccessful()) {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("REQ_CHECK_PREMIUM_USERS", result.toString());

                        String status = result.getString("response");
                        getNotiifcationCount();

                        if (status.equals("true"))
                        {
                            list.clear();

                            JSONArray data=result.getJSONArray("data");
                            if (data.length()>0)
                            {
                                for (int i = 0; i <data.length() ; i++)
                                {
                                    JSONObject jsonObject=data.getJSONObject(i);
                                    MySubscriptionModel mySubscriptionModel=new MySubscriptionModel();

                                    mySubscriptionModel.setSubscription_id(jsonObject.getString("id"));
                                    mySubscriptionModel.setCompetition_id(jsonObject.getString("competition_id"));
                                    mySubscriptionModel.setPlan_id(jsonObject.getString("plan_id"));

                                    JSONObject Plans=jsonObject.getJSONObject("Plans");

                                    mySubscriptionModel.setPlan_name(Plans.getString("name"));
                                    mySubscriptionModel.setFees(Plans.getString("entry_fee"));
                                    mySubscriptionModel.setPlanBenefits(Plans.getString("benefits"));
                                    mySubscriptionModel.setPlanDesc(Plans.getString("description"));

                                    JSONObject CompetitionLists=jsonObject.getJSONObject("CompetitionLists");

                                    mySubscriptionModel.setCompetition_name(CompetitionLists.getString("name"));
                                    mySubscriptionModel.setStart_date(CompetitionLists.getString("start_date"));
                                    mySubscriptionModel.setStart_time(CompetitionLists.getString("start_time"));
                                    mySubscriptionModel.setEnd_date(CompetitionLists.getString("end_date"));
                                    mySubscriptionModel.setEnd_time(CompetitionLists.getString("end_time"));


                                    String input_date="",input_time="";
                                    input_date=CompetitionLists.getString("start_date");
                                    input_time=CompetitionLists.getString("start_time");
                                    String finalDay="",dateee="";
                                    String currentDate="";
                                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        Date dt1=format1.parse(input_date);
                                        DateFormat format2=new SimpleDateFormat("EEEE");
                                        DateFormat formatSd=new SimpleDateFormat("dd-MM-yyyy");
                                        //todo for day
                                        finalDay=format2.format(dt1);

                                        //todo for date
                                        dateee=formatSd.format(dt1);

                                        currentDate= UtilClass.getCurrentDate();
                                        Log.e("finalStartDate ",currentDate);
                                    }catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                    String timeeeGet="",currentTime="",currentTimeformat="";

                                    currentTime=UtilClass.getCurrentTime();
                                    Log.e("currentTime ",currentTime+"");

                                    String current12="";
                                    try
                                    {
                                        Date cccdd=null;

                                        SimpleDateFormat input=new SimpleDateFormat("hh:mm");
                                        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");

                                        Date dt3bfffh11=input.parse(currentTime);
                                        current12=output.format(dt3bfffh11);
                                        Log.e("current12 ",current12+"");
                                        Log.e("StartTime ",input_time+"");

                                    }catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }

                                    //todo for time
                                    SimpleDateFormat format21=new SimpleDateFormat("hh:mm aa");
                                    try {
                                        Date dt311=format21.parse(input_time);
                                        Date dt311Curr=format21.parse(current12);
                                        DateFormat formatSd=new SimpleDateFormat("HH:mm aa");
                                        timeeeGet=formatSd.format(dt311);
                                        currentTimeformat=formatSd.format(dt311Curr);
                                        Log.e("timeeeGet ",timeeeGet);
                                        Log.e("currentTimeformat ",currentTimeformat);
                                    }catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }


                                    String compareDate="";
                                    compareDate=UtilClass.compareDates(dateee,currentDate);

                                    String compareTime="";
                                    compareTime=UtilClass.compareTiminng24(timeeeGet,currentTimeformat);


                                    Log.e("CompareDate ",compareDate);
                                    Log.e("compareTime ",compareTime);


                                    if (compareDate.equalsIgnoreCase("equal") || compareDate.equalsIgnoreCase("before"))
                                    {
                                        if ( compareDate.equalsIgnoreCase("before"))
                                        {
                                            if (compareTime.equalsIgnoreCase("after"))
                                            {
                                               isRunning="1";
                                            }
                                        }
                                        else
                                        {
                                            if (compareTime.equalsIgnoreCase("equal") || compareTime.equalsIgnoreCase("before"))
                                            {
                                                isRunning="1";
                                            }
                                            else
                                            {
                                                isRunning="0";
                                            }
                                        }
                                    }
                                    else
                                    {
                                        isRunning="0";
                                    }







                                    list.add(mySubscriptionModel);
                                }

                                Log.e("ListSize ",list.size()+"");
                            }
                            ivPremium.setVisibility(View.VISIBLE);


                            //todo coming from Buy Subscription then only show this
                            if (key.equalsIgnoreCase("buy"))
                            {
                                showSuccessPopup("You have subscribed Acepay Competiton entry pass.\nYou will receive Tips or news  that " +
                                        "will help you learn how to \ntrade in crypto more efficiently.\nOnce or twice day by day,\nyou will get notifications alert." +
                                        "\nWhich will help you to \nbecome " +
                                        "a winner and get \nmany rewards.\nBest of luck \n \nStay home stay safe \nTrade with Acepay \nBecause there is" +
                                        " no space for losses.");
                            }


                        } else
                            {
                            ivPremium.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;

            case Constant.REQ_UPI_PAYOUT_API:

                try {

                    Log.e("UpiPaymentResponse ", response.toString());

                    if (response.isSuccessful())
                    {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("resultUPII", result.toString());

                        String statuscode = result.getString("statuscode");
                        String message = result.getString("message");
                        Log.e("statuscode", statuscode.toString());
                        Log.e("message", message);

                        if (statuscode.equals("1"))
                        {
                            Log.e("Inside 1", "11");
                          //  Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                            upiDialog.cancel();
                        }
                        else
                        {
                            Log.e("Inside 0", "11");
                           // Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                        }


                    }



                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    upiDialog.cancel();
                }

                break;

            case Constant.REQ_USER_NOTIFICATION:

                try {
                    JSONObject result = new JSONObject(response.body().string());

                    Log.e("NotificationResulr ", result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    notiList.clear();

                    if (status.equals("true")) {

                        JSONArray data = result.getJSONArray("data");
                        if (data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                NotificationModel notificationModel = new NotificationModel();

                                JSONObject jsonObject = data.getJSONObject(i);

                                if (jsonObject.getString("view_status").equalsIgnoreCase("Open"))
                                {
                                    notificationModel.setNotification_id(jsonObject.getString("id"));
                                    notiList.add(notificationModel);
                                }

                              /*
                              if (jsonObject.getString("notification_type").equalsIgnoreCase("Notification"))
                                {
                                    if (jsonObject.getString("view_status").equalsIgnoreCase("Open"))
                                     {
                                        notificationModel.setNotification_id(jsonObject.getString("id"));
                                        notiList.add(notificationModel);
                                     }
                                }*/
                            }

                            if (notiList.size() > 0)
                            {
                                Log.e("NotiListSize ", notiList.size() + "");
                                rlCountt.setVisibility(View.VISIBLE);
                                tvCount.setText(String.valueOf(notiList.size()));

                            } else {
                                rlCountt.setVisibility(View.GONE);
                            }
                        }
                    } else
                        {
//                        Constant.alertWithIntent(this, message, Dashboard.class);
                    }

                }
                catch (JSONException e)
                {
                      e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;

            case Constant.REQ_BTC_RATE:

                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("dashbardres", result.toString());
                        callStateService();

                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));

                            BigDecimal d = new BigDecimal(calcul);

                            sellPrice= String.format("%.2f",Double.valueOf(data.getString("sell")));

                            Double buyprice=Double.parseDouble(data.getString("sell"));
                            getSellPrice =buyprice;

                            Log.e("GetSellPrice ",getSellPrice+"");

                            tvBuyrate.setText(formatter.format(Double.valueOf(data.getString("buy"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " ");
                            txSellRate.setText(formatter.format(Double.valueOf(data.getString("sell"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this, Constant.BUY, data.getString("buy"));
                            PreferenceFile.getInstance().saveData(this, Constant.SELL, data.getString("sell"));
                            PreferenceFile.getInstance().saveData(this, Constant.BUYRATE, tvBuyrate.getText().toString());
                            PreferenceFile.getInstance().saveData(this, Constant.SELLRATE, txSellRate.getText().toString());

                            Double buy_rate = Double.parseDouble(String.valueOf(d)) * Double.parseDouble(data.getString("buy"));

                            // tvINR.setText(String.valueOf(buy_rate));

                            if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                            }


                        } else {
                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY) != null) {
                                double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));

                                BigDecimal d = new BigDecimal(calcul);

                                tvBuyrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " ");
                                txSellRate.setText(" " + formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                PreferenceFile.getInstance().saveData(this, Constant.BUYRATE, tvBuyrate.getText().toString());
                                PreferenceFile.getInstance().saveData(this, Constant.SELLRATE, txSellRate.getText().toString());
                                Double buy_rate = Double.parseDouble(String.valueOf(d)) * Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL));

                                if (buy_rate > 0) {

                                    tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                    PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                                } else {
                                    tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    PreferenceFile.getInstance().saveData(this, Constant.INR_PRICE_BITCOIN, tvINR.getText().toString());
                                }

                            }

                        }

                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void showMySubscriptions()
    {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View promptsView2 = inflater.inflate(R.layout.show_my_subscription, null);

        dialogBuilder.setView(promptsView2);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView tvLabel=(TextView) promptsView2.findViewById(R.id.tvLabel);
        TextView tvNoData=(TextView) promptsView2.findViewById(R.id.tvNoData);
        RecyclerView recyclerSub=(RecyclerView) promptsView2.findViewById(R.id.recyclerSub);
        ImageView ivCross=(ImageView) promptsView2.findViewById(R.id.ivCross);
        /*final Dialog dialog=new Dialog(Dashboard.this, R.style.StatisticsDialog);
        dialog.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(Dashboard.this);
        View promptsView2 = li.inflate(R.layout.show_my_subscription, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        */


        if (list.size()>0)
        {
            tvNoData.setVisibility(View.GONE);
            recyclerSub.setVisibility(View.VISIBLE);
            recyclerSub.setLayoutManager(new LinearLayoutManager(Dashboard.this));
            CustomMySubscriptionsAdapter customMySubscriptionsAdapter=new CustomMySubscriptionsAdapter(Dashboard.this,list);
            recyclerSub.setAdapter(customMySubscriptionsAdapter);
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerSub.setVisibility(View.GONE);
        }

        ivCross.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               alertDialog .dismiss();
            }
        });

        alertDialog.show();
    }

    public void showExtraServices()
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View promptsView2 = inflater.inflate(R.layout.show_extra_servuices, null);

        dialogBuilder.setView(promptsView2);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setLayout(width, height);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView tvLabel=(TextView) promptsView2.findViewById(R.id.tvLabel);
        TextView tvNoData=(TextView) promptsView2.findViewById(R.id.tvNoData);

        RecyclerView recyclerSub=(RecyclerView) promptsView2.findViewById(R.id.recyclerSub);
        ImageView ivCross=(ImageView) promptsView2.findViewById(R.id.ivCross);

        serviceList.clear();

        ServiceModel serviceModel=new ServiceModel("1","Mobile Recharge");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("2","DTH");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("3","Electricity");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("4","Credit Card Bill");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("5","Postpaid");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("6","Donate");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("7","Book A Cylinder");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("8","Broadband");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("9","Landline");
        serviceList.add(serviceModel);

        serviceModel=new ServiceModel("10","Waterbill");
        serviceList.add(serviceModel);

        if (serviceList.size()>0)
        {
            tvNoData.setVisibility(View.GONE);
            recyclerSub.setVisibility(View.VISIBLE);
            recyclerSub.setLayoutManager(new GridLayoutManager(this, 3));
            ServicesAdapter servicesAdapter=new ServicesAdapter(Dashboard.this,serviceList);
            recyclerSub.setAdapter(servicesAdapter);

            servicesAdapter.onItemSelectedListener(new ServicesAdapter.onCLickListner()
            {
                @Override
                public void onItemClick(int layoutPosition, View view)
                {
                    showPopUp("coming");
                }
            });
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerSub.setVisibility(View.GONE);
        }

        ivCross.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog .dismiss();
            }
        });

        alertDialog.show();
    }


    ArrayList<MySubscriptionModel> list=new ArrayList<>();
    ArrayList<ServiceModel> serviceList=new ArrayList<>();

    public void showSuccessPopup(String message)
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View promptsView2 = inflater.inflate(R.layout.congratulations_pop_up, null);

        dialogBuilder.setView(promptsView2);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView ivCross=(ImageView) promptsView2.findViewById(R.id.ivCross);
        TextView tvReason=(TextView) promptsView2.findViewById(R.id.tvReason);
        tvReason.setText(message);

        ivCross.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog .dismiss();
            }
        });
        alertDialog.show();
    }

    String isRunning="0";

    public void showPopUp(String key)
    {
        final Dialog dialog=new Dialog(Dashboard.this, R.style.StatisticsDialog);
        dialog.setTitle("acepay");
        dialog.setCancelable(true);

        LayoutInflater li = LayoutInflater.from(Dashboard.this);
        View promptsView2 = li.inflate(R.layout.deposit_transaction_admin_popup, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(false);
        Button btnokk=(Button)promptsView2.findViewById(R.id.btnokk);
        TextView tvReason=(TextView) promptsView2.findViewById(R.id.tvReason);

        if (key.equalsIgnoreCase("coming"))
        {
            tvReason.setText("Coming Soon");
        }
        else {
            tvReason.setText("You are restricted to do this transaction");
        }



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


    ArrayList<NotificationModel> notiList=new ArrayList<>();
    @Override
    protected void onResume()
    {
        super.onResume();

        if (getIntent().hasExtra("key"))
        {
            key=getIntent().getExtras().getString("key");
            Log.e("Key:--",key);
        }

    }
}
