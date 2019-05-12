package com.example.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.apps.realestate.R;
import com.example.item.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    // group titles
    private List<String> listDataGroup;

    // child data in format of header title, child title
    private HashMap<String, List<Package>> listDataChild;
    List<Package> listCurrPack = new ArrayList<>();

    public ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, List<Package>> listChildData) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_pack_child, null);
        }

        Package p = listDataChild.get(this.listDataGroup.get(groupPosition)).get(childPosition);
        TextView packName = convertView
                .findViewById(R.id.pack_name);
        packName.setText(p.getName());
        TextView packPrice = convertView
                .findViewById(R.id.pack_price);
        packPrice.setText(p.getPrice());
        TextView packNumber = convertView
                .findViewById(R.id.no_people);
        packNumber.setText(p.getSeats());
        TextView packDuration = convertView
                .findViewById(R.id.pack_duration);
        packDuration.setText(p.getDuration());
        TextView packStartTime = convertView
                .findViewById(R.id.time_start);
        packStartTime.setText(p.getStartTime());
        TextView packEndTime = convertView
                .findViewById(R.id.time_end);
        packEndTime.setText(p.getEndTime());
        TextView packDesc = convertView
                .findViewById(R.id.desc_text);
        packDesc.setText(p.getDescription());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_group, null);
        }

        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}