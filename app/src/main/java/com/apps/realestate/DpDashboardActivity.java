package com.apps.realestate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.item.DailyPass;
import com.example.item.ItemType;
import com.example.util.Constant;
import com.example.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DpDashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<DailyPass> mListFlexiDp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this), MainActivity.mListFlexiDp);
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);

        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewPager2);

        CardFragmentPagerAdapter pagerAdapter2 = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this), MainActivity.mListFlexiDp);
        ShadowTransformer fragmentCardShadowTransformer2 = new ShadowTransformer(viewPager2, pagerAdapter2);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager2.setAdapter(pagerAdapter2);
        viewPager2.setPageTransformer(false, fragmentCardShadowTransformer2);
        viewPager2.setOffscreenPageLimit(3);

        ViewPager viewPager3 = (ViewPager) findViewById(R.id.viewPager3);

        CardFragmentPagerAdapter pagerAdapter3 = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this), MainActivity.mListFlexiDp);
        ShadowTransformer fragmentCardShadowTransformer3 = new ShadowTransformer(viewPager3, pagerAdapter3);
        fragmentCardShadowTransformer.enableScaling(true);

        viewPager3.setAdapter(pagerAdapter3);
        viewPager3.setPageTransformer(false, fragmentCardShadowTransformer3);
        viewPager3.setOffscreenPageLimit(3);

    }


    /**
     * Change value in dp to pixels
     * @param dp
     * @param context
     * @return
     */
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }



}
