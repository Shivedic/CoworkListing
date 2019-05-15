package com.apps.realestate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.apps.Volley.Volley_Request;
import com.example.adapter.PropertyAdapterLatest;
import com.example.item.ItemCowork;
import com.example.item.ItemProperty;
import com.example.util.Constant;
import com.example.util.ItemOffsetDecoration;
import com.example.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<ItemProperty> mListItem;
    static ArrayList<ItemCowork> mListCoItem;
    static public RecyclerView recyclerView;
    static PropertyAdapterLatest adapter;
    static private ProgressBar progressBar;
    static private LinearLayout lyt_not_found;
    static String Country, State, City;
    Toolbar toolbar;
    JsonUtils jsonUtils;
    LinearLayout adLayout;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);
        mContext = SearchActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_search));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        Country = intent.getStringExtra("country");
        State = intent.getStringExtra("state");
        City = intent.getStringExtra("city");

        mListItem = new ArrayList<>();
        mListCoItem = new ArrayList<>();
        lyt_not_found = findViewById(R.id.lyt_not_found);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.vertical_courses_list);
        adLayout = findViewById(R.id.adview);
        if (JsonUtils.personalization_ad) {
            JsonUtils.showPersonalizedAds(adLayout, SearchActivity.this);
        } else {
            JsonUtils.showNonPersonalizedAds(adLayout, SearchActivity.this);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(SearchActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        //if (JsonUtils.isNetworkAvailable(SearchActivity.this)) {
         //   new getSearch().execute(Constant.SEARCH_URL + Search.replace(" ", "%20") + "&type_id=" + typeId + "&purpose=" + typePurpose);
         //}
        Volley_Request postRequest = new Volley_Request();
        String req = "{\"country\":\""+ Country +"\",\"state\":\""+ State +"\",\"city\":\""+ City +"\"}";
        postRequest.createRequest(getApplicationContext(), Constant.SEARCHBASIC_URL, "POST", "searchVal", req);
    }

    public static void searchResponse(String result){
    try{
        JSONObject respObj = new JSONObject(result);
        JSONArray jsonArray = respObj.getJSONArray("propertydata");
        JSONObject objJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            objJson = jsonArray.getJSONObject(i);
            ItemCowork objItem = new ItemCowork();
            objItem.setPId(objJson.getString(Constant.PLACE_ID));
            objItem.setPropertyName(objJson.getString(Constant.PLACE_TITLE));
            objItem.setPropertyThumbnailB(objJson.getString(Constant.PLACE_IMAGE));
            objItem.setRateAvg(objJson.getString(Constant.PLACE_RATE));
            objItem.setPropertyPrice("1001");
            //objItem.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
            //objItem.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
            objItem.setPropertyStartTime(objJson.getString(Constant.PLACE_TIME_START));
            objItem.setPropertyEndTime(objJson.getString(Constant.PLACE_TIME_END));
            objItem.setPropertyWeekStart(objJson.getString(Constant.PLACE_WDSTART));
            objItem.setPropertyWeekEnd(objJson.getString(Constant.PLACE_WDEND));
            objItem.setPropertyArea("1000");
            objItem.setPropertyAddress(objJson.getString(Constant.PLACE_ADDRESS));
            objItem.setPropertyPurpose(objJson.getString(Constant.PLACE_PURPOSE));
            objItem.setpropertyTotalRate(objJson.getString(Constant.PLACE_TOTAL_RATE));
            if (i % 2 == 0) {
                objItem.setRight(true);
            }
            mListCoItem.add(objItem);
        }

        } catch(Exception e){
Log.d("myTag", "error : " , e);
    }
        displayData();
    }

    @SuppressLint("StaticFieldLeak")
    private class getSearch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            showProgress(false);
            if (null == result || result.length() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemProperty objItem = new ItemProperty();
                        objItem.setPId(objJson.getString(Constant.PROPERTY_ID));
                        objItem.setPropertyName(objJson.getString(Constant.PROPERTY_TITLE));
                        objItem.setPropertyThumbnailB(objJson.getString(Constant.PROPERTY_IMAGE));
                        objItem.setRateAvg(objJson.getString(Constant.PROPERTY_RATE));
                        objItem.setPropertyPrice(objJson.getString(Constant.PROPERTY_PRICE));
                        objItem.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
                        objItem.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
                        objItem.setPropertyArea(objJson.getString(Constant.PROPERTY_AREA));
                        objItem.setPropertyAddress(objJson.getString(Constant.PROPERTY_ADDRESS));
                        objItem.setPropertyPurpose(objJson.getString(Constant.PROPERTY_PURPOSE));
                        objItem.setpropertyTotalRate(objJson.getString(Constant.PROPERTY_TOTAL_RATE));
                        if (i % 2 == 0) {
                            objItem.setRight(true);
                        }
                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }


    private static void displayData() {
        //adapter = new PropertyAdapterLatest(SearchActivity.this, mListItem);
        adapter = new PropertyAdapterLatest(mContext, mListCoItem);

        recyclerView.setAdapter(adapter);
         Log.d("myTag", "search result : " + mListCoItem.size() + " : " + adapter.getItemCount());
        if (adapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            lyt_not_found.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
