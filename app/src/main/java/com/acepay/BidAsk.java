package com.acepay;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.BidAskAdapter;
import com.acepay.Model.BidAskModel;

import java.util.ArrayList;

public class BidAsk extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    BidAskAdapter adapter;
    TextView txName;
    ArrayList<BidAskModel> models;
    ImageView ivarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_ask);
        models=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);

        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        models=(ArrayList<BidAskModel>) getIntent().getSerializableExtra("mylist");
        Log.e("size--->",models.size()+"");

        adapter = new BidAskAdapter(this,models,getIntent().getStringExtra("key"));
        recyclerView.setAdapter(adapter);

        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);

        if(getIntent().getStringExtra("key").equals("ask")){

            txName.setText("Asking List");
        }
        else
        {
            txName.setText("Bidding List");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivarrow:
               finish();
                break;
        }

    }
}
