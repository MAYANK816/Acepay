package com.acepay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.Transaction_model;
import com.acepay.R;
import com.acepay.Statements;
import com.acepay.Transaction_details;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pro22 on 12/12/17.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyBookingHolder> {

    Context context;
    BitCoinAddressAdapter.MyClickListener mcl;
    String key,amount;
    ArrayList<Transaction_model> list;
    private List<Transaction_model> getInterestList=new ArrayList<>();
    int flag;

    public TransactionAdapter(Context context, ArrayList<Transaction_model> list,int flag)
    {
        this.context=context;
        this.list=list;
        this.flag=flag;
        getInterestList.clear();
        getInterestList.addAll(list);
        Log.e("adapter-->",list.size()+"");
    }

    @Override
    public TransactionAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_details,parent,false);
        return new TransactionAdapter.MyBookingHolder(view);
    }

    public void filter(String text)
    {
        list.clear();
        if (text.isEmpty())
        {
            list.addAll(getInterestList);
        }
        else
        {
            text = text.toLowerCase();
            for (Transaction_model item : getInterestList) {
                if (item.getTransaction_id().toLowerCase().contains(text)) {
                    list.add(item);
                }
            }
        }

        if (list.size() > 0) {

            Statements.recyclerView.setVisibility(View.VISIBLE);

        } else {

            Statements.recyclerView.setVisibility(View.GONE);
           // Toast.makeText(context, "No Data Found ! ", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TransactionAdapter.MyBookingHolder holder, @SuppressLint("RecyclerView") final int position) {

            holder.description.setText(list.get(position).getDescription());
            holder.transaction_id.setText(list.get(position).getTransaction_id());
            String timeCreated = list.get(position).getDate();
          //  holder.date.setText(timeCreated);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null; //You will get date object relative to server/client timezone wherever it is parsed
            try {
                date = dateFormat.parse(timeCreated);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);

                // String timess = dateStr.substring(19, 22);
                holder.date.setText(dateStr);
                holder.date.setText(timeCreated);

            } catch (ParseException e) {
                e.printStackTrace();
            }



        if(list.get(position).getStatus().equalsIgnoreCase("Success") ||
                list.get(position).getStatus().equalsIgnoreCase("Yes")||
                list.get(position).getStatus().equalsIgnoreCase("Active")||
                list.get(position).getStatus().equalsIgnoreCase("Approved")){

            holder.txbit.setText("Success");
            holder.txbit.setTextColor(Color.parseColor("#d078e4"));

        }
        else if(list.get(position).getStatus().equalsIgnoreCase("pending") ||
                list.get(position).getStatus().equalsIgnoreCase("no")){
            holder.txbit.setText("Pending");
            holder.txbit.setTextColor(Color.parseColor("#B3F2D91A"));

        }
        else
        {
            holder.txbit.setText("Failure");
            holder.txbit.setTextColor(Color.RED);

        }

        Double bit=Double.parseDouble(list.get(position).getAmount());
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Double InrAmount=Double.parseDouble(list.get(position).getInrAmount());
        Double newinr = Double.parseDouble(BigDecimal.valueOf(InrAmount).toPlainString());


        if(!list.get(position).getBtc().equals("6") && !list.get(position).getBtc().equals("7") &&
                !list.get(position).getBtc().equals("8") && !list.get(position).getBtc().equals("9")&&
                !list.get(position).getBtc().equals("10") && !list.get(position).getBtc().equals("11")) {
            Log.e("inside_btc-->",list.get(position).getBtc());

            if(!list.get(position).getBtc().equals("2") && !list.get(position).getBtc().equals("3")) {

                Log.e("if_btc-->",list.get(position).getBtc()+" yes");
                if(list.get(position).getBtc().equals("13")) {
                    holder.txINR.setVisibility(View.GONE);

                }else{
                    holder.txINR.setText(String.format("%.8f", finacal) + "Ace");
                    holder.txINR.setVisibility(View.VISIBLE);
                }
                holder.tvshowinr.setVisibility(View.VISIBLE);
                holder.tvshowinr.setText(String.format("%.2f", newinr) +
                        PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));
            }
            else {

                holder.tvshowinr.setVisibility(View.GONE);
                holder.txINR.setVisibility(View.VISIBLE);
                Log.e("inr-->",list.get(position).getInrAmount()+
                        PreferenceFile.getInstance().getPreferenceData(context,Constant.Currency_Symbol));
                holder.txINR.setText(list.get(position).getInrAmount()+
                        PreferenceFile.getInstance().getPreferenceData(context,Constant.Currency_Symbol));
            }
        }
        else
        {
            holder.tvshowinr.setVisibility(View.GONE);
            holder.txINR.setText(String.format("%.2f",finacal)+PreferenceFile.getInstance().getPreferenceData(context,Constant.Currency_Symbol));
        }

        holder.lnlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!list.get(position).getBtc().equals("13")) {
                    Intent intent = new Intent(context, Transaction_details.class);
                    intent.putExtra("btc", list.get(position).getBtc());
                    intent.putExtra("id", list.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date,description,transaction_id,txINR,txbit,tvshowinr;
        LinearLayout lnlayer;

        public MyBookingHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            date= (TextView) itemView.findViewById(R.id.date);
            description= (TextView) itemView.findViewById(R.id.description);
            transaction_id= (TextView) itemView.findViewById(R.id.transaction_id);
            txINR= (TextView) itemView.findViewById(R.id.txINR);
            tvshowinr= (TextView) itemView.findViewById(R.id.tvshowinr);
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
