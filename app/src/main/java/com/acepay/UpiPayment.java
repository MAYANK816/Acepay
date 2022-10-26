package com.acepay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acepay.Model.FeesChargeModet;
import com.acepay.Model.MySubscriptionModel;
import com.acepay.Model.NotificationModel;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Util.UtilClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class UpiPayment extends AppCompatActivity implements RetrofitResponse {
    String SendUpiAmount="";
    String SendUpiValue="";
    TextView tvName,tvSendUPI;
    Double getSellPrice=0.00;
    EditText etAmount;
    String sellPrice="";
    ArrayList<FeesChargeModet> NetFeeslist = new ArrayList<>();
    Double charges=0.00;
    Double gst=0.00;
    Double total=0.00;
    String BTCConvertedValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_payment);
        tvName = (TextView) findViewById(R.id.tvName);
        tvSendUPI = (TextView) findViewById(R.id.tvSendUPI);
        etAmount = (EditText) findViewById(R.id.etAmount);
        tvName.setText(getIntent().getStringExtra("addr"));
        tvSendUPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //       Log.e("AmountValue--",amount+"");

                Log.e("ClickSendButton","Clickk");

                if (etAmount.getText().toString().trim().isEmpty())
                {
                    Constant.alertDialog(UpiPayment.this,"Enter amount to send");
                }
                else if (Integer.parseInt(etAmount.getText().toString().trim())< 100)
                {
                    Constant.alertDialog(UpiPayment.this,"Amount must be  at least 100");
                }
                else
                {
                    SendUpiAmount=etAmount.getText().toString().trim();
                    SendUpiValue=getIntent().getStringExtra("addr");
                    Log.e("amount",SendUpiAmount);
                    Log.e("address",SendUpiValue);
                    Log.e("callingnet", "onClick: " );
                    callNetworkFee();
//                   callPayOutApi(addr,etAmount.getText().toString().trim(),"Nexgen",
//                           "8699202650");
                }
            }
        });
    }
    public void callNetworkFee()
    {
        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once1");
            new Retrofit2(this, UpiPayment.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"1").callService(false);
            //   new Retrofit2(this, MyTickets.this, Constant.REQ_MYTICKET,PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void setCovertedValue(String setAmount)
    {
        if(setAmount.length()>0) {

            if((!setAmount.equals(".")) || (!setAmount.equals(""))) {

                if (!PreferenceFile.getInstance().getPreferenceData(UpiPayment.this, Constant.BTC_amount).equals(null) ||
                        (!PreferenceFile.getInstance().getPreferenceData(UpiPayment.this, Constant.BTC_amount).equals("")))
                {

                    for (int x=0;x<NetFeeslist.size();x++)
                    {
                        if(Double.parseDouble(setAmount)
                                >= Double.parseDouble(NetFeeslist.get(x).getFrom().trim())
                                && Double.parseDouble(setAmount)
                                <= Double.parseDouble(NetFeeslist.get(x).getTo().trim())){
                            Log.e("firstifinupi", "setCovertedValue: ");
                            Log.e("netfeeslist",NetFeeslist.toString());
                            charges=Double.parseDouble(NetFeeslist.get(x).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(x).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;
                            Log.e("gstDashboard",gst+" changes "+charges+" total "+total+"sellPrice "+getSellPrice);
                            double calcul = ((Double.parseDouble(setAmount)) / total);
                            BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);

                            break;

                        }

                        else if(Double.parseDouble(setAmount) >=
                                Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getFrom().trim())){
                            Log.e("firstelseifinupi", "setCovertedValue: ");
                            charges=Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(NetFeeslist.size()-1).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;

                            Log.e("gst",gst+" changes "+charges+" total "+total+"GetSellPrice "+getSellPrice);
                            double calcul = ((Double.parseDouble(setAmount)) / total);

                            BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);
                            break;

                        }
                        else {
                            Log.e("elseupi", "setCovertedValue: ");
                            double calcul = Double.parseDouble(setAmount) / total;

                            BTCConvertedValue=(String.format("%.8f", calcul));
                            Log.e("BTCConvertedValue-",BTCConvertedValue);

                            charges=Double.parseDouble(NetFeeslist.get(x).getFees());
                            gst=Double.parseDouble(NetFeeslist.get(x).getGst());
                            total=((charges*gst)/100)+charges;
                            gst=(charges*gst)/100;

                        }
                    }

                    if(NetFeeslist.size()<0) {

                        Log.e("0size", "setCovertedValue:");
                        double calcul = Double.parseDouble(setAmount) / total;

                        BTCConvertedValue=(String.format("%.8f", calcul));
                        Log.e("BTCConvertedValue-",BTCConvertedValue);
                    }
                }
            }

        }

        else {
            Log.e("upihoch","poch");
            BTCConvertedValue="0.00";
            Log.e("BTCConvertedValue-",BTCConvertedValue);
        }

        callSecurePin(SendUpiAmount,SendUpiValue,"Nexgen","869920265");

    }


   
    public void callSecurePin(String amount,String upi,String name,String number)
    {

        Log.e("BTCConvertedValue--",BTCConvertedValue);

        Intent intent1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent1 = new Intent(UpiPayment.this, CheckSecurePin.class);
        }
        intent1.putExtra("key", "sell");
        intent1.putExtra("type", "upi");
        intent1.putExtra("amount",amount);
        intent1.putExtra("upi",upi);
        intent1.putExtra("name",name);
        intent1.putExtra("number",number);
        intent1.putExtra("sellPrice",getIntent().getStringExtra("sellPrice").trim());
        intent1.putExtra("charge",charges.toString());
        intent1.putExtra("gst",gst.toString());
        intent1.putExtra("fee",total.toString());
        intent1.putExtra("btcConverted",BTCConvertedValue);
//      intent1.putExtra("btcConverted","1.81521147");
        startActivity(intent1);

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {


        switch (requestCode)
        {

            case Constant.REQ_NET_FEES:
                Log.e("netFees","done");
                if (response.isSuccessful())
                {
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        Log.e("statusfgsta", status);

                        if (status.equals("true"))
                        {
                            NetFeeslist.clear();
                            Log.e("status", status);
                            JSONArray mydata = result.getJSONArray("data");

                            for (int i = 0; i < mydata.length(); i++)
                            {
                                Log.e("status", status);
                                JSONObject myobj = mydata.getJSONObject(i);

                                FeesChargeModet model = new FeesChargeModet();
                                model.setFees(myobj.getString("net_fee"));
                                model.setFrom(myobj.getString("from_amount"));
                                model.setTo((myobj.getString("to_amount")));
                                model.setGst((myobj.getString("gst")));

                                NetFeeslist.add(model);
                                Log.e("njsc", "" + NetFeeslist.size());
                            }

                            Log.e("listsize-->",NetFeeslist.size()+"");


                            setCovertedValue(SendUpiAmount);
//                            callSecurePin(SendUpiAmount,SendUpiValue,"Nexgen","869920265");


                        }
                        else
                        {
                            Constant.alertDialog(this, message);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                            e.printStackTrace();
                    }
                }
                else
                {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

        }
    }
}