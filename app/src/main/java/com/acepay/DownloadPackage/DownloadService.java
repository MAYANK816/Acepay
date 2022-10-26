package com.acepay.DownloadPackage;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;



import androidx.media.app.NotificationCompat;

import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.RetrofitService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadService extends IntentService
{
    public DownloadService()
    {
        super("Download Service");
    }
    private androidx.core.app.NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    Intent intent;
    String id;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        id=intent.getStringExtra("D_id");

        Log.e("inside-->","download");
        Log.e("id-->",id);
        Log.e("storage-->",intent.getStringExtra("STORAGE"));
       // url=intent.getStringExtra("DOWNLOAD_URL");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(String.valueOf(System.currentTimeMillis())+".pdf")
//                .setContentText("Downloading..........")
//                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
        initDownload(intent.getStringExtra("D_id"),intent.getStringExtra("STORAGE"));
    }

    private void initDownload(String id,String store)
    {
        System.setProperty("http.keepAlive", "false");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.download(id);
        try
        {
            downloadFile(call.execute().body(),store);
            Log.e("call-->","yes");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(ResponseBody body,String storage) throws IOException
    {
        int count;
        byte data[] = new byte[1024 * 3];
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 6);
        File outputFile = new File(storage,
                System.currentTimeMillis()+".pdf");
        OutputStream output = new FileOutputStream(outputFile);
        while ((count = bis.read(data)) != -1)
        {
            output.write(data, 0, count);
        }
        Log.e("call service-->","yes");
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();
    }



    private void onDownloadComplete()
    {

        Download download = new Download();
        download.setProgress(100);
        intent=new Intent(this, ShowWebview.class);
        intent.putExtra("id",id);
        Log.e("Downloading-->","complete   "+id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationManager.cancel(0);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("Download Completed");
        notificationManager.notify(0, notificationBuilder.build());
        DownloadService.this.stopSelf();

        Toast.makeText(this, "Downloading Completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}