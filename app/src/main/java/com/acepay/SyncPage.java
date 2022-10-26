package com.acepay;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Model.ContactsModel;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SyncPage extends AppCompatActivity implements View.OnClickListener , RetrofitResponse {

    ImageView ivarrow;
    TextView txName;
    TextView tvSink,tvtext;
    ArrayList<String> alContacts,nameData;
    boolean doubleBackToExitPressedOnce = false;
    public static ArrayList<ContactsModel> list;
    Cursor cursor;
    String name, phonenumber ;
    LinearLayout lnChangePin;
    public  static final int RequestPermissionCode  = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sink_page);

        alContacts=new ArrayList<>();
        nameData=new ArrayList<>();

        alContacts.clear();
        nameData.clear();

        lnChangePin = (LinearLayout) findViewById(R.id.lnChangePin);
        tvSink = (TextView) findViewById(R.id.tvSink);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        txName = (TextView) findViewById(R.id.txName);
        tvtext = (TextView) findViewById(R.id.tvtext);
        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Sync Contact List");
        tvSink.setOnClickListener(this);
        list=new ArrayList<ContactsModel>();
        list.clear();
        getAllContacts();
        callStateService();

    }

    private void callStateService() {

        lnChangePin.setVisibility(View.GONE);

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, SyncPage.this, Constant.REQ_SINK_INTRUCTION,
                    Constant.SINK_INTRUCTION).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
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
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ivarrow:

                finish();

                break;

            case R.id.tvSink:

                try {

                    Log.e("tvSink", "onClick: " + alContacts.size());

                    if (alContacts.size() > 0) {

                        JSONObject object = new JSONObject();

                        JSONArray jsonArr = new JSONArray();

                        for (int i = 0; i < alContacts.size(); i++) {

                            JSONObject pnObj = new JSONObject();

                            pnObj.put(alContacts.get(i).toString(), nameData.get(i).toString());

                            jsonArr.put(pnObj);

                        }

                        object.put("user_id", PreferenceFile.getInstance().
                                getPreferenceData(this, Constant.ID));
                        object.put("phone", "" + jsonArr);


                            Log.e("object", object.toString());

                            new Retrofit2(SyncPage.this, SyncPage.this, object,
                                    Constant.REQ_Add_CONTACT, Constant.Add_CONTACT, "3").
                                    callService(true);

                    } else {

                        getAllContacts();

                        if (alContacts.size() > 0) {

                            JSONObject object = new JSONObject();

                            JSONArray jsonArr = new JSONArray();

                            for (int i = 0; i < alContacts.size(); i++) {

                                JSONObject pnObj = new JSONObject();

                                pnObj.put(alContacts.get(i).toString(), nameData.get(i).toString());

                                jsonArr.put(pnObj);

                            }

                            object.put("user_id", PreferenceFile.getInstance().
                                    getPreferenceData(this, Constant.ID));
                            object.put("phone", "" + jsonArr);

                            Log.e("object", object.toString());

                            new Retrofit2(SyncPage.this, SyncPage.this, object,
                                    Constant.REQ_Add_CONTACT, Constant.Add_CONTACT, "3").
                                    callService(true);


                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }

    }


    public void getAllContacts(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            alContacts.add(phonenumber);
            if(name.isEmpty() || name.equals("")) {
                nameData.add("unknown number");
            }else{
                nameData.add(name);
            }

            Log.e("CONTACTS",alContacts.size()+""+ nameData.size()+" "+phonenumber+" "+name);
        }

        cursor.close();

    }






    private void getContacts(){

        try {
            HashMap<String, RequestBody> map = new HashMap<String, RequestBody>();
            map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"),
                    PreferenceFile.getInstance().getPreferenceData(SyncPage.this, Constant.ID).trim()));
            if (Constant.isConnectingToInternet(SyncPage.this)) {
                new Retrofit2(SyncPage.this, SyncPage.this, map,
                        Constant.REQ_GET_CONTACTS, Constant.GET_CONTACTS, "4")
                        .callService(true);
            }
            else {
                Constant.alertDialog(this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_GET_CONTACTS:

                if (response.isSuccessful()) {

                    JSONObject result1 = null;

                    try {

                        result1 = new JSONObject(response.body().string());

                        Log.e("REQ_Add_CONTACT", "REQ_GET_CONTACTS"+result1.toString());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){

                            JSONArray jsonArray=result1.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                ContactsModel contactsModel=new ContactsModel();
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                contactsModel.setId(jsonObject.getString("id"));
                                contactsModel.setName(jsonObject.getString("name"));
                                contactsModel.setNumber(jsonObject.getString("phone"));
                                contactsModel.setStatus(jsonObject.getString("type"));

                                if(jsonObject.getString("type").equalsIgnoreCase("active")) {
                                    contactsModel.setInviteStatus("0");
                                }else{
                                    contactsModel.setInviteStatus("2");
                                }


                                list.add(contactsModel);

                            }

                            Intent i=new Intent(SyncPage.this,DetailsSync.class);
                            startActivity(i);


                        }else {
                            Constant.alertDialog(this,message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;

                case Constant.REQ_Add_CONTACT:

                if (response.isSuccessful()) {

                    JSONObject result1 = null;
                    try {
                        result1 = new JSONObject(response.body().string());

                        Log.e("REQ_Add_CONTACT", "REQ_Add_CONTACT"+result1.toString());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){
                            getContacts();

                        }else {
                            Constant.alertDialog(this,message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;

            case Constant.REQ_SINK_INTRUCTION:

                if (response.isSuccessful()) {

                    JSONObject result1 = null;

                    try {
                        lnChangePin.setVisibility(View.VISIBLE);

                        result1 = new JSONObject(response.body().string());
                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){
                            JSONObject data=result1.getJSONObject("data");
                            tvtext.setText(data.getString("text"));
                        }else {
                            Constant.alertDialog(this,message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }
}
