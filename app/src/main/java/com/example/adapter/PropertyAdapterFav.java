package com.example.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.realestate.PropertyDetailsActivity;
import com.apps.realestate.R;
import com.example.db.DatabaseHelper;
import com.example.item.ItemCowork;
import com.example.item.ItemProperty;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.github.ornolfr.ratingview.RatingView;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PropertyAdapterFav extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ItemCowork> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;
    private int AD_COUNT = 0;
    private DatabaseHelper databaseHelper;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public PropertyAdapterFav(Context context, ArrayList<ItemCowork> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_estate_item, parent, false);
            vh = new ItemRowHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_estate_item_left, parent, false);

            vh = new SecondViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == VIEW_ITEM) {
            final ItemRowHolder holder = (ItemRowHolder) viewHolder;
            final ItemCowork singleItem = dataList.get(position);
            holder.text.setText(singleItem.getPropertyName());
            holder.textPrice.setText(mContext.getString(R.string.currency_symbol)+singleItem.getPropertyPrice());
            holder.textAddress.setText(singleItem.getPropertyAddress());
            holder.textBed.setText(singleItem.getPropertyStartTime()+" - "+ singleItem.getPropertyEndTime());
            holder.textBath.setText(singleItem.getPropertyWeekStart()+" - "+singleItem.getPropertyWeekEnd());
          //  holder.textSquare.setText(singleItem.getPropertyArea());
            holder.ratingView.setVisibility(View.GONE);
             Picasso.get().load(singleItem.getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(holder.image);
            holder.textTotalRate.setVisibility(View.GONE);

            if (databaseHelper.getFavouriteById(singleItem.getPId())) {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            } else {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
            }

            holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues fav = new ContentValues();
                    if (databaseHelper.getFavouriteById(singleItem.getPId())) {
                        databaseHelper.removeFavouriteById(singleItem.getPId());
                        holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
                        Toast.makeText(mContext, mContext.getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                    } else {
                        fav.put(DatabaseHelper.KEY_ID, singleItem.getPId());
                        fav.put(DatabaseHelper.KEY_TITLE, singleItem.getPropertyName());
                        fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getPropertyThumbnailB());
                        fav.put(DatabaseHelper.KEY_RATE, singleItem.getRateAvg());
                        fav.put(DatabaseHelper.KEY_STARTT, singleItem.getPropertyStartTime());
                        fav.put(DatabaseHelper.KEY_ENDT, singleItem.getPropertyEndTime());
                        fav.put(DatabaseHelper.KEY_WEEKS, singleItem.getPropertyWeekStart());
                        fav.put(DatabaseHelper.KEY_WEEKE, singleItem.getPropertyWeekEnd());
                        fav.put(DatabaseHelper.KEY_ADDRESS, singleItem.getPropertyAddress());
                        fav.put(DatabaseHelper.KEY_AREA, singleItem.getPropertyArea());
                        fav.put(DatabaseHelper.KEY_PRICE, singleItem.getPropertyPrice());
                        fav.put(DatabaseHelper.KEY_PURPOSE, singleItem.getPropertyPurpose());
                        databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                        holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                        Toast.makeText(mContext, mContext.getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Constant.SAVE_ADS_FULL_ON_OFF.equals("true")) {
                        AD_COUNT++;
                        if (AD_COUNT == Integer.parseInt(Constant.SAVE_ADS_CLICK)) {
                            AD_COUNT = 0;
                            mInterstitial = new InterstitialAd(mContext);
                            mInterstitial.setAdUnitId(Constant.SAVE_ADS_FULL_ID);
                            AdRequest adRequest;
                            if (JsonUtils.personalization_ad) {
                                adRequest = new AdRequest.Builder()
                                        .build();
                            } else {
                                Bundle extras = new Bundle();
                                extras.putString("npa", "1");
                                adRequest = new AdRequest.Builder()
                                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                        .build();
                            }
                            mInterstitial.loadAd(adRequest);
                            mInterstitial.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // TODO Auto-generated method stub
                                    super.onAdLoaded();
                                    if (mInterstitial.isLoaded()) {
                                        mInterstitial.show();
                                    }
                                }

                                public void onAdClosed() {
                                    Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                                    intent.putExtra("Id", singleItem.getPId());
                                    mContext.startActivity(intent);

                                }

                                @Override
                                public void onAdFailedToLoad(int errorCode) {
                                    Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                                    intent.putExtra("Id", singleItem.getPId());
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                            intent.putExtra("Id", singleItem.getPId());
                            mContext.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                        intent.putExtra("Id", singleItem.getPId());
                        mContext.startActivity(intent);
                    }
                }
            });

            holder.txtPurpose.setText(singleItem.getPropertyPurpose());
            if (mContext.getResources().getString(R.string.isRTL).equals("true")) {
                holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_right_button : R.drawable.sale_right_button);
            }else {
                holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_left_button : R.drawable.sale_left_button);
            }
        }else {
            final SecondViewHolder holder = (SecondViewHolder) viewHolder;
            final ItemCowork singleItem = dataList.get(position);
            holder.text.setText(singleItem.getPropertyName());
            holder.textPrice.setText(mContext.getString(R.string.currency_symbol)+singleItem.getPropertyPrice());
            holder.textAddress.setText(singleItem.getPropertyAddress());
            holder.textBed.setText(singleItem.getPropertyStartTime()+" - "+ singleItem.getPropertyEndTime());
            holder.textBath.setText(singleItem.getPropertyWeekStart()+" - "+singleItem.getPropertyWeekEnd());
            //holder.textSquare.setText(singleItem.getPropertyArea());
            holder.ratingView.setVisibility(View.GONE);
             Picasso.get().load(singleItem.getPropertyThumbnailB()).placeholder(R.drawable.header_top_logo).into(holder.image);
            holder.textTotalRate.setVisibility(View.GONE);

            if (databaseHelper.getFavouriteById(singleItem.getPId())) {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            } else {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
            }

            holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues fav = new ContentValues();
                    if (databaseHelper.getFavouriteById(singleItem.getPId())) {
                        databaseHelper.removeFavouriteById(singleItem.getPId());
                        holder.ic_home_fav.setImageResource(R.drawable.ic_fav);
                        Toast.makeText(mContext, mContext.getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                    } else {
                        fav.put(DatabaseHelper.KEY_ID, singleItem.getPId());
                        fav.put(DatabaseHelper.KEY_TITLE, singleItem.getPropertyName());
                        fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getPropertyThumbnailB());
                        fav.put(DatabaseHelper.KEY_RATE, singleItem.getRateAvg());
                        fav.put(DatabaseHelper.KEY_STARTT, singleItem.getPropertyStartTime());
                        fav.put(DatabaseHelper.KEY_ENDT, singleItem.getPropertyEndTime());
                        fav.put(DatabaseHelper.KEY_WEEKS, singleItem.getPropertyWeekStart());
                        fav.put(DatabaseHelper.KEY_WEEKE, singleItem.getPropertyWeekEnd());
                        fav.put(DatabaseHelper.KEY_ADDRESS, singleItem.getPropertyAddress());
                        fav.put(DatabaseHelper.KEY_AREA, singleItem.getPropertyArea());
                        fav.put(DatabaseHelper.KEY_PRICE, singleItem.getPropertyPrice());
                        fav.put(DatabaseHelper.KEY_PURPOSE, singleItem.getPropertyPurpose());
                        databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                        holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                        Toast.makeText(mContext, mContext.getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Constant.SAVE_ADS_FULL_ON_OFF.equals("true")) {
                        AD_COUNT++;
                        if (AD_COUNT == Integer.parseInt(Constant.SAVE_ADS_CLICK)) {
                            AD_COUNT = 0;
                            mInterstitial = new InterstitialAd(mContext);
                            mInterstitial.setAdUnitId(Constant.SAVE_ADS_FULL_ID);
                            AdRequest adRequest;
                            if (JsonUtils.personalization_ad) {
                                adRequest = new AdRequest.Builder()
                                        .build();
                            } else {
                                Bundle extras = new Bundle();
                                extras.putString("npa", "1");
                                adRequest = new AdRequest.Builder()
                                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                        .build();
                            }
                            mInterstitial.loadAd(adRequest);
                            mInterstitial.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    // TODO Auto-generated method stub
                                    super.onAdLoaded();
                                    if (mInterstitial.isLoaded()) {
                                        mInterstitial.show();
                                    }
                                }

                                public void onAdClosed() {
                                    Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                                    intent.putExtra("Id", singleItem.getPId());
                                    mContext.startActivity(intent);

                                }

                                @Override
                                public void onAdFailedToLoad(int errorCode) {
                                    Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                                    intent.putExtra("Id", singleItem.getPId());
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                            intent.putExtra("Id", singleItem.getPId());
                            mContext.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                        intent.putExtra("Id", singleItem.getPId());
                        mContext.startActivity(intent);
                    }
                }
            });

            holder.txtPurpose.setText(singleItem.getPropertyPurpose());
            if (mContext.getResources().getString(R.string.isRTL).equals("true")) {
                holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_right_button : R.drawable.sale_right_button);
            }else {
                holder.txtPurpose.setBackgroundResource(singleItem.getPropertyPurpose().equals("Rent") ? R.drawable.rent_left_button : R.drawable.sale_left_button);
            }

        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).isRight() ? VIEW_ITEM : VIEW_PROG;
    }

    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,ic_home_fav;
        private TextView text, textPrice, textAddress, textBed, txtPurpose,textBath,textSquare,textTotalRate;
        private RelativeLayout lyt_parent;
        public RatingView ratingView;

        public SecondViewHolder(View v) {
            super(v);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textPrice = itemView.findViewById(R.id.textPrice);
            textAddress = itemView.findViewById(R.id.textAddress);
            textBed = itemView.findViewById(R.id.textBed);
            txtPurpose = itemView.findViewById(R.id.textPurpose);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ratingView = itemView.findViewById(R.id.ratingView);
            textBath=itemView.findViewById(R.id.textBath);
            textSquare=itemView.findViewById(R.id.textSquare);
            ic_home_fav=itemView.findViewById(R.id.ic_home_fav);
            textTotalRate=itemView.findViewById(R.id.textAvg);
        }
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image,ic_home_fav;
        private TextView text, textPrice, textAddress, textBed, txtPurpose,textBath,textSquare,textTotalRate;
        private RelativeLayout lyt_parent;
        public RatingView ratingView;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textPrice = itemView.findViewById(R.id.textPrice);
            textAddress = itemView.findViewById(R.id.textAddress);
            textBed = itemView.findViewById(R.id.textBed);
            txtPurpose = itemView.findViewById(R.id.textPurpose);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ratingView = itemView.findViewById(R.id.ratingView);
            textBath=itemView.findViewById(R.id.textBath);
            textSquare=itemView.findViewById(R.id.textSquare);
            ic_home_fav=itemView.findViewById(R.id.ic_home_fav);
            textTotalRate=itemView.findViewById(R.id.textAvg);
        }
    }

}
