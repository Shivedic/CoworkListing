package com.apps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apps.PaymentIntegrationMethods.OrderConfirmed;
import com.apps.Volley.Volley_Request;
import com.apps.realestate.MainActivity;
import com.apps.realestate.R;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Config {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    // id to handle the notification in the notification tray
    public static final String SHARED_PREF = "ah_firebase";
    static ProgressDialog progressDialog;
    public static Context mContext;

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
    public static boolean validateEmail(EditText editText, Context context) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            //editText.setError(context.getString(R.string.err_msg_email));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static void showCustomAlertDialog(Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showLoginCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("Login");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //Config.moveTo(context, Login.class);

            }
        });
        alertDialog.setConfirmText("Signup");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //Config.moveTo(context, SignUp.class);

            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }


    public static void showPincodeCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("Login");
        alertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //Config.moveTo(context, Login.class);

            }
        });
        alertDialog.setConfirmText("Signup");
        alertDialog.setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //Config.moveTo(context, SignUp.class);

            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void addOrder(final Context context, String transactionId, String duration,String amount) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mContext = context;
        String req = "{\"userid\":\"" + "1" + "\",\"duration\":\"" + duration + "\",\"amount\":\"" + amount + "\",\"payid\":\"" + transactionId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(context, context.getResources().getString(R.string.mJSONURL_addflexidp), "POST", "AddFlexiOrder", req);

/*
        Api.getClient().addOrder(MainActivity.userId,
                MyCartList.cartistResponseData.getCartid(),
                ChoosePaymentMethod.address,
                ChoosePaymentMethod.mobileNo,
                transactionId,
                "succeeded",
                CartListAdapter.totalAmountPayable,
                paymentMode,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(context, OrderConfirmed.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                */
    }

    public static void addOrederResponse(String response){
        try {
            progressDialog.dismiss();
            Log.d("myTag", "recieved response : " + response);
           // Log.d("myTag", "last index of " + response.substring(response.lastIndexOf(">") + 1, response.length()));
          //  String responseString = response.substring(response.lastIndexOf(">")+ 12, response.length());
           // responseString = responseString.substring(responseString.indexOf("{"), responseString.lastIndexOf("}") + 1);
            //Log.d("myTag", "returned  : " + responseString);
            JSONObject jObj = new JSONObject(response);

            if(jObj.getString("success").equals("true")) {
                //  String no="8958043695";
                //String msg="new Order";
                Intent intent = new Intent(mContext, OrderConfirmed.class);
                //PendingIntent pi= PendingIntent.getActivity(mContext, 0, intent,0);
                //SmsManager sms=SmsManager.getDefault();
                //sms.sendTextMessage(no, null, msg, pi,null);
                // Intent intent = new Intent(mContext, OrderConfirmed.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finishAffinity();
            }
        } catch (Exception e ) {
            Log.d("myTag", "error in add order : " , e);
        }
    }

    public static void showCartCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }
}