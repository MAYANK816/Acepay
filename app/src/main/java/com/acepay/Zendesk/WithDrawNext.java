package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;


public class WithDrawNext extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification;
    TextView txName;
    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_next);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);

        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Withdraw");

        acc_verification.setOnClickListener(this);

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
                intent=new Intent(WithDrawNext.this,WithDrawFinal.class);
                startActivity(intent);
                break;

//            case R.id.ivarrow:
//                finish();
//                break;

        }
    }

}