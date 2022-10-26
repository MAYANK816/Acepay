package com.acepay.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.acepay.Model.CustomModel;
import com.acepay.R;

import java.util.ArrayList;

/**
 * Created by pro22 on 23/11/17.
 */

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CustomModel> arrayList;
    private TextView tvCode,tvCountry;
    private ImageView category_image;

    public CustomAdapter(Context context, ArrayList<CustomModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(R.layout.customer_list, viewGroup, false);
        tvCode = (TextView) view.findViewById(R.id.tvCode);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);

        if(!arrayList.get(i).getCode().equals("")) {
            tvCode.setText("(" + arrayList.get(i).getCode() + ") ");
        }else {
            tvCode.setVisibility(View.GONE);
        }
        tvCountry.setText(arrayList.get(i).getCountry_name());



        return view;
    }
}