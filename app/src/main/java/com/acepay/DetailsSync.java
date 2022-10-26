package com.acepay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.SyncAdapter;
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

public class DetailsSync extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    private RecyclerView recycler;
    boolean doubleBackToExitPressedOnce = false;
    ImageView ivarrow, ivTick;
    TextView txName;
    SyncAdapter syncAdapter;
    TextView tvStatus;
    public static CheckBox chkAll;
    ArrayList<ContactsModel> resultlist;
    ArrayList<ContactsModel> resultlist2=new ArrayList<>();
    int pos = 0;

    boolean mselectAllClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_details);

        initialize();
    }

    private void initialize() {

        recycler = (RecyclerView) findViewById(R.id.recycler);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        ivTick = (ImageView) findViewById(R.id.ivTick);
        txName = (TextView) findViewById(R.id.txName);
        chkAll = (CheckBox) findViewById(R.id.chkAll);

        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        ivTick.setVisibility(View.VISIBLE);
        txName.setText("Sync Contacts");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setNestedScrollingEnabled(false);

        resultlist = new ArrayList<ContactsModel>();
        resultlist.clear();

        syncAdapter = new SyncAdapter(DetailsSync.this, SinkPage.list);
        recycler.setAdapter(syncAdapter);

        Log.e("sizeeeeeeee",SinkPage.list.size()+" ");

        chkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    mselectAllClick=true;
                    Log.e("DATA", "onCheckedChanged: " );
                    resultlist.clear();
                    for (int i = 0; i < SinkPage.list.size(); i++) {

                        if (SinkPage.list.get(i).getStatus().equalsIgnoreCase("inactive")
                                || SinkPage.list.get(i).getStatus().equalsIgnoreCase("send") ) {
                            resultlist.addAll(SinkPage.list);
                            SinkPage.list.get(i).setInviteStatus("1"); //invite

                        }
                    }

                    syncAdapter.notifyDataSetChanged();
                } else {
                    Log.e("DATA", "onunCheckedChanged: " );
//                    resultlist.removeAll(SinkPage.list);
                    resultlist.clear();
                    for (int i = 0; i < SinkPage.list.size(); i++) {
                        if (SinkPage.list.get(i).getStatus().equalsIgnoreCase("inactive")
                                || SinkPage.list.get(i).getStatus().equalsIgnoreCase("send") ) {

                            Log.e("DATA", "onunCheckedChanged: "+"ifff" );
//                            resultlist.removeAll(SinkPage.list)
                            if (!mselectAllClick) {
                                if (SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("2")) {
                                    resultlist.add(SinkPage.list.get(i));
                                    SinkPage.list.get(i).setInviteStatus("2"); //invite
                                }
                            }else {
                                resultlist.add(SinkPage.list.get(i));
                                SinkPage.list.get(i).setInviteStatus("2");
                            }


                        }else{
                            SinkPage.list.get(i).setInviteStatus("0"); //active
                            resultlist.add(SinkPage.list.get(i));
                            Log.e("DATA", "onunCheckedChanged: "+"elseee" );
                        }
                    }
                    syncAdapter.notifyDataSetChanged();
                }
                Log.e("DATA", "onCheckedChanged: " + SinkPage.list.size() + " " + resultlist.size());
            }
        });


        syncAdapter.onItemClickListener(new SyncAdapter.itemClick() {
            @Override
            public void onItemClick(int layoutPosition, View view1, String inviteStatus) {

                if (inviteStatus.equalsIgnoreCase("1")) {
                    Log.e("checkinside","yes");
                    if (resultlist.size() > 0) {
                        for (int i = 0; i < resultlist.size(); i++) {
                            if (!resultlist.get(i).getId().equalsIgnoreCase(SinkPage.list.get(layoutPosition).getId()))
                                resultlist.add(SinkPage.list.get(layoutPosition));
                            SinkPage.list.get(layoutPosition).setInviteStatus("1"); //invite
                            break;
                        }

                    } else {
                        resultlist.add(SinkPage.list.get(layoutPosition));
                        SinkPage.list.get(layoutPosition).setInviteStatus("1"); //invite
                    }


                    int count=0;
                    for (int i = 0; i < SinkPage.list.size(); i++) {
                        if (SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("1")||
                                SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("0")){
                            count++;
                        }
                    }
                    Log.e("sidezeerrrreeee",count+"         "+SinkPage.list.size());

                    if (count==SinkPage.list.size()){
                        chkAll.setChecked(true);
                    }
                }
                else if (inviteStatus.equalsIgnoreCase("2")){
                    mselectAllClick=false;
                    Log.e("checkinside","yes");
                    if (resultlist.size() > 0) {
                        for (int i = 0; i < resultlist.size(); i++) {
                            if (!resultlist.get(i).getId().equalsIgnoreCase(SinkPage.list.get(layoutPosition).getId()))
                                resultlist.add(SinkPage.list.get(layoutPosition));
                            SinkPage.list.get(layoutPosition).setInviteStatus("2"); //invite
                            chkAll.setChecked(false);
                            break;
                        }


                    } else {
                        resultlist.add(SinkPage.list.get(layoutPosition));
                        SinkPage.list.get(layoutPosition).setInviteStatus("2"); //invite
                        chkAll.setChecked(false);
                    }
                }
                else {

//                    chkAll.setChecked(false);

                    resultlist.remove(SinkPage.list.remove(layoutPosition));
                    SinkPage.list.get(layoutPosition).setInviteStatus("0"); //invite


                }

                syncAdapter.notifyDataSetChanged();


                Log.e("onItemClick", "onItemClick: " +
                        SinkPage.list.get(layoutPosition).getName() + " " + resultlist.size());


            }


        });

//        inactive>>Invite,send>>Invitation Already Sent,


        ivTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=0;
                for (int i = 0; i < SinkPage.list.size(); i++) {
                    if (SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("1")){
                        count++;
                    }
                }


//                if (resultlist.size() > 0) {
                if (count > 0) {
                    alertDialog(DetailsSync.this, "dfgdf", "df", "dgdfg");
                } else {
                    Constant.alertDialog(DetailsSync.this, "Please select atleast one contact.");
                }
            }
        });


    }



    public void alertDialog(final Context context, final String msg, final String id, final String number) {

        final Dialog dialog = new Dialog(DetailsSync.this);

//        dialog.setTitle("FIN-CEX");
        dialog.setCancelable(false);


        dialog.setContentView(R.layout.dialog);

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final String msg1 = "Get " + PreferenceFile.getInstance().getPreferenceData(DetailsSync.this, Constant.Currency_Symbol) + 100 + " free on your first transaction with referral code'" + PreferenceFile.getInstance().getPreferenceData(DetailsSync.this,
                        Constant.REFERCODE) + "'.";

        Log.e("MESSAGELENGTH", "alertDialog: "+msg1.length() );

        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
        TextView btncancel = (TextView) dialog.findViewById(R.id.btncansel);
        tvText.setGravity(Gravity.CENTER);
        tvText.setText(msg1);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String separator = ",";

                if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                    separator = ",";
                }

                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder stringBuilderId = new StringBuilder();



                for (int i = 0; i < SinkPage.list.size(); i++) {
                    if (SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("1")){
                        resultlist2.add(SinkPage.list.get(i));
                    }
                }



                if (resultlist2.size() > 1) {
                    for (int i = 0; i < resultlist2.size(); i++) {
                        stringBuilder.append(resultlist2.get(i).getNumber().toString().trim() + separator);
                        stringBuilderId.append(resultlist2.get(i).getId().toString().trim() + separator);
                    }
                } else {
                    if (resultlist2.size() > 0) {
                        stringBuilder.append(resultlist2.get(0).getNumber().toString().trim());
                        stringBuilderId.append(resultlist2.get(0).getId().toString().trim());
                    }
                }


                if (resultlist2.size() > 1) {
                    sendMessage(stringBuilderId.toString().substring(0, stringBuilderId.toString().length() - 1),
                            stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
                            , msg1);

                } else {
                    sendMessage(stringBuilderId.toString().trim(), stringBuilder.toString().trim(), msg1);
                }


//                Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
//                        Uri.parse("smsto:"+stringBuilder.toString().substring(0,stringBuilder.toString().length()-1)));
//                smsIntent.putExtra("sms_body", msg1);
//                startActivity(smsIntent);
//
//
//
//                if (resultlist.size() > 1) {
//                    sendInvitation(stringBuilderId.toString().substring(0, stringBuilderId.toString().length() - 1),
//                            stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1), "");
//                } else {
//                    sendInvitation(stringBuilderId.toString().trim(),
//                            stringBuilder.toString().trim(), "");
//                }

                dialog.dismiss();
            }

        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
    }





/*
    public void alertDialog(final Context context, final String msg, final String id, final String number) {

        try {


            // invite status for 1 >> inactive(checkbox true)
//             invite status for 2 >> inactive(checkbox false >> bydefault)
//             invite status for 0 >> active()
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("BOK");

            final String msg1 = "Get rewards of "
                    + PreferenceFile.getInstance().getPreferenceData
                    (DetailsSync.this, Constant.Currency_Symbol) + 100
                    + " free on your first transaction with referral code'" +
                    PreferenceFile.getInstance().getPreferenceData(DetailsSync.this,
                            Constant.REFERCODE) + "'.";

            Log.e("MESSAGELENGTH", "alertDialog: "+msg1.length() );

            alertDialogBuilder.setMessage(msg1);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

//                Sender sender = new Sender("sms.digimiles.in", 8000,
//                        "di78-bitpay", "didimile", msg
//                        , "1", "0",
//                        PreferenceFile.getInstance().getPreferenceData(context, Constant.COUNTRY_CODE) + number,
//                        "MetaPay", PreferenceFile.getInstance().getPreferenceData(DetailsSync.this, Constant.Currency_Code));
//                sender.call();


                    String separator = ",";

                    if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                        separator = ",";
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    StringBuilder stringBuilderId = new StringBuilder();



                    for (int i = 0; i < SinkPage.list.size(); i++) {
                        if (SinkPage.list.get(i).getInviteStatus().equalsIgnoreCase("1")){
                            resultlist2.add(SinkPage.list.get(i));
                        }
                    }



                    if (resultlist2.size() > 1) {
                        for (int i = 0; i < resultlist2.size(); i++) {
                            stringBuilder.append(resultlist2.get(i).getNumber().toString().trim() + separator);
                            stringBuilderId.append(resultlist2.get(i).getId().toString().trim() + separator);
                        }
                    } else {
                        if (resultlist2.size() > 0) {
                            stringBuilder.append(resultlist2.get(0).getNumber().toString().trim());
                            stringBuilderId.append(resultlist2.get(0).getId().toString().trim());
                        }
                    }


                    if (resultlist2.size() > 1) {
                        sendMessage(stringBuilderId.toString().substring(0, stringBuilderId.toString().length() - 1),
                                stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
                                , msg1);

                    } else {
                        sendMessage(stringBuilderId.toString().trim(), stringBuilder.toString().trim(), msg1);
                    }


//                Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
//                        Uri.parse("smsto:"+stringBuilder.toString().substring(0,stringBuilder.toString().length()-1)));
//                smsIntent.putExtra("sms_body", msg1);
//                startActivity(smsIntent);
//
//
//
//                if (resultlist.size() > 1) {
//                    sendInvitation(stringBuilderId.toString().substring(0, stringBuilderId.toString().length() - 1),
//                            stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1), "");
//                } else {
//                    sendInvitation(stringBuilderId.toString().trim(),
//                            stringBuilder.toString().trim(), "");
//                }

                    arg0.dismiss();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
*/


    private void sendMessage(String id, String phoneNo, String message1) {

        try {


            if (phoneNo.length() > 0 && message1.length() > 0) {

                TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                int simState = telMgr.getSimState();
                Log.e("sendMessage", "sendMessage: " +
                        phoneNo + " " + message1.length()+" simState "+simState);


                switch (simState) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        displayAlert();
                        break;
                    case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                        // do something
                        Toast.makeText(getBaseContext(),
                                "SIM_STATE_NETWORK_LOCKED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                        // do something
                        Toast.makeText(getBaseContext(),
                                "SIM_STATE_PIN_REQUIRED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                        // do something
                        Toast.makeText(getBaseContext(),
                                "SIM_STATE_PUK_REQUIRED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.SIM_STATE_READY:
                        // do something
                        sendSMS(id, phoneNo, message1); // method to send message
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        // do something
                        Toast.makeText(getBaseContext(),
                                "SIM_STATE_UNKNOWN",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(getBaseContext(),
                        "Please enter both phone number and message.",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayAlert() {

        new AlertDialog.Builder(DetailsSync.this)
                .setMessage("Sim card not available.")
                .setCancelable(false)
                // .setIcon(R.drawable.alert)
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Log.e("I am inside ok", "ok");
                                dialog.cancel();
                            }
                        })
                .show();

    }


    private void sendSMS(final String id, final String phoneNumber, final String message) {

        try {

            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            Log.e("sendMessage", "sendSMS: " + phoneNumber + " " + message);

            PendingIntent sentPI = PendingIntent.getBroadcast(DetailsSync.this, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(DetailsSync.this,
                    0, new Intent(DELIVERED), 0);

            // ---when the SMS has been sent---
            final String string = "deprecation";

            registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent arg1) {

                    switch (getResultCode()) {

                        case Activity.RESULT_OK:
                            Toast.makeText(DetailsSync.this, "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            if (resultlist2.size() > 1) {
                                sendInvitation(id,
                                        phoneNumber, message);
                            } else {
                                sendInvitation(id,
                                        phoneNumber, message);
                            }

                            break;

                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Log.e("RESULT_FAILURE", "sendSMS: " + phoneNumber + " " + message);

                            Toast.makeText(DetailsSync.this, "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(DetailsSync.this, "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(DetailsSync.this, "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;

                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }, new IntentFilter(SENT));

            // ---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:

                            Toast.makeText(DetailsSync.this, "SMS delivered",
                                    Toast.LENGTH_SHORT).show();

                            Log.e("sendMessage", "sendSMS:RESULT_OK " + phoneNumber + " " + message);
//
//                            if (resultlist2.size() > 1) {
//                                sendInvitation(id,
//                                        phoneNumber, message);
//                            } else {
//                                sendInvitation(id,
//                                        phoneNumber, message);
//                            }
//

                            break;
                        case Activity.RESULT_CANCELED:
                            Log.e("sendMessage", "sendSMS: " + phoneNumber + " " + message);

                            Toast.makeText(DetailsSync.this, "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            final String msg1 = "Get rewards of "
                    + "100 free on your first transaction with referral code'" +
                    PreferenceFile.getInstance().getPreferenceData(DetailsSync.this,
                            Constant.REFERCODE) + "'.";
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, msg1, sentPI, deliveredPI);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void sendInvitation(String id, String number, String msg) {

        try {
            HashMap<String, RequestBody> map = new HashMap<String, RequestBody>();
            map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"),
                    PreferenceFile.getInstance().getPreferenceData(DetailsSync.this, Constant.ID)));
            map.put("id", RequestBody.create(MediaType.parse("multipart/form-data"), id));
            map.put("number", RequestBody.create(MediaType.parse("multipart/form-data"), number));
            if (Constant.isConnectingToInternet(DetailsSync.this)) {
                new Retrofit2(DetailsSync.this, DetailsSync.this, map,
                        Constant.REQ_SEND_INVITATION, Constant.SEND_INVITATION, "4")
                        .callService(true);
            } else {
                Constant.alertDialog(DetailsSync.this, DetailsSync.this.getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                doubleBackToExitPressedOnce = false;
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
                        SinkPage.list.clear();
                        Log.e("REQ_Add_CONTACT", "REQ_GET_CONTACTS" + result1.toString());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")) {

                            JSONArray jsonArray = result1.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                ContactsModel contactsModel = new ContactsModel();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                contactsModel.setId(jsonObject.getString("id"));
                                contactsModel.setName(jsonObject.getString("name"));
                                contactsModel.setNumber(jsonObject.getString("phone"));
                                contactsModel.setStatus(jsonObject.getString("type"));
                                contactsModel.setInviteStatus("0");
                                SinkPage.list.add(contactsModel);

                            }

                            syncAdapter = new SyncAdapter(DetailsSync.this, SinkPage.list);
                            recycler.setAdapter(syncAdapter);

                            try{
                                chkAll.setChecked(false);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            syncAdapter.onItemClickListener(new SyncAdapter.itemClick() {
                                @Override
                                public void onItemClick(int layoutPosition, View view1, String inviteStatus) {


                                    if (inviteStatus.equalsIgnoreCase("1")) {

                                        if (resultlist.size() > 0) {
                                            for (int i = 0; i < resultlist.size(); i++) {
                                                if (!resultlist.get(i).getId().equalsIgnoreCase(SinkPage.list.get(layoutPosition).getId()))
                                                    resultlist.add(SinkPage.list.get(layoutPosition));
                                                SinkPage.list.get(layoutPosition).setInviteStatus("1"); //invite
                                                break;
                                            }


                                        } else {
                                            resultlist.add(SinkPage.list.get(layoutPosition));
                                            SinkPage.list.get(layoutPosition).setInviteStatus("1"); //invite
                                        }
                                    } else {
                                        resultlist.remove(SinkPage.list.remove(layoutPosition));
                                        SinkPage.list.get(layoutPosition).setInviteStatus("0"); //invite

                                    }

                                    Log.e("onItemClick", "onItemClick: " +
                                            SinkPage.list.get(layoutPosition).getName() + " " + resultlist.size());


                                }


                            });


                        } else {
                            Constant.alertDialog(this, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;

            case Constant.REQ_SEND_INVITATION:

                if (response.isSuccessful()) {

                    JSONObject result1 = null;

                    try {

                        result1 = new JSONObject(response.body().string());

                        Log.e("onServiceResponse", "onServiceResponse:" + result1.toString());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")) {


                            getContacts();

                        } else {

                            Constant.alertDialog(DetailsSync.this, message);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;

        }

    }

    private void getContacts() {

        try {
            HashMap<String, RequestBody> map = new HashMap<String, RequestBody>();
            map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"),
                    PreferenceFile.getInstance().getPreferenceData(DetailsSync.this, Constant.ID).trim()));
            if (Constant.isConnectingToInternet(DetailsSync.this)) {
                new Retrofit2(DetailsSync.this, DetailsSync.this, map,
                        Constant.REQ_GET_CONTACTS, Constant.GET_CONTACTS, "4")
                        .callService(true);
            } else {
                Constant.alertDialog(this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

