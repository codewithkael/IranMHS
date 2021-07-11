package com.topapp.malek.iranmhs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class CM {
    Context cn ;
    public static ArrayList<answer> ansdata;
    public static String number;
    public static String type;
    public static String ver = "1.0.0.0";
    public  CM(Context ct){
        this.cn = ct;
    }

    public static String getStg(Context cc, String Key){

      //  if(Key.equals("ServerIP")) return "http://188.212.22.221:8056/";

        String val = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cc);
        val = preferences.getString(Key,null);
        return val;
    }
    public static boolean getbStg(Context cc, String Key){
        boolean val = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cc);
        val = preferences.getBoolean(Key,false);
        return val;
    }
    public static void setstringstg(Context cc, String key, String val){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(cc).edit();
        editor.putString(key, val);
        editor.apply();
        editor.commit();
    }
    public static void setboolstg(Context cc, String key, boolean val){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(cc).edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

}
