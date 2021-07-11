package com.topapp.malek.iranmhs;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class MyTextWatcher implements TextWatcher {

    public View view;
    public questions data;
    public int posi;
    public questionadapter.ViewHolder holder;
    public MyTextWatcher(View view, questions data, int pos, questionadapter.ViewHolder h) {
        this.view = view;
        this.posi = pos;
        this.data = data;
        this.holder = h;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //access view
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //access view
    }
    @Override
    public void afterTextChanged(Editable s) {

    }
}