package com.apps.realestate;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.PaymentIntegrationMethods.RazorPayIntegration;

public class CheckOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Bundle bundle = getIntent().getExtras();
        String duration = bundle.getString("dur");
        String price = bundle.getString("price");
       /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChoosePaymentMethod fragment = new ChoosePaymentMethod();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
           */
        Intent intent = new Intent(getApplicationContext(), RazorPayIntegration.class);
        intent.putExtra("duration", duration);
        intent.putExtra("price", price);
        startActivity(intent);
           }
}
