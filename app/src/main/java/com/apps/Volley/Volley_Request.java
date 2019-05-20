package com.apps.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.Config;
import com.apps.realestate.MainActivity;
import com.apps.realestate.PropertyView;
import com.apps.realestate.SearchActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Volley_Request {
    static String responseString = "";

    public static void createRequest(Context mContext, String requestURI, String type, String returnPath, final String requestBody) {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        if (type.equals("GET")) {
            Log.d("myTag", "inside get req");
            // Initialize a new JsonObjectRequest instance
          /*
            final String returnPathCopy = returnPath;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    requestURI,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Do something with response

                            for (int i = 0; i < 10; i++) {
                                //Log.d("myTag", "onResponse len : " + response.length());
                                if (response != null) {
                                    responseString = response.toString();
                                    Log.d("myTag", "responsestring != 1 : " + responseString);

                                } else {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (Exception e) {
                                        Log.d("myTag", "error in sleep", e);
                                    }
                                }
                            }


                            //Log.d("myTag", "response : " + response.toString());
//responseString = response.toString();
                            // Log.d("myTag", "response assigned : " + responseString.toString());

                            //intent for Create Notification Page
                            // Intent i = new Intent(LandingActivity.this, SignupActivity.class);

                            // i.putExtra("input",response.toString());
                            //  i.putExtra("schoolId", "pihu007");
                            // startActivity(i);
                            // citySpinnerLoader(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Do something when error occurred
                            Log.d("myTag", "error aaya : " + error.toString());
                        }
                    }
            );
            jsonObjectRequest.setTag("GET");
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    Log.d("DEBUG", "request running: " + request.getTag().toString());
                    return true;
                }
            });
            requestQueue.add(jsonObjectRequest);
        */
        } else if (type.equals("POST")) {

            Log.d("myTag", "requesturi : " + requestURI + " : " + requestBody);
            //Log.d("myTag", "requesturi : " + requestURI);
            final String returnPathCopy = returnPath;
            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(requestBody);
                //requestArray = new JSONObject(requestBody);

            } catch (Exception e) {
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURI, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("myTag", "got response : " + response);
                    String responseString = response;

                    if (returnPathCopy == "AddFlexiOrder")
                        Config.addOrederResponse(responseString);
                 else if(returnPathCopy =="propdetail")
                        PropertyView.getPropertyResponse(responseString);
                    else if(returnPathCopy =="locationVal")
                        MainActivity.locationResponse(responseString);
                    else if(returnPathCopy =="searchVal")
                        SearchActivity.searchResponse(responseString);
                    else if(returnPathCopy =="SendEnquiry")
                        PropertyView.sendEnquiryResp(responseString);
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        Log.d("myTag","responsse body : " + new String(response.data));
                        responseString = String.valueOf(new String(response.data));
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        }
    }
}
