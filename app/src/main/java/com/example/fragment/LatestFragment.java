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
        final LinearLayout coolContainer = (LinearLayout) mDialog.findViewById(R.id.filter_coolholder);
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
        final CheckBox cb1 = mDialog.findViewById(R.id.filter_monitor);
        mDialog.findViewById(R.id.filter_monitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb1.isChecked()){
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

        req = req.substring(0, req.length() - 1);
        req += "}";


        //2.
        final CheckBox cb2 = mDialog.findViewById(R.id.filter_sound);
        mDialog.findViewById(R.id.filter_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2.isChecked()){
                    req += "\"sound\":\"2\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"sound\":\"2\",")))));
                    if(req.contains("\"sound\":\"2\",")) {

                        req = req.replace("\"sound\":\"2\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //filter_video
        final CheckBox cb3 = mDialog.findViewById(R.id.filter_video);
        mDialog.findViewById(R.id.filter_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb3.isChecked()){
                    req += "\"video\":\"3\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"video\":\"3\",")))));
                    if(req.contains("\"video\":\"3\",")) {

                        req = req.replace("\"video\":\"3\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });
        req = req.substring(0, req.length() - 1);
        req += "}";
        //printer
        final CheckBox cb4 = mDialog.findViewById(R.id.filter_printer);
        mDialog.findViewById(R.id.filter_printer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb4.isChecked()){
                    req += "\"printer\":\"4\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"printer\":\"4\",")))));
                    if(req.contains("\"printer\":\"4\",")) {

                        req = req.replace("\"printer\":\"4\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //scanner
        final CheckBox cb5 = mDialog.findViewById(R.id.filter_scanner);
        mDialog.findViewById(R.id.filter_scanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb5.isChecked()){
                    req += "\"scanner\":\"5\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"scanner\":\"5\",")))));
                    if(req.contains("\"scanner\":\"5\",")) {

                        req = req.replace("\"scanner\":\"5\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //copier
        final CheckBox cb6 = mDialog.findViewById(R.id.filter_copier);
        mDialog.findViewById(R.id.filter_copier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb6.isChecked()){
                    req += "\"copier\":\"6\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"copier\":\"6\",")))));
                    if(req.contains("\"copier\":\"6\",")) {

                        req = req.replace("\"copier\":\"6\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //3dprint
        final CheckBox cb7 = mDialog.findViewById(R.id.filter_3dprint);
        mDialog.findViewById(R.id.filter_3dprint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb7.isChecked()){
                    req += "\"3dprint\":\"7\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"3dprint\":\"7\",")))));
                    if(req.contains("\"3dprint\":\"7\",")) {

                        req = req.replace("\"3dprint\":\"7\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //pcs
        final CheckBox cb8 = mDialog.findViewById(R.id.filter_pcs);
        mDialog.findViewById(R.id.filter_pcs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb8.isChecked()){
                    req += "\"pcs\":\"8\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"pcs\":\"8\",")))));
                    if(req.contains("\"pcs\":\"8\",")) {

                        req = req.replace("\"pcs\":\"8\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //macs
        final CheckBox cb9 = mDialog.findViewById(R.id.filter_macs);
        mDialog.findViewById(R.id.filter_macs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb9.isChecked()){
                    req += "\"macs\":\"9\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"macs\":\"9\",")))));
                    if(req.contains("\"macs\":\"9\",")) {

                        req = req.replace("\"macs\":\"9\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //chromecast
        final CheckBox cb10 = mDialog.findViewById(R.id.filter_chromecast);
        mDialog.findViewById(R.id.filter_chromecast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb10.isChecked()){
                    req += "\"chromecast\":\"10\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"chromecast\":\"10\",")))));
                    if(req.contains("\"chromecast\":\"10\",")) {

                        req = req.replace("\"chromecast\":\"10\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //appletv
        final CheckBox cb11 = mDialog.findViewById(R.id.filter_appletv);
        mDialog.findViewById(R.id.filter_appletv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb11.isChecked()){
                    req += "\"appletv\":\"11\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"appletv\":\"11\",")))));
                    if(req.contains("\"appletv\":\"11\",")) {

                        req = req.replace("\"appletv\":\"11\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //projectors
        final CheckBox cb12 = mDialog.findViewById(R.id.filter_projectors);
        mDialog.findViewById(R.id.filter_projectors).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb12.isChecked()){
                    req += "\"projectors\":\"12\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"projectors\":\"12\",")))));
                    if(req.contains("\"projectors\":\"12\",")) {

                        req = req.replace("\"projectors\":\"12\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //microphone
        final CheckBox cb13 = mDialog.findViewById(R.id.filter_microphone);
        mDialog.findViewById(R.id.filter_microphone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb13.isChecked()){
                    req += "\"microphone\":\"13\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"microphone\":\"13\",")))));
                    if(req.contains("\"microphone\":\"13\",")) {

                        req = req.replace("\"microphone\":\"13\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //photostudio
        final CheckBox cb14 = mDialog.findViewById(R.id.filter_photostudio);
        mDialog.findViewById(R.id.filter_photostudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb14.isChecked()){
                    req += "\"photostudio\":\"14\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"photostudio\":\"14\",")))));
                    if(req.contains("\"photostudio\":\"14\",")) {

                        req = req.replace("\"photostudio\":\"14\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //greenscreen
        final CheckBox cb15 = mDialog.findViewById(R.id.filter_greenscreen);
        mDialog.findViewById(R.id.filter_greenscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb15.isChecked()){
                    req += "\"greenscreen\":\"15\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"greenscreen\":\"15\",")))));
                    if(req.contains("\"greenscreen\":\"15\",")) {

                        req = req.replace("\"greenscreen\":\"15\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //recstudio
        final CheckBox cb16 = mDialog.findViewById(R.id.filter_recstudio);
        mDialog.findViewById(R.id.filter_recstudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb16.isChecked()){
                    req += "\"recstudio\":\"16\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"recstudio\":\"16\",")))));
                    if(req.contains("\"recstudio\":\"16\",")) {

                        req = req.replace("\"recstudio\":\"16\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //litequip
        final CheckBox cb17 = mDialog.findViewById(R.id.filter_litequip);
        mDialog.findViewById(R.id.filter_litequip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb17.isChecked()){
                    req += "\"litequip\":\"17\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"litequip\":\"17\",")))));
                    if(req.contains("\"litequip\":\"17\",")) {

                        req = req.replace("\"litequip\":\"17\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //drone
        final CheckBox cb18 = mDialog.findViewById(R.id.filter_drone);
        mDialog.findViewById(R.id.filter_drone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb18.isChecked()){
                    req += "\"drone\":\"18\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"drone\":\"18\",")))));
                    if(req.contains("\"drone\":\"18\",")) {

                        req = req.replace("\"drone\":\"18\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //arequip
        final CheckBox cb19 = mDialog.findViewById(R.id.filter_arequip);
        mDialog.findViewById(R.id.filter_arequip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb19.isChecked()){
                    req += "\"arequip\":\"19\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"arequip\":\"19\",")))));
                    if(req.contains("\"arequip\":\"19\",")) {

                        req = req.replace("\"arequip\":\"19\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //vrequip
        final CheckBox cb20 = mDialog.findViewById(R.id.filter_vrequip);
        mDialog.findViewById(R.id.filter_vrequip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb20.isChecked()){
                    req += "\"vrequip\":\"20\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"vrequip\":\"20\",")))));
                    if(req.contains("\"vrequip\":\"20\",")) {

                        req = req.replace("\"vrequip\":\"20\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //screenprint
        final CheckBox cb21 = mDialog.findViewById(R.id.filter_screenprint);
        mDialog.findViewById(R.id.filter_screenprint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb21.isChecked()){
                    req += "\"screenprint\":\"21\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"screenprint\":\"21\",")))));
                    if(req.contains("\"screenprint\":\"21\",")) {

                        req = req.replace("\"screenprint\":\"21\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //parking
        final CheckBox cb22 = mDialog.findViewById(R.id.filter_parking);
        mDialog.findViewById(R.id.filter_parking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb22.isChecked()){
                    req += "\"parking\":\"22\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"parking\":\"22\",")))));
                    if(req.contains("\"parking\":\"22\",")) {

                        req = req.replace("\"parking\":\"22\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //bikeparking
        final CheckBox cb23 = mDialog.findViewById(R.id.filter_bikeparking);
        mDialog.findViewById(R.id.filter_bikeparking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb23.isChecked()){
                    req += "\"bikeparking\":\"23\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"bikeparking\":\"23\",")))));
                    if(req.contains("\"bikeparking\":\"23\",")) {

                        req = req.replace("\"bikeparking\":\"23\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //carshare
        final CheckBox cb24 = mDialog.findViewById(R.id.filter_carshare);
        mDialog.findViewById(R.id.filter_carshare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb24.isChecked()){
                    req += "\"carshare\":\"24\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"carshare\":\"24\",")))));
                    if(req.contains("\"carshare\":\"24\",")) {

                        req = req.replace("\"carshare\":\"24\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //walk
        final CheckBox cb25 = mDialog.findViewById(R.id.filter_walk);
        mDialog.findViewById(R.id.filter_walk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb25.isChecked()){
                    req += "\"walk\":\"25\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"walk\":\"25\",")))));
                    if(req.contains("\"walk\":\"25\",")) {

                        req = req.replace("\"walk\":\"25\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //10min
        final CheckBox cb26 = mDialog.findViewById(R.id.filter_10min);
        mDialog.findViewById(R.id.filter_10min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb26.isChecked()){
                    req += "\"10min\":\"26\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"10min\":\"26\",")))));
                    if(req.contains("\"10min\":\"26\",")) {

                        req = req.replace("\"10min\":\"26\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //wheelchair
        final CheckBox cb27 = mDialog.findViewById(R.id.filter_wheelchair);
        mDialog.findViewById(R.id.filter_wheelchair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb27.isChecked()){
                    req += "\"wheelchair\":\"27\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"wheelchair\":\"27\",")))));
                    if(req.contains("\"wheelchair\":\"27\",")) {

                        req = req.replace("\"wheelchair\":\"27\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //member
        final CheckBox cb28 = mDialog.findViewById(R.id.filter_member);
        mDialog.findViewById(R.id.filter_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb28.isChecked()){
                    req += "\"member\":\"28\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"member\":\"28\",")))));
                    if(req.contains("\"member\":\"28\",")) {

                        req = req.replace("\"member\":\"28\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //wifi
        final CheckBox cb29 = mDialog.findViewById(R.id.filter_wifi);
        mDialog.findViewById(R.id.filter_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb29.isChecked()){
                    req += "\"wifi\":\"29\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"wifi\":\"29\",")))));
                    if(req.contains("\"wifi\":\"29\",")) {

                        req = req.replace("\"wifi\":\"29\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //heating
        final CheckBox cb30 = mDialog.findViewById(R.id.filter_heating);
        mDialog.findViewById(R.id.filter_heating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb30.isChecked()){
                    req += "\"heating\":\"30\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"heating\":\"30\",")))));
                    if(req.contains("\"heating\":\"30\",")) {

                        req = req.replace("\"heating\":\"30\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //ac
        final CheckBox cb31 = mDialog.findViewById(R.id.filter_ac);
        mDialog.findViewById(R.id.filter_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb31.isChecked()){
                    req += "\"ac\":\"31\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"ac\":\"31\",")))));
                    if(req.contains("\"ac\":\"31\",")) {

                        req = req.replace("\"ac\":\"31\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //desk
        final CheckBox cb32 = mDialog.findViewById(R.id.filter_desk);
        mDialog.findViewById(R.id.filter_desk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb32.isChecked()){
                    req += "\"desk\":\"32\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"desk\":\"32\",")))));
                    if(req.contains("\"desk\":\"32\",")) {

                        req = req.replace("\"desk\":\"32\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //beanbags
        final CheckBox cb33 = mDialog.findViewById(R.id.filter_beanbags);
        mDialog.findViewById(R.id.filter_beanbags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb33.isChecked()){
                    req += "\"beanbags\":\"33\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"beanbags\":\"33\",")))));
                    if(req.contains("\"beanbags\":\"33\",")) {

                        req = req.replace("\"beanbags\":\"33\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //chairs
        final CheckBox cb34 = mDialog.findViewById(R.id.filter_chairs);
        mDialog.findViewById(R.id.filter_chairs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb34.isChecked()){
                    req += "\"chairs\":\"34\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"chairs\":\"34\",")))));
                    if(req.contains("\"chairs\":\"34\",")) {

                        req = req.replace("\"chairs\":\"34\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //ballchair
        final CheckBox cb35 = mDialog.findViewById(R.id.filter_ballchair);
        mDialog.findViewById(R.id.filter_ballchair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb35.isChecked()){
                    req += "\"ballchair\":\"35\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"ballchair\":\"35\",")))));
                    if(req.contains("\"ballchair\":\"35\",")) {

                        req = req.replace("\"ballchair\":\"35\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //hammocks
        final CheckBox cb36 = mDialog.findViewById(R.id.filter_hammocks);
        mDialog.findViewById(R.id.filter_hammocks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb36.isChecked()){
                    req += "\"hammocks\":\"36\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"hammocks\":\"36\",")))));
                    if(req.contains("\"hammocks\":\"36\",")) {

                        req = req.replace("\"hammocks\":\"36\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //events
        final CheckBox cb37 = mDialog.findViewById(R.id.filter_events);
        mDialog.findViewById(R.id.filter_events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb37.isChecked()){
                    req += "\"events\":\"37\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"events\":\"37\",")))));
                    if(req.contains("\"events\":\"37\",")) {

                        req = req.replace("\"events\":\"37\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //workshop
        final CheckBox cb38 = mDialog.findViewById(R.id.filter_workshop);
        mDialog.findViewById(R.id.filter_workshop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb38.isChecked()){
                    req += "\"workshop\":\"38\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"workshop\":\"38\",")))));
                    if(req.contains("\"workshop\":\"38\",")) {

                        req = req.replace("\"workshop\":\"38\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //lunches
        final CheckBox cb39 = mDialog.findViewById(R.id.filter_lunches);
        mDialog.findViewById(R.id.filter_lunches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb39.isChecked()){
                    req += "\"lunches\":\"39\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"lunches\":\"39\",")))));
                    if(req.contains("\"lunches\":\"39\",")) {

                        req = req.replace("\"lunches\":\"39\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //drinks
        final CheckBox cb40 = mDialog.findViewById(R.id.filter_drinks);
        mDialog.findViewById(R.id.filter_drinks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb40.isChecked()){
                    req += "\"drinks\":\"40\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"drinks\":\"40\",")))));
                    if(req.contains("\"drinks\":\"40\",")) {

                        req = req.replace("\"drinks\":\"40\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //facebook
        final CheckBox cb41 = mDialog.findViewById(R.id.filter_facebook);
        mDialog.findViewById(R.id.filter_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb41.isChecked()){
                    req += "\"facebook\":\"41\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"facebook\":\"41\",")))));
                    if(req.contains("\"facebook\":\"41\",")) {

                        req = req.replace("\"facebook\":\"41\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //slack
        final CheckBox cb42 = mDialog.findViewById(R.id.filter_slack);
        mDialog.findViewById(R.id.filter_slack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb42.isChecked()){
                    req += "\"slack\":\"42\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"slack\":\"42\",")))));
                    if(req.contains("\"slack\":\"42\",")) {

                        req = req.replace("\"slack\":\"42\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //mentorship
        final CheckBox cb43 = mDialog.findViewById(R.id.filter_mentorship);
        mDialog.findViewById(R.id.filter_mentorship).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb43.isChecked()){
                    req += "\"mentorship\":\"43\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"mentorship\":\"43\",")))));
                    if(req.contains("\"mentorship\":\"43\",")) {

                        req = req.replace("\"mentorship\":\"43\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //app
        final CheckBox cb44 = mDialog.findViewById(R.id.filter_app);
        mDialog.findViewById(R.id.filter_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb44.isChecked()){
                    req += "\"app\":\"44\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"app\":\"44\",")))));
                    if(req.contains("\"app\":\"44\",")) {

                        req = req.replace("\"app\":\"44\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //pitching
        final CheckBox cb45 = mDialog.findViewById(R.id.filter_pitching);
        mDialog.findViewById(R.id.filter_pitching).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb45.isChecked()){
                    req += "\"pitching\":\"45\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"pitching\":\"45\",")))));
                    if(req.contains("\"pitching\":\"45\",")) {

                        req = req.replace("\"pitching\":\"45\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //pitching
        final CheckBox cb46 = mDialog.findViewById(R.id.filter_pitching);
        mDialog.findViewById(R.id.filter_pitching).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb46.isChecked()){
                    req += "\"pitching\":\"46\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"pitching\":\"46\",")))));
                    if(req.contains("\"pitching\":\"46\",")) {

                        req = req.replace("\"pitching\":\"46\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //incubator
        final CheckBox cb47 = mDialog.findViewById(R.id.filter_incubator );
        mDialog.findViewById(R.id.filter_incubator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb47.isChecked()){
                    req += "\"incubator\":\"47\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"incubator\":\"47\",")))));
                    if(req.contains("\"incubator\":\"47\",")) {

                        req = req.replace("\"incubator\":\"47\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //accelerator
        final CheckBox cb48 = mDialog.findViewById(R.id.filter_accelerator );
        mDialog.findViewById(R.id.filter_accelerator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb48.isChecked()){
                    req += "\"accelerator\":\"48\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"accelerator\":\"48\",")))));
                    if(req.contains("\"accelerator\":\"48\",")) {

                        req = req.replace("\"accelerator\":\"48\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //sports
        final CheckBox cb49 = mDialog.findViewById(R.id.filter_sports );
        mDialog.findViewById(R.id.filter_sports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb49.isChecked()){
                    req += "\"sports\":\"49\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"sports\":\"49\",")))));
                    if(req.contains("\"sports\":\"49\",")) {

                        req = req.replace("\"sports\":\"49\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //toastmaster
        final CheckBox cb50 = mDialog.findViewById(R.id.filter_toastmaster );
        mDialog.findViewById(R.id.filter_toastmaster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb50.isChecked()){
                    req += "\"toastmaster\":\"50\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"toastmaster\":\"50\",")))));
                    if(req.contains("\"toastmaster\":\"50\",")) {

                        req = req.replace("\"toastmaster\":\"50\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //day
        final CheckBox cb51 = mDialog.findViewById(R.id.filter_day );
        mDialog.findViewById(R.id.filter_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb51.isChecked()){
                    req += "\"day\":\"51\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"day\":\"51\",")))));
                    if(req.contains("\"day\":\"51\",")) {

                        req = req.replace("\"day\":\"51\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //night
        final CheckBox cb52 = mDialog.findViewById(R.id.filter_night );
        mDialog.findViewById(R.id.filter_night).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb52.isChecked()){
                    req += "\"night\":\"52\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"night\":\"52\",")))));
                    if(req.contains("\"night\":\"52\",")) {

                        req = req.replace("\"night\":\"52\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //hour24
        final CheckBox cb53 = mDialog.findViewById(R.id.filter_hour24 );
        mDialog.findViewById(R.id.filter_hour24).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb53.isChecked()){
                    req += "\"hour24\":\"53\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"hour24\":\"53\",")))));
                    if(req.contains("\"hour24\":\"53\",")) {

                        req = req.replace("\"hour24\":\"53\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //days5
        final CheckBox cb54 = mDialog.findViewById(R.id.filter_days5 );
        mDialog.findViewById(R.id.filter_days5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb54.isChecked()){
                    req += "\"days5\":\"54\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"days5\":\"54\",")))));
                    if(req.contains("\"days5\":\"54\",")) {

                        req = req.replace("\"days5\":\"54\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //days6
        final CheckBox cb55 = mDialog.findViewById(R.id.filter_days6 );
        mDialog.findViewById(R.id.filter_days6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb55.isChecked()){
                    req += "\"days6\":\"55\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"days6\":\"55\",")))));
                    if(req.contains("\"days6\":\"55\",")) {

                        req = req.replace("\"days6\":\"55\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //outdoor
        final CheckBox cb56 = mDialog.findViewById(R.id.filter_outdoor );
        mDialog.findViewById(R.id.filter_outdoor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb56.isChecked()){
                    req += "\"outdoor\":\"56\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"outdoor\":\"56\",")))));
                    if(req.contains("\"outdoor\":\"56\",")) {

                        req = req.replace("\"outdoor\":\"56\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //swimming
        final CheckBox cb57 = mDialog.findViewById(R.id.filter_swimming );
        mDialog.findViewById(R.id.filter_swimming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb57.isChecked()){
                    req += "\"swimming\":\"57\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"swimming\":\"57\",")))));
                    if(req.contains("\"swimming\":\"57\",")) {

                        req = req.replace("\"swimming\":\"57\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //lounge
        final CheckBox cb58 = mDialog.findViewById(R.id.filter_lounge );
        mDialog.findViewById(R.id.filter_lounge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb58.isChecked()){
                    req += "\"lounge\":\"58\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"lounge\":\"58\",")))));
                    if(req.contains("\"lounge\":\"58\",")) {

                        req = req.replace("\"lounge\":\"58\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //nap
        final CheckBox cb59 = mDialog.findViewById(R.id.filter_nap );
        mDialog.findViewById(R.id.filter_nap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb59.isChecked()){
                    req += "\"nap\":\"59\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"nap\":\"59\",")))));
                    if(req.contains("\"nap\":\"59\",")) {

                        req = req.replace("\"nap\":\"59\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //yoga
        final CheckBox cb60 = mDialog.findViewById(R.id.filter_yoga );
        mDialog.findViewById(R.id.filter_yoga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb60.isChecked()){
                    req += "\"yoga\":\"60\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"yoga\":\"60\",")))));
                    if(req.contains("\"yoga\":\"60\",")) {

                        req = req.replace("\"yoga\":\"60\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //meditation
        final CheckBox cb61 = mDialog.findViewById(R.id.filter_meditation );
        mDialog.findViewById(R.id.filter_meditation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb61.isChecked()){
                    req += "\"meditation\":\"61\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"meditation\":\"61\",")))));
                    if(req.contains("\"meditation\":\"61\",")) {

                        req = req.replace("\"meditation\":\"61\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //coolholder
        final CheckBox cb62 = mDialog.findViewById(R.id.filter_coolholder );
        mDialog.findViewById(R.id.filter_coolholder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb62.isChecked()){
                    req += "\"coolholder\":\"62\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"coolholder\":\"62\",")))));
                    if(req.contains("\"coolholder\":\"62\",")) {

                        req = req.replace("\"coolholder\":\"62\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";


        //pool
        final CheckBox cb63 = mDialog.findViewById(R.id.filter_pool );
        mDialog.findViewById(R.id.filter_pool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb63.isChecked()){
                    req += "\"pool\":\"63\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"pool\":\"63\",")))));
                    if(req.contains("\"pool\":\"63\",")) {

                        req = req.replace("\"pool\":\"63\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //foosball
        final CheckBox cb64 = mDialog.findViewById(R.id.filter_foosball );
        mDialog.findViewById(R.id.filter_foosball).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb64.isChecked()){
                    req += "\"foosball\":\"64\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"foosball\":\"64\",")))));
                    if(req.contains("\"foosball\":\"64\",")) {

                        req = req.replace("\"foosball\":\"64\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //trampoline
        final CheckBox cb65 = mDialog.findViewById(R.id.filter_trampoline );
        mDialog.findViewById(R.id.filter_trampoline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb65.isChecked()){
                    req += "\"trampoline\":\"65\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"trampoline\":\"65\",")))));
                    if(req.contains("\"trampoline\":\"65\",")) {

                        req = req.replace("\"trampoline\":\"65\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //min
        final CheckBox cb66 = mDialog.findViewById(R.id.filter_min );
        mDialog.findViewById(R.id.filter_min).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb66.isChecked()){
                    req += "\"min\":\"66\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"min\":\"66\",")))));
                    if(req.contains("\"min\":\"66\",")) {

                        req = req.replace("\"min\":\"66\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //library
        final CheckBox cb67 = mDialog.findViewById(R.id.filter_library );
        mDialog.findViewById(R.id.filter_library).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb67.isChecked()){
                    req += "\"library\":\"67\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"library\":\"67\",")))));
                    if(req.contains("\"library\":\"67\",")) {

                        req = req.replace("\"library\":\"67\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //gym
        final CheckBox cb68 = mDialog.findViewById(R.id.filter_gym );
        mDialog.findViewById(R.id.filter_gym).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb68.isChecked()){
                    req += "\"gym\":\"68\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"gym\":\"68\",")))));
                    if(req.contains("\"gym\":\"68\",")) {

                        req = req.replace("\"gym\":\"68\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //wine
        final CheckBox cb69 = mDialog.findViewById(R.id.filter_wine );
        mDialog.findViewById(R.id.filter_wine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb69.isChecked()){
                    req += "\"wine\":\"69\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"wine\":\"69\",")))));
                    if(req.contains("\"wine\":\"69\",")) {

                        req = req.replace("\"wine\":\"69\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //games
        final CheckBox cb70 = mDialog.findViewById(R.id.filter_games );
        mDialog.findViewById(R.id.filter_games).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb70.isChecked()){
                    req += "\"games\":\"70\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"games\":\"70\",")))));
                    if(req.contains("\"games\":\"70\",")) {

                        req = req.replace("\"games\":\"70\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //karaoke
        final CheckBox cb71 = mDialog.findViewById(R.id.filter_karaoke );
        mDialog.findViewById(R.id.filter_karaoke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb71.isChecked()){
                    req += "\"karaoke\":\"71\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"karaoke\":\"71\",")))));
                    if(req.contains("\"karaoke\":\"71\",")) {

                        req = req.replace("\"karaoke\":\"71\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //dog
        final CheckBox cb72 = mDialog.findViewById(R.id.filter_dog );
        mDialog.findViewById(R.id.filter_dog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb72.isChecked()){
                    req += "\"dog\":\"72\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"dog\":\"72\",")))));
                    if(req.contains("\"dog\":\"72\",")) {

                        req = req.replace("\"dog\":\"72\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //art
        final CheckBox cb73 = mDialog.findViewById(R.id.filter_art );
        mDialog.findViewById(R.id.filter_art).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb73.isChecked()){
                    req += "\"art\":\"73\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"art\":\"73\",")))));
                    if(req.contains("\"art\":\"73\",")) {

                        req = req.replace("\"art\":\"73\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //laundry
        final CheckBox cb74 = mDialog.findViewById(R.id.filter_laundry );
        mDialog.findViewById(R.id.filter_laundry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb74.isChecked()){
                    req += "\"laundry\":\"74\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"laundry\":\"74\",")))));
                    if(req.contains("\"laundry\":\"74\",")) {

                        req = req.replace("\"laundry\":\"74\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //court
        final CheckBox cb75 = mDialog.findViewById(R.id.filter_court );
        mDialog.findViewById(R.id.filter_court).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb75.isChecked()){
                    req += "\"court\":\"75\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"court\":\"75\",")))));
                    if(req.contains("\"court\":\"75\",")) {

                        req = req.replace("\"court\":\"75\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //water
        final CheckBox cb76 = mDialog.findViewById(R.id.filter_water );
        mDialog.findViewById(R.id.filter_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb76.isChecked()){
                    req += "\"water\":\"76\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"water\":\"76\",")))));
                    if(req.contains("\"water\":\"76\",")) {

                        req = req.replace("\"water\":\"76\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //alcohol
        final CheckBox cb77 = mDialog.findViewById(R.id.filter_alcohol );
        mDialog.findViewById(R.id.filter_alcohol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb77.isChecked()){
                    req += "\"alcohol\":\"77\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"alcohol\":\"77\",")))));
                    if(req.contains("\"alcohol\":\"77\",")) {

                        req = req.replace("\"alcohol\":\"77\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //snacks
        final CheckBox cb78 = mDialog.findViewById(R.id.filter_snacks );
        mDialog.findViewById(R.id.filter_snacks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb78.isChecked()){
                    req += "\"snacks\":\"78\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"snacks\":\"78\",")))));
                    if(req.contains("\"snacks\":\"78\",")) {

                        req = req.replace("\"snacks\":\"78\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //kitchen
        final CheckBox cb79 = mDialog.findViewById(R.id.filter_kitchen );
        mDialog.findViewById(R.id.filter_kitchen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb79.isChecked()){
                    req += "\"kitchen\":\"79\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"kitchen\":\"79\",")))));
                    if(req.contains("\"kitchen\":\"79\",")) {

                        req = req.replace("\"kitchen\":\"79\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //purchasesnack
        final CheckBox cb80 = mDialog.findViewById(R.id.filter_purchasesnack );
        mDialog.findViewById(R.id.filter_purchasesnack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb80.isChecked()){
                    req += "\"purchasesnack\":\"80\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"purchasesnack\":\"80\",")))));
                    if(req.contains("\"purchasesnack\":\"80\",")) {

                        req = req.replace("\"purchasesnack\":\"80\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //onsite
        final CheckBox cb81 = mDialog.findViewById(R.id.filter_onsite );
        mDialog.findViewById(R.id.filter_onsite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb81.isChecked()){
                    req += "\"onsite\":\"81\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"onsite\":\"81\",")))));
                    if(req.contains("\"onsite\":\"81\",")) {

                        req = req.replace("\"onsite\":\"81\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //tea
        final CheckBox cb82 = mDialog.findViewById(R.id.filter_tea );
        mDialog.findViewById(R.id.filter_tea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb82.isChecked()){
                    req += "\"tea\":\"82\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"tea\":\"82\",")))));
                    if(req.contains("\"tea\":\"82\",")) {

                        req = req.replace("\"tea\":\"82\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //coffee
        final CheckBox cb83 = mDialog.findViewById(R.id.filter_coffee );
        mDialog.findViewById(R.id.filter_coffee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb83.isChecked()){
                    req += "\"coffee\":\"83\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"coffee\":\"83\",")))));
                    if(req.contains("\"coffee\":\"83\",")) {

                        req = req.replace("\"coffee\":\"83\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //cafe
        final CheckBox cb84 = mDialog.findViewById(R.id.filter_cafe );
        mDialog.findViewById(R.id.filter_cafe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb84.isChecked()){
                    req += "\"cafe\":\"84\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"cafe\":\"84\",")))));
                    if(req.contains("\"cafe\":\"84\",")) {

                        req = req.replace("\"cafe\":\"84\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //coffeepurchase
        final CheckBox cb85 = mDialog.findViewById(R.id.filter_coffeepurchase );
        mDialog.findViewById(R.id.filter_coffeepurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb85.isChecked()){
                    req += "\"coffeepurchase\":\"85\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"coffeepurchase\":\"85\",")))));
                    if(req.contains("\"coffeepurchase\":\"85\",")) {

                        req = req.replace("\"coffeepurchase\":\"85\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //individual
        final CheckBox cb86 = mDialog.findViewById(R.id.filter_individual );
        mDialog.findViewById(R.id.filter_individual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb86.isChecked()){
                    req += "\"individual\":\"86\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"individual\":\"86\",")))));
                    if(req.contains("\"individual\":\"86\",")) {

                        req = req.replace("\"individual\":\"86\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //team2
        final CheckBox cb87 = mDialog.findViewById(R.id.filter_team2 );
        mDialog.findViewById(R.id.filter_team2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb87.isChecked()){
                    req += "\"team2\":\"87\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"team2\":\"87\",")))));
                    if(req.contains("\"team2\":\"87\",")) {

                        req = req.replace("\"team2\":\"87\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //team3
        final CheckBox cb88 = mDialog.findViewById(R.id.filter_team3 );
        mDialog.findViewById(R.id.filter_team3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb88.isChecked()){
                    req += "\"team3\":\"88\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"team3\":\"88\",")))));
                    if(req.contains("\"team3\":\"88\",")) {

                        req = req.replace("\"team3\":\"88\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //team7
        final CheckBox cb89 = mDialog.findViewById(R.id.filter_team7 );
        mDialog.findViewById(R.id.filter_team7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb89.isChecked()){
                    req += "\"team7\":\"89\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"team7\":\"89\",")))));
                    if(req.contains("\"team7\":\"89\",")) {

                        req = req.replace("\"team7\":\"89\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //conference
        final CheckBox cb90 = mDialog.findViewById(R.id.filter_conference );
        mDialog.findViewById(R.id.filter_conference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb90.isChecked()){
                    req += "\"conference\":\"90\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"conference\":\"90\",")))));
                    if(req.contains("\"conference\":\"90\",")) {

                        req = req.replace("\"conference\":\"90\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //meeting
        final CheckBox cb91 = mDialog.findViewById(R.id.filter_meeting );
        mDialog.findViewById(R.id.filter_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb91.isChecked()){
                    req += "\"meeting\":\"91\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"meeting\":\"91\",")))));
                    if(req.contains("\"meeting\":\"91\",")) {

                        req = req.replace("\"meeting\":\"91\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //office
        final CheckBox cb92 = mDialog.findViewById(R.id.filter_office );
        mDialog.findViewById(R.id.filter_office).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb92.isChecked()){
                    req += "\"office\":\"92\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"office\":\"92\",")))));
                    if(req.contains("\"office\":\"92\",")) {

                        req = req.replace("\"office\":\"92\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //seating
        final CheckBox cb93 = mDialog.findViewById(R.id.filter_seating );
        mDialog.findViewById(R.id.filter_seating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb93.isChecked()){
                    req += "\"seating\":\"93\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"seating\":\"93\",")))));
                    if(req.contains("\"seating\":\"93\",")) {

                        req = req.replace("\"seating\":\"93\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //virtual
        final CheckBox cb94 = mDialog.findViewById(R.id.filter_virtual );
        mDialog.findViewById(R.id.filter_virtual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb94.isChecked()){
                    req += "\"virtual\":\"94\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"virtual\":\"94\",")))));
                    if(req.contains("\"virtual\":\"94\",")) {

                        req = req.replace("\"virtual\":\"94\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //coference
        final CheckBox cb95 = mDialog.findViewById(R.id.fitler_conference );
        mDialog.findViewById(R.id.fitler_conference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb95.isChecked()){
                    req += "\"coference\":\"95\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"coference\":\"95\",")))));
                    if(req.contains("\"coference\":\"95\",")) {

                        req = req.replace("\"coference\":\"95\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";

        //meeting
        final CheckBox cb96 = mDialog.findViewById(R.id.filter15_meeting );
        mDialog.findViewById(R.id.filter15_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb96.isChecked()){
                    req += "\"meeting\":\"96\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"meeting\":\"96\",")))));
                    if(req.contains("\"meeting\":\"96\",")) {

                        req = req.replace("\"meeting\":\"96\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";


        //
        final CheckBox cb97 = mDialog.findViewById(R.id.filter15_meeting );
        mDialog.findViewById(R.id.filter15_meeting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb97.isChecked()){
                    req += "\"meeting\":\"97\",";
                    Log.d("myTag", "req addition : " + req);
                } else {
                    Log.d("myTag", (String.valueOf( (req.contains("\"meeting\":\"97\",")))));
                    if(req.contains("\"meeting\":\"97\",")) {

                        req = req.replace("\"meeting\":\"97\",", "");
                    }
                    Log.d("myTag", "req subtract : " + req);
                }
            }
        });

        req = req.substring(0, req.length() - 1);
        req += "}";
        //req done

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
