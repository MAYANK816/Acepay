package com.acepay.NotificatiomPackage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.NotificationAdapter;
import com.acepay.Dashboard;
import com.acepay.Model.NotificationModel;
import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NotificationNew extends Fragment implements RetrofitResponse {
    RecyclerView recyclerView;
    Context context;
    ArrayList<NotificationModel> AllUserModel;
    NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        context=getActivity();
        View android = inflater.inflate(R.layout.activity_notification_new, container, false);

        AllUserModel=new ArrayList<>();
        recyclerView= (RecyclerView) android.findViewById(R.id.recyclerview);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);



        return  android;
    }

    public void callservice() {

        if (Constant.isConnectingToInternet(getActivity())) {

            new Retrofit2(getActivity(), NotificationNew.this, Constant.REQ_USER_NOTIFICATION,Constant.USER_NOTIFICATION+ PreferenceFile.getInstance().getPreferenceData(getActivity(),Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(getActivity(), getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {


            case Constant.REQ_USER_NOTIFICATION:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result--->", result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        AllUserModel.clear();
                        JSONArray data = result.getJSONArray("data");

                        for (int x = 0; x < data.length(); x++) {

                            JSONObject obj = data.getJSONObject(x);


                                if (obj.getString("notification_type").equalsIgnoreCase("Notification")) {
                                    NotificationModel allUserModel = new NotificationModel();
                                    allUserModel.setNotification_id(obj.getString("id"));
                                    allUserModel.setNotification_title(obj.getString("title"));
                                    allUserModel.setNotification_description(obj.getString("message"));
                                    allUserModel.setNotification_date(obj.getString("created"));
                                    allUserModel.setNotification_type(obj.getString("notification_type"));
                                    allUserModel.setRead_status(obj.getString("view_status"));
                                    AllUserModel.add(allUserModel);
                                }

                        }

                        Log.e("size-->",AllUserModel.size()+"");
                        adapter = new NotificationAdapter(getActivity(), AllUserModel);
                        recyclerView.setAdapter(adapter);
                    }
                    else {

                        Constant.alertWithIntent(getActivity(),message, Dashboard.class);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }
}


