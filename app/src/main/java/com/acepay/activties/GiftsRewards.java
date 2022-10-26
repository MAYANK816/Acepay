package com.acepay.activties;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Util.UtilClass;

import org.json.JSONObject;

import java.math.BigDecimal;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class GiftsRewards extends AppCompatActivity implements RetrofitResponse {

    ImageView ivarrow,ivGifts;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifts_rewards);

        findIds();

    }

    public void callGiftsRewards()
    {
        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, GiftsRewards.this, Constant.REQ_GIFTS_REWARDS, Constant.GIFTS_REWARDS).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void findIds()
    {
        ivarrow=(ImageView)findViewById(R.id.ivarrow);
        ivGifts=(ImageView)findViewById(R.id.ivGifts);

        ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        callGiftsRewards();
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {

        Log.e("GiftsRewardsResponse ",response+"");

        switch (requestCode)
        {
            case Constant.REQ_GIFTS_REWARDS:
                Log.e("GiftsRewardsResponseinside",response.body().toString()+"");

                try {

                    if (response.isSuccessful()) {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("GiftsRewards ",result.toString());


                        String status = result.getString("response");
                        String message = result.getString("message");


                        if (status.equals("true"))
                        {
                            JSONObject data = result.getJSONObject("data");

                            Picasso.with(GiftsRewards.this).load(Constant.Gift_reward_image+data.getString("gift"))
                                    .placeholder(getResources().getDrawable(R.drawable.placeholder)).into(ivGifts);

                        }
                        else
                        {

                        }

                    } else {
                        Constant.alertDialog(this, getResources().getString(R.string.try_again));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }
}
