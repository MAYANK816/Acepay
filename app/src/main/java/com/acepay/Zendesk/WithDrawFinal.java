package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;


public class WithDrawFinal extends ToolabarActivity implements View.OnClickListener{
    CardView acc_verification, rs_deposite;
    TextView txName;
//    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_final);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);

        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Withdraw");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
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
                intent=new Intent(WithDrawFinal.this,SubmitRequest.class);
                intent.putExtra("first","I have made Rs withdrawal request in the app");
                intent.putExtra("on","on");
                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:
                intent=new Intent(WithDrawFinal.this,SubmitRequest.class);
                intent.putExtra("first","What is the Rs withdrawal request? I have just sold bitcoin and waiting");
                intent.putExtra("on","on");
                startActivity(intent);
                break;
//            case R.id.ivarrow:
//                finish();
//                break;
        }
    }

}
