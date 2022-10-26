package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;


public class SendRecevNext extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification, rs_deposite;
    TextView acc_verification1, rs_deposite1;
    TextView txName;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_recev_next);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);
        acc_verification1 = (TextView) findViewById(R.id.tv_acc_verification1);
        rs_deposite1 = (TextView) findViewById(R.id.tv_rs_deposite1);

        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Send/Receive Transaction");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();

        acc_verification1.setText(bundle.getString("first"));
        rs_deposite1.setText(bundle.getString("sec"));
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.tv_acc_verification:
                intent = new Intent(SendRecevNext.this, SubmitRequest.class);
                intent.putExtra("first",getResources().getString(R.string.app_name)+" user's mobile number");

                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:
                intent = new Intent(SendRecevNext.this, SubmitRequest.class);
                intent.putExtra("first","Bitcoin Address");
                startActivity(intent);
                break;

//            case R.id.ivarrow:
//                finish();
//                break;
        }
    }
}
