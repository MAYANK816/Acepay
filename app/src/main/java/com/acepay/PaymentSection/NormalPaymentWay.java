package com.acepay.PaymentSection;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;

import com.acepay.R;
import com.acepay.ToolabarActivity;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.RetrofitResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NormalPaymentWay extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

//    ImageView  ivarrow;
    TextView txName,tv_rupees,tvTEXt,tv_ac_holder,tv_ifsc,
        tv_ac_number,tvNext,tv_next,rupees,ac_holder_name,ac_number,ifsc_code;
    String amount,bank_id;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_payment_way);
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        txName = (TextView) findViewById(R.id.txName);
        tv_ac_number = (TextView) findViewById(R.id.ac_number);
        tvNext = (TextView) findViewById(R.id.tvNext);
        tvTEXt = (TextView) findViewById(R.id.tvTEXt);
        ac_holder_name = (TextView) findViewById(R.id.ac_holder_name);
        rupees = (TextView) findViewById(R.id.rupees);
        tv_rupees = (TextView) findViewById(R.id.rupees);
        tv_ac_holder = (TextView) findViewById(R.id.ac_holder_name);
        tv_ifsc = (TextView) findViewById(R.id.ifsc_code);
        tv_next = (TextView) findViewById(R.id.tvNext);
        ac_number = (TextView) findViewById(R.id.ac_number);
        ifsc_code = (TextView) findViewById(R.id.ifsc_code);

        bank_id=getIntent().getStringExtra("bank_id");

//        tvTEXt.setText("Please make sure that beneficiary name must be "+getResources().getString(R.string.app_name)+" Pvt. Ltd. ");
        tvTEXt.setText("Please make sure that beneficiary name must be same as shown");

        rupees.setText(" deposited "+ getIntent().getStringExtra("amount")+
                PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol) +" via RTGS/NEFT/IMPS \n from your bank to our bank.");

        ac_holder_name.setText(getIntent().getStringExtra("account_holder_name"));
        ac_number.setText(getIntent().getStringExtra("account_number"));
        ifsc_code.setText(getIntent().getStringExtra("ifsc"));


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.alertDialogTwoButtons(NormalPaymentWay.this,"Are you sure you want to cancel this deposit?");
            }
        });


        tv_next.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        txName.setVisibility(View.VISIBLE);
        txName.setText("Normal Transfer");

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

//            case R.id.ivarrow:
//                Constant.alertDialogTwoButtons(this,"Are you sure you want to cancel this deposit?");
//
//                break;

            case R.id.tvNext:

                intent=new Intent(NormalPaymentWay.this,NormalFinal.class);
                intent.putExtra("amount",getIntent().getStringExtra("amount"));
                intent.putExtra("ifsc",getIntent().getStringExtra("ifsc"));
                intent.putExtra("brach_name",getIntent().getStringExtra("brach_name"));
                intent.putExtra("account_holder_name",getIntent().getStringExtra("account_holder_name"));
                intent.putExtra("bank_name",getIntent().getStringExtra("bank_name"));
                intent.putExtra("bank_id",getIntent().getStringExtra("bank_id"));
                intent.putExtra("account_number",getIntent().getStringExtra("account_number"));
                startActivity(intent);

                break;

        }
    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {


        }

        }
}
