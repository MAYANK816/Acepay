package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;


public class RsDepositeNext extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification, rs_deposite, tv_pin, tv_withdraw, tv_sendrcv, tv_other;
    TextView txName,acc_verification1,rs_deposite1,tv_pin1;
//    ImageView ivarrow;
    String flag="";

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rs_deposite_next);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);
        tv_pin = (CardView) findViewById(R.id.tv_pin);
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
//        ivarrow.setVisibility(View.VISIBLE);
        txName = (TextView) findViewById(R.id.txName);
        acc_verification1 = (TextView) findViewById(R.id.tv_acc_verification1);
        rs_deposite1 = (TextView) findViewById(R.id.tv_rs_deposite1);
        tv_pin1 = (TextView) findViewById(R.id.tv_pin1);
        txName.setText("Rs deposit");


        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        acc_verification1.setText(bundle.getString("first"));
        rs_deposite1.setText(bundle.getString("sec"));
        tv_pin1.setText(bundle.getString("third"));
        flag=(bundle.getString("flag"));
        if (flag.equals("1")){
            tv_pin.setVisibility(View.INVISIBLE);
            view.setVisibility(View.INVISIBLE);
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
                finish();
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_acc_verification:
                intent = new Intent(RsDepositeNext.this, SubmitRequest.class);
                intent.putExtra("first","Amount less than 50,000");
                /*intent.putExtra("first", "Express Transfer");
                intent.putExtra("sec", "Normal Transfer");
                intent.putExtra("third", "Payment Gatway");*/
                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:
                intent = new Intent(RsDepositeNext.this, SubmitRequest.class);
                intent.putExtra("first","Amount between 50,000 and 5,00,000");
                /*intent.putExtra("first", "Express Transfer");

                intent.putExtra("sec", "Normal Transfer");
                intent.putExtra("third", "Payment Gatway");*/
                startActivity(intent);
                break;

            case R.id.tv_pin:
                intent = new Intent(RsDepositeNext.this, SubmitRequest.class);
                intent.putExtra("first","Amount more than 5,00,000");
               /* intent.putExtra("header","PIN");
                intent.putExtra("first","My account got blocked");
                intent.putExtra("sec","SMS not received after 24 hours");
                intent.putExtra("third","Forgot PIN");*/
                startActivity(intent);
                break;

//            case R.id.ivarrow:
//               finish();
//                break;
        }
    }
}
