package com.acepay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.acepay.TransationSaction.TransationBitcoinType;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class INRTransactionAdapter extends RecyclerView.Adapter<INRTransactionAdapter.MyBookingHolder> {

    Context context;
    BitCoinAddressAdapter.MyClickListener mcl;
    ArrayList<Transaction_model> list;

    public INRTransactionAdapter()
    {

    }

    public INRTransactionAdapter(Context context, ArrayList<Transaction_model> list)
    {
        this.context=context;
        this.list=list;
        Log.e("INRTransactionAdapter ",list.size()+"");
    }

    @Override
    public INRTransactionAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inrtransaction_adapter,parent,false);
        return new INRTransactionAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(INRTransactionAdapter.MyBookingHolder holder, @SuppressLint("RecyclerView") final int position) {

        Log.e("btc-->",list.get(position).getBtc());

        if(list.get(position).getBtc().equals("6")){
            Log.e("btc-->","inside5");
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.deposit_black) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }
        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5");
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.deposit_black) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }
        if(list.get(position).getBtc().equals("4"))
        {
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.withdraw_circle) );
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.black_receive) );
        }
        if(list.get(position).getBtc().equals("1") )
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.transfer_black) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.inr_wallet) );
            Log.e("btc-->","inside1");
        }
        if( list.get(position).getBtc().equals("2")){

            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.black_receive) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_inr_circle) );
        }
        if(list.get(position).getBtc().equals("3")){
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.deposit_black) );
//            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }

        holder.description.setText(list.get(position).getDescription());

        String timeCreated = list.get(position).getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = format.parse(timeCreated);
            holder.date.setText(Constant.getTimeAgo(new DateTime(d1).getMillis(),context));

        } catch (ParseException e) {
            e.printStackTrace();
        }
       // holder.date.setText(Constant.getTimeAgo(new DateTime(timeCreated).getMillis(),context));

        /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null; //You will get date object relative to server/client timezone wherever it is parsed
        try {
            date = dateFormat.parse(timeCreated);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS"); //If you need time just put specific format for time like 'HH:mm:ss'
            String dateStr = formatter.format(date);
            Log.e("datetime", "" + dateStr);
            // String timess = dateStr.substring(19, 22);
            holder.date.setText(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        Log.e("amountadapter--->", list.get(position).getAmount());

        Double d=Double.parseDouble(list.get(position).getAmount());
        Log.e("amountadapter--->",String.format("%.2f", d));

        holder.txbit.setText(String.format("%.2f", d)+" "+ PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));

        //holder.txbit.setText(list.get(position).getAmount()+" Ace");

        holder.lnlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BitcoinTransaction.class);
                intent.putExtra("order_id", list.get(position).getId());
                intent.putExtra("key", "inr");
                intent.putExtra("type",list.get(position).getBtc());

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
