//package com.acepay.Zendesk;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//
//import com.acepay.R;
//import com.acepay.ToolabarActivity;
//import com.zendesk.sdk.model.access.AnonymousIdentity;
//import com.zendesk.sdk.model.access.Identity;
//import com.zendesk.sdk.network.impl.ZendeskConfig;
//import com.zendesk.sdk.support.SupportActivity;
//
///**
// * Created by pro22 on 12/1/18.
// */
//
//public class Support extends ToolabarActivity {
//
//    private Button help_button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.support_layer);
//        help_button = (Button) findViewById(R.id.help_button);
//        // ZendeskConfig.INSTANCE.init(this, "https://rememberthedate.zendesk.com", "b892498e99e6e01920489988755918219684f60af86bcd10", "mobile_sdk_client_7f7d3c9abd345afe080e");
////        ZendeskConfig.INSTANCE.init(this, "https://metapay.zendesk.com", "83010dc07c1cb130b12ace0cdf759662cba96cf65230d4c8", "mobile_sdk_client_26a622b2fbe960fb7781");
//        // ZendeskConfig.INSTANCE.init(this, "https://omniwear.zendesk.com", "23705744c16d8e0698b45920f18aa26e43d7", "mobile_sdk_client_b7fd695c0e9a6056");
////
////        Identity identity = new AnonymousIdentity.Builder().build();
////        ZendeskConfig.INSTANCE.setIdentity(identity);
//
//        help_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new SupportActivity.Builder().show(Support.this);
//
//              /*CreateRequest createRequest = new CreateRequest();
//                createRequest.setSubject("Subject");
//                createRequest.setDescription("Description");
//
//                List<CustomField> customFields = new ArrayList<CustomField>() {
//                    {
//                        Long customFieldId = 1234567L;
//                        String customFieldValue = "Android 5.0";
//                        CustomField androidVersionCustomField = new CustomField(customFieldId, customFieldValue);
//                        add(androidVersionCustomField);
//                    }
//                };
//
//                createRequest.setCustomFields(customFields);
//                RequestProvider requestProvider = ZendeskConfig.INSTANCE.provider().requestProvider();
//                requestProvider.createRequest(createRequest, new ZendeskCallback<CreateRequest>() {
//                    @Override
//                    public void onSuccess(CreateRequest createRequest) {
//                        Log.e("onSuccess", "Request/"+createRequest.toString()+"//"+createRequest.getDescription()+"//"+createRequest.getEmail()+"/>"+createRequest.getId());
//                        // Logger.i(LOG_TAG, "Request created...");
//                    }
//
//                    @Override
//                    public void onError(ErrorResponse errorResponse) {
//                        Log.e("onError", "Request");
//                        Log.e("onError", errorResponse.toString() + "/>" + errorResponse.getReason() + "/" + errorResponse.getResponseBody() + ">>" + errorResponse.getStatus());
//                        // Logger.e(LOG_TAG, errorResponse);
//                    }
//                });*/
//            }
//        });
//
//    }
//
//}
