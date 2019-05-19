package com.apps.realestate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.Volley.Volley_Request;
import com.bumptech.glide.Glide;
import com.example.adapter.ExpandableListViewAdapter;
import com.example.adapter.ViewPagerAdapter;
import com.example.db.DatabaseHelper;
import com.example.fragment.AmenitiesFragment;
import com.example.fragment.GalleryFragment;
import com.example.fragment.OffersFragment;
import com.example.item.ItemCowork;
import com.example.item.ItemProperty;
import com.example.item.Package;
import com.example.item.Review;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.github.ornolfr.ratingview.RatingView;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.internal.ListenerClass;

public class PropertyView extends AppCompatActivity {

    public static Activity mActivity;
    static ImageView imageFloor, imageMap, imageCall, imageRating,image_rate_close;
    static TextView txtName, txtAddress, txtPrice, txtBed, txtBath, txtArea, txtPhone, txtAmenities, txtOffers;
    static WebView webView;
    static Toolbar toolbar;
    static ScrollView mScrollView;
    static ProgressBar mProgressBar;
    //ItemProperty objBean;
    static  ItemCowork objItem;
    static ArrayList<Package> mPackageList;
    static ArrayList<Review> mReviewList;
    static String Id;
    static ArrayList<String> mGallery, mAmenities, mOffers;
    private FragmentManager fragmentManager;
    static RatingView ratingView;
    static String rateMsg;
    static Menu menu;
    static DatabaseHelper databaseHelper;
    static View view, view1, viewOffers, viewOffers1;
    static JsonUtils jsonUtils;
    static LinearLayout adLayout;
    static boolean iswhichscreen;
    static ImageView image_fur, image_very, img_plus;
    static TextView textFur, textVery;
    static FrameLayout containerAmenities;
    static Button enquiryBtn;
    static Button tourBtn;

    //Package
    private static ExpandableListView expandableListView;
    private static ExpandableListViewAdapter expandableListViewAdapter;
    private static List<String> typeDataGroup = new ArrayList<>();
    private static HashMap<String, List<Package>> listDataChild = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        String req = "{\"place_id\":\""+ "146" +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getApplicationContext(), getResources().getString(R.string.mJSONURL_propertydetails), "POST", "propdetail", req);
        setContentView(R.layout.activity_property_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(getString(R.string.property_details));

        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());

        databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent i = getIntent();
        Id = i.getStringExtra("Id");

        fragmentManager = getSupportFragmentManager();
        containerAmenities = findViewById(R.id.ContainerAmenities);
        //objBean = new ItemProperty();
        objItem = new ItemCowork();
        mGallery = new ArrayList<>();
        mAmenities = new ArrayList<>();
        mOffers = new ArrayList<>();
mReviewList = new ArrayList<>();
        image_fur = findViewById(R.id.image_fur);
        textFur = findViewById(R.id.textFur);
        image_very = findViewById(R.id.image_very);
        textVery = findViewById(R.id.textVery);
        imageFloor = findViewById(R.id.image_floor);
        imageMap = findViewById(R.id.imageMap);
        imageCall = findViewById(R.id.imageCall);
        txtName = findViewById(R.id.text);
        txtAddress = findViewById(R.id.textAddress);
        txtPrice = findViewById(R.id.textPrice);
        txtBed = findViewById(R.id.textBed);
        txtBath = findViewById(R.id.textBath);
        txtArea = findViewById(R.id.textSquare);
        txtPhone = findViewById(R.id.textPhone);
        txtAmenities = findViewById(R.id.txtAmenities);
        txtOffers = findViewById(R.id.txtOffers);
        view = findViewById(R.id.viewAmenities);
        view1 = findViewById(R.id.viewAmenities1);
        viewOffers = findViewById(R.id.viewOffers);
        viewOffers1 = findViewById(R.id.viewOffers1);
        ratingView = findViewById(R.id.ratingView);
        imageRating = findViewById(R.id.image_rating);
        webView = findViewById(R.id.property_description);

        mScrollView = findViewById(R.id.scrollView);
        mProgressBar = findViewById(R.id.progressBar1);
        webView.setBackgroundColor(Color.TRANSPARENT);
        adLayout = findViewById(R.id.adview);
        img_plus = findViewById(R.id.plus_img);
        expandableListView = findViewById(R.id.expandableListPack);
        enquiryBtn = findViewById(R.id.enquiry_btn);
        tourBtn = findViewById(R.id.btn_book_tour);
        enquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnquiry();
            }
        });

        tourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTour();
            }
        });

        Intent intent = getIntent();
        iswhichscreen = intent.getBooleanExtra("isNotification", false);
        if (!iswhichscreen) {
            if (JsonUtils.personalization_ad) {
                JsonUtils.showPersonalizedAds(adLayout, PropertyView.this);
            } else {
                JsonUtils.showNonPersonalizedAds(adLayout, PropertyView.this);
            }

        }


/*
        if (JsonUtils.isNetworkAvailable(PropertyView.this)) {
            //new getProperty().execute(Constant.SINGLE_PROPERTY_URL + Id);
            new getProperty().execute(Constant.SINGLE_PROPERTY_URL + "146");
        } else {
         //   showToast(getString(R.string.conne_msg1));
        }
*/
    /*
        imageRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRating();
            }
        });
*/

        containerAmenities.setVisibility(View.GONE);
        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(containerAmenities.getVisibility() == View.GONE){ containerAmenities.setVisibility(View.VISIBLE); img_plus.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus)); }
                else if(containerAmenities.getVisibility() == View.VISIBLE){ containerAmenities.setVisibility(View.GONE); img_plus.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon)); }
            }
        });

        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + objItem.getPropertyMapLatitude() + "," + objItem.getPropertyMapLongitude() + " (" + objItem.getPropertyName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + objItem.getPropertyPhone()));
                startActivity(callIntent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class getProperty extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            if (null == result || result.length() == 0) {
                //showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray("propertydata");
                    JSONObject objectJson = jsonArray.getJSONObject(0);

                    JSONArray basicArray = objectJson.getJSONArray("basic_detail");
                    JSONObject objJson = basicArray.getJSONObject(0);
/*
                    objBean.setPId(objJson.getString(Constant.PROPERTY_ID));
                    objBean.setPropertyName(objJson.getString(Constant.PROPERTY_TITLE));
                    objBean.setPropertyThumbnailB(objJson.getString(Constant.PROPERTY_IMAGE));
                    objBean.setRateAvg(objJson.getString(Constant.PROPERTY_RATE));
                    objBean.setPropertyPrice(objJson.getString(Constant.PROPERTY_PRICE));
                    objBean.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
                    objBean.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
                    objBean.setPropertyArea(objJson.getString(Constant.PROPERTY_AREA));
                    objBean.setPropertyAddress(objJson.getString(Constant.PROPERTY_ADDRESS));
                    objBean.setPropertyPhone(objJson.getString(Constant.PROPERTY_PHONE));
                    objBean.setPropertyDescription(objJson.getString(Constant.PROPERTY_DESC));
                    objBean.setPropertyFloorPlan(objJson.getString(Constant.PROPERTY_FLOOR_PLAN));
                    objBean.setPropertyAmenities(objJson.getString(Constant.PROPERTY_AMENITIES));
                    objBean.setPropertyPurpose(objJson.getString(Constant.PROPERTY_PURPOSE));
                    objBean.setPropertyMapLatitude(objJson.getString(Constant.PROPERTY_LATITUDE));
                    objBean.setPropertyMapLongitude(objJson.getString(Constant.PROPERTY_LONGITUDE));
                    objBean.setPropertyVery(objJson.getString(Constant.PROPERTY_VERY));
                    objBean.setPropertyFur(objJson.getString(Constant.PROPERTY_FUR));
*/

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
                    objItem.setPropertyMapLatitude(objJson.getString(Constant.PROPERTY_LATITUDE));
                    objItem.setPropertyMapLongitude(objJson.getString(Constant.PROPERTY_LONGITUDE));
                    objItem.setPropertyPhone(objJson.getString(Constant.PLACE_PHONE));


                    typeDataGroup.add("flexi");
                    typeDataGroup.add("fixed");
                    typeDataGroup.add("office");
                    ArrayList<Package> flexiPack = new ArrayList<>();
                    ArrayList<Package> fixedPack = new ArrayList<>();
                    ArrayList<Package> officePack = new ArrayList<>();
                    JSONArray packArray = objectJson.getJSONArray("package");
                    for(int i=0; i < packArray.length(); i++){
                        JSONObject packObj = packArray.getJSONObject(i);
                        Package p = new Package();
                        p.setP_id(packObj.getString(Constant.PACK_ID));
                        p.setName(packObj.getString(Constant.PACK_NAME));
                        p.setDescription(packObj.getString(Constant.PACK_DESC));
                        p.setCategory(packObj.getString(Constant.PACK_CATEGORY));
                        p.setDuration(packObj.getString(Constant.PACK_DURATION));
                        p.setStartTime(packObj.getString(Constant.PACK_STARTTIME));
                        p.setEndTime(packObj.getString(Constant.PACK_ENDTIME));
                        p.setQty(packObj.getString(Constant.PACK_QUANTITY));
                        p.setSeats(packObj.getString(Constant.PACK_SEATS));
                        p.setPrice(packObj.getString(Constant.PACK_PRICE));
                        if(p.getCategory().equals("flexi")){flexiPack.add(p);}
                        else if(p.getCategory().equals("fixed")){fixedPack.add(p);}
                        else if(p.getCategory().equals("office")){officePack.add(p);}
                    }



                    JSONArray amenitiesArray = objectJson.getJSONArray("amenities");
                    for(int i=0; i < amenitiesArray.length(); i++) {
                    JSONObject amenitiesJSON = amenitiesArray.getJSONObject(0);

                        Iterator<String> keys = amenitiesJSON.keys();

                        while(keys.hasNext()) {
                            String key = keys.next();
                            if (amenitiesJSON.get(key) instanceof JSONObject) {
                                Log.d("myTag", "value : " + key + " : " + amenitiesJSON.get(key));
                                // do something with jsonObject here
                            }
                        }
                    }

                        JSONArray jsonArrayGallery = objJson.getJSONArray(Constant.GALLERY_ARRAY_NAME);
                    if (jsonArrayGallery.length() != 0) {
                        for (int j = 0; j < jsonArrayGallery.length(); j++) {
                            JSONObject objChild = jsonArrayGallery.getJSONObject(j);
                            if (!objChild.has(Constant.SUCCESS)) {
                                mGallery.add(objChild.getString(Constant.GALLERY_IMAGE_NAME));
                            } else {
                                mGallery.add(objJson.getString(Constant.PROPERTY_IMAGE));
                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("myTag", " error ", e);
                }
               // setResult();
            }
        }
    }


    public static void getPropertyResponse(String result){
        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        if (null == result || result.length() == 0) {
            //showToast(getString(R.string.nodata));
        } else {

            try {
                JSONObject mainJson = new JSONObject(result);
                JSONArray jsonArray = mainJson.getJSONArray("propertydata");
                JSONObject objectJson = jsonArray.getJSONObject(0);

                JSONArray basicArray = objectJson.getJSONArray("basic_detail");
                JSONObject objJson = basicArray.getJSONObject(0);
/*
                    objBean.setPId(objJson.getString(Constant.PROPERTY_ID));
                    objBean.setPropertyName(objJson.getString(Constant.PROPERTY_TITLE));
                    objBean.setPropertyThumbnailB(objJson.getString(Constant.PROPERTY_IMAGE));
                    objBean.setRateAvg(objJson.getString(Constant.PROPERTY_RATE));
                    objBean.setPropertyPrice(objJson.getString(Constant.PROPERTY_PRICE));
                    objBean.setPropertyBed(objJson.getString(Constant.PROPERTY_BED));
                    objBean.setPropertyBath(objJson.getString(Constant.PROPERTY_BATH));
                    objBean.setPropertyArea(objJson.getString(Constant.PROPERTY_AREA));
                    objBean.setPropertyAddress(objJson.getString(Constant.PROPERTY_ADDRESS));
                    objBean.setPropertyPhone(objJson.getString(Constant.PROPERTY_PHONE));
                    objBean.setPropertyDescription(objJson.getString(Constant.PROPERTY_DESC));
                    objBean.setPropertyFloorPlan(objJson.getString(Constant.PROPERTY_FLOOR_PLAN));
                    objBean.setPropertyAmenities(objJson.getString(Constant.PROPERTY_AMENITIES));
                    objBean.setPropertyPurpose(objJson.getString(Constant.PROPERTY_PURPOSE));
                    objBean.setPropertyMapLatitude(objJson.getString(Constant.PROPERTY_LATITUDE));
                    objBean.setPropertyMapLongitude(objJson.getString(Constant.PROPERTY_LONGITUDE));
                    objBean.setPropertyVery(objJson.getString(Constant.PROPERTY_VERY));
                    objBean.setPropertyFur(objJson.getString(Constant.PROPERTY_FUR));
*/

                objItem.setPId(objJson.getString(Constant.PLACE_ID));
                objItem.setPropertyName(objJson.getString(Constant.PLACE_TITLE));
                objItem.setPropertyThumbnailB(objJson.getString(Constant.PLACE_IMAGE));
                objItem.setRateAvg("4");
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
                objItem.setpropertyTotalRate("36");
                objItem.setPropertyMapLatitude(objJson.getString(Constant.PLACE_LATITUDE));
                objItem.setPropertyMapLongitude(objJson.getString(Constant.PLACE_LONGITUDE));
                objItem.setPropertyPhone(objJson.getString(Constant.PLACE_PHONE));
                objItem.setPropertyDescription(objJson.getString(Constant.PLACE_DESC));
                objItem.setPropertyFloorPlan(objJson.getString(Constant.PLACE_FLOOR_PLAN));

                typeDataGroup.add("flexi");
                typeDataGroup.add("fixed");
                typeDataGroup.add("office");
                ArrayList<Package> flexiPack = new ArrayList<>();
                ArrayList<Package> fixedPack = new ArrayList<>();
                ArrayList<Package> officePack = new ArrayList<>();
                JSONArray packArray = objectJson.getJSONArray("package");
                for(int i=0; i < packArray.length(); i++){
                    JSONObject packObj = packArray.getJSONObject(i);
                    Package p = new Package();
                    p.setP_id(packObj.getString(Constant.PACK_ID));
                    p.setName(packObj.getString(Constant.PACK_NAME));
                    p.setDescription(packObj.getString(Constant.PACK_DESC));
                    p.setCategory(packObj.getString(Constant.PACK_CATEGORY));
                    p.setDuration(packObj.getString(Constant.PACK_DURATION));
                    p.setStartTime(packObj.getString(Constant.PACK_STARTTIME));
                    p.setEndTime(packObj.getString(Constant.PACK_ENDTIME));
                    p.setQty(packObj.getString(Constant.PACK_QUANTITY));
                    p.setSeats(packObj.getString(Constant.PACK_SEATS));
                    p.setPrice(packObj.getString(Constant.PACK_PRICE));
                    if(p.getCategory().equals("flexi")){flexiPack.add(p);}
                    else if(p.getCategory().equals("fixed")){fixedPack.add(p);}
                    else if(p.getCategory().equals("office")){officePack.add(p);}
                    Log.d("myTag", "pack : " + p.getCategory() + " : " + p.getName());
                }
                listDataChild.put("flexi", flexiPack);
                listDataChild.put("fixed", fixedPack);
                listDataChild.put("office", officePack);

                JSONArray amenitiesArray = objectJson.getJSONArray("amenities");
                String amenities = "";
                for (int i = 0; i < amenitiesArray.length(); i++) {
                    JSONObject amenitiesJSON = amenitiesArray.getJSONObject(0);

                    Iterator<String> keys = amenitiesJSON.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        //Log.d("myTag", "value : " + key + " : " + amenitiesJSON.get(key));


                        if (amenitiesJSON.get(key).equals("1")) {
                            //Log.d("myTag", "value : " + amenitiesJSON.get(key));
                            amenities += key;
                            amenities += ",";
                            // do something with jsonObject here
                        }

                    }
                }
                amenities = amenities.substring(0, amenities.length() - 1);
                objItem.setPropertyAmenities(amenities);

                JSONArray reviewsArray = objectJson.getJSONArray("reviews");
                for(int i=0; i<reviewsArray.length() && i<5; i++){
                    Review review = new Review();
                    review.setUserName(reviewsArray.getJSONObject(i).getString("user_name"));
                    review.setDate(reviewsArray.getJSONObject(i).getString("review_date"));
                    review.setDesc(reviewsArray.getJSONObject(i).getString("review_desc"));
                    review.setTag(reviewsArray.getJSONObject(i).getString("review_tag"));
                    review.setResponse(reviewsArray.getJSONObject(i).getString("response"));
                    mReviewList.add(review);
                }
                objItem.setReviews(mReviewList);

                JSONArray offersArray = objectJson.getJSONArray("offers");
                String offers = "";
                for (int i = 0; i < offersArray.length(); i++) {
                    JSONObject offersJSON = offersArray.getJSONObject(0);

                    Iterator<String> keys = offersJSON.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        //Log.d("myTag", "value : " + key + " : " + amenitiesJSON.get(key));
                            offers += offersJSON.get(key);
                            offers += ",";
                            // do something with jsonObject here
                    }
                }
                offers = offers.substring(0, offers.length() - 1);
                objItem.setPropertyOffers(offers);



                JSONArray jsonArrayGalleryMain = objJson.getJSONArray("gallery");
                JSONArray jsonArrayGallery = jsonArrayGalleryMain.getJSONObject(0).getJSONArray("gallery_image");
                if (jsonArrayGallery.length() != 0) {
                    for (int j = 0; j < jsonArrayGallery.length(); j++) {
                        JSONObject objChild = jsonArrayGallery.getJSONObject(j);
                        String img = objChild.getString(Constant.GALLERY_IMAGE_NAME.replaceAll("\\\\", ""));
                        mGallery.add(img);
/*
                        if (!objChild.has(Constant.SUCCESS)) {
                            mGallery.add(objChild.getString(Constant.GALLERY_IMAGE_NAME));
                        } else {
                            mGallery.add(objJson.getString(Constant.PROPERTY_IMAGE));
                        }
*/
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("myTag", "error : " , e);
            }
            setResult();
        }
    }

    private static void setResult() {

        txtName.setText(objItem.getPropertyName());
        txtAddress.setText(objItem.getPropertyAddress());
        //txtBath.setText(objItem.getPropertyBath() + getString(R.string.bed_bath2));
        //txtBed.setText(objItem.getPropertyBed() + getString(R.string.bed_bath));
        txtBath.setText("3" + mActivity.getString(R.string.bed_bath2));
        txtBed.setText("3" + mActivity.getString(R.string.bed_bath));
        //txtArea.setText(objBean.getPropertyArea());
        txtArea.setText("1000");
        txtPhone.setText(objItem.getPropertyPhone());
        txtPrice.setText(mActivity.getString(R.string.currency_symbol) + objItem.getPropertyPrice());
        ratingView.setRating(Float.parseFloat(objItem.getRateAvg()));

        Glide.with(mActivity).load("http://www.viaviweb.in/envato/cc/real_estate_app_new_demo/images/floor_plan/40670_house-design.jpg").into(imageFloor);
        //Picasso.get().load(objItem.getPropertyFloorPlan()).placeholder(R.drawable.header_top_logo).into(imageFloor);
     /*   Picasso.get().load("http://shivedic.in/cokarma/JSON/images/floor_plan/57386_GoodWorks-Cowork-Private-Offices-1.jpg").into(imageFloor, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Log.d("myTag", "error : " , e);
            }
        });
*/
        imageFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, FloorImageActivity.class);
                intent.putExtra("ImageF", objItem.getPropertyFloorPlan());
                mActivity.startActivity(intent);
            }
        });

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = objItem.getPropertyDescription();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #868686;text-align:left;font-size:12px;margin-left:0px;line-height:1.8}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

        //Package

        /*
        if (objBean.getPropertyFur().equals(getString(R.string.detail_un_semi))) {
            image_fur.setImageResource(R.drawable.ic_unfurnished);
            image_fur.setBackgroundResource(R.drawable.circle_gray_detail);
            textFur.setText(getString(R.string.detail_un_semi));

        } else if (objBean.getPropertyFur().equals(getString(R.string.detail_fur))) {
            image_fur.setImageResource(R.drawable.ic_furnished);
            image_fur.setBackgroundResource(R.drawable.circle_green_detail);
            textFur.setText(getString(R.string.detail_fur));
        } else if (objBean.getPropertyFur().equals(getString(R.string.detail_semi))) {
            image_fur.setImageResource(R.drawable.ic_semi_furnished);
            image_fur.setBackgroundResource(R.drawable.circle_orange_detail);
            textFur.setText(getString(R.string.detail_semi));
        }

        if (objBean.getPropertyVery().equals("1"))//verify
        {
            image_very.setBackgroundResource(R.drawable.circle_green_detail);
            image_very.setImageResource(R.drawable.ic_verified_properties);
            textVery.setText(getString(R.string.detail_very));
        } else {
            image_very.setBackgroundResource(R.drawable.circle_gray_detail);
            image_very.setImageResource(R.drawable.ic_non_verified_properties);
            textVery.setText(getString(R.string.detail_un_very));
        }
*/



        if (!mGallery.isEmpty()) {
            GalleryFragment sliderFragment = GalleryFragment.newInstance(mGallery);
            FragmentActivity activity = (FragmentActivity)view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.ContainerGallery, sliderFragment).commit();
        }

        if (!objItem.getPropertyAmenities().isEmpty())
            mAmenities = new ArrayList<>(Arrays.asList(objItem.getPropertyAmenities().split(",")));

        if (!objItem.getPropertyAmenities().isEmpty()) {
            AmenitiesFragment amenitiesFragment = AmenitiesFragment.newInstance(mAmenities);
            FragmentActivity activity = (FragmentActivity)view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.ContainerAmenities, amenitiesFragment).commit();
        } else {
            txtAmenities.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        if (!objItem.getReviews().isEmpty()) {
            ReviewFragment amenitiesFragment = ReviewFragment.newInstance(mReviewList);
            FragmentActivity activity = (FragmentActivity)view.getContext();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.ContainerReviews, amenitiesFragment).commit();
        } else {
            //txtAmenities.setVisibility(View.GONE);
            //view.setVisibility(View.GONE);
            //view1.setVisibility(View.GONE);
        }

        if (!objItem.getPropertyOffers().isEmpty())
            mOffers = new ArrayList<>(Arrays.asList(objItem.getPropertyOffers().split(",")));
        if (!objItem.getPropertyOffers().isEmpty()) {
            OffersFragment offersFragment = OffersFragment.newInstance(mOffers);
            FragmentActivity mactivity = (FragmentActivity)view.getContext();
            FragmentManager mfragmentManager = mactivity.getSupportFragmentManager();
            mfragmentManager.beginTransaction().replace(R.id.ContainerOffers, offersFragment).commit();
        } else {
            txtOffers.setVisibility(View.GONE);
            viewOffers.setVisibility(View.GONE);
            viewOffers1.setVisibility(View.GONE);
        }

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

       /*
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                getExpandableListViewSize(expandableListView);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                getExpandableListViewSize(expandableListView);
            }
        });
*/
        //Log.d("myTag", "sending : " + typeDataGroup.get(0) + " : " + typeDataGroup.get(1) + " : " + typeDataGroup.get(2));
        expandableListViewAdapter = new ExpandableListViewAdapter(mActivity, typeDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);
        getExpandableListViewSize(expandableListView);
    }

    private void showEnquiry() {
        final Dialog mDialog = new Dialog(mActivity, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.reqest_enq_dailog);
        jsonUtils = new JsonUtils(PropertyView.mActivity);
        jsonUtils.forceRTLIfSupported(mActivity.getWindow());
        TextView tv = (TextView)mDialog.findViewById(R.id.enq_label);
        tv.setText("Message");
        EditText et = (EditText) mDialog.findViewById(R.id.enquiry_text);
        et.setSelection(0);
        mDialog.show();
    }

    private void showTour() {
        final Dialog mDialog = new Dialog(mActivity, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.book_tour_dailog);
        jsonUtils = new JsonUtils(PropertyView.mActivity);
        jsonUtils.forceRTLIfSupported(mActivity.getWindow());
        //TextView tv = (TextView)mDialog.findViewById(R.id.enq_label);
        //tv.setText("Message");
        //EditText et = (EditText) mDialog.findViewById(R.id.enquiry_text);
        //et.setSelection(0);
        mDialog.show();
    }

    private static void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static void getExpandableListViewSize(ExpandableListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 10;
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1)) + 30;
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    public void showToast(String msg) {
        Toast.makeText(PropertyView.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showRating() {
        final String deviceId;
        final Dialog mDialog = new Dialog(PropertyView.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.rate_dialog);
        jsonUtils = new JsonUtils(this);
        jsonUtils.forceRTLIfSupported(getWindow());
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        final RatingView ratingView = mDialog.findViewById(R.id.ratingView);
        image_rate_close=mDialog.findViewById(R.id.image_rate_close);
        ratingView.setRating(0);
        Button button = mDialog.findViewById(R.id.btn_submit);

        image_rate_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JsonUtils.isNetworkAvailable(PropertyView.this)) {
                    new SentRating().execute(Constant.RATING_URL + Id + "&rate=" + ratingView.getRating() + "&device_id=" + deviceId);
                } else {
                    showToast(getString(R.string.conne_msg1));
                }
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class SentRating extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;
        String Rate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PropertyView.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                showToast("No data found from web!!!");

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        rateMsg = objJson.getString("MSG");
                        if (objJson.has(Constant.PROPERTY_RATE)) {
                            Rate = objJson.getString(Constant.PROPERTY_RATE);
                        } else {
                            Rate = "";
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setRate();
            }

        }

        private void setRate() {
            showToast(rateMsg);
            if (!Rate.isEmpty())
                ratingView.setRating(Float.parseFloat(Rate));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_property, menu);
        this.menu = menu;
        isFavourite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_bookmark:
                ContentValues fav = new ContentValues();
                if (databaseHelper.getFavouriteById(Id)) {
                    databaseHelper.removeFavouriteById(Id);
                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_white_24dp);
                    Toast.makeText(PropertyView.this, getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                } else {
                    fav.put(DatabaseHelper.KEY_ID, Id);
                    fav.put(DatabaseHelper.KEY_TITLE, objItem.getPropertyName());
                    fav.put(DatabaseHelper.KEY_IMAGE, objItem.getPropertyThumbnailB());
                    fav.put(DatabaseHelper.KEY_RATE, objItem.getRateAvg());
                    // fav.put(DatabaseHelper.KEY_BED, objBean.getPropertyBed());
                    //fav.put(DatabaseHelper.KEY_BATH, objBean.getPropertyBath());
                    fav.put(DatabaseHelper.KEY_ADDRESS, objItem.getPropertyAddress());
                    fav.put(DatabaseHelper.KEY_AREA, objItem.getPropertyArea());
                    fav.put(DatabaseHelper.KEY_PRICE, objItem.getPropertyPrice());
                    fav.put(DatabaseHelper.KEY_PURPOSE, objItem.getPropertyPurpose());
                    databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                    menu.getItem(0).setIcon(R.drawable.ic_bookmark_white_24dp);
                    Toast.makeText(PropertyView.this, getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    private void isFavourite() {
        if (databaseHelper.getFavouriteById(Id)) {
            menu.getItem(0).setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        //mIsBackButtonPressed = true;
        super.onBackPressed();

    }
}
