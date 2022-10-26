package com.acepay.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Model.MySubscriptionModel;
import com.acepay.R;
import com.acepay.Util.UtilClass;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomMySubscriptionsAdapter extends RecyclerView.Adapter<CustomMySubscriptionsAdapter.MyViewHolder> {

    ArrayList<MySubscriptionModel> list=new ArrayList<>();
    Context context;

    public CustomMySubscriptionsAdapter(Context context, ArrayList<MySubscriptionModel> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_subscription,viewGroup,false);
        return new CustomMySubscriptionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tvBenefits.setText("Benefits:-"+list.get(position).getPlanBenefits()+" ₹");
//        holder.tvPlan.setText(list.get(position).getPlan_name()+"("+list.get(position).getFees()+" ₹)");
        holder.tvPlan.setText(list.get(position).getPlan_name());

        String input_date=list.get(position).getStart_date();
        String finalDay="",dateee="";
        String currentDate="";
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1=format1.parse(input_date);
            DateFormat format2=new SimpleDateFormat("EEEE");
            DateFormat formatSd=new SimpleDateFormat("dd-MM-yyyy");
            //todo for day
            finalDay=format2.format(dt1);

            //todo for date
            dateee=formatSd.format(dt1);
            Log.e("FinalDay ",+position+finalDay);

            currentDate= UtilClass.getCurrentDate();
            Log.e("finalStartDate ",currentDate);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String timeeeGet="",currentTime="",currentTimeformat="";

        currentTime=UtilClass.getCurrentTime();
        Log.e("currentTime ",currentTime+"");

        String current12="";
        try
        {
            Date cccdd=null;

            SimpleDateFormat input=new SimpleDateFormat("hh:mm");
            SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");

            Date dt3bfffh11=input.parse(currentTime);
            current12=output.format(dt3bfffh11);
            Log.e("current12 ",current12+"");
            Log.e("StartTime ",list.get(position).getStart_time()+"");

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }




        //todo for time
        SimpleDateFormat format21=new SimpleDateFormat("hh:mm aa");
        try {
            Date dt311=format21.parse(list.get(position).getStart_time());
            Date dt311Curr=format21.parse(current12);
            DateFormat formatSd=new SimpleDateFormat("HH:mm aa");
            timeeeGet=formatSd.format(dt311);
            currentTimeformat=formatSd.format(dt311Curr);
            Log.e("timeeeGet ",timeeeGet);
            Log.e("currentTimeformat ",currentTimeformat);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        String compareDate="";
        compareDate=UtilClass.compareDates(dateee,currentDate);

        String compareTime="";
        compareTime=UtilClass.compareTiminng24(timeeeGet,currentTimeformat);


        Log.e("CompareDate ",compareDate);
        Log.e("compareTime ",compareTime);


        if (compareDate.equalsIgnoreCase("equal") || compareDate.equalsIgnoreCase("before"))
        {
            if ( compareDate.equalsIgnoreCase("before"))
            {
                if (compareTime.equalsIgnoreCase("after"))
                {
                    holder.tvDate.setText("Running");
                    holder.tvCompetiton.setText("Running Competition");
                }
            }
            else
            {
                if (compareTime.equalsIgnoreCase("equal") || compareTime.equalsIgnoreCase("before"))
                {
                    holder.tvDate.setText("Running");
                    holder.tvCompetiton.setText("Running Competition");
                }
                else
                {
                    holder.tvDate.setText("Start after some time");
                    holder.tvCompetiton.setText(list.get(position).getCompetition_name());
                }
            }
        }
        else
        {
            holder.tvDate.setText("Start on "+finalDay+" at "+list.get(position).getStart_time());
            holder.tvCompetiton.setText(list.get(position).getCompetition_name());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompetiton,tvDate,tvBenefits,tvPlan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompetiton=(TextView)itemView.findViewById(R.id.tvCompetiton);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvBenefits=(TextView)itemView.findViewById(R.id.tvBenefits);
            tvPlan=(TextView)itemView.findViewById(R.id.tvPlan);
        }
    }
}
