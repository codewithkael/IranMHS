package com.topapp.malek.iranmhs;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class QViewPager extends FragmentStatePagerAdapter {

    ArrayList<questionnaire> ldata;
int UserID = 1;
    int mNumOfTabs;


    public QViewPager(FragmentManager fm, ArrayList<questionnaire> data,int userID) {
        super(fm);
        ldata = data;
        UserID = userID;
        this.mNumOfTabs = data.size();
    }

    @Override
    public Fragment getItem(int position) {
    questionnaire qdata = ldata.get(position);

        return  QFragment.newInstance(qdata,UserID);
    }



    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return ldata.get(position).QTitle + " - " + ldata.get(position).QCode;
      //  return super.getPageTitle(position);
    }

//    @NonNull
//    @Override
//    public Fragment createFragment(int position) {
//        questionnaire qdata = ldata.get(position);
//
//        return  QFragment.newInstance(qdata);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNumOfTabs;
//    }
}