package com.acepay.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.WinnerModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.MyViewHolder> {

    Context context;
    ArrayList<WinnerModel> list;

    public WinnersAdapter(Context context,ArrayList<WinnerModel> list) {
        this.context = context;
        this.list=list;
    }

   /* public WinnersAdapter(Context context) {
        this.context = context;
        this.list=list;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_winners,viewGroup,false);
        return new WinnersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {

        Picasso.with(context).load(Constant.ImagePath+list.get(position).getImage()).
                placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                .into(myViewHolder.ivProfile);

        myViewHolder.tvRank.setText("Rank"+position+1);

        myViewHolder.tvName.setText(list.get(position).getName());
        double hs=Double.parseDouble(list.get(position).getHigh_Score());
        float ff=Float.parseFloat(list.get(position).getHigh_Score());
        String tA = new DecimalFormat("##.##").format(ff);
        myViewHolder.tvScore.setText("HighScore:"+ff+"B");
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView ivProfile;
        TextView tvRank,tvName,tvScore;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ivProfile=(CircleImageView)itemView.findViewById(R.id.ivProfile);
            tvRank=(TextView) itemView.findViewById(R.id.tvRank);
            tvName=(TextView) itemView.findViewById(R.id.tvName);
            tvScore=(TextView) itemView.findViewById(R.id.tvScore);
        }
    }
}
