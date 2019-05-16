package com.apps.realestate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.AmenitiesAdapter;
import com.example.adapter.ReviewsAdapter;
import com.example.fragment.AmenitiesFragment;
import com.example.item.Review;
import com.example.util.ItemOffsetDecoration;

import java.util.ArrayList;


public class ReviewFragment extends Fragment {

    public RecyclerView recyclerView;
    static ArrayList<Review> mList;
    ReviewsAdapter mAdapter;

    public static ReviewFragment newInstance(ArrayList<Review> categoryId) {
        ReviewFragment f = new ReviewFragment();
        mList = categoryId;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        recyclerView = rootView.findViewById(R.id.vertical_review_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(requireActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        mAdapter = new ReviewsAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
