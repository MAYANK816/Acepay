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


public class TicketMain extends ToolabarActivity implements View.OnClickListener {
    CardView acc_verification, rs_deposite, tv_pin, tv_withdraw, tv_sendrcv, tv_other;
    TextView txName;
    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_main);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);
        tv_pin = (CardView) findViewById(R.id.tv_pin);
        tv_withdraw = (CardView) findViewById(R.id.tv_withdraw);
        tv_sendrcv = (CardView) findViewById(R.id.tv_send_rcv);
        tv_other = (CardView) findViewById(R.id.tv_other);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Support");
        rs_deposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
            case R.id.tv_acc_verification:
                intent=new Intent(TicketMain.this,TicketAccountVerify.class);
                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:


                intent=new Intent(TicketMain.this,RsDeposite.class);

              /*  intent.putExtra("header","Rs deposite");
                intent.putExtra("first","Express Transfer");
                intent.putExtra("sec","Normal Transfer");
                intent.putExtra("third","Payment Gatway");*/
                startActivity(intent);
                break;

            case R.id.tv_pin:
                intent=new Intent(TicketMain.this,PinNext.class);
               /* intent.putExtra("header","PIN");
                intent.putExtra("first","My account got blocked");
                intent.putExtra("sec","SMS not received after 24 hours");
                intent.putExtra("third","Forgot PIN");*/
                startActivity(intent);
                break;
            case R.id.tv_withdraw:
                intent=new Intent(TicketMain.this,WithDrawNext.class);
              /*  intent.putExtra("header","Withdrawal");
                intent.putExtra("first","I have not received money in my verified bank account");*/

                startActivity(intent);
                break;
            case R.id.tv_send_rcv:
                intent=new Intent(TicketMain.this,SendReceive.class);
          /*      intent.putExtra("header","Send/receive transaction");
                intent.putExtra("first","I sent bitcoins but receipient did not receive them");
                intent.putExtra("sec","Someone has sent bitcoins to me but I have not received them");*/
                startActivity(intent);
                break;
            case R.id.tv_other:

                intent=new Intent(TicketMain.this,OtherNext.class);
             /*   intent.putExtra("header","Other");
                intent.putExtra("first","Bitcoin Cash Address");
                intent.putExtra("sec","Any other issue in app");
                //intent.putExtra("flag","1");*/
                startActivity(intent);

                break;
//            case R.id.ivarrow:
//                finish();
//                break;
        }
    }
}
