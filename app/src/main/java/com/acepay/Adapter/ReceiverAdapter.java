package com.acepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.acepay.R;


/**
 * Created by pro22 on 27/10/17.
 */

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.MyBookingHolder> {
    Context context;

    public ReceiverAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public ReceiverAdapter.MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layot,parent,false);
        return new ReceiverAdapter.MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiverAdapter.MyBookingHolder holder, final int position) {


    }
    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyBookingHolder extends RecyclerView.ViewHolder {

        ImageView imgAppointmentCompletion;
        TextView textName,textServiceType,textPayment,textDate,textAddress,txViewDetails;
        public MyBookingHolder(View itemView) {
            super(itemView);

        }
    }
}