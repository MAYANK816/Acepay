package com.acepay.QuickHelp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.acepay.Dashboard;
import com.acepay.R;
import com.acepay.ToolabarActivity;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.UtilClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;




public class DashBoardHelp extends ToolabarActivity implements View.OnClickListener {


    private LinearLayout lnrefresh,lnReceiver,lntransaction,lnSend,llBuyRate,llSellRate;
    private LinearLayout lnDeposit,lnBank,lnTransfer,lnRece,lnINR,lnSettings,lnRateChart;
    private TextView txBuy,txSelling,tvBitcoin,tvwallet,txName,tvINR,tvBuyrate,txSellRate;
    private static final String SHOWCASE_ID = "Dashboard help";
    boolean doubleBackToExitPressedOnce = false;
    DecimalFormat formatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_help);

        try {

            lnrefresh = (LinearLayout) findViewById(R.id.lnrefresh);
            lnReceiver = (LinearLayout) findViewById(R.id.lnReceiver);
            lntransaction = (LinearLayout) findViewById(R.id.lntransaction);
            lnSend = (LinearLayout) findViewById(R.id.lnSend);
            llBuyRate = (LinearLayout) findViewById(R.id.llBuyRate);
            llSellRate = (LinearLayout) findViewById(R.id.llSellRate);

            lnDeposit = (LinearLayout) findViewById(R.id.lnDeposit);
            lnBank = (LinearLayout) findViewById(R.id.lnBank);
            lnTransfer = (LinearLayout) findViewById(R.id.lnTransfer);
            lnRece = (LinearLayout) findViewById(R.id.lnRece);
            lnINR = (LinearLayout) findViewById(R.id.lnINR);
            lnSettings = (LinearLayout) findViewById(R.id.lnSettings);
            lnRateChart = (LinearLayout) findViewById(R.id.lnRateChart);
            tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
            tvwallet = (TextView) findViewById(R.id.tvwallet);
            txName = (TextView) findViewById(R.id.txName);
            tvINR = (TextView) findViewById(R.id.tvINR);
            tvBuyrate = (TextView) findViewById(R.id.tvBuyrate);
            txSellRate = (TextView) findViewById(R.id.txSellRate);

            txBuy = (TextView) findViewById(R.id.txBuy);
            txSelling = (TextView) findViewById(R.id.txSelling);
            formatter = new DecimalFormat("#,##,###");

            lnRateChart.setOnClickListener(this);
            lnSettings.setOnClickListener(this);
            lnrefresh.setOnClickListener(this);
            lnReceiver.setOnClickListener(this);
            lntransaction.setOnClickListener(this);
            lnSend.setOnClickListener(this);
            llBuyRate.setOnClickListener(this);
            llSellRate.setOnClickListener(this);

            lnDeposit.setOnClickListener(this);
            lnBank.setOnClickListener(this);
            lnTransfer.setOnClickListener(this);
            lnRece.setOnClickListener(this);
            lnINR.setOnClickListener(this);

            txBuy.setOnClickListener(this);
            txSelling.setOnClickListener(this);


            Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
            String symbol = UtilClass.getCurrencySymbol(PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Code));
            PreferenceFile.getInstance().saveData(this, Constant.Currency_Symbol, symbol);


            Double val = 1.0;

            if (bit > 0) {
                tvBitcoin.setText(String.format("%.8f", finacal));
            } else {
                tvBitcoin.setText("0.00000000");
            }

            Double inr = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));
            BigDecimal d = new BigDecimal(inr);
            if (inr > 0) {
                tvwallet.setText(" " + String.format("%.0f", d) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
            } else {
                tvwallet.setText(" 0 " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
            }


            Double tvINR1 = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.INR_PRICE_BITCOIN).replaceAll("₹","").replaceAll(",",""));
            tvINR.setText(" " + formatter.format(tvINR1) + " " + PreferenceFile.getInstance().getPreferenceData(DashBoardHelp.this, Constant.Currency_Symbol));


            Double BUYRATE = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUYRATE).replaceAll("₹","").replaceAll(",",""));
            Double SELLRATE = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELLRATE).replaceAll("₹","").replaceAll(",",""));

            tvBuyrate.setText( formatter.format(BUYRATE) +" "+PreferenceFile.getInstance().getPreferenceData(DashBoardHelp.this, Constant.Currency_Symbol)+" ");
            txSellRate.setText(" "+formatter.format(SELLRATE)+" "+PreferenceFile.getInstance().getPreferenceData(DashBoardHelp.this, Constant.Currency_Symbol));
            txName.setText("Quick Help");

        //    MaterialShowcaseView.resetSingleUse(this, SHOWCASE_ID);

            presentShowcaseSequence(); // one second delay
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void presentShowcaseSequence() {

//        ShowcaseConfig config = new ShowcaseConfig();
//        config.setDelay(0); // half second between each showcase view
//
//        final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
//
//        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
//            @Override
//            public void onShow(MaterialShowcaseView itemView, int position) {
////                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        sequence.setConfig(config);
//
//        sequence.addSequenceItem(lnrefresh, "This is your acepay balance. " +
//                "You can buy or sell it by  click on Buy or Sell button on Dashboard.", "NEXT");
//
////        sequence.addSequenceItem(
////                new MaterialShowcaseView.Builder(this)
////                        .setTarget(mButtonTwo)
////                        .setDismissText("GOT IT")
////                        .setContentText("This is button two")
////                        .withRectangleShape(true)
////                        .build()
////        );
//        sequence.addSequenceItem(txBuy,"Tap here to buy acepay ","NEXT");
//        sequence.addSequenceItem(txSelling,"Tap here to go to sell acepay screen","NEXT");
//
//    /*    sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder(this)
//                        .setTarget(txSelling)
//                        .setDismissText("NEXT")
//                        .setContentText("Tap here to go to sell bitcoin screen")
//                        .withRectangleShape()
//                        .build()
//        );*/
//
//        sequence.addSequenceItem(llBuyRate,"This is your current buy rate for 1 acepay","NEXT");
//        sequence.addSequenceItem(llSellRate,"This is your current sell rate for 1 acepay","NEXT");
//
////        sequence.addSequenceItem(lnSettings,"Tap here to go to settings screen","NEXT");
//        sequence.addSequenceItem(lnSettings,"Tap here to go to Messages screen","NEXT");
//        sequence.addSequenceItem(lnReceiver,"Tap here to go to receive aceoay screen","NEXT");
//        sequence.addSequenceItem(lntransaction,"Tap here to go to see your BTC transaction","NEXT");
//        sequence.addSequenceItem(lnSend,"Tap here to see your send acepay details","NEXT");
////        sequence.addSequenceItem(lnRateChart,"Tap here to see the Rate Chart","NEXT");
//        sequence.addSequenceItem(lnRateChart,"Tap here to see the Activities","NEXT");
//
//        sequence.addSequenceItem(lnDeposit,"Tap here to go to Rs deposit screen from here you can transfer Rs via our payment gateway you can create a deposit request and then transfer the amount via RTGS/NEFT via IMPS. ","NEXT");
//        sequence.addSequenceItem(lnBank,"Here, you can make a request to withdraw your Rs account balance to your registered bank account.","NEXT");
//        sequence.addSequenceItem(lnTransfer,"Here, you can transfer the wallet money to other wallet Account.","NEXT");
//        sequence.addSequenceItem(lnRece,"Here, you can Receive the wallet money from the other wallet Account.","NEXT");
//
//
//
//
//        sequence.addSequenceItem(
//                new MaterialShowcaseView.Builder(this)
////                        .setTitleText("GO ON IT")
//                        .setTarget(lnINR)
//                        .setDismissText("SKIP")
//                        .setDismissOnTargetTouch(true)
//                        .setDismissOnTouch(true)
//                        .setContentText("Tap here to go to see your INR transaction")
//                        .setListener(new IShowcaseListener() {
//                            @Override
//                            public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {
////                                Intent i=new Intent(DashBoardHelp.this, Dashboard.class);
////                                startActivity(i);
////                                finish();
//                            }
//
//                            @Override
//                            public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
//                                Intent i=new Intent(DashBoardHelp.this, Dashboard.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        })
//                        .build()
//        );
//
//
//        sequence.start();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lnrefresh ||v.getId() == R.id.lnReceiver ||v.getId() == R.id.lntransaction ||v.getId() == R.id.lnSend ||v.getId() == R.id.llBuyRate ||v.getId() == R.id.llSellRate ||
         (v.getId() == R.id.lnDeposit ||v.getId() == R.id.lnBank ||v.getId() == R.id.lnTransfer ||v.getId() == R.id.lnRece
                ||v.getId() == R.id.lnINR || v.getId() == R.id.lnRateChart ||v.getId() == R.id.lnSettings ||
                v.getId() == R.id.txBuy || v.getId() == R.id.txSelling)) {

            presentShowcaseSequence();

        } else if (v.getId() == R.id.ivarrow) {


        }
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 1000);
    }

}
