package com.acepay.Zendesk;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.MainSupport;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class SubmitRequest extends AppCompatActivity implements RetrofitResponse {
    ImageView ivarrow;
    TextView txName,txSubmit,tv_attechment,tv_date,tv_ordernu,tv_amt;
    EditText ed_catagary,ed_descrption,ed_date,ed_ordernu,ed_amt;
    String sub="";
    int flag=0;
    String imaggetoken;
    String picturePath="";
    File file;
    ProgressDialog progressDialog;
    Intent GalIntent,CropIntent;
    Uri uri;
   // UploadProvider provider;
    String tictid="";
    String on="dd";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_submit_request);
       /* ZendeskConfig.INSTANCE.init(this, "https://metapay.zendesk.com", "80907c42a02e325dcc3a3ecc46a873f8f37177194ad405d5", "mobile_sdk_client_3249035b3512d019b36c");
        Identity identity = new AnonymousIdentity.Builder().withNameIdentifier("Generic").build();

        ZendeskConfig.INSTANCE.setIdentity(identity);*/

      //  provider = ZendeskConfig.INSTANCE.provider().uploadProvider();
        // new SupportActivity.Builder().show(SubmitRequest.this);

        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        ed_descrption = (EditText) findViewById(R.id.ed_descrption);
        ed_date = (EditText) findViewById(R.id.ed_date);
        ed_amt = (EditText) findViewById(R.id.ed_amt);
        ed_ordernu = (EditText) findViewById(R.id.ed_ordernu);
        ed_catagary = (EditText) findViewById(R.id.ed_catagary);
        txName = (TextView) findViewById(R.id.txName);
        tv_amt = (TextView) findViewById(R.id.tv_amt);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_ordernu = (TextView) findViewById(R.id.tv_ordernu);
        txSubmit = (TextView) findViewById(R.id.txSubmit);
        tv_attechment = (TextView) findViewById(R.id.tv_attechment);


        txName.setText("Submit a request");

        Bundle bundle = getIntent().getExtras();

        ed_catagary.setText(bundle.getString("first"));
        if (ed_catagary.getText().equals("I have made Rs withdrawal request in the app"))
        {
            // on=bundle.getString("on");
            // if (on.equals("on")){
            tv_date.setVisibility(View.VISIBLE);
            tv_ordernu.setVisibility(View.VISIBLE);
            ed_date.setVisibility(View.VISIBLE);
            ed_ordernu.setVisibility(View.VISIBLE);
            ed_amt.setVisibility(View.VISIBLE);
            tv_amt.setVisibility(View.VISIBLE);
            //}
        }

/*
        txSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_descrption.getText().toString().equals("")) {
                    request();
                    CreateRequest createRequest = new CreateRequest();
                    createRequest.setSubject(ed_catagary.getText().toString());
                    createRequest.setDescription(ed_descrption.getText().toString());
                 //   createRequest.setTags(Arrays.asList("printer", PreferenceFile.getInstance().getPreferenceData(SubmitRequest.this,Constant.Username)));

                    if(flag==1){
                        Log.e("imagetoken-->","click");
                        createRequest.setAttachments(Arrays.asList(imaggetoken));
                    }

                    List<CustomField> customFields = new ArrayList<CustomField>() {{

                        Long customFieldId = 1234567L;
                        String customFieldValue = "Android 5.0";
                        CustomField androidVersionCustomField = new CustomField(customFieldId, customFieldValue);

                        add(androidVersionCustomField);
                    }};

                    createRequest.setCustomFields(customFields);

                    RequestProvider requestProvider = ZendeskConfig.INSTANCE.provider().requestProvider();

                    requestProvider.createRequest(createRequest, new ZendeskCallback<CreateRequest>() {
                        @Override
                        public void onSuccess(CreateRequest createRequest) {

                            progressDialog.dismiss();

                            Constant.alertWithIntent(SubmitRequest.this,"Sucessfully ticket created.", MainSupport.class);
                            Log.e("onSuccess", "getDescription-- " + createRequest.getDescription());
                            Log.e("onSuccess", "getDescription-- " + createRequest.getId());
                            Log.e("onSuccess", "getDescription-- " + createRequest.getEmail());
                            Log.e("onSuccess", "getDescription-- " + createRequest.getSubject());
                            tictid=createRequest.getId();
                          //  callmyservice();

                            // Logger.i(LOG_TAG, "Request created...");
                        }

                        @Override
                        public void onError(ErrorResponse errorResponse) {

                            progressDialog.dismiss();
                            Log.e("onError", "Request");
                            Log.e("onError", errorResponse.toString());
                            Log.e("getReason", errorResponse.getReason());
                            Log.e("getResponseBody", errorResponse.getResponseBody());
                            Log.e("getStatus", errorResponse.getStatus() + "");
                            // Logger.e(LOG_TAG, errorResponse);
                        }
                    });
                } else {
                    Toast.makeText(SubmitRequest.this, "Please enter description first", Toast.LENGTH_SHORT).show();
                    Log.e("onError", "else");
                }
            }

        });
*/
        tv_attechment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageFromGallery();
            }
        });

        ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivarrow.setVisibility(View.VISIBLE);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Submit a request");
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


    void request() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(SubmitRequest.this, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(SubmitRequest.this);
        }
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {
                uri = data.getData();
                ImageCropFunction();
            }
        }
        else if (requestCode == 1)
        {
             /*Gallery/Camera Cropped Image*/

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                //  imgProfile.setImageBitmap(bitmap);

                //imagepath="1";

                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file = new File(dir, System.currentTimeMillis() + ".png");
                if (file.exists()) {
                    file.delete();
                }
                file = new File(dir, System.currentTimeMillis() + ".png");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file.getPath());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("file--->", file.isFile() + " " + file.exists());

                flag=1;
                //   CreateRequest createRequest = new CreateRequest();
                // createRequest.setAttachments(Arrays.asList("{attachmentToken}"));
              /*  final File fileToUpload = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "picture.png");
                Log.e("getStatus",fileToUpload.toString());
*/
                request();
/*
                provider.uploadAttachment("screenshot.png", file, "image/png",  new ZendeskCallback<UploadResponse>() {
                    @Override
                    public void onSuccess(UploadResponse uploadResponse) {
                        Log.e("getToken",uploadResponse.getToken());
                        Log.e("getExpiresAt",uploadResponse.getExpiresAt().toString());
                        progressDialog.dismiss();
                         imaggetoken=uploadResponse.getToken();
                        // Handle success
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        progressDialog.dismiss();
                        Log.e("getStatus",""+errorResponse.getStatus());
                        Log.e("getReason",errorResponse.getReason());
                        Log.e("getResponseBodyType",errorResponse.getResponseBodyType());
                        Log.e("getResponseBody",errorResponse.getResponseBody());
                        // Handle error
                    }
                });
*/
                //req = RequestBody.create(MediaType.parse("image"), file);
                // body = MultipartBody.Part.createFormData("passbook_image", file.getName(), req);
            }
        }
    }
    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    public void callmyservice(){

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("ticket_id",tictid);
            postParam.put("description", ed_descrption.getText().toString());
            postParam.put("subject", ed_catagary.getText().toString());
            //postParam.put("user_id","111");
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));

            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(SubmitRequest.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(this, SubmitRequest.this, postParam, Constant.REQ_CREATETICKET, Constant.CREATETICKET, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(SubmitRequest.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        Log.e("REQ_CREATETICKET", "REQ_CREATETICKET");
        switch (requestCode) {
            case Constant.REQ_CREATETICKET:
                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");
                            Constant.alertWithIntent(this, "Ticket Created Sucessfully", MainSupport.class);

                        } else {
                            Constant.alertDialog(this, message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }
                break;
        }
    }

}
