package com.example.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.realestate.R;
import com.example.adapter.AmenitiesAdapter;
import com.example.util.ItemOffsetDecoration;

import java.util.ArrayList;

public class OffersFragment extends Fragment {

    public RecyclerView recyclerView;
    static ArrayList<String> mList;
    AmenitiesAdapter mAdapter;

    public static OffersFragment newInstance(ArrayList<String> categoryId) {
        OffersFragment f = new OffersFragment();
        mList = categoryId;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_amenities, container, false);
        recyclerView = rootView.findViewById(R.id.vertical_courses_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        mAdapter = new AmenitiesAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
