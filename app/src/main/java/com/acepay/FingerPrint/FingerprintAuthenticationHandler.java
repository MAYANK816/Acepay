package com.acepay.FingerPrint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private static FingerPrintLister mListener;

    // Constructor
    public FingerprintAuthenticationHandler(Context mContext) {
        context = mContext;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success){
        Log.e("update-->","update");
//        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
//        textView.setText(e);
        if(success){

            mListener.fingerPrintcall(e,success);
          //  textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
           Log.e("click-->","success");
        }
        else {
            mListener.fingerPrintcall(e,success);
        }
    }

    public static void bindListener(FingerPrintLister listener) {
        mListener = listener;
    }
}