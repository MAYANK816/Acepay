package com.acepay.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.ContactsModel;
import com.acepay.R;

import java.util.ArrayList;

public class SyncAdapter extends RecyclerView.Adapter<SyncAdapter.MyBookingHolder> {

    Context context;
    ArrayList<ContactsModel> list;
    ArrayList<ContactsModel> resultlist;
    MyBookingHolder myBookingHolder;
    private int pos = 0;
    public itemClick listener;
    View view;

    public SyncAdapter(Context context, ArrayList<ContactsModel> list) {
        this.context = context;
        this.list = list;
        resultlist=new ArrayList<ContactsModel>();
    }

    @Override
    public MyBookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_adapter, parent, false);
        return new MyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyBookingHolder holder, final int position) {

        myBookingHolder = holder;

        holder.tvMobile.setText(list.get(position).getNumber());

        if (list.get(position).getStatus().equalsIgnoreCase("inactive")) {
            holder.tvStatus.setVisibility(View.GONE);
            holder.chk.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.GONE);
            holder.tvStatus.setText("Invite");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.btn_borderless));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.white));
//            Log.e("tvStatus", "onBindViewHolder: " + list.get(position).getStatus());
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    listener.onItemClick(position, holder.tvStatus);
                }
            });
        } else if (list.get(position).getStatus().equalsIgnoreCase("send")) {
            holder.tvStatus.setText("Re-invite");
            holder.chk.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.GONE);
//            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.btn_border));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvStatus.setOnClickListener(null);
        } else {
            holder.tvStatus.setText("Active");
            holder.chk.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
//            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.btn_border));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvStatus.setOnClickListener(null);
        }


        if (list.get(position).getName() != null) {
            holder.tvName.setText(list.get(position).getName());

        } else {
            holder.tvName.setText("N/A");

        }

//        invite status 0 for >> active

//        Log.e("CHECKKKK", "onBindViewHolder:dfgdfg "+list.get(position).getStatus()
//                + "\n"+list.get(position).getName()+" "+ "\n"+list.get(position).getInviteStatus()+" ");


        if(list.get(position).getInviteStatus().equalsIgnoreCase("1")){
            holder.chk.setChecked(true);
//            Log.e("CHECKKKK", "onBindViewHolder: "+list.get(position).getStatus());

        }else if(list.get(position).getInviteStatus().equalsIgnoreCase("2")){
            holder.chk.setChecked(false);
//            Log.e("CHECKKKK", "onBindViewHolder: "+list.get(position).getStatus());
        }else{
            holder.chk.setChecked(false);
//            Log.e("CHECKKKK", "onBindViewHolder: ELSE "+list.get(position).getStatus());
        }

      /*  holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (compoundButton.isChecked()) {
                        if (list.get(position).getStatus().equalsIgnoreCase("inactive") ||
                                list.get(position).getStatus().equalsIgnoreCase("send")) {
                            listener.onItemClick(position, view, "1");

                            Log.e("CHKLIST", "IFFF "+list.get(position).getName() +" "+
                                    list.get(position).getStatus() +" "+list.get(position).getInviteStatus());

                        }
                    } else {
                        if (list.get(position).getStatus().equalsIgnoreCase("inactive") ||
                                list.get(position).getStatus().equalsIgnoreCase("send")) {
                            listener.onItemClick(position, view, "1");

                            Log.e("CHKLIST", "ELSE  "+list.get(position).getName() +" "+
                                    compoundButton.isChecked() +" "+list.get(position).getInviteStatus());
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/

    }

    public void onItemClickListener(itemClick myListener) {
        listener = myListener;
    }

    public interface itemClick {
        public void onItemClick(int layoutPosition, View view1, String inviteStatus);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyBookingHolder extends RecyclerView.ViewHolder {

        private TextView tvMobile, tvStatus, tvName;
        private CheckBox chk;
        private ImageView ivImage;

        public MyBookingHolder(View itemView) {
            super(itemView);
            tvMobile = (TextView) itemView.findViewById(R.id.tvMobile);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            chk = (CheckBox) itemView.findViewById(R.id.chk);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);


            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        if (compoundButton.isChecked()) {
                            if (list.get(getLayoutPosition()).getStatus().equalsIgnoreCase("inactive") ||
                                    list.get(getLayoutPosition()).getStatus().equalsIgnoreCase("send")) {
                                listener.onItemClick(getLayoutPosition(), view, "1");

                                Log.e("CHKLIST", "IFFF "+list.get(getLayoutPosition()).getName() +" "+
                                        list.get(getLayoutPosition()).getStatus() +" "+list.get(getLayoutPosition()).getInviteStatus());

                            }
                        } else {
                            if (list.get(getLayoutPosition()).getStatus().equalsIgnoreCase("inactive") ||
                                    list.get(getLayoutPosition()).getStatus().equalsIgnoreCase("send")) {
                                listener.onItemClick(getLayoutPosition(), view, "2");

                                Log.e("CHKLIST", "ELSE  "+list.get(getLayoutPosition()).getName() +" "+
                                        compoundButton.isChecked() +" "+list.get(getLayoutPosition()).getInviteStatus());
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}