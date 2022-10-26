package com.acepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Model.AdminBank;
import com.acepay.PaymentSection.DepositTeamCondition;
import com.acepay.PaymentSection.NormalPaymentWay;
import com.acepay.Util.Constant;
import com.acepay.Util.DecimalDigitsInputFilter;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DepositMoney extends ToolabarActivity implements RetrofitResponse {

    TextView tvBitcoin, tvmaxmin,tvNote, tvwallet, tvINR, txBitcoin, tvSellrate, tvsymbol,tvNext;
    public static TextView tvAmount;
    Typeface tfArchitectsDaughter;
    Double finacals, bit;
    Double price;
//    ImageView ivarrow, tvsymbol;
    public static Spinner spBank;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<AdminBank> Bank;
    ArrayList<String> Bank_name;
    String max, min;
    String[] defaultbank = {"Select Bank"};
    DecimalFormat formatter1;
    private NumberFormat formatter;
    String IFSC, brach_name, account_holder_name, bank_name, bank_id, account_number;

    public static String amount,bankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);
        Bank = new ArrayList<>();
        Bank_name = new ArrayList<String>();

//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        spBank = (Spinner) findViewById(R.id.spBank);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
//        currentsymbol = (TextView) findViewById(R.id.currentsymbol);
        tvmaxmin = (TextView) findViewById(R.id.tvmaxmin);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvNext = (TextView) findViewById(R.id.tvNext);
//        tvsymbol = (TextView) findViewById(R.id.tvsymbol);
        txBitcoin = (TextView) findViewById(R.id.txBitcoin);
        tvwallet = (TextView) findViewById(R.id.tvwallet);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(DepositMoney.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        tvAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});


        formatter1 = new DecimalFormat("#0.00");



        tvINR = (TextView) findViewById(R.id.tvINR);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        final Double inr = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));
        if (inr > 0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        } else {
            tvwallet.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        callStateService();

        if (getIntent().getStringExtra("key").equals("paypal")) {

            spBank.setVisibility(View.GONE);
        }

        spBank.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text, defaultbank));
        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (bit > 0) {
            tvBitcoin.setText(String.format("%.8f", finacals));
        } else {
            tvBitcoin.setText("0.00000000");
        }

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.hideKeyboard(DepositMoney.this, v);



                amount=tvAmount.getText().toString();

                bankName=spBank.getSelectedItem().toString();



                if (tvAmount.getText().toString().equals("")) {
                    Constant.alertDialog(DepositMoney.this, "Please enter amount.");
                } else if (Double.parseDouble(tvAmount.getText().toString()) <
                        Double.parseDouble(formatter1.format(Double.valueOf(min))) ||
                        Double.parseDouble(tvAmount.getText().toString()) >
                                Double.parseDouble(formatter1.format(Double.valueOf(max)))) {
                    Constant.alertDialog(DepositMoney.this, "Please enter valid amount.");
                } else if (spBank.getSelectedItem().toString().equals("Select Bank") &&
                        (!getIntent().getStringExtra("key").equals("paypal"))) {
                    Constant.alertDialog(DepositMoney.this, "Please select bank.");
                } else {


                    if (getIntent().getStringExtra("key").equals("paypal")) {
                        Intent intent = new Intent(DepositMoney.this, PaypalPayment.class);
                        intent.putExtra("amount",String.valueOf(formatter1.format(Double.valueOf(tvAmount.getText().toString()))));
                        intent.putExtra("key", "paypal");
                        startActivity(intent);
                    }

                    if (getIntent().getStringExtra("key").equals("normal")) {

                        Intent intent = new Intent(DepositMoney.this, NormalPaymentWay.class);
                        intent.putExtra("key", "normal");
                        intent.putExtra("amount",String.valueOf(formatter1.format(Double.valueOf(tvAmount.getText().toString()))));
                        intent.putExtra("ifsc", IFSC);
                        intent.putExtra("brach_name", brach_name);
                        intent.putExtra("account_holder_name", account_holder_name);
                        intent.putExtra("bank_name", bank_name);
//                            intent.putExtra("bank_name", bank_name);
                        intent.putExtra("bank_id", bank_id);
                        intent.putExtra("account_number", account_number);
                        startActivity(intent);
                    }

                    if (getIntent().getStringExtra("key").equals("payu")) {

                        Intent intent = new Intent(DepositMoney.this, NormalPaymentWay.class);
                        intent.putExtra("amount",String.valueOf(formatter1.format(Double.valueOf(tvAmount.getText().toString()))));
                        intent.putExtra("key", "payu");
                        intent.putExtra("ifsc", IFSC);
                        intent.putExtra("brach_name", brach_name);
                        intent.putExtra("account_holder_name", account_holder_name);
                        intent.putExtra("bank_name", bank_name);
                        intent.putExtra("bank_id", bank_id);
                        intent.putExtra("account_number", account_number);
                        startActivity(intent);
                    }
                }
            }
        });

//        ivarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//
//                Constant.hideKeyboard(DepositMoney.this, v);
//            }
//        });

        txName.setText("Deposit");

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            price = Double.parseDouble(buy);

            txBitcoin.setText(formatter.format(Double.valueOf(buy)) + " " + PreferenceFile.getInstance().getPreferenceData(DepositMoney.this, Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell)) + " " + PreferenceFile.getInstance().getPreferenceData(DepositMoney.this, Constant.Currency_Symbol));

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(DepositMoney.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            if (buy_rate > 0) {

                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(DepositMoney.this, Constant.Currency_Symbol));
            } else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(DepositMoney.this, Constant.Currency_Symbol));
            }

        }
    };


    @Override
    public void onBackPressed() {

        /*if (!doubleBackToExitPressedOnce) {
            finish();
            Log.e("Biding","once");
        }*/

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
//            super.onBackPressed();
            Log.e("Biding", "once");
//            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                finish();
            }
        }, 1000);
    }


    private void BTCRATE() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositMoney.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositMoney.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX +
                    PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositMoney.this, Constant.REQ_AdminBank, Constant.AdminBank).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void setAdapter() {


        Log.e("bank--<", Bank_name.size() + "");
//        spBank.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Bank_name));
        spBank.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text, Bank_name));

        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          //      ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));

                if (spBank.getSelectedItem().equals("Select Bank")) {

                }

                if (!spBank.getSelectedItem().equals("Select Bank")) {

                    bank_id = Bank.get(spBank.getSelectedItemPosition() - 1).getId();
                    bank_name = Bank.get(spBank.getSelectedItemPosition() - 1).getBank_name();
                    account_holder_name = Bank.get(spBank.getSelectedItemPosition() - 1).getAcc_holder_name();
                    brach_name = Bank.get(spBank.getSelectedItemPosition() - 1).getBranch_name();
                    IFSC = Bank.get(spBank.getSelectedItemPosition() - 1).getIfsc_code();
                    account_number = Bank.get(spBank.getSelectedItemPosition() - 1).getAccount_number();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_AdminBank:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    Log.e("responseAdmin-->", result1.toString());

                    callService();


                    Log.e("status-->", status + " message-->" + message);

                    if (status.equals("true")) {

                        Bank.clear();
                        Bank_name.clear();
                        Bank_name.add(0, "Select Bank");

                        JSONArray data = result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);
                            AdminBank country = new AdminBank();
                            country.setId(CountryObj.getString("id"));
                            country.setBank_name(CountryObj.getString("bank_name"));
                            country.setBranch_name(CountryObj.getString("branch_name"));
                            country.setAccount_number(CountryObj.getString("account_number"));
                            country.setAcc_holder_name(CountryObj.getString("acc_holder_name"));
                            country.setIfsc_code(CountryObj.getString("ifsc_code"));
                            Bank_name.add(CountryObj.getString("bank_name"));
                            Bank.add(country);
                        }

                        setAdapter();

                    } else {

                        Constant.alertDialog(this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_MIN_MAX:
                try {

                    JSONObject result1 = new JSONObject(response.body().string());

                    Log.e("MINMAXXX ",result1.toString());

                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    BTCRATE();

                    if(status.equals("true")){

                        JSONArray data=result1.getJSONArray("data");

                        Log.e("ArrayData ",data.toString());
                        Log.e("keyDataaaaa ",getIntent().getStringExtra("key"));


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);


                            if(getIntent().getStringExtra("key").equals("paypal")) {

                                if(PreferenceFile.getInstance().getPreferenceData(this,
                                        Constant.BITCOIN_ADDRESS)!=null) {

                                    if (CountryObj.getString("type").equals("10")) {

                                        Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                        Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                        min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                        max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                        Log.e("REQ_MIN_MAX 1", "onServiceResponse:"+min+ " "+max);

                                        if(CountryObj.has("note")) {
                                            tvNote.setText(CountryObj.getString("note"));
                                        }
                                        tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " "
                                                + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " max: " +
                                                formatter.format(Double.valueOf(max)) + " " +
                                                PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    }

                                }
                                else {

                                    if (CountryObj.getString("type").equals("13")) {

                                        Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                        Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                        min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                        max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                        Log.e("REQ_MIN_MAX 2", "onServiceResponse:"+min+ " "+max);


                                        if(CountryObj.has("note")) {
                                            tvNote.setText(CountryObj.getString("note"));
                                        }
                                        tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min))
                                                + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " max: "
                                                + formatter.format(Double.valueOf(max)) +
                                                " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    }

                                }
                            }

                            else
                                {

                                if(PreferenceFile.getInstance().getPreferenceData(this,
                                        Constant.BITCOIN_ADDRESS)!=null)
                                {
                                    Log.e("insideis ","ifff");

                                    if (CountryObj.getString("type").equals("1")) {

                                        Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                        Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                        min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                        max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                        Log.e("REQ_MIN_MAX 3", "onServiceResponse:"+min+ " "+max);


                                        if(CountryObj.has("note")) {
                                            tvNote.setText(CountryObj.getString("note"));
                                        }
                                        tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " " +
                                                PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)
                                                + " max: " + formatter.format(Double.valueOf(max)) + " " +
                                                PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    }
                                }

                                if (CountryObj.getString("type").equals("13")) {

                                    Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                    Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                    min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                    max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                    Log.e("REQ_MIN_MAX 4", "onServiceResponse:"+min+ " "+max);


                                    if(CountryObj.has("note")) {
                                        tvNote.setText(CountryObj.getString("note"));
                                    }
                                    tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) +" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol) + " max: "
                                            + formatter.format(Double.valueOf(max))+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol) );
                                }
                            }
                        }

                    }else {

                        Constant.alertDialog(this,message);
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
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());



                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            price = data.getDouble("buy");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                        } else {
                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY) != null) {
                                price = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL));
                                // buyrate=Double.valueOf(data.getString("sell"));
                                txBitcoin.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                tvSellrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL));

                                if (buy_rate > 0) {

                                    tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                } else {
                                    tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;
        }
    }
}
