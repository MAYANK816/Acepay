package com.acepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.MyTicketModel;
import com.acepay.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pro22 on 11/1/18.
 */

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.MyBookingHolder> {

    Context context;
    BidAskAdapter.MyClickListener mcl;
    ArrayList<MyTicketModel> list;
    String key;
    DecimalFormat formatter = new DecimalFormat("#,##,###");

    public MyTicketAdapter(Context context, ArrayList<MyTicketModel> list,String key)
    {
        this.context=context;
        this.list=list;
        this.key=key;
    }

    @Override
    public MyTicketAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myticket_adapter,parent,false);
        return new  MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTicketAdapter.MyBookingHolder holder, int position) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'000z'");
        Date newDate = null;
        try {
            newDate = format.parse(list.get(position).getDate());
            format = new SimpleDateFormat("MMM dd,yyyy");
            String date = format.format(newDate);
            holder.tvdate.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvdescription.setText(list.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvdescription,tvdate;

        public MyBookingHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvdescription= (TextView) itemView.findViewById(R.id.tvdescription);
            tvdate= (TextView) itemView.findViewById(R.id.tvdate);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

            }
        }
    }


}
