package com.acepay;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Deposit extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {
    ImageView ivarrow;
    TextView txName, tvUpload, tvdeposit, tvpath;
    String picturePath = "";
    public static File file;
    EditText edReference_no, txdate, edAmount;
    MultipartBody.Part body;
    DateFormat dateFormatter;
    DatePickerDialog datePickerDialog;
    private HashMap<String, RequestBody> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        txName = (TextView) findViewById(R.id.txName);
        tvUpload = (TextView) findViewById(R.id.tvUpload);
        tvpath = (TextView) findViewById(R.id.tvpath);
        tvdeposit = (TextView) findViewById(R.id.tvdeposit);
        tvpath = (TextView) findViewById(R.id.tvpath);
        edReference_no = (EditText) findViewById(R.id.edReference_no);
        txdate = (EditText) findViewById(R.id.txdate);
        edAmount = (EditText) findViewById(R.id.edAmount);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tvdeposit.setOnClickListener(this);
        txdate.setOnClickListener(this);
        tvUpload.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Normal Deposit");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Deposit.this, Dashboard.class);
        startActivity(intent);
    }

    private void date() {

        final Calendar newCalendar = Calendar.getInstance();
        final Date curDate = new Date();

        dateFormatter.format(curDate);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, -1);

                        if (minAdultAge.after(userAge)) {
                            Constant.alertDialog(Deposit.this, getString(R.string.please_select_valid_date));
                        } else {
                            txdate.setText(dateFormatter.format(userAge.getTime()));
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.ivarrow:

                intent = new Intent(Deposit.this, Dashboard.class);
                startActivity(intent);
                break;

            case R.id.txdate:

                date();
                break;

            case R.id.tvUpload:

                selectImage();

                break;

            case R.id.tvdeposit:

                if (validation()) {

                    Intent intent1 = new Intent(Deposit.this, CheckSecurePin.class);
                    intent1.putExtra("key", "deposit");
                    intent1.putExtra("transaction_id", edReference_no.getText().toString());
                    intent1.putExtra("deposit", edAmount.getText().toString());
                    intent1.putExtra("transaction_date", txdate.getText().toString());
                    intent1.putExtra("path", picturePath);
                    startActivity(intent1);

//                   map = new HashMap<String, RequestBody>();
//
//                   map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)));
//                   map.put("transaction_id", RequestBody.create(MediaType.parse("multipart/form-data"), edReference_no.getText().toString()));
//                   map.put("deposit", RequestBody.create(MediaType.parse("multipart/form-data"), edAmount.getText().toString()));
//                   map.put("transaction_date", RequestBody.create(MediaType.parse("multipart/form-data"),txdate.getText().toString()));
//
//                   if (Constant.isConnectingToInternet(Deposit.this)) {
//                       Log.e("connect--->", "2");
//                       new Retrofit2(Deposit.this, Deposit.this, map,body,Constant.REQ_Wallet_deposit, Constant.Wallet_deposit, "2").callService(true);
//                   }
//                   else {
//
//                       Log.e("connect--->", "no");
//                       Constant.alertDialog(Deposit.this, getResources().getString(R.string.check_connection));
//                   }
                }
                break;
        }
    }

    public boolean validation() {

        if (edReference_no.getText().toString().equals("")) {
            Constant.alertDialog(this, getString(R.string.please_enter_reference_no));
            return false;
        }
        if (txdate.getText().toString().equals("")) {

            Constant.alertDialog(this, getString(R.string.please_select_date));
            return false;

        }
        if (edAmount.getText().toString().equals("")) {
            Constant.alertDialog(this, getString(R.string.please_select_amount));
            return false;

        }
        if (edAmount.getText().toString().equals(".")) {
            Constant.alertDialog(this, getString(R.string.please_select_valid_amount));
            return false;
        }

        if (Double.parseDouble(edAmount.getText().toString()) <= 0) {
            Constant.alertDialog(this, getString(R.string.please_select_valid_amount));
            return false;
        }

        if (picturePath.equals("")) {
            Constant.alertDialog(this, getString(R.string.please_select_receipt_image));
            return false;
        }


        return true;
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Deposit.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("inner--->", requestCode + " resultCode " + resultCode);


        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // tvpath.setImageBitmap(photo);

                picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/PhysicsSketchpad";
                tvpath.setText(picturePath);
                File dir = new File(picturePath);
                if (!dir.exists())
                    dir.mkdirs();

                file = new File(dir, "sketchpad" + ".png");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                photo.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);

                Log.e("file exists-->", file.exists() + " file isfile-->" + file.isFile());

            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);

                tvpath.setText(picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                file = new File(picturePath);
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);
            }
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_Wallet_deposit:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());

                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    Log.e("result-->", result1.toString());

                    if (status.equals("true")) {

                        Log.e("sucuss-->", result1.toString());
                        Constant.alertWithIntent(this, message, Dashboard.class);
                    } else {
                        Constant.alertDialog(this, message);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

        }

    }

}
