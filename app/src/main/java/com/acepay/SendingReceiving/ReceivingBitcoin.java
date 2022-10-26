package com.acepay.SendingReceiving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.ReceiverAdapter;
import com.acepay.R;


public class ReceivingBitcoin  extends Fragment
{
    RecyclerView recyclerView;
    View view;
    ReceiverAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view= inflater.inflate(R.layout.fragment_receiving_bitcoin, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter=new ReceiverAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }
}