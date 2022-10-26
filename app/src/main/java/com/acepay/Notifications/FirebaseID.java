package com.acepay.Notifications;

import android.content.SharedPreferences;
import android.util.Log;

import com.acepay.Util.App;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
public class FirebaseID extends FirebaseInstanceIdService
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String refToken;

    @Override
    public void onTokenRefresh() {
        Log.e("class call","yes");
        refToken= FirebaseInstanceId.getInstance().getToken();
        Log.e("priyarefToken",refToken);

        try
        {
            sharedPreferences= App.getIdPref();
            editor=sharedPreferences.edit();
            editor.putString("TOKEN",refToken);
            editor.commit();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }


}
