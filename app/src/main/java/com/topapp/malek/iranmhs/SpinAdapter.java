package com.topapp.malek.iranmhs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class SpinAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private spval[] values;

    public SpinAdapter(Context context, int textViewResourceId, spval[] values) {
        super(context, textViewResourceId);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.length;
    }

    public T getItem(int position) {



        return (T) values[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public int getPos(int id){
        for(int i = 0;i<values.length;i++){
            if(values[i].ID == id) return i;
        }
        return  0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.myfont);
        label.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        label.setTypeface(typeface,Typeface.BOLD);
        label.setTextSize(18);


        label.setText(values[position].text);
        if(values[position].text.equals("بله") || values[position].text.equals("بلی")){
            label.setTextColor(Color.parseColor("#00c853"));
        }
        if(values[position].text.equals("خیر")){
            label.setTextColor(Color.parseColor("#d50000"));
        }


        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.myfont);
        label.setTypeface(typeface);
        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label.setTextSize(25);
        label.setHeight(80);
        label.setText(values[position].text);

        return label;
    }
}