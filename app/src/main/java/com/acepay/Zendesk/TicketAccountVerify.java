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

public class TicketAccountVerify extends ToolabarActivity implements View.OnClickListener {

    CardView acc_verification, rs_deposite, tv_pin, tv_withdraw, tv_sendrcv, tv_other;
    TextView txName;
//    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_account_verify);

        acc_verification = (CardView) findViewById(R.id.first);
        rs_deposite = (CardView) findViewById(R.id.tv_second);
        tv_pin = (CardView) findViewById(R.id.tv_third);
        tv_withdraw = (CardView) findViewById(R.id.tv_fourth);
        tv_sendrcv = (CardView) findViewById(R.id.tv_five);
        tv_other = (CardView) findViewById(R.id.tv_six);
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);

//        ivarrow.setVisibility(View.VISIBLE);
        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Account Verification");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
        tv_sendrcv.setOnClickListener(this);
        tv_other.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            Log.e("Biding","once");
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.first:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);
                intent.putExtra("first","Email verification pending");

                startActivity(intent);
                break;
            case R.id.tv_second:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);

                intent.putExtra("first","Approval pending");
                startActivity(intent);
                break;
            case R.id.tv_third:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);
                intent.putExtra("first","Change in bank account");
                startActivity(intent);
                break;
            case R.id.tv_fourth:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);
                intent.putExtra("first","Company bank account");
                startActivity(intent);
                break;
            case R.id.tv_five:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);
                intent.putExtra("first","Request rejected");
                startActivity(intent);
                break;
            case R.id.tv_six:
                intent = new Intent(TicketAccountVerify.this, SubmitRequest.class);
                intent.putExtra("first","Level two verification");
                startActivity(intent);
                break;
//            case R.id.ivarrow:
//                finish();
//                break;


        }
    }
}

