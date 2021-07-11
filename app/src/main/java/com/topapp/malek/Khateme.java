package com.topapp.malek;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class Khateme extends AppCompatActivity {

    public static int userid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khateme);

        TextView tvtv = findViewById(R.id.textView16);
        String sts = "<p><span style=\"color: #0000ff;\">&nbsp;درصورتی که آزمودنی در حين مصاحبه سوالی را مطرح کرده باشد که به انتهای پرسشگری موکول کرده ايد، <u>به سوالات وی در حد امکان پاسخ دهيد.</u></span></p><p><span style=\"color: #0000ff;\">&nbsp;در پايان پرسشگری، از آزمودنی برای همکاری اش <u>تشکر</u> و قدردانی کنيد و <u>هديه</u> طرح را به او تحويل دهيد.</span></p><p><strong>&nbsp;در پايان، به او بگویید:</strong></p><p>احتمال دارد پس از ارزيابی پرسشنامه، نياز باشد برای تکميل اطلاعات يا کنترل اطلاعات، بصورت <u>تلفنی</u> يا مراجعه <u>حضوری</u> با شما تماس گرفته شود. ممکن است <u>خود من</u> يا <u>یا کسی که بر کار من نظارت دارد</u> اين کار را انجام دهند. می خواهيم از شما درخواست کنيم، درصورتی که موافقيد، برای اين کار به شما مراجعه کنيم.</p>";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvtv.setText(Html.fromHtml(sts, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvtv.setText(HtmlCompat.fromHtml(sts, HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        RadioGroup rg = findViewById(R.id.myrgg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                EditText tt = findViewById(R.id.editText2);

                int idx = group.indexOfChild(radioButton);
                if(idx == 1){
                tt.setVisibility(View.VISIBLE);
                }else{
                    tt.setVisibility(View.GONE);
                }
            }
        });


        RadioGroup rg2 = findViewById(R.id.myrgg2);
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                EditText tt = findViewById(R.id.editText22);

                int idx = group.indexOfChild(radioButton);
                if(idx == 1){
                    tt.setVisibility(View.VISIBLE);
                }else{
                    tt.setVisibility(View.GONE);
                }
            }
        });

        Button btn = findViewById(R.id.button9);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(getApplicationContext());
                finish();
//                RadioGroup rg = findViewById(R.id.myrgg);
//                int isel = rg.getCheckedRadioButtonId();
//
//                if(isel == -1){
//                    Toast.makeText(getApplicationContext(),"لطفا پاسخ اجازه را انتخاب کنید" , Toast.LENGTH_LONG).show();
//                    return;
//
//                }
//
//                View radioButton = rg.findViewById(isel);
//                int idx = rg.indexOfChild(radioButton);
//                db.setuseragrement(userid,idx);
//                if(idx == 0){
//                    Intent ii = new Intent(getApplicationContext(), Main2Activity.class);
//                    Main2Activity.UserID = userid;
//                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(ii);
//                }else{
//
//                }




            }
        });




    }

}
