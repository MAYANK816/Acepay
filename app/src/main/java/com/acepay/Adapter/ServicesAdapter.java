package com.acepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.ServiceModel;
import com.acepay.R;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder>
{

    Context context;
    ArrayList<ServiceModel> list;

    public ServicesAdapter(Context context, ArrayList<ServiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_service,viewGroup,false);
        return new ServicesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {

        myViewHolder.tvService.setText(list.get(i).getName());

        if (i==0)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_recharge));
        } if (i==1)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.dth));
        } if (i==2)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.electricity));
        } if (i==3)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.credidcard));
        } if (i==4)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.postpaid));
        } if (i==5)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.donate_something));
        } if (i==6)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.book_cylinder));
        } if (i==7)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.broadband));
        } if (i==8)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.landline));
        } if (i==9)
        {
            myViewHolder.ivService.setImageDrawable(context.getResources().getDrawable(R.drawable.waterbill));
        }
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivService;
        TextView tvService;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivService=(ImageView)itemView.findViewById(R.id.ivService);
            tvService=(TextView) itemView.findViewById(R.id.tvService);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(getAdapterPosition(),v);
                }
            });
        }
    }

    onCLickListner listner;

    public void onItemSelectedListener(onCLickListner cLickListner)
    {
        this.listner=cLickListner;
    }

    public interface onCLickListner
    {
         void onItemClick(int layoutPosition,View view);
    }

}
