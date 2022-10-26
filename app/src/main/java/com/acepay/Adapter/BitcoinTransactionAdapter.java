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

public class BitcoinTransactionAdapter extends
        RecyclerView.Adapter<BitcoinTransactionAdapter.MyBookingHolder> {

    Context context;
    BitCoinAddressAdapter.MyClickListener mcl;
    ArrayList<Transaction_model> list;

    public BitcoinTransactionAdapter(Context context, ArrayList<Transaction_model> list)
    {
        this.context=context;
        this.list=list;
        Log.e("adapter-->",list.size()+"");
    }

    @Override
    public BitcoinTransactionAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bcc,parent,false);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bitcoin_transaction,parent,false);
        return new BitcoinTransactionAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(BitcoinTransactionAdapter.MyBookingHolder holder, final int position) {

        Log.e("priya-->",list.get(position).getStatus());

        if(!list.get(position).getStatus().equalsIgnoreCase("success") && !list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("inside-->","notsuccess");
            holder.txINR.setTextColor(Color.RED);
        }

        if(list.get(position).getBtc().equals("6")){
            Log.e("btc-->","inside5");
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.receive_circle) );
        }
        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5");
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.walleticon) );
        }
        if(list.get(position).getBtc().equals("4"))
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.sell_bit_circle) );
        }
        if(list.get(position).getBtc().equals("1") )
        {
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.send_circle) );
            Log.e("btc-->","inside1");
        }
        if( list.get(position).getBtc().equals("2")){

            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.sell_bit_circle) );
        }
        if(list.get(position).getBtc().equals("3")){
            holder.ivlogoicon.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.walleticon) );
        }
        if(!list.get(position).getRate().equals("rate"))
        {
            holder.rate.setText("  Rate: "+list.get(position).getRate()+" "+ PreferenceFile.getInstance().getPreferenceData(context, Constant.Currency_Symbol));

        }

        if(list.get(position).getBtc().equals("6")){
            Log.e("btc-->","inside5");
            holder.description.setText("Acepay Receive");
        }
        if(list.get(position).getBtc().equals("5")){
            Log.e("btc-->","inside5");
            holder.description.setText("acepay Purchased");
        }
        else if(list.get(position).getBtc().equals("3") && list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("btc-->","inside5");
            holder.description.setText("acepay Purchased");
        }
        else if(list.get(position).getBtc().equals("2") && list.get(position).getStatus().equalsIgnoreCase("Approved")){
            Log.e("btc-->","inside5");
            holder.description.setText("Sell acepay");
        }
        else
        {
            holder.description.setText(list.get(position).getDescription());
        }

        holder.txINR.setText(list.get(position).getStatus());

        if(list.get(position).getBtc().equals("6")){
            holder.transaction_id.setText("Order id:  " + list.get(position).getTransaction_id());
//            holder.transaction_id.setText("Sender Address: " + list.get(position).getTransaction_id());
        }else {
            holder.transaction_id.setText("Order id: " + list.get(position).getTransaction_id());
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

           /* DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
            }
*/
            Log.e("amountadapter--->", list.get(position).getAmount());

        Double d=Double.parseDouble(list.get(position).getAmount());


        if(list.get(position).getBtc().equals("2") || list.get(position).getBtc().equals("3") && list.get(position).getStatus().equalsIgnoreCase("Pending")){
            holder.txbit.setText(String.format("%.8f", d)+" " +" Ace"+" ("+list.get(position).getInrAmount()+" "+PreferenceFile.getInstance().getPreferenceData(context,Constant.Currency_Symbol)+")");
        }
        else {
            holder.txbit.setText(String.format("%.8f", d) + " Ace");
        }

        Log.e("bitcoinAdapter_status",list.get(position).getStatus());
        Log.e("bitcoinAdapter_status",list.get(position).getBtc());

        if(list.get(position).getBtc().equals("6") || (list.get(position).getBtc().equals("2") && list.get(position).getStatus().equalsIgnoreCase("Pending")) || (list.get(position).getBtc().equals("3") && list.get(position).getStatus().equalsIgnoreCase("Pending"))){
            holder.tvInvoice.setVisibility(View.GONE);
        }

        if(list.get(position).getBtc().equalsIgnoreCase("1"))
        {  // get reciver name/number

            holder.txNumber.setVisibility(View.VISIBLE);
            holder.txName.setVisibility(View.VISIBLE);


            holder.txName.setText("Receiver name: "+list.get(position).getReceiver_name());
            holder.txNumber.setText("Receiver Number: "+list.get(position).getReceiver_no());



        }

        else if(list.get(position).getBtc().equalsIgnoreCase("6"))
        {

            holder.txNumber.setVisibility(View.VISIBLE);
            holder.txName.setVisibility(View.VISIBLE);

            Log.e("inside6 ",list.get(position).getTransaction_by());

            //todo when welcome money transfer by admin(initial verification process)
            if (list.get(position).getTransaction_by().equalsIgnoreCase("2"))
            {
                holder.txName.setText("Sender name: "+"admin");
                holder.txNumber.setText("Sender Number: "+"admin");
            }else {


                holder.txName.setText("Sender name: "+list.get(position).getReceiver_name());
                holder.txNumber.setText("Sender Number: "+list.get(position).getReceiver_no());
            }



        }
        else
            {
            holder.txNumber.setVisibility(View.GONE);
            holder.txName.setVisibility(View.GONE);
        }

            //holder.txbit.setText(list.get(position).getAmount()+" Ace");

            holder.tvInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(context, Invoice.class);
                        intent.putExtra("order_id", list.get(position).getId());
                        if (list.get(position).getBtc().equals("4")) {
                            intent.putExtra("key", "sell");
                        }
                        if (list.get(position).getBtc().equals("1")) {
                            intent.putExtra("key", "transfer_bitcoin");
                        }
                        if (list.get(position).getBtc().equals("5")) {
                            intent.putExtra("key", "buy");
                        }
                       if (list.get(position).getBtc().equals("3")) {
                            intent.putExtra("key", "bidding");
                        }
                       if (list.get(position).getBtc().equals("2")) {
                            intent.putExtra("key", "asking");
                        }
                       if (list.get(position).getBtc().equals("1")) {
                            intent.putExtra("key", "transfer_bitcoin");
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
        TextView date,description,transaction_id,txINR,txbit,rate,tvInvoice,txName,txNumber;
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
            txINR= (TextView) itemView.findViewById(R.id.txINR);
            txbit= (TextView) itemView.findViewById(R.id.txbit);
            tvInvoice= (TextView) itemView.findViewById(R.id.tvInvoice);
            txName= (TextView) itemView.findViewById(R.id.txName);
            txNumber= (TextView) itemView.findViewById(R.id.txNumber);
            lnlayer= (LinearLayout) itemView.findViewById(R.id.lnlayer);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

            }
        }
    }

}
