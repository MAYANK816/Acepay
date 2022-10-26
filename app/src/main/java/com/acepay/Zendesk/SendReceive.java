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


public class SendReceive extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification, rs_deposite;
    TextView txName;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);

        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Send/Receive Transaction");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
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

            case R.id.tv_acc_verification:
                intent=new Intent(SendReceive.this,SendRecevNext.class);
                // intent.putExtra("header","Rs deposite");
                intent.putExtra("first",getResources().getString(R.string.app_name)+" user's mobile number");
                intent.putExtra("sec","bitcoin address");

                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:
                intent=new Intent(SendReceive.this,SendRecevNext.class);
                // intent.putExtra("header","PIN");
                intent.putExtra("first",getResources().getString(R.string.app_name)+"  mobile number");
                intent.putExtra("sec",getResources().getString(R.string.app_name)+" receiving bitcoin address");

                startActivity(intent);
                break;

//            case R.id.ivarrow:
//                finish();
//                break;
        }
    }
}
