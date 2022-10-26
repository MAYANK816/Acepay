package com.acepay.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.acepay.Model.ActivitiesModel;
import com.acepay.R;
import com.acepay.Util.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {

    Context context;

    ArrayList<ActivitiesModel> list;

    public ActivitiesAdapter(Context context,ArrayList<ActivitiesModel> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_activities,viewGroup,false);
        return new ActivitiesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {

        if (list.get(position).getImage().equalsIgnoreCase("admin"))
        {
           myViewHolder.ivProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }
        else {
            Picasso.with(context).load(Constant.ImagePath+list.get(position)).placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                    .into(myViewHolder.ivProfile);
        }

        myViewHolder.tvProfileName.setText(list.get(position).getName());

        if (list.get(position).getType().equalsIgnoreCase("buy_btc"))
        {
            myViewHolder.tvType.setText("Buy Btc");
        }else if (list.get(position).getType().equalsIgnoreCase("sell_btc"))
        {
            myViewHolder.tvType.setText("Sell Btc");
        }else if (list.get(position).getType().equalsIgnoreCase("transfer_btc"))
        {
            myViewHolder.tvType.setText("Transfer/Receive Btc");
        }


        myViewHolder.tvDate.setText(list.get(position).getDate_time());
        myViewHolder.tvBtc.setText("B "+list.get(position).getBtc());

//2020-04-30T01:25:47+00:00
     /*   String finalDay="";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'000z'");
        try {
            Date dt1=format1.parse(list.get(position).getDate_time());
            DateFormat format2=new SimpleDateFormat("dd/mm/yyyy hh:mm a");
            finalDay=format2.format(dt1);
            Log.e("FinalDay ",+position+finalDay);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        myViewHolder.tvDate.setText(finalDay);*/

        if (list.get(position).getType().equalsIgnoreCase("sell_btc"))
        {
            myViewHolder.tvBtcRate.setVisibility(View.VISIBLE);
            myViewHolder.tvType.setTextColor(context.getResources().getColor(R.color.red_light));
            myViewHolder.tvBtc.setTextColor(context.getResources().getColor(R.color.red_light));
            myViewHolder.tvBtcRate.setText("Rate "+list.get(position).getBtc_rate());
        }
        else if (list.get(position).getType().equalsIgnoreCase("transfer_btc"))
        {
            myViewHolder.tvType.setTextColor(context.getResources().getColor(R.color.sky_blue));
            myViewHolder.tvBtc.setTextColor(context.getResources().getColor(R.color.sky_blue));
            myViewHolder.tvBtcRate.setVisibility(View.GONE);
        }
        else {
            myViewHolder.tvBtcRate.setVisibility(View.VISIBLE);
            myViewHolder.tvType.setTextColor(context.getResources().getColor(R.color.light_green));
            myViewHolder.tvBtc.setTextColor(context.getResources().getColor(R.color.light_green));
            myViewHolder.tvBtcRate.setText("Rate "+list.get(position).getBtc_rate());
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvProfileName,tvType,tvBtcRate,tvDate,tvBtc;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ivProfile=(ImageView)itemView.findViewById(R.id.ivProfile);
            tvProfileName=(TextView) itemView.findViewById(R.id.tvProfileName);
            tvType=(TextView) itemView.findViewById(R.id.tvType);
            tvBtcRate=(TextView) itemView.findViewById(R.id.tvBtcRate);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvBtc=(TextView) itemView.findViewById(R.id.tvBtc);
        }
    }
}
