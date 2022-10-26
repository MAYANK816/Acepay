package com.acepay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.RetrofitResponse;
import com.squareup.picasso.Picasso;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ViewProfile extends ToolabarActivity implements View.OnClickListener, RetrofitResponse
{
    ImageView imgProfile;
    TextView txName,tvverifed;
//    RadioButton rbMale,rbFemal,ivarrow;
    TextView edFirst,edlast,edEmail,edDob,edCity,edState,edCountry,edGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        txName= (TextView) findViewById(R.id.txName);
        edCity= (TextView) findViewById(R.id.edCity);

//        rbMale= (RadioButton) findViewById(R.id.rbMale);
//        rbFemal= (RadioButton) findViewById(R.id.rbFemal);
        edFirst= (TextView) findViewById(R.id.edFirst);
        edlast= (TextView) findViewById(R.id.edlast);
        edEmail= (TextView) findViewById(R.id.edEmail);
        edDob= (TextView) findViewById(R.id.edDob);
        edGender= (TextView) findViewById(R.id.edGender);
        edState= (TextView) findViewById(R.id.edState);
        edCountry= (TextView) findViewById(R.id.edCountry);
        tvverifed= (TextView) findViewById(R.id.tvverifed);


        txName.setVisibility(View.VISIBLE);
        txName.setText("View Profile");

        if(PreferenceFile.getInstance().getPreferenceData(this, Constant.email_verification)!=null){

            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.email_verification).equals("1")){
                tvverifed.setVisibility(View.GONE);
                tvverifed.setText("Verified");
                tvverifed.setTextColor(Color.parseColor("#2fd49e"));

            }else {
                tvverifed.setVisibility(View.GONE);
                tvverifed.setText("Not Verified");
                tvverifed.setTextColor(Color.parseColor("#d40e0e"));
            }
        }

        Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.City_name));

        edFirst.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username));
        edEmail.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Email));
        edDob.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Dob));
        edCity.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.City_name));
        edState.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.State_name));
        edCountry.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Country_name));

        Log.e("Gender---> ",PreferenceFile.getInstance().getPreferenceData(this,Constant.Gender));

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Gender).equals("Male"))
        {
//            rbMale.setChecked(true);
            edGender.setText("Male");
        }
        else
         {
//            rbFemal.setChecked(true);
            edGender.setText("Female");
         }

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Image)!=null)
        {

            Log.e("imagepath---> ",
                    Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image));

            Picasso.with(this).load(Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image))
                    .resize(100,100).placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .into(imgProfile);
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {

//           case  R.id.ivarrow:
//
//               finish();
//
//               intent=new Intent(ViewProfile.this,Dashboard.class);
//               startActivity(intent);
//
//            break;
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        switch (requestCode)
        {

        }
    }

}
