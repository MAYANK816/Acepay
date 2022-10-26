package com.acepay.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.PlansModel;
import com.acepay.R;
import com.acepay.competition.BuyEntryPass;

import java.util.ArrayList;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.MyViewHolder> {

    Context context;
    ArrayList<PlansModel> list;

    public PlansAdapter(Context context, ArrayList<PlansModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_plans,viewGroup,false);
        return new PlansAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvName.setText(list.get(position).getName());
        holder.tvPrice.setText(list.get(position).getPrice()+"  ₹");

        if (list.get(position).isFlag())
        {
            holder.ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.checked));
        }
        else {
            holder.ivCheck.setImageDrawable(context.getResources().getDrawable(R.drawable.tickkk));
        }

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvPrice;
        ImageView ivCheck;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ivCheck=(ImageView)itemView.findViewById(R.id.ivCheck);
            tvName=(TextView) itemView.findViewById(R.id.tvName);
            tvPrice=(TextView) itemView.findViewById(R.id.tvPrice);

            ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCurrItem(getAdapterPosition());
                }
            });
        }
    }

    private void selectCurrItem(int position)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (i == position){
                list.get(i).setFlag(true);
                BuyEntryPass.plan_id=list.get(position).getId();
                BuyEntryPass.plan_price=list.get(position).getPrice();
                Log.e("PlanId ",BuyEntryPass.plan_id);
                Log.e("PlanPrice ",BuyEntryPass.plan_price);
            }
            else
            {
                list.get(i).setFlag(false);
            }

            notifyDataSetChanged();
        }
    }

}
