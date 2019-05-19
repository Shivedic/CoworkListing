package com.example.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apps.realestate.AdvanceSearchActivity;
import com.apps.realestate.R;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.adapter.FilterAdapter;
import com.example.adapter.PropertyAdapterLatest;
import com.example.item.ItemCowork;
import com.example.item.ItemProperty;
import com.example.item.ItemType;
import com.example.util.Constant;
import com.example.util.ItemOffsetDecoration;
import com.example.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class LatestFragment extends Fragment {

    ArrayList<ItemCowork> mListItem;
    public RecyclerView recyclerView;
    PropertyAdapterLatest adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;
    ArrayList<ItemType> mListType;
    ArrayList<String> mPropertyName;
    FilterAdapter filterAdapter;
    String string_very, string_fur, final_value_min, final_value_max, string_sort;
    int save_sort = 1;
    JsonUtils jsonUtils;
    String req = "{";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.row_recyclerview_home, container, false);
        mListItem = new ArrayList<>();
        mListType = new ArrayList<>();
        mPropertyName = new ArrayList<>();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        string_sort = "DESC";
        if (JsonUtils.isNetworkAvailable(requireActivity())) {
            new getLatest().execute(Constant.FEATURE_URL);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class getLatest extends AsyncTask<String, Void, String> {

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

                        mListItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    private void displayData() {
        if (getActivity()!=null){
            adapter = new PropertyAdapterLatest(getActivity(), mListItem);
            recyclerView.setAdapter(adapter);

            if (adapter.getItemCount() == 0) {
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                lyt_not_found.setVisibility(View.GONE);
            }

            if (JsonUtils.isNetworkAvailable(getActivity())) {
                new getType().execute(Constant.PROPERTIES_TYPE);
            }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.search:
                showSearch();
                break;
            case R.id.search_sort:
                showSearchSort();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    private void showSearch() {
        final Dialog mDialog = new Dialog(requireActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.search_dialog);
        jsonUtils = new JsonUtils(getActivity());
        jsonUtils.forceRTLIfSupported(getActivity().getWindow());
        RecyclerView recyclerView = mDialog.findViewById(R.id.rv_fil_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        filterAdapter = new FilterAdapter(getActivity(), mListType);
        recyclerView.setAdapter(filterAdapter);

        ImageView image_fil_close = mDialog.findViewById(R.id.image_fil_close);
        image_fil_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        CrystalRangeSeekbar appCompatSeekBar = mDialog.findViewById(R.id.rangeSeekbar3);
        final Button buttonPriceMin = mDialog.findViewById(R.id.btn_seek_price_min);
        final Button buttonPriceMax = mDialog.findViewById(R.id.btn_seek_price_max);
        buttonPriceMax.setText(getResources().getString(R.string.max_value) + getString(R.string.max_value_price));
        buttonPriceMin.setText(getResources().getString(R.string.min_value) + getString(R.string.min_value_price));
        appCompatSeekBar.setMaxValue(Integer.parseInt(getString(R.string.min_value_price)));
        appCompatSeekBar.setMinValue(Integer.parseInt(getString(R.string.max_value_price)));
        appCompatSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                buttonPriceMin.setText(getResources().getString(R.string.min_value) + String.valueOf(minValue));
                buttonPriceMax.setText(getResources().getString(R.string.max_value) + String.valueOf(maxValue));
                final_value_max = String.valueOf(maxValue);
                final_value_min = String.valueOf(minValue);
            }
        });

        RadioGroup radioGroup = mDialog.findViewById(R.id.myRadioGroup);
        RadioButton fil_non_very = mDialog.findViewById(R.id.filter_recommended_non_very);
        RadioButton fil_very = mDialog.findViewById(R.id.filter_recommended_very);
        fil_non_very.setChecked(true);
        string_very = "0";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.filter_recommended_non_very) {
                    string_very = "0";
                } else if (checkedId == R.id.filter_recommended_very) {
                    string_very = "1";
                }
            }

        });

        RadioGroup radioGroupFur = mDialog.findViewById(R.id.myRadioGroupFur);
        RadioButton filter_fur = mDialog.findViewById(R.id.filter_fur);
        RadioButton filter_semi = mDialog.findViewById(R.id.filter_semi);
        RadioButton filter_un_semi = mDialog.findViewById(R.id.filter_un_semi);
        filter_fur.setChecked(true);
        string_fur = getString(R.string.filter_furnishing);
        radioGroupFur.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int iCheck) {
                if (iCheck == R.id.filter_fur) {
                    string_fur = getString(R.string.filter_furnishing);
                } else if (iCheck == R.id.filter_semi) {
                    string_fur = getString(R.string.filter_furnishing_semi);
                } else if (iCheck == R.id.filter_un_semi) {
                    string_fur = getString(R.string.filter_furnishing_un_fur);
                }
            }
        });

        Button btn_submit_apply = mDialog.findViewById(R.id.btn_submit);
        btn_submit_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constant.SEARCH_FIL_ID.isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.choose_one_type), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(requireActivity(), AdvanceSearchActivity.class);
                    intent.putExtra("Verify", string_very);
                    intent.putExtra("PriceMin", final_value_min);
                    intent.putExtra("PriceMax", final_value_max);
                    intent.putExtra("Furnishing", string_fur);
                    intent.putExtra("TypeId", Constant.SEARCH_FIL_ID);
                    startActivity(intent);
                    Constant.SEARCH_FIL_ID = "";
                    mDialog.dismiss();
                }

            }
        });

        mDialog.show();
    }
*/

    private void showSearch() {
        final Dialog mDialog = new Dialog(requireActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.adv_filter_dailog);
        jsonUtils = new JsonUtils(getActivity());
        jsonUtils.forceRTLIfSupported(getActivity().getWindow());

        //type
        final ImageView typeIcon = (ImageView) mDialog.findViewById(R.id.typeimg);
        final LinearLayout typeContainer = (LinearLayout) mDialog.findViewById(R.id.typecontainer);
        LinearLayout typetitle = (LinearLayout) mDialog.findViewById(R.id.typetitle);
        typeContainer.setVisibility(View.GONE);
        typetitle.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                if(typeContainer.getVisibility() == View.GONE) { typeContainer.setVisibility(View.VISIBLE); typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(typeContainer.getVisibility() == View.VISIBLE) {typeContainer.setVisibility(View.GONE); typeIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //work
        final ImageView workIcon = (ImageView) mDialog.findViewById(R.id.workimg);
        final LinearLayout workContainer = (LinearLayout) mDialog.findViewById(R.id.workholder);
        LinearLayout worktitle = (LinearLayout) mDialog.findViewById(R.id.worktitle);
        workContainer.setVisibility(View.GONE);
        worktitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workContainer.getVisibility() == View.GONE) { workContainer.setVisibility(View.VISIBLE); workIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(workContainer.getVisibility() == View.VISIBLE) {workContainer.setVisibility(View.GONE); workIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //transportation
        final ImageView transporationIcon = (ImageView) mDialog.findViewById(R.id.transportation);
        final LinearLayout transportationContainer = (LinearLayout) mDialog.findViewById(R.id.transportationholder);
        LinearLayout transportationtitle = (LinearLayout) mDialog.findViewById(R.id.transportationtitle);
        transportationContainer.setVisibility(View.GONE);
        transportationtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(transportationContainer.getVisibility() == View.GONE) { transportationContainer.setVisibility(View.VISIBLE); transporationIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(transportationContainer.getVisibility() == View.VISIBLE) {transportationContainer.setVisibility(View.GONE); transporationIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //accessibility
        final ImageView accessibilityIcon = (ImageView) mDialog.findViewById(R.id.accessibility);
        final LinearLayout accessibilityContainer = (LinearLayout) mDialog.findViewById(R.id.accessibilityholder);
        LinearLayout accessibilitytitle = (LinearLayout) mDialog.findViewById(R.id.accessibilitytitle);
        accessibilityContainer.setVisibility(View.GONE);
        accessibilitytitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accessibilityContainer.getVisibility() == View.GONE) { accessibilityContainer.setVisibility(View.VISIBLE); accessibilityIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(accessibilityContainer.getVisibility() == View.VISIBLE) {accessibilityContainer.setVisibility(View.GONE); accessibilityIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //classic basic
        final ImageView classicIcon = (ImageView) mDialog.findViewById(R.id.classic);
        final LinearLayout classicContainer = (LinearLayout) mDialog.findViewById(R.id.classicholder);
        LinearLayout classictitle = (LinearLayout) mDialog.findViewById(R.id.classictitle);
        classicContainer.setVisibility(View.GONE);
        classictitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classicContainer.getVisibility() == View.GONE) { classicContainer.setVisibility(View.VISIBLE); classicIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(classicContainer.getVisibility() == View.VISIBLE) {classicContainer.setVisibility(View.GONE); classicIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //seating
        final ImageView seatingIcon = (ImageView) mDialog.findViewById(R.id.seating);
        final LinearLayout seatingContainer = (LinearLayout) mDialog.findViewById(R.id.seatingholder);
        LinearLayout seatingtitle = (LinearLayout) mDialog.findViewById(R.id.seatingtitle);
        seatingContainer.setVisibility(View.GONE);
        seatingtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seatingContainer.getVisibility() == View.GONE) { seatingContainer.setVisibility(View.VISIBLE); seatingIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(seatingContainer.getVisibility() == View.VISIBLE) {seatingContainer.setVisibility(View.GONE); seatingIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //community
        final ImageView communityIcon = (ImageView) mDialog.findViewById(R.id.community);
        final LinearLayout communityContainer = (LinearLayout) mDialog.findViewById(R.id.communityholder);
        LinearLayout communitytitle = (LinearLayout) mDialog.findViewById(R.id.communitytitle);
        communityContainer.setVisibility(View.GONE);
        communitytitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(communityContainer.getVisibility() == View.GONE) {communityContainer.setVisibility(View.VISIBLE); communityIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(communityContainer.getVisibility() == View.VISIBLE) {communityContainer.setVisibility(View.GONE); communityIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //timming
        final ImageView timmingIcon = (ImageView) mDialog.findViewById(R.id.timming);
        final LinearLayout timmingContainer = (LinearLayout) mDialog.findViewById(R.id.timmingholder);
        LinearLayout timmingtitle = (LinearLayout) mDialog.findViewById(R.id.timmingtitle);
        timmingContainer.setVisibility(View.GONE);
        timmingtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timmingContainer.getVisibility() == View.GONE) {timmingContainer.setVisibility(View.VISIBLE); timmingIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(timmingContainer.getVisibility() == View.VISIBLE) {timmingContainer.setVisibility(View.GONE); timmingIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //working
        final ImageView workingIcon = (ImageView) mDialog.findViewById(R.id.working);
        final LinearLayout workingContainer = (LinearLayout) mDialog.findViewById(R.id.workingholder);
        LinearLayout workingtitle = (LinearLayout) mDialog.findViewById(R.id.workingtitle);
        workingContainer.setVisibility(View.GONE);
        workingtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workingContainer.getVisibility() == View.GONE) {workingContainer.setVisibility(View.VISIBLE); workingIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(workingContainer.getVisibility() == View.VISIBLE) {workingContainer.setVisibility(View.GONE); workingIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //relax zone
        final ImageView relaxIcon = (ImageView) mDialog.findViewById(R.id.relax);
        final LinearLayout relaxContainer = (LinearLayout) mDialog.findViewById(R.id.relaxholder);
        LinearLayout relaxtitle = (LinearLayout) mDialog.findViewById(R.id.relaxtitle);
        relaxContainer.setVisibility(View.GONE);
        relaxtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relaxContainer.getVisibility() == View.GONE) {relaxContainer.setVisibility(View.VISIBLE); relaxIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(relaxContainer.getVisibility() == View.VISIBLE) {relaxContainer.setVisibility(View.GONE); relaxIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //cool stuff
        final ImageView coolIcon = (ImageView) mDialog.findViewById(R.id.cool);
        final LinearLayout coolContainer = (LinearLayout) mDialog.findViewById(R.id.coolholder);
        LinearLayout cooltitle = (LinearLayout) mDialog.findViewById(R.id.cooltitle);
        coolContainer.setVisibility(View.GONE);
        cooltitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coolContainer.getVisibility() == View.GONE) {coolContainer.setVisibility(View.VISIBLE); coolIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(coolContainer.getVisibility() == View.VISIBLE) {coolContainer.setVisibility(View.GONE); coolIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //catering
        final ImageView cateringIcon = (ImageView) mDialog.findViewById(R.id.catering);
        final LinearLayout cateringContainer = (LinearLayout) mDialog.findViewById(R.id.cateringholder);
        LinearLayout cateringtitle = (LinearLayout) mDialog.findViewById(R.id.cateringtitle);
        cateringContainer.setVisibility(View.GONE);
        cateringtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cateringContainer.getVisibility() == View.GONE) {cateringContainer.setVisibility(View.VISIBLE); cateringIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(cateringContainer.getVisibility() == View.VISIBLE) {cateringContainer.setVisibility(View.GONE); cateringIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //caffein
        final ImageView caffeinIcon = (ImageView) mDialog.findViewById(R.id.caffein);
        final LinearLayout caffeinContainer = (LinearLayout) mDialog.findViewById(R.id.caffeinholder);
        LinearLayout caffeintitle = (LinearLayout) mDialog.findViewById(R.id.caffeinetitle);
        caffeinContainer.setVisibility(View.GONE);
        caffeintitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caffeinContainer.getVisibility() == View.GONE) {caffeinContainer.setVisibility(View.VISIBLE); caffeinIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(caffeinContainer.getVisibility() == View.VISIBLE) {caffeinContainer.setVisibility(View.GONE); caffeinIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //headcount
        final ImageView headcountIcon = (ImageView) mDialog.findViewById(R.id.headcount);
        final LinearLayout headcountContainer = (LinearLayout) mDialog.findViewById(R.id.headcountholder);
        LinearLayout headcounttitle = (LinearLayout) mDialog.findViewById(R.id.headcounttitle);
        headcountContainer.setVisibility(View.GONE);
        headcounttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headcountContainer.getVisibility() == View.GONE) {headcountContainer.setVisibility(View.VISIBLE); headcountIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(headcountContainer.getVisibility() == View.VISIBLE) {headcountContainer.setVisibility(View.GONE); headcountIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //type
        final ImageView Icon = (ImageView) mDialog.findViewById(R.id.type);
        final LinearLayout Container = (LinearLayout) mDialog.findViewById(R.id.typeholder);
        LinearLayout title = (LinearLayout) mDialog.findViewById(R.id.title);
        Container.setVisibility(View.GONE);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Container.getVisibility() == View.GONE) {Container.setVisibility(View.VISIBLE); Icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(Container.getVisibility() == View.VISIBLE) {Container.setVisibility(View.GONE); Icon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });

        //pay / hour
        final ImageView payIcon = (ImageView) mDialog.findViewById(R.id.pay);
        final LinearLayout payContainer = (LinearLayout) mDialog.findViewById(R.id.payholder);
        LinearLayout paytitle = (LinearLayout) mDialog.findViewById(R.id.paytitle);
        payContainer.setVisibility(View.GONE);
        paytitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payContainer.getVisibility() == View.GONE) {payContainer.setVisibility(View.VISIBLE); payIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_minus));}
                else if(payContainer.getVisibility() == View.VISIBLE) {payContainer.setVisibility(View.GONE); payIcon.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));}
            }
        });



        ImageView image_fil_close = mDialog.findViewById(R.id.image_fil_close);
        image_fil_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        // Creating post request string

        // 1. Monitor req


        final CheckBox cb = mDialog.findViewById(R.id.filter_monitor);
        mDialog.findViewById(R.id.filter_monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    req += "\"monitor\":\"1\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"monitor\":\"1\",")))));
                    if(req.contains("\"monitor\":\"1\",")) {

                        req = req.replace("\"monitor\":\"1\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });
/*
        final CheckBox cb = mDialog.findViewById(R.id.filter_monitor);
        mDialog.findViewById(R.id.filter_monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    req += "\"monitor\":\"1\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"monitor\":\"1\",")))));
                    if(req.contains("\"monitor\":\"1\",")) {

                        req = req.replace("\"monitor\":\"1\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

*/
        req = req.substring(0, req.length() - 1);
        req += "}";

        // req done

        Button btn_submit_apply = mDialog.findViewById(R.id.btn_submit);
        btn_submit_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constant.SEARCH_FIL_ID.isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.choose_one_type), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(requireActivity(), AdvanceSearchActivity.class);
                    intent.putExtra("Verify", string_very);
                    intent.putExtra("PriceMin", final_value_min);
                    intent.putExtra("PriceMax", final_value_max);
                    intent.putExtra("Furnishing", string_fur);
                    intent.putExtra("TypeId", Constant.SEARCH_FIL_ID);
                    startActivity(intent);
                    Constant.SEARCH_FIL_ID = "";
                    mDialog.dismiss();
                }

            }
        });

        mDialog.show();
    }

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
            }
        }
    }

    private void showSearchSort() {
        final Dialog mDialog = new Dialog(requireActivity(), R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.search_dialog_sort);
        jsonUtils = new JsonUtils(getActivity());
        jsonUtils.forceRTLIfSupported(getActivity().getWindow());
        RadioGroup radioGroupSort = mDialog.findViewById(R.id.myRadioGroup);
        RadioButton filter_dis = mDialog.findViewById(R.id.sort_distance);
        RadioButton filter_low = mDialog.findViewById(R.id.sort_law);
        RadioButton filter_high = mDialog.findViewById(R.id.sort_high);
        RelativeLayout rel_other = mDialog.findViewById(R.id.rel_other);
        RadioButton filter_all = mDialog.findViewById(R.id.sort_all);
        filter_all.setText(requireActivity().getString(R.string.sort_by_latest));
        View view_all = mDialog.findViewById(R.id.view_all);

        rel_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        if (save_sort == 1) {
            filter_all.setChecked(true);
        } else if (save_sort == 2) {
            filter_high.setChecked(true);
        } else if (save_sort == 3) {
            filter_low.setChecked(true);
        } else if (save_sort == 4) {
            filter_dis.setChecked(true);
        }
        string_sort = "DESC";
        radioGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int iCheck) {
                mListItem.clear();
                if (iCheck == R.id.sort_all) {
                    save_sort = 1;
                    string_sort = "LATEST";
                    if (JsonUtils.isNetworkAvailable(requireActivity())) {
                        new getLatest().execute(Constant.LATEST_URL);
                    }
                } else if (iCheck == R.id.sort_high) {
                    save_sort = 2;
                    string_sort = "DESC";
                    if (JsonUtils.isNetworkAvailable(requireActivity())) {
                        new getLatest().execute(Constant.PRICE_URL + string_sort);
                    }
                } else if (iCheck == R.id.sort_law) {
                    save_sort = 3;
                    string_sort = "ASC";
                    if (JsonUtils.isNetworkAvailable(requireActivity())) {
                        new getLatest().execute(Constant.PRICE_URL + string_sort);
                    }
                } else if (iCheck == R.id.sort_distance) {
                    save_sort = 4;
                    string_sort = getString(R.string.sort_by_distance);
                    if (JsonUtils.isNetworkAvailable(requireActivity())) {
                        new getLatest().execute(Constant.DISTANCE_URL + Constant.USER_LATITUDE + "&user_long=" + Constant.USER_LONGITUDE);
                    }
                }

                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
