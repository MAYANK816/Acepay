package com.acepay;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.RetrofitResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ReferalCode extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {
    TextView txName,tv_reffeal_code,tv_share,tvcopy;
//    ImageView ivarrow;
    String refralcode;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal_code);
        txName = (TextView) findViewById(R.id.txName);
//        tv_copy = (TextView) findViewById(R.id.tv_copy);
        tv_reffeal_code = (TextView) findViewById(R.id.tv_referal_code);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tvcopy = (TextView) findViewById(R.id.tvcopy);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName.setText("Refer Code");
        tv_share.setOnClickListener(this);
        tvcopy.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
//        tv_copy.setOnClickListener(this);
        refralcode= PreferenceFile.getInstance().getPreferenceData(this, Constant.REFERCODE);
        //  Log.e("reffffff",refralcode);
        tv_reffeal_code.setText(refralcode);

    }

    @Override
    public void onBackPressed() {

//        if (doubleBackToExitPressedOnce) {
//            finishAffinity();
//
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//                finish();
//            }
//        }, 1000);
        finish();
    }


    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.tvcopy:

                ClipboardManager cm = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv_reffeal_code.getText().toString());
//
                Toast.makeText(this, "Refer Code Copied", Toast.LENGTH_SHORT).show();
                break;


            case R.id.tv_share:
                share();
                break;
        }
    }



    public  void share()
    {
        String shareBody = refralcode;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MetaPay");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent,"Share via MetaPay" ));

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

    }

}
