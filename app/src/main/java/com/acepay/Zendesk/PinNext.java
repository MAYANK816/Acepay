package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;


public class PinNext extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification, rs_deposite, tv_pin, tv_withdraw, tv_sendrcv, tv_other;
    TextView txName;
    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_next);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);
        tv_pin = (CardView) findViewById(R.id.tv_pin);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
//        ivarrow.setVisibility(View.VISIBLE);
        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Pin");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
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
                intent = new Intent(PinNext.this, SubmitRequest.class);
                intent.putExtra("first","My account got blocked");
                startActivity(intent);
                break;
            case R.id.tv_rs_deposite:
                intent = new Intent(PinNext.this, SubmitRequest.class);
                intent.putExtra("first","SMS not received after 24 hours");
                startActivity(intent);
                break;
            case R.id.tv_pin:
                intent = new Intent(PinNext.this, SubmitRequest.class);
                intent.putExtra("first","Forgot PIN");
                startActivity(intent);
                break;
//            case R.id.ivarrow:
//               finish();
//                break;
        }
    }
}
