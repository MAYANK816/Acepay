package com.acepay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.NotificationModel;
import com.acepay.NotificationDetails;
import com.acepay.R;


import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pro22 on 21/12/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyBookingHolder> {

    Context context;
    String dateStr;
    String timedate;
    BitCoinAddressAdapter.MyClickListener mcl;
    String key, amount;
    ArrayList<NotificationModel> list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
        Log.e("size-->", list.size() + "");
    }

    @Override
    public NotificationAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new NotificationAdapter.MyBookingHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(NotificationAdapter.MyBookingHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvtitle.setText(list.get(position).getNotification_title());
        holder.tvdesription.setText(list.get(position).getNotification_description());




        if (list.get(position).getRead_status().equalsIgnoreCase("Open")) {

            holder.imDot.setVisibility(View.VISIBLE);
        } else {
            holder.imDot.setVisibility(View.GONE);
        }

        if (list.get(position).getNotification_type().equals("Notification")) {
            holder.imgProfile.setBackground(context.getResources().getDrawable(R.drawable.bell));
            holder.imgProfile.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gradient_1)));
        }

        if (list.get(position).getNotification_type().equals("Alert")) {
            holder.imgProfile.setBackground(context.getResources().getDrawable(R.drawable.alert));
            holder.imgProfile.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gradient_1)));
        }

        if (list.get(position).getNotification_type().equals("Update")) {
            holder.imgProfile.setBackground(context.getResources().getDrawable(R.drawable.message));
            holder.imgProfile.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gradient_1)));
        }
        String timeCreated = list.get(position).getNotification_date();
        holder.tvdate.setText(timeCreated);
        Log.e("TimeCreated ",timeCreated);
      /*  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
        try {
            date = dateFormat.parse(timeCreated);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
            DateFormat dateformat = new SimpleDateFormat("HH:MM:SS"); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
            timedate = dateformat.format(date);
            Log.e("datetime", "" + dateStr);
            // String timess = dateStr.substring(19, 22);
            holder.tvdate.setText(dateStr + " " + timedate);



        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        holder.lnframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NotificationDetails.class);
                intent.putExtra("message", list.get(position).getNotification_description());
//                intent.putExtra("date", dateStr + " " + timedate);
                intent.putExtra("date", list.get(position).getNotification_date());
                intent.putExtra("title", list.get(position).getNotification_title());
                intent.putExtra("type", list.get(position).getNotification_type());
                intent.putExtra("id", list.get(position).getNotification_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvtitle, tvdesription, tvdate;
        LinearLayout lnframe;
        ImageView imgProfile, imDot;

        public MyBookingHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvtitle = (TextView) itemView.findViewById(R.id.tvtitle);
            tvdesription = (TextView) itemView.findViewById(R.id.tvdesription);
            imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
            imDot = (ImageView) itemView.findViewById(R.id.imDot);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            lnframe = (LinearLayout) itemView.findViewById(R.id.lnframe);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

            }
        }
    }

    public interface MyClickListener {

        public void setOnItemClick(View v, int pos);

        public void setEditlick(View v, int pos);
    }

    public void setOnItemClickListener(BitCoinAddressAdapter.MyClickListener mcl) {
        this.mcl = mcl;
    }

}