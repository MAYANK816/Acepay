package com.acepay.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.BidAskModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pro22 on 7/12/17.
 */

public class BidAskAdapter extends RecyclerView.Adapter<BidAskAdapter.MyBookingHolder>
{
    Context context;
    MyClickListener mcl;
    ArrayList<BidAskModel> list;
    String key;
    DecimalFormat formatter = new DecimalFormat("#,##,###");

    public BidAskAdapter(Context context, ArrayList<BidAskModel> list,String key)
    {
        this.context=context;
        this.list=list;
        this.key=key;
    }

    @Override
    public BidAskAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_ask_list,parent,false);
        return new BidAskAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(BidAskAdapter.MyBookingHolder holder, int position)
    {
        Double inr= Double.parseDouble(list.get(position).getAmount());
        holder.txAmount.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol)+" ");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'000z'");
        Date newDate = null;
        try {
            newDate = format.parse(list.get(position).getDate());
            format = new SimpleDateFormat("MMM dd,yyyy hh:mm ");
            String date = format.format(newDate);
            holder.txdate.setText("("+date+")");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(key.equalsIgnoreCase("bid")){

        }
        else {

        }

        Double ff= Double.parseDouble(list.get(position).getQuantity());

        Double finacal = Double.parseDouble(BigDecimal.valueOf(ff).toPlainString());

      //  tvBitcoin.setText(String.format("%.8f", finacal));
        holder.tvquantity.setText("Quantity: "+String.format("%.8f", finacal)+"Ace");

                holder.tvrate.setText("Rate: "+list.get(position).getRate()+ " " + PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));
        //holder.tvquantity.setText("Quantity: "+list.get(position).getAmount());

        if(list.get(position).getStatus().equals("Pending")){
            holder.tvstatus.setTextColor(Color.parseColor("#d40e0e"));
        }
        else
        {
            holder.tvstatus.setTextColor(Color.parseColor("#2fd49e"));
        }
        holder.tvstatus.setText(list.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txAmount,txdate,tvrate,tvquantity,tvstatus;

        public MyBookingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txAmount= (TextView) itemView.findViewById(R.id.txAmount);
            txdate= (TextView) itemView.findViewById(R.id.txdate);
            tvrate= (TextView) itemView.findViewById(R.id.tvrate);
            tvquantity= (TextView) itemView.findViewById(R.id.tvquantity);
            tvstatus= (TextView) itemView.findViewById(R.id.tvstatus);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

            }
        }
    }

    public interface MyClickListener {
        public void setOnItemClick(View v, int pos);
        public void setEditlick(View v, int pos);
    }

    public void setOnItemClickListener(MyClickListener mcl) {
        this.mcl = mcl;
    }
}
