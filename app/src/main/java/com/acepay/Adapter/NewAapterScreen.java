package com.acepay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.acepay.Model.AdvertismentImages;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pro22 on 16/1/18.
 */

public class NewAapterScreen  extends PagerAdapter {

    private Context context;
    ArrayList<AdvertismentImages> arrayList;
    private LayoutInflater inflater;

    public NewAapterScreen(Context context, ArrayList<AdvertismentImages> arrayList){

        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {

        return arrayList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view.equals(object);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        System.gc();

        ImageView ivimage;
        LinearLayout lnLeaner;

        TextView tvText,tvdescription;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.new_adapter_layer, container, false);
        ivimage=(ImageView)viewLayout.findViewById(R.id.ivimage);
        lnLeaner=(LinearLayout) viewLayout.findViewById(R.id.lnLeaner);
        tvText=(TextView)viewLayout.findViewById(R.id.tvText);
        tvdescription=(TextView)viewLayout.findViewById(R.id.tvdescription);

  /*      Picasso.with(context)
                .load(Constant.Advert_image+ arrayList.get(position).getImage()).
                resize(800,800).placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .into(ivimage); */

        Picasso.with(context)
                .load(Constant.Advert_image+ arrayList.get(position).getImage())
                .placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .into(ivimage);

        tvText.setText(arrayList.get(position).getTitle());
        tvdescription.setText(arrayList.get(position).getDescription());
        ((ViewPager) container).addView(viewLayout);

        lnLeaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url="";

                if(arrayList.get(position).getAdvertisement_url().contains("http")){

                    url=arrayList.get(position).getAdvertisement_url();
                }
                else {
                    url= "http://"+arrayList.get(position).getAdvertisement_url();
                }

                Intent intent = new Intent(Intent.ACTION_VIEW,
                    //    Uri.parse(arrayList.get(position).getAdvertisement_url()));
                       Uri.parse(url));

                context.startActivity(intent);
            }
        });

        return viewLayout;

    }

}
