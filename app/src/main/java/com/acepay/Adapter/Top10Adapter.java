package com.acepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.Top10ListModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.MyViewHolder> {


    Context context;
    ArrayList<Top10ListModel> lists;

    public Top10Adapter(Context context, ArrayList<Top10ListModel> lists) {
        this.context = context;
        this.lists=lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_top_10,viewGroup,false);
        return new Top10Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {

        Picasso.with(context).load(Constant.ImagePath+lists.get(i).getImage()).placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .into(myViewHolder.ivProfile);
        myViewHolder.tvBtc.setText("B "+lists.get(i).getBtc_amount());
        myViewHolder.tvProfileName.setText(lists.get(i).getName());
        myViewHolder.tvNoofTrade.setText("Number of Trade: "+lists.get(i).getTrade());
        myViewHolder.tvDate.setText(lists.get(i).getDate());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProfile;
        TextView tvProfileName,tvNoofTrade,tvBtc,tvDate;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ivProfile=(CircleImageView)itemView.findViewById(R.id.ivProfile);
            tvProfileName=(TextView) itemView.findViewById(R.id.tvProfileName);
            tvNoofTrade=(TextView) itemView.findViewById(R.id.tvNoofTrade);
            tvBtc=(TextView) itemView.findViewById(R.id.tvBtc);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
        }
    }


}
