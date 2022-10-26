package com.acepay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Invoice;
import com.acepay.Model.Transaction_model;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pro22 on 20/12/17.
 */

public class INRAdapter extends RecyclerView.Adapter<INRAdapter.MyBookingHolder> {

    Context context;
    BitCoinAddressAdapter.MyClickListener mcl;
    ArrayList<Transaction_model> list;

    public INRAdapter(Context context, ArrayList<Transaction_model> list)
    {
        this.context=context;
        this.list=list;
        Log.e("adapter-->",list.size()+"");
    }

    @Override
    public INRAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inr_transaction,parent,false);
        return new INRAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(INRAdapter.MyBookingHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.description.setText(list.get(position).getDescription());
       // holder.txstatur.setText(list.get(position).getStatus());
        holder.transaction_id.setText("Order id: "+list.get(position).getTransaction_id());

        if(list.get(position).getStatus().equalsIgnoreCase("Yes") || list.get(position).getStatus().equalsIgnoreCase("Active")|| list.get(position).getStatus().equalsIgnoreCase("success")|| list.get(position).getStatus().equalsIgnoreCase("approved")){
            Log.e("btc-->","inside5");
            holder.txstatur.setTextColor(context.getResources().getColor(R.color.button_background));
            holder.txstatur.setText("Success");
        }
        else if(list.get(position).getStatus().equalsIgnoreCase("no")){
            Log.e("btc-->","inside5");
            holder.txstatur.setTextColor(context.getResources().getColor(R.color.button_background));
            holder.txstatur.setText("Pending");
        }
        else {
            Log.e("btc-->","inside5");
            holder.txstatur.setTextColor(Color.RED);
            holder.txstatur.setText("Failure");
        }
        
        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5");
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }
        if(list.get(position).getBtc().equals("4"))
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.withdraw_circle) );
        }
        if(list.get(position).getBtc().equals("1") )
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.inr_wallet) );
            Log.e("btc-->","inside1");
        }
        if( list.get(position).getBtc().equals("2")){

            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_inr_circle) );
        }
        if(list.get(position).getBtc().equals("3")){
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.deposit_circle) );
        }

        String timeCreated = list.get(position).getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = format.parse(timeCreated);
            holder.date.setText(Constant.getTimeAgo(new DateTime(d1).getMillis(),context));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(list.get(position).getBtc().equals("2") || list.get(position).getBtc().equals("1")){
            holder.tvInvoice.setVisibility(View.GONE);
        }

        if(list.get(position).getBtc().equalsIgnoreCase("1"))
        {  // get reciver name/number

            holder.txNumber.setVisibility(View.VISIBLE);
            holder.txName.setVisibility(View.VISIBLE);
            holder.txName.setText("Receiver name: "+list.get(position).getReceiver_name());
            holder.txNumber.setText("Receiver Number: "+list.get(position).getReceiver_no());
        }

        else if(list.get(position).getBtc().equalsIgnoreCase("2")){

            holder.txNumber.setVisibility(View.VISIBLE);
            holder.txName.setVisibility(View.VISIBLE);


            //todo when welcome money transfer by admin by starting verification process
            if (list.get(position).getTransaction_by().equalsIgnoreCase("2"))
            {

                holder.txName.setText("Sender name: "+"Admin");
                holder.txNumber.setText("Sender Number: "+"Admin");
            }
            else {

                holder.txName.setText("Sender name: "+list.get(position).getReceiver_name());
                holder.txNumber.setText("Sender Number: "+list.get(position).getReceiver_no());
            }


        }
        else
        {
            holder.txNumber.setVisibility(View.GONE);
            holder.txName.setVisibility(View.GONE);
        }

        Log.e("amountadapter--->", list.get(position).getAmount());

        Double d=Double.parseDouble(list.get(position).getAmount());

        holder.txbit.setText(String.format("%.2f", d) + " " +
                PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));

        holder.lnlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Invoice.class);

                Log.e("order_id-->",list.get(position).getId());
                intent.putExtra("order_id", list.get(position).getId());
                if(list.get(position).getBtc().equals("4")) {
                    intent.putExtra("key", "withdraw");
                }
                if(list.get(position).getBtc().equals("3")) {
                    intent.putExtra("key","deposit");
                }
                if(list.get(position).getBtc().equals("1")) {
                    intent.putExtra("key","inrtransfer");
                }
                if(list.get(position).getBtc().equals("2")) {
                    intent.putExtra("key","InrReceive");
                }
                if(list.get(position).getBtc().equals("5")) {
                    intent.putExtra("key","payu");
                }
                if(list.get(position).getBtc().equals("6")) {
                    intent.putExtra("key","paypal");
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date,description,transaction_id,txstatur,txbit,rate,tvInvoice,txName,txNumber;
        LinearLayout lnlayer;
        ImageView ivlogoicon;

        public MyBookingHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            date= (TextView) itemView.findViewById(R.id.date);
            description= (TextView) itemView.findViewById(R.id.description);
            transaction_id= (TextView) itemView.findViewById(R.id.transaction_id);
            ivlogoicon= (ImageView) itemView.findViewById(R.id.ivlogoicon);
            rate= (TextView) itemView.findViewById(R.id.rate);
            txstatur= (TextView) itemView.findViewById(R.id.txINR);
            txbit= (TextView) itemView.findViewById(R.id.txbit);
            tvInvoice= (TextView) itemView.findViewById(R.id.tvInvoice);
            lnlayer= (LinearLayout) itemView.findViewById(R.id.lnlayer);
            txName= (TextView) itemView.findViewById(R.id.txName);
            txNumber= (TextView) itemView.findViewById(R.id.txNumber);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

            }
        }
    }

}
