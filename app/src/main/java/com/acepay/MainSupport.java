package com.acepay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.acepay.Zendesk.TicketMain;

public class MainSupport extends ToolabarActivity implements View.OnClickListener {

    TextView txName;
    CardView tv_rate_app,tv_knowlege,tv_crete_tikt,my_tick;
//    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_support);

      /*  ZendeskConfig.INSTANCE.init(this, "https://metapay.zendesk.com", "7b5b2197c0b75ab55e87b61e60b8fbf8e1ad2f48302c8bab", "mobile_sdk_client_3f693ea4959e1206d347");
        Identity identity = new AnonymousIdentity.Builder().build();
        ZendeskConfig.INSTANCE.setIdentity(identity);*/

        tv_rate_app=(CardView)findViewById(R.id.tv_app_rate);
        tv_knowlege=(CardView)findViewById(R.id.tv_knowledge_base);
        tv_crete_tikt=(CardView)findViewById(R.id.tv_crete_tikt);
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);

        txName = (TextView) findViewById(R.id.txName);

        my_tick = (CardView) findViewById(R.id.my_tick);
        txName.setText("Support");
//        ivarrow.setOnClickListener(this);
        tv_knowlege.setOnClickListener(this);
        tv_rate_app.setOnClickListener(this);
        tv_crete_tikt.setOnClickListener(this);
        my_tick.setOnClickListener(this);
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


            case R.id.tv_knowledge_base:

                break;

            case R.id.tv_app_rate:
                alertDialog(this);
                break;

            case R.id.tv_crete_tikt:
                intent = new Intent(MainSupport.this, TicketMain.class);
                startActivity(intent);
                break;

            case R.id.my_tick:
               // new SupportActivity().Builder().Show(MainSupport.this);

                break;
        }
    }
    public void alertDialog(Context context) {

        final Dialog dialog=new Dialog(MainSupport.this);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.apprate_pop_up);

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;


        dialog.show();

        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);
        TextView tv_dont=(TextView)dialog.findViewById(R.id.tv_dont_show);

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
