package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.realestate.HomeMoreActivity;
import com.apps.realestate.MainActivity;
import com.apps.realestate.PropertyDetailsActivity;
import com.apps.realestate.PropertyView;
import com.apps.realestate.R;
import com.apps.realestate.SearchActivity;
import com.example.adapter.HomeAdapter;
import com.example.db.DatabaseHelper;
import com.example.item.ItemCowork;
import com.example.item.ItemProperty;
import com.example.item.ItemType;
import com.example.util.Constant;
import com.example.util.EnchantedViewPager;
import com.example.util.ItemOffsetDecoration;
import com.example.util.JsonUtils;
import com.example.util.NothingSelectedSpinnerAdapter;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    ScrollView mScrollView;
    ProgressBar mProgressBar;
    ArrayList<ItemCowork> mSliderList;
    RecyclerView mPopularView, mLatestView;
    HomeAdapter mPopularAdapter, mLatestAdapter;
    //ArrayList<ItemProperty> mLatestList;
    ArrayList<ItemCowork> mPopularList,mLatestList;
    Button btnPopular, btnLatest;
    RelativeLayout lytRecent;
    DatabaseHelper databaseHelper;
    EnchantedViewPager mViewPager;
    CustomViewPagerAdapter mAdapter;
    CircleIndicator circleIndicator;
    int currentCount = 0;
    ArrayList<ItemType> mListType;
    ArrayList<String> mPropertyName;
    EditText edtSearch;
    Button btnSubmit;
    Spinner spinnerType, spinnerPurpose, spinnerState;
    String srt_type[];
    LinearLayout lay_home_bottom;
    private FragmentManager fragmentManager;
    View home_view_1,home_view_2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        mScrollView = rootView.findViewById(R.id.scrollView);
        mProgressBar = rootView.findViewById(R.id.progressBar);
        mPopularView = rootView.findViewById(R.id.rv_featured);
        mLatestView = rootView.findViewById(R.id.rv_latest);
        btnPopular = rootView.findViewById(R.id.btn_latest);
        btnLatest = rootView.findViewById(R.id.btn_featured);
        lytRecent = rootView.findViewById(R.id.lyt_recent_view);

        mSliderList = new ArrayList<>();
        mPopularList = new ArrayList<>();
        mLatestList = new ArrayList<>();
        mListType = new ArrayList<>();
        mPropertyName = new ArrayList<>();
        fragmentManager = requireActivity().getSupportFragmentManager();
        srt_type = getResources().getStringArray(R.array.purpose_array);

        mPopularView.setHasFixedSize(false);
        mPopularView.setNestedScrollingEnabled(false);
        mPopularView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        mPopularView.addItemDecoration(itemDecoration);
        lay_home_bottom = rootView.findViewById(R.id.lay_home_bottom);
        home_view_1=rootView.findViewById(R.id.home_view_1);
        home_view_2=rootView.findViewById(R.id.home_view_2);
        if (getActivity().getResources().getString(R.string.isRTL).equals("true")) {
            home_view_1.setBackgroundResource(R.drawable.bg_gradient_home_shadow_white_right);
            home_view_2.setBackgroundResource(R.drawable.bg_gradient_home_shadow_white_right);
         }else {
            home_view_1.setBackgroundResource(R.drawable.bg_gradient_home_shadow_white_left);
            home_view_2.setBackgroundResource(R.drawable.bg_gradient_home_shadow_white_left);
         }

        mLatestView.setHasFixedSize(false);
        mLatestView.setNestedScrollingEnabled(false);
        mLatestView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mLatestView.addItemDecoration(itemDecoration);
        mViewPager = rootView.findViewById(R.id.viewPager);
        circleIndicator = rootView.findViewById(R.id.indicator_unselected_background);

        mViewPager.useScale();
        mViewPager.removeAlpha();
        mAdapter = new CustomViewPagerAdapter();

        edtSearch = rootView.findViewById(R.id.edt_name);
        btnSubmit = rootView.findViewById(R.id.btn_submit);
        spinnerType = rootView.findViewById(R.id.spPropertyType);
        spinnerPurpose = rootView.findViewById(R.id.spPropertyPurpose);
        spinnerState = rootView.findViewById(R.id.spPropState);

        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ((MainActivity) requireActivity()).highLightNavigation(1, getString(R.string.menu_feature));
                ((MainActivity) requireActivity()).spaceNavigationView.changeCurrentItem(1);
                LatestFragment latestFragment = new LatestFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(HomeFragment.this);
                fragmentTransaction.add(R.id.Container, latestFragment);
                fragmentTransaction.commit();
                */
                Intent intent = new Intent(getActivity(), HomeMoreActivity.class);
                intent.putExtra("callUrl", "Latest");
                startActivity(intent);
            }
        });

        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeMoreActivity.class);
                intent.putExtra("callUrl", "Popular");
                startActivity(intent);
            }
        });

        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new Home().execute(Constant.HOME_URL);
        } else {
            showToast(getString(R.string.conne_msg1));
        }

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class Home extends AsyncTask<String, Void, String> {

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

            if (null == result || result.length() == 0) {
                showToast(getString(R.string.nodata));
            } else {

                try {

                    JSONObject mainJson = new JSONObject(result);
                    JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

                    JSONArray jsonSlider = jsonArray.getJSONArray(Constant.HOME_FEATURED_ARRAY);
                    JSONObject objJsonSlider;
                    for (int i = 0; i < jsonSlider.length(); i++) {
                        objJsonSlider = jsonSlider.getJSONObject(i);
                        ItemCowork objItem = new ItemCowork();
                        objItem.setPId(objJsonSlider.getString(Constant.PLACE_ID));
                        objItem.setPropertyName(objJsonSlider.getString(Constant.PLACE_TITLE));
                        objItem.setPropertyThumbnailB(objJsonSlider.getString(Constant.PLACE_IMAGE));
                        objItem.setPropertyAddress(objJsonSlider.getString(Constant.PLACE_ADDRESS));
                        objItem.setPropertyPrice("1001");
                        objItem.setRateAvg(objJsonSlider.getString(Constant.PLACE_RATE));
                        objItem.setpropertyTotalRate(objJsonSlider.getString(Constant.PLACE_TOTAL_RATE));
                        mSliderList.add(objItem);
                    }

                    JSONArray jsonLatest = jsonArray.getJSONArray(Constant.HOME_LATEST_ARRAY);
                    JSONObject objJson;
                    for (int i = 0; i < jsonLatest.length(); i++) {
                        objJson = jsonLatest.getJSONObject(i);
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
                        mLatestList.add(objItem);
                    }

                    JSONArray jsonPopular = jsonArray.getJSONArray(Constant.HOME_POPULAR_ARRAY);
                    JSONObject objJsonPopular;
                    for (int i = 0; i < jsonPopular.length(); i++) {
                        objJsonPopular = jsonPopular.getJSONObject(i);
                        ItemCowork objItem = new ItemCowork();
                        objItem.setPId(objJsonPopular.getString(Constant.PLACE_ID));
                        objItem.setPropertyName(objJsonPopular.getString(Constant.PLACE_TITLE));
                        objItem.setPropertyThumbnailB(objJsonPopular.getString(Constant.PLACE_IMAGE));
                        objItem.setRateAvg(objJsonPopular.getString(Constant.PLACE_RATE));
                        objItem.setPropertyPrice("1001");
                        //objItem.setPropertyBed(objJsonPopular.getString(Constant.PROPERTY_BED));
                        //objItem.setPropertyBath(objJsonPopular.getString(Constant.PROPERTY_BATH));
                        objItem.setPropertyStartTime(objJsonPopular.getString(Constant.PLACE_TIME_START));
                        objItem.setPropertyEndTime(objJsonPopular.getString(Constant.PLACE_TIME_END));
                        objItem.setPropertyWeekStart(objJsonPopular.getString(Constant.PLACE_WDSTART));
                        objItem.setPropertyWeekEnd(objJsonPopular.getString(Constant.PLACE_WDEND));
                        objItem.setPropertyArea("1000");
                        objItem.setPropertyAddress(objJsonPopular.getString(Constant.PLACE_ADDRESS));
                        objItem.setPropertyPurpose(objJsonPopular.getString(Constant.PLACE_PURPOSE));
                        objItem.setpropertyTotalRate(objJsonPopular.getString(Constant.PLACE_TOTAL_RATE));
                        mPopularList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }
        }
    }

    private void setResult() {
        if (getActivity() != null) {
            mLatestAdapter = new HomeAdapter(getActivity(), mLatestList);
            mLatestView.setAdapter(mLatestAdapter);

            mPopularAdapter = new HomeAdapter(getActivity(), mPopularList);
            mPopularView.setAdapter(mPopularAdapter);

            if (!mSliderList.isEmpty()) {
                mViewPager.setAdapter(mAdapter);
                circleIndicator.setViewPager(mViewPager);
                autoPlay(mViewPager);
            }

            if (JsonUtils.isNetworkAvailable(getActivity())) {
                new getType().execute(Constant.PROPERTIES_TYPE);
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class getType extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemType objItem = new ItemType();
                        objItem.setTypeId(objJson.getString(Constant.TYPE_ID));
                        objItem.setTypeName(objJson.getString(Constant.TYPE_NAME));
                        mPropertyName.add(objJson.getString(Constant.TYPE_NAME));
                        mListType.add(objItem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult2();
            }
        }
    }

    private void setResult2() {

        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(MainActivity.countryStateList.keySet());
        temp.add(0, "Select Country");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, temp);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item_home);
        spinnerType.setAdapter(typeAdapter);


        ArrayList<String> temp2 = new ArrayList<>();
        temp2.add(0, "Select State");
        ArrayAdapter<String> typeAdapterState = new ArrayAdapter<>(requireActivity(), R.layout.spinner_item_home, temp2);
        typeAdapterState.setDropDownViewResource(R.layout.spinner_item_home);
//        spinnerState.setAdapter(
  //              new NothingSelectedSpinnerAdapter(typeAdapterState,
    //    R.layout.contact_spinner_row_nothing_selected_home, requireActivity()));
        spinnerState.setAdapter(typeAdapterState);


        ArrayList<String> temp3 = new ArrayList<>();
        temp3.add(0, "Select City");
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_item_home, temp3);
        cityAdapter.setDropDownViewResource(R.layout.spinner_item_home);
        spinnerPurpose.setAdapter(cityAdapter);

//        spinnerPurpose.setAdapter(
  //              new NothingSelectedSpinnerAdapter(cityAdapter,
    //                    R.layout.contact_spinner_row_nothing_selected_home, requireActivity()));



        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                    ((TextView) parent.getChildAt(0)).setTextSize(13);

                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                    ((TextView) parent.getChildAt(0)).setTextSize(13);
                    ArrayList<String> temp = new ArrayList<>();
                    Log.d("myTag",  "Now : " + MainActivity.countryStateList.get(spinnerType.getSelectedItem().toString()));
                    temp.addAll(MainActivity.countryStateList.get(spinnerType.getSelectedItem().toString()));
                    temp.add(0, "Select State");
                    ArrayAdapter<String> typeAdapterState = new ArrayAdapter<>(requireActivity(), R.layout.spinner_item_home, temp);
                    typeAdapterState.setDropDownViewResource(R.layout.spinner_item_home);
                    spinnerState.setAdapter(
                            typeAdapterState);
                    //spinnerState.setAdapter(
                      //      new NothingSelectedSpinnerAdapter(typeAdapterState,
                        //            R.layout.contact_spinner_row_nothing_selected_home, requireActivity()));
                    spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub
                            if (position == 0) {
                                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                                ((TextView) parent.getChildAt(0)).setTextSize(13);

                            } else {
                                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                                ((TextView) parent.getChildAt(0)).setTextSize(13);
                                ArrayList<String> temp = new ArrayList<>();
                                temp.addAll(MainActivity.stateCityList.get(spinnerState.getSelectedItem().toString()));
                                temp.add(0, "Select City");
                                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_item_home, temp);
                                cityAdapter.setDropDownViewResource(R.layout.spinner_item_home);
                                spinnerPurpose.setAdapter(
                                        cityAdapter);

//                                spinnerPurpose.setAdapter(
  //                                      new NothingSelectedSpinnerAdapter(cityAdapter,
    //                                            R.layout.contact_spinner_row_nothing_selected_home, requireActivity()));
                                spinnerPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int position, long id) {
                                        // TODO Auto-generated method stub
                                        if (position == 0) {
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                                            ((TextView) parent.getChildAt(0)).setTextSize(13);

                                        } else {
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gray_light));
                                            ((TextView) parent.getChildAt(0)).setTextSize(13);

                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // TODO Auto-generated method stub

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String search = edtSearch.getText().toString();
               // if (!search.isEmpty()) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("city", String.valueOf(spinnerPurpose.getSelectedItem()));
                    intent.putExtra("state", String.valueOf(spinnerState.getSelectedItem()));
                    intent.putExtra("country", String.valueOf(spinnerType.getSelectedItem()));
                    startActivity(intent);
                   // edtSearch.getText().clear();
                //}
            }
        });


    }

    private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mAdapter != null && viewPager.getAdapter().getCount() > 0) {
                        int position = currentCount % mAdapter.getCount();
                        currentCount++;
                        viewPager.setCurrentItem(position);
                        autoPlay(viewPager);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 2500);
    }

    private class CustomViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        private CustomViewPagerAdapter() {
            // TODO Auto-generated constructor stub
            inflater = requireActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mSliderList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
            assert imageLayout != null;
            ImageView image = imageLayout.findViewById(R.id.image);
            TextView text = imageLayout.findViewById(R.id.text);
            TextView textAddress = imageLayout.findViewById(R.id.textAddress);
            LinearLayout lytParent = imageLayout.findViewById(R.id.rootLayout);
            RatingView ratingView = imageLayout.findViewById(R.id.ratingView);
            TextView text_count = imageLayout.findViewById(R.id.text_count);
            TextView text_price = imageLayout.findViewById(R.id.text_price);

            ratingView.setRating(Float.parseFloat(mSliderList.get(position).getRateAvg()));
            text_count.setText("(" + mSliderList.get(position).getpropertyTotalRate() + ")");
            text_price.setText(getString(R.string.currency_symbol) + mSliderList.get(position).getPropertyPrice());
            text.setText(mSliderList.get(position).getPropertyName());
            textAddress.setText(mSliderList.get(position).getPropertyAddress());

            Picasso.get().load(mSliderList.get(position).getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(image);
            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            lytParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PropertyView.class);
                    intent.putExtra("Id", mSliderList.get(position).getPId());
                    startActivity(intent);
                }
            });
            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
