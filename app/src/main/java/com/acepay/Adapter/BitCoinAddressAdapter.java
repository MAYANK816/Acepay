package com.acepay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acepay.CheckSecurePin;
import com.acepay.Model.BitcoinAddressModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BitCoinAddressAdapter extends RecyclerView.Adapter<BitCoinAddressAdapter.MyBookingHolder> {
        Context context;
        MyClickListener mcl;
        String key,amount,charges,amount_inr,chages_inr,final_charge,gst,rate;
        String actualBtc;
        ArrayList<BitcoinAddressModel> list;
        MyClickListener myClickListener;

public BitCoinAddressAdapter(Context context, String actualBtc, ArrayList<BitcoinAddressModel> list, String key,
        String amount, String charges, String amount_inr, String chages_inr, String final_charge,
        String gst, String rate)
        {
        this.context=context;
        this.actualBtc=actualBtc;
        this.list=list;
        this.key=key;
        this.amount=amount;
        this.charges=charges;
        this.amount_inr=amount_inr;
        this.chages_inr=chages_inr;
        this.final_charge=final_charge;
        this.rate=rate;
        this.gst=gst;
        }

@Override
public BitCoinAddressAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bitcoin_address_list,parent,false);
        return new BitCoinAddressAdapter.MyBookingHolder(view);
        }

@Override
public void onBindViewHolder(BitCoinAddressAdapter.MyBookingHolder holder, final int position) {


        if(list.get(position).getUsername().trim().equalsIgnoreCase("null") ||
        list.get(position).getUsername().trim().equals("") || list.get(position).getUsername().trim().equals("")){
        holder.txUsername.setVisibility(View.GONE);
        }else{
        holder.txUsername.setVisibility(View.VISIBLE);
        holder.txUsername.setText(list.get(position).getUsername());
        }





        holder.tvAddress.setText(list.get(position).getBitcoin_address());
        holder.tvNumber.setText(list.get(position).getRegister_no());

        // Log.e("user bank-->",Constant.BANK_IMAGE+PreferenceFile.getInstance().getPreferenceData(context,Constant.PASSBOOK_IMAGE));
        Picasso.with(context)
        .load(Constant.ImagePath+list.get(position).getProfile()).resize(100,100).placeholder(context.getResources().getDrawable(R.drawable.user_profile))
        .into(holder.imgProfile);

        holder.lnframe.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        if(key.equals("send")) {

        if(!list.get(position).getBitcoin_address().equals(PreferenceFile.getInstance().getPreferenceData(context,Constant.BITCOIN_ADDRESS))) {

        Intent intent1 = new Intent(context, CheckSecurePin.class);
        intent1.putExtra("key", "send");
        intent1.putExtra("to_address", list.get(position).getBitcoin_address());
        intent1.putExtra("amount", amount);
        intent1.putExtra("charges", charges);
        intent1.putExtra("amount_inr", amount_inr);
        intent1.putExtra("charge_inr", chages_inr);
        intent1.putExtra("gst", gst);
        intent1.putExtra("rate", rate);
        intent1.putExtra("final_charge", final_charge);
        intent1.putExtra("actual_btc", actualBtc);
        context.startActivity(intent1);
        }
        }
        }
        });

        }

@Override
public int getItemCount() {
        return list.size();
        }

public class MyBookingHolder extends RecyclerView.ViewHolder{
    ImageView imdelete,imgProfile;
    TextView txUsername,tvAddress,tvNumber;
    LinearLayout lnframe;

    public MyBookingHolder(View itemView) {
        super(itemView);

        imdelete= (ImageView) itemView.findViewById(R.id.imdelete);
        imgProfile= (ImageView) itemView.findViewById(R.id.imgProfile);
        txUsername= (TextView) itemView.findViewById(R.id.txUsername);
        tvAddress= (TextView) itemView.findViewById(R.id.tvAddress);
        tvNumber= (TextView) itemView.findViewById(R.id.tvNumber);
        lnframe= (LinearLayout) itemView.findViewById(R.id.lnframe);

        imdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.OnItemClickView(view,getAdapterPosition());
            }
        });
    }

}

public interface MyClickListener
{
    void OnItemClickView(View v,int position);
}
    public void setOnItemClickListener(BitCoinAddressAdapter.MyClickListener myClickListener)
    {
        this.myClickListener=myClickListener;
    }
}