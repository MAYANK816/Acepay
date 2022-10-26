package com.acepay.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.acepay.DepositMoney;
import com.acepay.R;
import java.util.List;

public class Constant {
//https://tigerpay.app:8080/
  static ProgressDialog progressDialog;

    /*Local Server*/
 /* public static final String BASE_URL = "http://13.59.195.176:8080/";
    public static final String PDF_URL = "http://13.59.195.176/";
    public static final String STATEMENT_PDF_URL = "http://13.59.195.176/";
    public static final String ImagePath = "http://13.59.195.176/img/profiles/";
    public static final String BANK_IMAGE = "http://13.59.195.176/img/banks/";
    public static final String PAN_IMAGE = "http://13.59.195.176/img/pancards/";
    public static final String Adhar_image = "http://13.59.195.176/img/aadhar/";
    public static final String Advert_image = "http://13.59.195.176/img/advertisements/";
    public static final String Gift_reward_image = "http://13.59.195.176/img/giftRewards/";   */

//    /*Live Server*/
//    public static final String BASE_URL = "https://tigerpay.app:8080/";
//    public static final String PDF_URL = "https://tigerpay.app/";
//    public static final String STATEMENT_PDF_URL = "https://tigerpay.app/";
//    public static final String ImagePath = "https://tigerpay.app/img/profiles/";
//    public static final String BANK_IMAGE = "https://tigerpay.app/img/banks/";
//    public static final String PAN_IMAGE = "https://tigerpay.app/img/pancards/";
//    public static final String Adhar_image = "https://tigerpay.app/img/aadhar/";
//    public static final String Advert_image = "https://tigerpay.app/img/advertisements/";
//    public static final String Gift_reward_image = "https://tigerpay.app/img/giftRewards/";

//    public static final String BASE_URL = "http://18.191.233.107:3000/";
//    public static final String BASE_URL_PAYMENT_OTP = " https://api.yodrm.com/mypay/";
//    public static final String PDF_URL = "http://18.191.233.107:3000/";
//    public static final String STATEMENT_PDF_URL = "http://18.191.233.107:3000";
//    public static final String ImagePath = "http://18.191.233.107:3000/img/profiles/";
//    public static final String BANK_IMAGE = "http://18.191.233.107:3000/img/banks/";
//    public static final String PAN_IMAGE = "http://18.191.233.107:3000/img/pancards/";
//    public static final String Adhar_image = "http://18.191.233.107:3000/img/aadhar/";
//    public static final String Advert_image = "http://18.191.233.107:3000/img/advertisements/";
//    public static final String Gift_reward_image = "http://18.191.233.107:3000/img/giftRewards/";

// todo MCoin Devlopment URL
//  public static final String BASE_URL = "http://18.191.233.107:3000/";
    public static final String BASE_URL = "http://134.209.144.10:3000/";// New Base Url For Acepay
//    public static final String BASE_URL_upi = "https://api.roundpay.net//API/";// New Base Url For Acepay
    public static final String BASE_URL_upi = "https://connectblock.io/ace-cash/api/v1/";// New Base Url For Acepay
//    public static final String PDF_URL = "http://18.191.233.107/";
    public static final String PDF_URL = "http://134.209.144.10/";
//    public static final String STATEMENT_PDF_URL = "http://18.191.233.107/";
    public static final String STATEMENT_PDF_URL = "http://134.209.144.10/";
//    public static final String ImagePath = "http://18.191.233.107/img/profiles/";
    public static final String ImagePath = "http://134.209.144.10/img/profiles/";
//    public static final String BANK_IMAGE = "http://18.191.233.107/img/banks/";
    public static final String BANK_IMAGE = "http://134.209.144.10/img/banks/";
//    public static final String PAN_IMAGE = "http://18.191.233.107/img/pancards/";
    public static final String PAN_IMAGE = "http://134.209.144.10/img/pancards/";
//    public static final String Adhar_image = "http://18.191.233.107/img/aadhar/";
    public static final String Adhar_image = "http://134.209.144.10/img/aadhar/";
//    public static final String Advert_image = "http://18.191.233.107/img/advertisements/";
    public static final String Advert_image = "http://134.209.144.10/img/advertisements/";
//    public static final String Gift_reward_image = "http://18.191.233.107/img/giftRewards/";
    public static final String Gift_reward_image = "http://134.209.144.10/img/giftRewards/";

    /*Keys*/
    public static final String ID = "id";
    public static final String Accunt_status = "status";
    public static final String phone = "phono";
    public static final String country_code = "country_code";
    public static final String INR_PRICE_BITCOIN = "Inr_price_Bitcoin";
    public static final String secure_pin = "secure_pin";
    public static final String Inr_Amount = "inr_amount";
    public static final String BTC_amount = "btc_amount";
    public static final String Currency_Code = "currency_code";
    public static final String COUNTRY_CODE = "countrys_codes";

    public static final String selectedCountryNameCode = "selectedCountryNameCode";
    public static final String Currency_Symbol = "currency_symbol";
    public static final String Username = "username";
    public static final String Email = "email";
    public static final String Dob = "dob";
    public static final String email_verification = "email_verification";
    public static final String Courtry_id = "courtry_id";
    public static final String City_name = "city";

    public static final String VERIFY_PAN = "verify_pan";
    public static final String VERIFY_BANK = "verify_bank";
    public static final String VERIFY_Adhaar = "verify_add";
    public static final String State_name = "State";
    public static final String State_id = "State_id";
    public static final String Country_id = "Country_id11";
    public static final String Country_name = "country";
    public static final String Gender = "gender";
    public static final String Image = "image";

    public static final String BankVerification = "bank_verification";
    public static final String PanVerification = "pan_verification";
    public static final String AadharVerification = "aadhar_verification";
    public static final String BANK_NAME = "bank_name";
    public static final String AC_name = "account_name";
    public static final String AC_no = "account_no";
    public static final String IFSC = "ifsc_code";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String BRANCH = "branch";
    public static final String PASSBOOK_IMAGE = "passbook_image";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String Adhaar_image = "aadhar_image";
    public static final String Adhaar_image_back = "aadhar_image_bank";
    public static final String Adhaar_number = "aadhar_number";
    public static final String Adhaar_line1 = "line1";
    public static final String Adhaar_line2 = "line2";
    public static final String LANDMARK = "landmark";
    public static final String Aadhar_city = "aadhar_city";
    public static final String Aadhar_state = "aadhar_state";
    public static final String Pan_name = "name";

    public static final String Pan_last = "last_name";
    public static final String Pan_image = "imagepan";
    public static final String Pan_number = "pan_number";
    public static final String Pan_dob = "dob";
    public static final String Pan_gender = "gender";
    public static final String COUNT_SECURITY = "count_security";
    public static final String BITCOIN_ADDRESS = "BITCOIN_ADDRESS";
    public static final String REFERCODE = "refer_code";
    public static final String VIBRATE = "vibrate";
    public static final String SOUND = "sound";

    public static final String LOCK_PIN = "lock_pin";
    public static final String Finger_Lock = "finger_lock";
    public static final String Lock_Transaction = "lock_outgoing_transaction";
    public static final String BUYRATE = "buyrate";
    public static final String SELLRATE = "sellrate";
    public static final String BUY = "buy";
    public static final String SELL = "sell";

    public static final String CHECKZENDECK = "checkzendesk";
    public static final String RATE_STATUS = "rate_status";
    public static final String ALERTRATEMY = "estimate_rate";
    public static final String CheckApp = "checkapp";
    public static final String ComeFrom = "ComeFrom";

    public static final String NORMAL_DEP_AMT = "normal_deposit_amt";
    public static final String NORMAL_DEP_BANK_NAME = "normal_deposit_bank_name";
    public static final String NORMAL_DEP_KEY = "normal_key";
    public static final String NORMAL_DEP_IFSC = "normal_key_ifsc";
    public static final String NORMAL_DEP_BRANCH_NAME = "normal_key_braNch_name";
    public static final String NORMAL_DEP_ACC_HOLDER_NAME = "normal_key_account_holder_name";
    public static final String NORMAL_DEP_BANK_ID = "normal_key_bank_id";
    public static final String NORMAL_DEP_ACC_NO = "normal_key_account_number";
    public static final String NORMAL_DEP_ID_RECVD = "normal_deposit_id";


    public static final String SignUp = BASE_URL + "signup";
    public static final int REQ_SignUp = 1;

    public static final String LOGIN = BASE_URL + "login";
    public static final int REQ_LOGIN = 2;

    public static final String Country = BASE_URL + "country";
    public static final int REQ_Country = 3;

    public static final String State = BASE_URL + "state/";
    public static final int REQ_State = 4;

    public static final String ADD_PROFILE = BASE_URL + "add_profile";
    public static final int REQ_ADD_PROFILE = 5;

    public static final String ABOUT_US = BASE_URL + "about_us";
    public static final int REQ_ABOUT_US = 6;

    public static final String EDIT_PROFILE = BASE_URL + "edit_profile_image";
    public static final int REQ_EDIT_PROFILE = 7;

    public static final String EDIT_SECURE_PIN = BASE_URL + "edit_pin";
    public static final int REQ_EDIT_SECURE_PIN = 8;

    public static final String PAN_CARD_VERIFY = BASE_URL + "pancard_verify";
    public static final int REQ_PAN_CARD_VERIFY = 9;

    public static final String IFSC_API = "https://ifsc.razorpay.com/";
    public static final int REQ_IFSC_API = 10;

    public static final String BANK_VERIFICATION = BASE_URL + "bank_verify";
    public static final int REQ_BANK_VERIFICATION = 11;

    public static final String Aadhar_verify = BASE_URL + "aadhar_verify";
    public static final int REQ_Aadhar_verify = 12;

    public static final String BTC_RATE = PDF_URL+"homes/btcRateNew";
    public static final int REQ_BTC_RATE = 13;

    public static final String BTC_ADDRESS = BASE_URL + "get_addr/";
    public static final int REQ_BTC_ADDRESS = 14;

    public static final String BUY_BTC = BASE_URL + "buy_btc";
    public static final int REQ_BUY_BTC = 15;

    public static final String SELL_BTC = BASE_URL + "sell_btc";
    public static final int REQ_SELL_BTC = 16;

    public static final String Wallet_deposit = BASE_URL + "wallet_deposit";
    public static final int REQ_Wallet_deposit = 17;

    public static final String Withdraw = BASE_URL + "withdraw";
    public static final int REQ_Withdraw = 18;

    public static final String MONEY_TRANSFER = BASE_URL + "wallet_money_transfer";
    public static final int REQ_MONEY_TRANSFER = 19;

    public static final String CHECK_NO = BASE_URL + "check_number";//8267836652
    public static final int REQ_CHECK_NO = 20;

    public static final String NEW_COURTRY_CODE = BASE_URL + "country_code";
    public static final int REQ_NEW_COURTRY_CODE = 21;

    public static final String BIDING = BASE_URL + "buy_bid_btc";
    public static final int REQ_BIDING = 22;

    public static final String Ask = BASE_URL + "ask_bid_btc";
    public static final int REQ_Ask = 23;

    public static final String SendAddress = BASE_URL + "ask_bid_btc";
    public static final int REQ_SendAddress = 24;

    public static final String City = BASE_URL + "city/";
    public static final int REQ_City = 25;

    public static final String AllRecord = BASE_URL + "find_user_for_url";
    public static final int REQ_AllRecord = 26;

    public static final String TRANSFER_BITCOIN = BASE_URL + "transfer_btc";
    public static final int REQ_TRANSFER_BITCOIN = 27;

    public static final String Left_balance = BASE_URL + "get_balance";
    public static final int REQ_Left_balance = 28;

    public static final String Block_USER = BASE_URL + "block_user";
    public static final int REQ_Block_USER = 29;

    public static final String bid_list = BASE_URL + "buy_bids_list/";
    public static final int REQ_bid_list = 93;

    public static final String Ask_list = BASE_URL + "sell_bids_list/";
    public static final int REQ_Ask_listt = 30;
    public static final String AddedAddresslist = BASE_URL + "bitcoin_address_list/";
    public static final int REQ_AddedAddresslist = 31;
    public static final String AddAddress = BASE_URL + "add_addr";
    public static final int REQ_AddAddress = 32;
    public static final String Buy_bitcoin = BASE_URL + "buy_bitcoin";
    public static final int REQ_Buy_bitcoin = 33;
    public static final String Statements = PDF_URL + "homes/transaction_statement/";
    //public static final String Statements = BASE_URL+"transaction_statemenNt/";
    public static final int REQ_Statements = 33;
    public static final String transaction_details = BASE_URL + "transaction_detail";
    public static final int REQ_transaction_details = 34;
    public static final String Invoice = BASE_URL + "invoice_of_btc";
    public static final int REQ_Invoice = 35;

    public static final String AdminBank = BASE_URL + "adminbank_list";
    public static final int REQ_AdminBank = 36;

    public static final String Terms_conditiona = BASE_URL + "transaction_terms";
    public static final int REQ_Terms_conditiona = 37;

    public static final String MIN_MAX = BASE_URL + "trans_conditions/";
    public static final int REQ_MIN_MAX = 38;

    public static final String Bitcoin_Transaction = BASE_URL + "btc_statement/";
    public static final int REQ_Bitcoin_Transaction = 39;

    public static final String INR_Transactions = PDF_URL + "homes/inr_statement/";
    public static final int REQ_INR_Transactions = 40;

    public static final String Invoice_inr = BASE_URL + "invoice_of_inr";
    public static final int REQ_Invoice_inr = 41;

    public static final String Date_Transaction_PDF = PDF_URL + "metaPay/homes/myPdfView";
    public static final int REQ_Date_Transaction_PDF = 42;

    public static final String Transaction_date = BASE_URL + "transaction_statement_by_date";
    public static final int REQ_Transaction_date = 43;

    public static final String All_transaction_pdf = PDF_URL + "metaPay/homes/AllTransactionPdf";
    public static final int REQ_All_transaction_pdf = 44;

    public static final String Paypay = BASE_URL + "paypal";
    public static final int REQ_Paypay = 45;

    public static final String Add_CONTACT = BASE_URL + "add_user_contactbook";
    public static final int REQ_Add_CONTACT = 46;

    public static final String CONTACTUS = BASE_URL + "support";
    public static final int REQ_CONTACTUS = 46;

    public static final String USER_NOTIFICATION = BASE_URL + "user_notifications/";
    public static final int REQ_USER_NOTIFICATION = 47;

    public static final String SINK_INTRUCTION = BASE_URL + "sink_intructions";
    public static final int REQ_SINK_INTRUCTION = 48;

    public static final String NOTIFICATION_UPDATE = BASE_URL + "user_notifications_update";
    public static final int REQ_NOTIFICATION_UPDATE = 49;

    public static final String FIND_USER = BASE_URL + "find_user_for_url";
    public static final int REQ_FIND_USER = 50;

    public static final String Latest_btc_transaction = BASE_URL + "latest_statement_btc/";
    public static final int REQ_Latest_btc_transaction = 51;

    public static final String Latest_INR_Transaction = BASE_URL + "latest_statement_inr/";
    public static final int REQ_Latest_INR_Transaction = 52;

    public static final String Dashboard_Refresh = BASE_URL + "dashboard/";
    public static final int REQ_Dashboard_Refresh = 53;

    public static final String FORGOT_PIN = BASE_URL + "forgot_pass";
    public static final int REQ_FORGOT_PIN = 54;

    public static final String CREATETICKET = BASE_URL + "ticket_generate";
    public static final int REQ_CREATETICKET = 55;

    public static final String MYTICKET = BASE_URL + "get_tickets/";
    public static final int REQ_MYTICKET = 56;

    public static final String BTCCHARGE = BASE_URL + "btc_charges";
    public static final int REQ_BTCCHARGE = 58;

    public static final String REMOVEBITCOINADDRESS = BASE_URL + "bitcoin_address_remove/";
    public static final int REQ_REMOVEBITCOINADDRESS = 59;

    public static final String ADVERTISEMAENT = BASE_URL + "avertisements";
    public static final int REQ_ADVERTISEMAENT = 60;

    public static final String COMPLETE_DEPOSIT = BASE_URL + "complete_deposit";
    public static final int REQ_COMPLETE_DEPOSIT = 61;

    public static final String BEGINNER_CONTENT = BASE_URL + "beginner_contents";
    public static final int REQ_BEGINNER_CONTENT = 62;

    public static final String TRANSACTION_LOG = BASE_URL + "transaction_log/";
    public static final int REQ_TRANSACTION_LOG = 63;

    public static final String PAYPAY_FEE = BASE_URL + "paypal_fee";
    public static final int REQ_PAYPAY_FEE = 64;

    public static final String ALERT_RATE = BASE_URL + "notification_rate";
    public static final int REQ_ALERT_RATE = 65;

    public static final String NET_FEES = BASE_URL + "net_fee/";
    public static final int REQ_NET_FEES = 67;

    public static final String CHECK_REFER_CODE = BASE_URL + "check_refer_code/";
    public static final int REQ_CHECK_REFER_CODE = 68;

    public static final String THIRD_PARTY_FEES = BASE_URL + "check_networkfee";
    public static final int REQ_THIRD_PARTY_FEES = 69;

    public static final String FIND_ADDRESS = BASE_URL + "find_address";
    public static final int REQ_FIND_ADDRESS = 70;

    public static final String CHECK_DEPOSIT_STATUS = PDF_URL + "homes/depositStatus";
    //public static final String CHECK_DEPOSIT_STATUS = BASE_URL+"deposit_status";
    public static final int REQ_CHECK_DEPOSIT_STATUS = 71;

    public static final String GET_CONTACTS = BASE_URL + "contact_list";
    public static final int REQ_GET_CONTACTS = 74;

    public static final String SEND_INVITATION = BASE_URL + "contact_sync_update";
    public static final int REQ_SEND_INVITATION = 75;

//http://13.59.195.176:8080/competition
//http://13.59.195.176:8080/deposit_control
    //http://13.59.195.176:8080/deposit_control
    public static final String ADMIN_CONTROL = BASE_URL + "deposit_control";
    public static final int REQ_ADMIN_CONTROL = 76;

    public static final String COMPETITION_PLANS = PDF_URL + "homes/competition";
    public static final int REQ_COMPETITION_PLANS = 77;

    public static final String UPI_DETAILS = BASE_URL + "upi_details";
    public static final int REQ_UPI_DETAILS = 78;

    //http://13.59.195.176:8080/buy_subscriptions
    public static final String BUY_SUBSCRIPTION = BASE_URL + "buy_subscriptions";
    public static final int REQ_BUY_SUBSCRIPTION= 79;

    public static final String CHECK_PLAN = BASE_URL + "plan_purchased_check";
    public static final int REQ_CHECK_PLAN= 80;

    public static final String CHECK_PREMIUM_USERS = PDF_URL + "homes/GetPremiumUser";
    public static final int REQ_CHECK_PREMIUM_USERS= 81;

    public static final String GIFTS_REWARDS = BASE_URL + "gift_rewards";
    public static final int REQ_GIFTS_REWARDS= 82;

    public static final String ACTIVITIES = PDF_URL + "homes/userActivity";
    public static final int REQ_ACTIVITIES= 83;

    public static final String TOP_10_LIST = PDF_URL + "homes/topTenList";
    public static final int REQ_TOP_10_LIST = 84;

    public static final String WINNERS_ANNOUNCEMENT = PDF_URL + "homes/winnerAnnouncement";
    public static final int REQ_WINNERS_ANNOUNCEMENT = 85;

    public static final String WINNERS_SUBMIT = BASE_URL + "winner_notification";
    public static final int REQ_WINNERS_SUBMIT = 86;

//    public static final String UPI_PAYOUT_API = BASE_URL_upi + "Payout";
//    public static final int REQ_UPI_PAYOUT_API = 87;

    public static final String UPI_PAYOUT_API = BASE_URL_upi + "payment.php";
    public static final int REQ_UPI_PAYOUT_API = 87;


    public static String getTimeAgo(long time, Context context) {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0)
        {
            return context.getString(R.string.time_just_now);
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS)
        {
            return context.getString(R.string.time_just_now);
        }
        else if (diff < 2 * MINUTE_MILLIS)
        {
            return context.getString(R.string.time_minute_ago);
        }
        else if (diff < 50 * MINUTE_MILLIS)
        {
            return diff / MINUTE_MILLIS + " " + context.getString(R.string.time_min_ago);
        }
        else if (diff < 90 * MINUTE_MILLIS)
        {
            return context.getString(R.string.time_an_hr_ago);
        }
        else if (diff < 24 * HOUR_MILLIS)
        {
            return diff / HOUR_MILLIS + " " + context.getString(R.string.time_hr_ago);
        }
        else if (diff < 48 * HOUR_MILLIS)
        {
            return context.getString(R.string.time_yesterday);
        }
        else
        {
            return diff / DAY_MILLIS + " " + context.getString(R.string.time_day_ago);
        }
    }

    public static boolean isConnectingToInternet(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
        {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;

    }

    public static boolean checkActivation(Context context)
    {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        Log.e(" FCM ", "Activity open: " + isActivityFound);  // true  foreground n false background

        return isActivityFound;
    }

    public static void alertDialog(Context context, String str)
    {
        final Dialog dialog = new Dialog(context);
//        dialog.setTitle("FIN-CEX");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.constant_dialog);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
        tvText.setText(str);

        btnok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // moveTaskToBack(true);
                dialog.dismiss();
            }
        });
    }

    public static void alertWithIntent(final Context context, String msg, final Class className)
    {

        final Dialog dialog = new Dialog(context);

//        dialog.setTitle("FIN-CEX");
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.constant_dialog);


        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
        tvText.setText(msg);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, className);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                dialog.dismiss();

            }
        });

    }

    public static void alertDialogTwoButtons(final Context context, String msg)
    {

        final Dialog dialog = new Dialog(context);
//        dialog.setTitle("FIN-CEX");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.two_btns);

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
        TextView btncancel = (TextView) dialog.findViewById(R.id.btncancel);
        tvText.setText(msg);

        btnok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try {
                    ((Activity) context).finish();
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_AMT, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_BANK_NAME, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_KEY, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_IFSC, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_BRANCH_NAME, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_ACC_HOLDER_NAME, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_BANK_ID, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_ACC_NO, null);
                    PreferenceFile.getInstance().saveData(context, Constant.NORMAL_DEP_ID_RECVD, null);
                    DepositMoney.tvAmount.setText("");
                    DepositMoney.spBank.setSelection(0);

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }

    public static void hideKeyboard(Context context, View view)
    {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null)
        {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void Progressdialog(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        }
        else
        {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void progressDismiss()
    {
        progressDialog.dismiss();
    }

}