package com.acepay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class AddBitcoinAddress extends AppCompatActivity implements View.OnClickListener{

    EditText edNumber,edname,edAddress;
    ImageView imView,ivarrow;
    TextView txNext,txName;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bitcoin_address);
        edNumber= (EditText) findViewById(R.id.edNumber);
        edname= (EditText) findViewById(R.id.edname);
        edAddress= (EditText) findViewById(R.id.edAddress);
        imView= (ImageView) findViewById(R.id.imView);
        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);
        txNext= (TextView) findViewById(R.id.txNext);
        ivarrow.setOnClickListener(this);
        imView.setOnClickListener(this);
        txNext.setOnClickListener(this);
        qrScan = new IntentIntegrator(this);

        txName.setVisibility(View.VISIBLE);
        txName.setText("Add Acepay Address");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.txNext:

                if(validation()){

                    Log.e("hit-->","yes");
                }

                break;

            case R.id.ivarrow:

               finish();

                break;

            case R.id.imView:

                qrScan.initiateScan();

                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());

                    Log.e("result--->",obj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException--->",e.toString());
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }

                if(result.getContents()!=null) {

                    if(result.getContents().contains("address")) {

                        String CurrentString = result.getContents();
                        String[] separated = CurrentString.split("address");
                        String address=separated[1];
                        String[] newAddress = address.split("Phone");
                        String properaddress=newAddress[0];
                        edAddress.setText(properaddress);

                        String newPhone= newAddress[1];

                        Log.e("newAddress-->",address);
                        Log.e("phone-->",newPhone);

                        String[] phn = newPhone.split("name");

                        edNumber.setText(phn[0]);
                        edname.setText(phn[1]);


                        Log.e("number split-->",address);

                    }
                    else {
                        Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
                    }
                }
                Log.e("JSONException--->",result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean validation(){

        if(edNumber.getText().toString().equals("")){

            Constant.alertDialog(this,"Please Enter Phone Number");
            return  false;
        }
        if(edname.getText().toString().equals("")){

            Constant.alertDialog(this,"Please Enter Username");
            return  false;
        }
        if(edAddress.getText().toString().equals("")){

            Constant.alertDialog(this,"Please Enter Address");
            return  false;
        }

        return  true;

    }
}
