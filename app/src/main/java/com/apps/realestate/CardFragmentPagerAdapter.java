package com.apps.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.example.item.DailyPass;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private float baseElevation;
    private ArrayList<DailyPass> dailPass = new ArrayList<>();

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, ArrayList<DailyPass> dp) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        dailPass = dp;
        for(int i = 0; i< 5; i++){
            addCardFragment(new CardFragment());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Log.d("myTag", "sending : " + (position) + " : " + dailPass.get(position).getDuration() + " : " + dailPass.get(position).getPrice() );
        CardFragment fragment = (CardFragment) super.instantiateItem(container, position);
        Bundle bundle=new Bundle();
            bundle.putString("dur", dailPass.get(position).getDuration()); //key and value
        bundle.putString("price", dailPass.get(position).getPrice()); //key and value

//set Fragmentclass Arguments
        fragment.setArguments(bundle);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}
