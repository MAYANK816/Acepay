package com.acepay.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {

    //interface
    private static SmsListener mListener;
    StringBuilder builder = new StringBuilder();

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        Log.e("InsideReceiver ","inside ");

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().split(":")[1];

                    message = message.substring(0, message.length() - 1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}

 /*   @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();

            Log.e("smsMessage--",smsMessage.toString());
            Log.e("sender--",sender);


            if (sender.contains("Bokpay")) {
                String messageBody = smsMessage.getMessageBody();

                Log.e("messageBody--",messageBody);

                builder.append(messageBody);
//                if(builder.toString().contains("Dear Customer, your OTP is")){
//                    String a[]=builder.toString().split("Dear Customer, your OTP is ");
//                    String b[]=a[0].split(" ");
//                    Log.e("abbbb",a[0].toString());
//                    Log.e("bbbbb",b[0].toString().toString().trim());
                    try {
                        mListener.messageReceived(builder.toString().trim());  // attach value to interface
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
//                }
                Log.e("builder",builder.toString());
            }


          *//*  if (sender.equals("IM-MetaPy")) {
                String messageBody = smsMessage.getMessageBody();
                Log.e("SmsReceiver???",messageBody);
                if(messageBody.contains("Dear Customer, your OTP is")){
                    String a[]=messageBody.split("Dear Customer, your OTP is ");
                    String b[]=a[0].split(" ");
                    Log.e("a",a[0].toString());
                    Log.e("b",b[0].toString());
                    try {
                        mListener.messageReceived(messageBody);  // attach value to interface
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }*//*
        }
    }

  public static void bindListener(SmsListener listener) {
        mListener = listener;
    }*/

