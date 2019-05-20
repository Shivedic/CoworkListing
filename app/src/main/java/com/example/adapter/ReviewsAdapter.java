package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.realestate.R;
import com.example.item.Review;
import com.github.ornolfr.ratingview.RatingView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ItemRowHolder> {

private ArrayList<Review> dataList;
private Context mContext;

public ReviewsAdapter(Context context, ArrayList<Review> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        }

@Override
public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false);
        return new ItemRowHolder(v);
        }

@Override
public void onBindViewHolder(ItemRowHolder holder, final int position) {
    holder.userName.setText(dataList.get(position).getUserName());
    holder.reviewDate.setText(dataList.get(position).getDate());
    holder.reviewDesc.setText(dataList.get(position).getDesc());
    holder.reviewResp.setText("@RESPONSE : " + dataList.get(position).getResponse());
    holder.ratingView.setRating(Float.parseFloat(dataList.get(position).getTag()));

}

@Override
public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
        }

public class ItemRowHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView reviewDate;
    public TextView reviewDesc;
    public TextView reviewResp;
    RatingView ratingView;

    private ItemRowHolder(View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.username_review);
        reviewDate = itemView.findViewById(R.id.review_date);
        reviewDesc = itemView.findViewById(R.id.review_desc);
        reviewResp = itemView.findViewById(R.id.review_response);
        ratingView = itemView.findViewById(R.id.ratingView2);
    }
}
}
