package com.acepay.Adapter;

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

import com.acepay.BitcoinTransaction;
import com.acepay.Model.Transaction_model;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BitcoinTransactionTypeAdapter extends RecyclerView.Adapter<BitcoinTransactionTypeAdapter.MyBookingHolder> {

    Context context;
    BitCoinAddressAdapter.MyClickListener mcl;
    ArrayList<Transaction_model> list;

    public BitcoinTransactionTypeAdapter()
    {
    }

    public BitcoinTransactionTypeAdapter(Context context, ArrayList<Transaction_model> list)
    {
        this.context=context;
        this.list=list;
        Log.e("adapter-->",list.size()+"");
    }

    @Override
    public BitcoinTransactionTypeAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bitcoin_transaction_type_adapter,parent,false);
        return new BitcoinTransactionTypeAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(BitcoinTransactionTypeAdapter.MyBookingHolder holder, final int position)
    {

        Log.e("btc-->",list.get(position).getBtc());

        if(!list.get(position).getStatus().equalsIgnoreCase("success")
                && !list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("inside-->","notsuccess");
            holder.tvInr.setTextColor(Color.RED);
        }

        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5buybtc");
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.walleticon) );
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.wallet_black_new) );
        }
        if(list.get(position).getBtc().equals("4"))
        {
            //rec btc
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.black_receive) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.sell_bit_circle) );
        }
        if(list.get(position).getBtc().equals("1") )
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.transfer_black) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.send_circle) );
            Log.e("btc-->","inside1sendbtc");
        }
        if( list.get(position).getBtc().equals("2")){
//rec btc
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.black_receive) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.sell_bit_circle) );
        }
        if(list.get(position).getBtc().equals("3")){
            Log.e("btc-->","inside5buybtc");

            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.wallet_black_new) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.walleticon) );
        }
        if(list.get(position).getBtc().equals("6")){
// dashborard rec btc icon
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.black_receive) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }


        if(!list.get(position).getInrAmount().equals("null")) {

            double inr = Double.parseDouble(list.get(position).getInrAmount());

            BigDecimal inrd = new BigDecimal(inr);


                holder.tvInr.setText(String.format("%.2f", inrd)
                        + " " + PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));


            //holder.tvInr.setText(String.format("%.2f", inrd) + " " + PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));
        }
      //  holder.tvInr.setText(list.get(position).getInrAmount());

        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5");
            holder.description.setText("Acepay Purchased");
        }
        else if(list.get(position).getBtc().equals("3") && list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("btc-->","inside3");
            holder.description.setText("Acepay Purchased");
        }
        else if(list.get(position).getBtc().equals("2") && list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("btc-->","inside3");
            holder.description.setText("Acepay sold");
        }
        else {
            holder.description.setText(list.get(position).getDescription());
        }

        String timeCreated = list.get(position).getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = format.parse(timeCreated);
            holder.date.setText(Constant.getTimeAgo(new DateTime(d1).getMillis(),context));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //   holder.date.setText(Constant.getTimeAgo(new DateTime(timeCreated).getMillis(),context));

        Log.e("amountadapter--->", list.get(position).getAmount());

        Double d=Double.parseDouble(list.get(position).getAmount());

        /*if(list.get(position).getBtc().equals("2") || list.get(position).getBtc().equals("3")){

            holder.txbit.setText(String.format("%.0f", d)+" "+PreferenceFile.getInstance().getPreferenceData(context,Constant.Currency_Symbol));
        }
        else {*/

            holder.txbit.setText(String.format("%.8f", d) + " Ace");
        //}

        //holder.txbit.setText(list.get(position).getAmount()+" Ace");

        holder.lnlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BitcoinTransaction.class);
                intent.putExtra("order_id", list.get(position).getId());
                intent.putExtra("key", "btc");
                intent.putExtra("type",list.get(position).getBtc());

                Log.e("btc-->",list.get(position).getBtc());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date,description,txbit,tvInr;
        LinearLayout lnlayer;
        ImageView ivlogoicon;

        public MyBookingHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            date= (TextView) itemView.findViewById(R.id.date);
            description= (TextView) itemView.findViewById(R.id.description);
            tvInr= (TextView) itemView.findViewById(R.id.tvInr);
            ivlogoicon= (ImageView) itemView.findViewById(R.id.ivlogoicon);
            txbit= (TextView) itemView.findViewById(R.id.txbit);
            lnlayer= (LinearLayout) itemView.findViewById(R.id.lnlayer);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

            }
        }
    }

}
