package com.apps.realestate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class HotDeskFragment extends Fragment {


    public HotDeskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_desk, container, false);
        // Inflate the layout for this fragment
        String[] awayStrings = {
                "gas",
                "cook",
                "Sunset",
                       };

        ListView lv = (ListView) view.findViewById(R.id.listView2);

        ArrayAdapter<String> lva = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, awayStrings);
        lv.setAdapter(lva);
/*
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_hot_desk);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((MainActivity) getActivity()).refreshNow();
                        Toast.makeText(getContext(), "Refresh Layout working", Toast.LENGTH_LONG).show();
                    }
                }
        );
*/
        return view;
    }
}
