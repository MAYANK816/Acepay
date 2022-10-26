package com.acepay.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.BlockScreen;
import com.acepay.Dashboard;
import com.acepay.LoginNew;
import com.acepay.NotificationDetails;
import com.acepay.NotificationPage;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONObject;

import java.util.Map;

public class FirebaseMessages extends FirebaseMessagingService {

    private String title, message, type, key = "";
    private Intent intent;
    //static PendingIntent pendingIntent;
    private Map<String, String> data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {

        //{piriya=Notification, user_id=27, id=85, type=Broadcast,
        // title=Welcome Money,
        // message=Thank you for choosing Acepay,
        // You are important to us, so we are giving you welcome balance.Keep trading and get rewards} inside

        Log.e("remoteMessage ", remoteMessage.toString());
//        Toast.makeText(this, "Noti:- "+remoteMessage.toString(), Toast.LENGTH_SHORT).show();

//8267836652
//        {piriya=Notification, id=988, type=Notification, title=Sell Bitcoins, message=You have sold 0.00469105 bitcoins.}
//        {piriya=Notification, id=1025, type=Notification, title=Buy Bitcoins, message=You have bought 1 bitcoins} inside
//        {type=Withdraw, title=Withdraw, message=Your 1403 rs withdraw request has been placed successfully} inside
//        {piriya=Notification, user_id=165, type=Notification, title=Inr Recieved, message=You have received 1580 rs from 1472580369 successfully} title-->Inr Recieved type-->Notification
//        {piriya=Notification, user_id=165, id=1010, type=Notification, title=Inr Send, message=You have transferred 1580 rs to 1472580369 successfully} title-->Inr Send type-->Notification
//        {piriya=Notification, id=1029, type=Notification, title=BTC Send Local Address, message=Bitcoin transferred successfully.} inside
//        BTC Send Local Address //transfer btc


        //todo admin broadcast notification
        /*{piriya=Notification, user_id=2, id=69, type=Broadcast, title=New Msg, message=testing Notifications}*/

        //todo admin pop up notification
        /*{piriya=Notification, user_id=2, type=Broadcast, title=test pop up notiifcation, message=testing purpose}*/



        if (remoteMessage.getData().size()>0)
        {

           /* Log.e("remoteMessage", data + " inside");

            Log.e( "Data Payload: " , remoteMessage.getData().toString());

            try {


                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e("JSONObject", json.toString());
//                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("CatchException ", e.getMessage());
            }
*/
            data = remoteMessage.getData();


            try
            {
                Log.e("remoteMessage", data + " inside");
                type = data.get("type");

                message = data.get("message");

                title = data.get("title").trim();

                if (type.equals("Broadcast"))
                {
                    if (data.get("piriya") != null)
                    {
                        key = data.get("piriya");
                    }
                }

                if (data.get("piriya") != null)
                {
                    key = data.get("piriya");
                    Log.e("key", key + " AVDFG");
                }

                if (type .equalsIgnoreCase("Broadcast"))
                {
                    Log.e("InsideType ", "Broadcast");
                    Intent intent = new Intent(getApplicationContext(), NotificationPage.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                }

                if (title.equalsIgnoreCase("Deposit failed"))
                {
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_AMT, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BANK_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_KEY, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_IFSC, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BRANCH_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ACC_HOLDER_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BANK_ID, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ACC_NO, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ID_RECVD, null);
                }

                if (title .equalsIgnoreCase("Welcome Money"))
                {
                    Log.e("Inside ", "Welcome Money");
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                }


                /*------------------------- Replace 13 Mar ------------------------------*/
                if (title.equalsIgnoreCase("Deposite Request") || title.equalsIgnoreCase("Deposit Rejected"))
                {
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_AMT, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BANK_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_KEY, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_IFSC, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BRANCH_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ACC_HOLDER_NAME, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_BANK_ID, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ACC_NO, null);
                    PreferenceFile.getInstance().saveData(this, Constant.NORMAL_DEP_ID_RECVD, null);
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                }
                if (title.equalsIgnoreCase("Sell Bitcoins"))
                {

                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    Log.e("key", "Sell Acepay");
                    return;
                }
                else {
                    Log.e("key", "Sell Acepay else");
                }

                if (title.equalsIgnoreCase("paypal"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    Log.e("key", "paypal");
                    return;
                } else {
                    Log.e("key", "paypal");
                }
                if (title.equalsIgnoreCase("payu"))
                {

                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    Log.e("key", "payu");
                    return;
                }
                else
                {
                    Log.e("key", "payu");
                }

                if (title.equalsIgnoreCase("Buy Bitcoins"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e("key", "Buy Acepay");
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                } else {
                    Log.e("key", "Buy Acepay else");
                }

                if (title.equalsIgnoreCase("BTC Send"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e("key", "BTC Send Local Addresss");
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                } else {
                    Log.e("key", "BTC Send Local Address");
                }

                if (title.equalsIgnoreCase("BTC Receive"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Log.e("key", "BTC Receive Local Addresss");
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                } else
                {
                    Log.e("key", "BTC Receive Local Address");
                }

                if (title.equalsIgnoreCase("Withdraw"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                }

                if (title.equalsIgnoreCase("Inr Recieved"))
                {
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    Log.e("key", "inr receive");
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                } else
                {
                    Log.e("key", "inr receive else");
                }
                if (title.equalsIgnoreCase("Inr Send"))
                {
                    Log.e("key", "inr send");
                    Intent intent = new Intent("refresh_wallet_balance");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    intent = new Intent(this, NotificationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("message", message);
                    intent.putExtra("date", "");
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    intent.putExtra("id", data.get("id"));
                    intent.putExtra("come", "firebase_message");
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    showSmallNotification(message, title, pendingIntent);
                    return;
                } else {
                    Log.e("key", "inr send else");
                }


                if (title.equalsIgnoreCase("User Account"))
                {
                    Log.e("key", "title" + "User Account");
                    if (data.get("status").equalsIgnoreCase("Inactive"))
                    {
                        Log.e("key", "title" + " User Account"+" inactive");
                        intent = new Intent(this, BlockScreen.class);
                        PreferenceFile.getInstance().saveData(this, Constant.Accunt_status, "Inactive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(121);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        showSmallNotification(message, title, pendingIntent);
                    }
                    else
                    {
                        Log.e("key", "title" + " User Account"+" active");
                        intent = new Intent(this, LoginNew.class);
                        PreferenceFile.getInstance().saveData(this, Constant.Accunt_status, "Active");
                        PreferenceFile.getInstance().saveData(this, Constant.COUNT_SECURITY, "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(121);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        showSmallNotification(message, title, pendingIntent);
                    }
                }
                else
                {
                    Log.e("key", "title" + "NO User Account");

//                if (Constant.checkActivation(getApplicationContext())) // foreground
//                {
                    if (PreferenceFile.getInstance().getPreferenceData(FirebaseMessages.this, Constant.LOCK_PIN) != null)
                    {
                        if (PreferenceFile.getInstance().getPreferenceData(FirebaseMessages.this, Constant.LOCK_PIN).equals("1"))
                        {
                            intent = new Intent(this, LoginNew.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(121);
                            intent.putExtra("key", "key");

                            if (type.equals("Broadcast"))
                            {
                                Log.e("broadcast", "in");
                                intent.putExtra("type", key);
                            }
                            intent.putExtra("type", key);
                            PreferenceFile.getInstance().saveData(this, Constant.ComeFrom, "1");
                            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            showSmallNotification(message, title, pendingIntent);

                        } else {
                            if (!key.equals(""))
                            {
                                Log.e("key", "in");
                                intent = new Intent(this, NotificationPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(121);
                                intent.putExtra("type", key);
                                intent.putExtra("key", "key");
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                showSmallNotification(message, title, pendingIntent);
                            }
                            else
                            {
                                intent = new Intent(this, Dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(121);
                                intent.putExtra("key", "key");
                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                showSmallNotification(message, title, pendingIntent);
                            }
                        }
                    } else {
                        Log.e("key", key + " lock pin null");
                        intent = new Intent(this, LoginNew.class);
                        PreferenceFile.getInstance().saveData(this, Constant.ComeFrom, "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(121);
                        intent.putExtra("key", "key");

                        if (type.equals("Broadcast")) {
                            Log.e("broadcast", "in");
                            intent.putExtra("type", key);
                        }

                        intent.putExtra("type", key);

                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        showSmallNotification(message, title, pendingIntent);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void showSmallNotification(String message, String title, PendingIntent pendingIntent)
    {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getResources().getString(R.string.app_name));

        if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.VIBRATE) != null)
        {
            Log.e("VIBRATE", "" + PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.VIBRATE));
        }
        if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.SOUND) != null)
        {
            Log.e("SOUND", "" + PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.SOUND));
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_ace)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setWhen(0);

        if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.SOUND) != null) {
            if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.SOUND).equalsIgnoreCase("on")) {
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


            } else {

            }
        }

        if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.VIBRATE) != null) {
            if (PreferenceFile.getInstance().getPreferenceData(getApplicationContext(), Constant.VIBRATE).equalsIgnoreCase("on")) {
                builder.setVibrate(new long[]{500, 500, 500, 500});
            } else {
                builder.setVibrate(new long[]{0L});
            }
        } else {
            builder.setVibrate(new long[]{0L});
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

        long[] v = {500, 1000};

    }

    @Override
    public void onMessageSent(String s)
    {

    }

}

