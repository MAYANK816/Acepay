package com.acepay;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Croping.Croping;
import com.acepay.Model.CountryModel;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class EditProfile extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow,imgProfile;
    TextView txName,txSubmit;
//  RadioButton rbMale,rbFemal;
    EditText edFirst,edlast,edEmail;
    EditText edDob;
    RequestBody req;
    Spinner spCountry,spState,spcity,edGender;
    ArrayList<CountryModel>Countrys;
    ImageView ivImageSelect;
    ArrayList<String> CountryList_name;
    ArrayList<CountryModel>States;
    ArrayList<CountryModel>City;
    ArrayList<String> State_name;
    ArrayList<String> city_name;
    String Country_id,state_id,city_id;
    File file;
    Bitmap bitmap;
    Intent CropIntent,CamIntent,GalIntent;
    private File profileIMG;
    private HashMap<String, RequestBody> map;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatePickerDialog datePickerDialog;;
    DateFormat dateFormatter;
    MultipartBody.Part body;
    Uri uri;
    String picturePath="";
    String[] defaultGender = {"Select Gender","Male","Female"};
    String[] defaultState = {"Select state"};
    String[] defaultCity = {"Select city"};
    Intent cameraIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_edit_profile);

        Countrys=new ArrayList<>();
        CountryList_name=new ArrayList<>();
        States=new ArrayList<>();
        City=new ArrayList<>();
        State_name=new ArrayList<>();
        city_name=new ArrayList<>();
        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        ivImageSelect= (ImageView) findViewById(R.id.ivImageSelect);
        txName= (TextView) findViewById(R.id.txName);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

//      rbMale= (RadioButton) findViewById(R.id.rbMale);
//      rbFemal= (RadioButton) findViewById(R.id.rbFemal);
        edFirst= (EditText) findViewById(R.id.edFirst);
        edlast= (EditText) findViewById(R.id.edlast);
        edEmail= (EditText) findViewById(R.id.edEmail);
        edDob= (EditText) findViewById(R.id.edDob);
        spState= (Spinner) findViewById(R.id.spState);
        spCountry= (Spinner) findViewById(R.id.spCountry);
        spcity= (Spinner) findViewById(R.id.spcity);
        edGender= (Spinner) findViewById(R.id.edGender);
        txSubmit= (TextView) findViewById(R.id.txSubmit);

        edDob.setOnClickListener(this);
        ivImageSelect.setOnClickListener(this);
        txSubmit.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Add Profile");
        //callService();

        callStateService();

        edGender.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,defaultGender));
        spState.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,defaultState));
        spcity.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,defaultCity));

    }

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select image from gallery"), 2);

    }

    public void popup()
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View diaview = inflater.inflate(R.layout.choose_image, null);
        dialogBuilder.setView(diaview);
        final ImageView cancal = (ImageView) diaview.findViewById(R.id.camera);
        final ImageView gallery = (ImageView) diaview.findViewById(R.id.gallery);
        final android.app.AlertDialog dialog = dialogBuilder.create();
        cancal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    try {

                        startActivityForResult(cameraIntent, 33);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                dialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 111);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
//    public void saveImage(Bitmap bitmap) {
//
////        cropImage(getRealPathFromURI(imageUri));
//
//        File storagepath = new File(Environment.getExternalStorageDirectory() + "/photos/");
//        storagepath.mkdirs();
//
//        File image = new File(storagepath, Long.toString(System.currentTimeMillis()) + ".jpg");
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
//        try {
//            FileOutputStream outputStream = new FileOutputStream(image);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void date() {

        final Calendar newCalendar = Calendar.getInstance();
        final Date curDate = new Date();

        dateFormatter.format(curDate);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    Constant.alertDialog(EditProfile.this,getString(R.string.please_select_valid_date));
                }
                else {
                    edDob.setText(dateFormatter.format(userAge.getTime()));
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, EditProfile.this, Constant.REQ_Country, Constant.Country).callService(true);
        } else {

            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, EditProfile.this, Constant.REQ_State, Constant.State+PreferenceFile.getInstance().getPreferenceData(this,Constant.Courtry_id)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

//    public void ImageCropFunction() {
//
//        //Image Crop Code
//        try {
//
//            CropIntent = new Intent("com.android.camera.action.CROP");
//
//            CropIntent.setDataAndType(uri, "image/*");
//
//            CropIntent.putExtra("crop", "true");
//            CropIntent.putExtra("outputX", 1080);
//            CropIntent.putExtra("outputY", 1080);
//            CropIntent.putExtra("aspectX", 4);
//            CropIntent.putExtra("aspectY", 4);
//            CropIntent.putExtra("scaleUpIfNeeded", true);
//            CropIntent.putExtra("return-data", true);
//            startActivityForResult(CropIntent, 1);
//        } catch (ActivityNotFoundException e) {
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33 && resultCode == RESULT_OK)
        {
            // TODO:  image from Camera
            bitmap=(Bitmap)data.getExtras().get("data");
            Uri imageUri = getImageUri(getApplicationContext(),bitmap);
            File finalFile = new File(getRealPathFromURI(imageUri));
            picturePath=imageUri.getPath();
            imgProfile.setImageBitmap(bitmap);
            Log.e("PathGet ",picturePath);
            file = new File(picturePath);
            Log.e("file--->", file.isFile() + " " + file.exists());
            req = RequestBody.create(MediaType.parse("image"), finalFile);
            body = MultipartBody.Part.createFormData("image", file.getName(), req);

        }
        else if (requestCode == 111 && resultCode == RESULT_OK && data != null)
        {
            /*Gallery Image*/
            Uri uri = data.getData();
            picturePath = getPath(uri);

            Log.e("PathGet ",picturePath);


            //   imagepath = data.getExtras().get("PATH").toString();
            file = new File(picturePath);
            Picasso.with(this).load(file).placeholder(R.drawable.placeholder).
                    into(imgProfile);

            Log.e("file--->", file.isFile() + " " + file.exists());
            RequestBody req = RequestBody.create(MediaType.parse("image"), file);
            body = MultipartBody.Part.createFormData("aadhar", file.getName(), req);


            //     cropImage(picturePath);
        }
        else if (requestCode == 301 && resultCode==1001)
        {
          /*Gallery/Camera Cropped Image*/

            picturePath = data.getExtras().get("PATH").toString();
            file = new File(picturePath);
            Log.e("file-->",file.exists()+"");
            Picasso.with(this).load(file).placeholder(R.drawable.placeholder).into(imgProfile);

            Log.e("file--->", file.isFile() + " " + file.exists());
            RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        }

    }

    private void cropImage(String path)
    {
        Intent intent = new Intent(this, Croping.class);
        intent.putExtra("SQUARE", path);
        startActivityForResult(intent, 301);
    }

    public String getPath(Uri uri)
    {
        if( uri == null )
        {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    public boolean verification(){

        if(edFirst.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_first_name));
            return false;
        }
        if(edlast.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_last_name));

            return false;
        }
        if(edEmail.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_email));

            return false;
        }
        if (!edEmail.getText().toString().matches(emailPattern)) {
            Constant.alertDialog(this, getString(R.string.please_enter_valid_email));
            return false;
        }
        if(edGender.getSelectedItem().toString().equalsIgnoreCase("Select gender")){

            Constant.alertDialog(this,"Please select gender");

            return false;
        }

        if(edDob.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_dob));
            return false;
        }

        if(spState.getSelectedItem().toString().equals("Select state")){

            Constant.alertDialog(this,getString(R.string.please_select_state));

            return false;
        }

        if(spcity.getSelectedItem().toString().equals("Select city")){

            Constant.alertDialog(this,getString(R.string.please_select_city));

            return false;
        }

        return true;
    }

    private void selectImage() {

        final CharSequence[] options = {"Take photo", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                if (options[item].equals("Choose from gallery")) {
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
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.ivImageSelect:
              //selectImage();
                popup();
                break;

            case R.id.edDob:
                date();
                break;

            case R.id.txSubmit:

                Constant.hideKeyboard(this,v);

                if(verification()){

                    String gender = null;

                    if(edGender.getSelectedItemPosition()==2){
                        gender="Female";
                    }else if(edGender.getSelectedItemPosition()==1){
                        gender="Male";
                    }

                    map = new HashMap<String, RequestBody>();

                    map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)));
                    map.put("first_name", RequestBody.create(MediaType.parse("multipart/form-data"), edFirst.getText().toString()));
                    map.put("last_name", RequestBody.create(MediaType.parse("multipart/form-data"), edlast.getText().toString()));
                    map.put("email", RequestBody.create(MediaType.parse("multipart/form-data"), edEmail.getText().toString()));
                    map.put("state", RequestBody.create(MediaType.parse("multipart/form-data"), state_id));
                    map.put("country", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this,Constant.Courtry_id)));
                    map.put("city", RequestBody.create(MediaType.parse("multipart/form-data"), city_id));
                    map.put("dob", RequestBody.create(MediaType.parse("multipart/form-data"), edDob.getText().toString()));
                    map.put("gender", RequestBody.create(MediaType.parse("multipart/form-data"), gender));

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this,Constant.ID));
                        postParam.put("first_name", edFirst.getText().toString());
                        postParam.put("last_name", edlast.getText().toString());
                        postParam.put("email", edEmail.getText().toString());
                        postParam.put("state",state_id);
                        postParam.put("country",Country_id);
                        postParam.put("dob", edDob.getText().toString());
                        postParam.put("gender", gender);
                        Log.e("postparam--->", postParam.toString());

                        if(picturePath.equals("")){

                            if (Constant.isConnectingToInternet(EditProfile.this)) {
                                Log.e("connect--->", "4");
                                new Retrofit2(this, EditProfile.this, map, Constant.REQ_ADD_PROFILE, Constant.ADD_PROFILE,"4").callService(true);
                            }
                            else {

                                Log.e("connect--->", "no");
                                Constant.alertDialog(EditProfile.this, getResources().getString(R.string.check_connection));
                            }
                        }
                        else {

                            if (Constant.isConnectingToInternet(EditProfile.this)) {
                                Log.e("connect--->", "2");
                                new Retrofit2(EditProfile.this, EditProfile.this, map,body,Constant.REQ_ADD_PROFILE, Constant.ADD_PROFILE, "2").callService(true);
                            }
                            else {

                                Log.e("connect--->", "no");
                                Constant.alertDialog(EditProfile.this, getResources().getString(R.string.check_connection));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                break;
        }

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_Country:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        Countrys.clear();
                        CountryList_name.clear();
                        CountryList_name.add(0, "Select country");

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++){
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            CountryList_name.add(CountryObj.getString("name"));
                            Countrys.add(country);

                            setAdapter();
                        }

                    }else {

                        Constant.alertDialog(this,message);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_State:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        States.clear();
                        State_name.clear();
                        State_name.add(0, "Select state");

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++){
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            State_name.add(CountryObj.getString("name"));
                            States.add(country);

                            setStateAdapter();
                        }

                    }else {

                        Constant.alertDialog(this,message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_City:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        City.clear();
                        city_name.clear();
                        city_name.add(0, "Select city");


                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++){
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            city_name.add(CountryObj.getString("name"));
                            City.add(country);

                            setCityAdapter();
                        }

                    }else {

                        Constant.alertDialog(this,message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_ADD_PROFILE:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    Log.e("result-->",result1.toString());

                    if(status.equals("true")){

                        JSONObject data=result1.getJSONObject("data");
                        PreferenceFile.getInstance().saveData(this, Constant.Username, data.getString("first_name")+" "+data.getString("last_name"));
                        PreferenceFile.getInstance().saveData(this, Constant.Email, data.getString("email"));
                        PreferenceFile.getInstance().saveData(this, Constant.Dob, data.getString("dob"));
                        PreferenceFile.getInstance().saveData(this, Constant.Gender, data.getString("gender"));
                        PreferenceFile.getInstance().saveData(this, Constant.Image, data.getString("image"));
                        PreferenceFile.getInstance().saveData(this, Constant.City_name, data.getString("city"));

                        PreferenceFile.getInstance().saveData(this, Constant.City_name, data.getString("city"));
                        PreferenceFile.getInstance().saveData(this, Constant.VERIFY_PAN, data.getString("verify_pan"));
                        PreferenceFile.getInstance().saveData(this, Constant.VERIFY_BANK, data.getString("verify_bank"));
                        PreferenceFile.getInstance().saveData(this, Constant.VERIFY_Adhaar, data.getString("verify_add"));

                        JSONObject StateName=data.getJSONObject("StateName");
                        PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                        PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));


                        JSONObject CountryName=data.getJSONObject("CountryName");
                        PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                        PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                        Log.e("result-->",result1.toString());
                        Constant.alertWithIntent(this,message,Dashboard.class);
                    }
                    else {

                        Constant.alertDialog(this,message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }

    }

    public void setAdapter(){

        spCountry.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,CountryList_name));

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spCountry.getSelectedItem().equals("Select country")) {

                }

                if (!spCountry.getSelectedItem().equals("Select country")){

                    Country_id = Countrys.get(spCountry.getSelectedItemPosition() - 1).getCountry_id();

                    callStateService();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setStateAdapter() {

        spState.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,State_name));

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spState.getSelectedItem().equals("Select state")) {
                    city_name.clear();
                    City.clear();
                    spcity.setAdapter(new ArrayAdapter<String>(EditProfile.this,R.layout.new_text, defaultCity));
                }

                if (!spState.getSelectedItem().equals("Select state")){

                    state_id = States.get(spState.getSelectedItemPosition() -1).getCountry_id();

                    Log.e("state--->",state_id);
                    callcity();
                    //callStateService();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void callcity(){

        Log.e("callservice-->","yes");

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, EditProfile.this, Constant.REQ_City, Constant.City+state_id).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void setCityAdapter() {

        spcity.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,city_name));

        spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spcity.getSelectedItem().equals("Select city")) {
                    /*StateList_name.clear();
                    States.clear();
                    spState.setAdapter(new ArrayAdapter<String>(Signup.this, R.layout.spinner_item, defaultState));*/
                }

                Log.e("city namew--->",spcity.getSelectedItem().toString());

                if (!spcity.getSelectedItem().toString().equals("Select city")){

                    Log.e("city selet-->",spcity.getSelectedItemPosition()+"");
                    Log.e("city size-->",City.size()+"");

                    city_id = City.get(spcity.getSelectedItemPosition() - 1).getCountry_name();

                    Log.e("city--->",city_id);


                    // callStateService();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
