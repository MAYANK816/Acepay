package com.acepay;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity implements  View.OnClickListener, RetrofitResponse {
    TextView txName;
    ImageView ivarrow;
    EditText etQuery;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        txName = (TextView) findViewById(R.id.txName);
        etQuery = (EditText) findViewById(R.id.etQuery);
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        txName.setText("Contact Us");
        ivarrow.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.ivarrow:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contactus_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            // action with ID action_settings was selected
            case R.id.send:

                if (validation()) {
                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                        postParam.put("name", PreferenceFile.getInstance().getPreferenceData(this, Constant.Username));
                        postParam.put("email", PreferenceFile.getInstance().getPreferenceData(this, Constant.Email));
                        postParam.put("subject", "knowledge base");

                        postParam.put("message", etQuery.getText().toString());
                        Log.e("postparam--->", postParam.toString());

                        if (Constant.isConnectingToInternet(ContactUs.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(this, ContactUs.this, postParam, Constant.REQ_CONTACTUS, Constant.CONTACTUS, "3").callService(true);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(ContactUs.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            default:
                break;
        }


        return true;
    }

    public boolean validation() {
        if (etQuery.getText().toString().equals("")) {
            Constant.alertDialog(this, "Please enter description");
            return false;
        }


        return true;
    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        Log.e("on service-->", "Dashboard");
        switch (requestCode) {
            case Constant.REQ_CONTACTUS:
                if (response.isSuccessful()) {
                    etQuery.setText("");

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");
                            Constant.alertDialog(this, message);

                        } else {
                            Constant.alertDialog(this, message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;
        }

    }
}