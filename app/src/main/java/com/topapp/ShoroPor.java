package com.topapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.Main2Activity;
import com.topapp.malek.iranmhs.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class ShoroPor extends AppCompatActivity {

    public static int userid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoro_por);

        TextView tvtv = findViewById(R.id.textView16);
        String sts = "<p><span style=\"color: #0000ff;\">&nbsp;سعی کنید پرسشگری به صورت <u>خصوصی</u> و بدون حضور سایر اعضای خانواده انجام شود.</span></p><p><span style=\"color: #0000ff;\">هنگام پرسشگری <u>تلفن همراه</u> خود را خاموش کنید و از فرد هم بخواهید که همین کار را بکند تا در روند پرسشگری وقفه ایجاد نشود.</span></p><p><span style=\"color: #0000ff;\">در زمانی که می خواهید مصاحبه اصلی را آغاز کنید، از متن زير، توضيحاتی را که قبلاً نداده ايد، برای فرد مصاحبه شونده <u>بخوانيد</u>.</span></p><p><span style=\"color: #000000;\">ما برای انجام مطالعه پژوهش ملی سلامت روان به شما مراجعه کرده ايم. اين مطالعه توسط <u>وزارت بهداشت با همکاری دانشگاههای سطح کشور</u> انجام می شود. در کل کشور، تعدادی از منازل <u>بطور تصادفی</u> انتخاب شده اند.</span></p><p><span style=\"color: #000000;\">هدف از آن اين است که <u>شيوع ناراحتی</u><u>&shy;</u><u>ها و بيماري&shy;های اعصاب و روان</u> در کشور تعيين شود و ميزان <u>استفاده بيماران از خدمات</u> بهداشتی و درمانی مشخص گردد. از نتايج اين مطالعه برای بهبود خدمات استفاده خواهد شد، بنابراين بسيار مهم است که به سوالات <u>صادقانه</u> و هر آنچه هست پاسخ دهيد. شرکت در مصاحبه و پاسخ دادن به سوالات، <u>اختياری</u> است و کليه اطلاعات کسب شده <u>محرمانه</u> باقی می&shy;ماند. اطلاعات کسب شده، فقط بصورت کلی و بدون ذکر نام و هويت شما مورد تجزيه و تحليل و بررسی قرار می گيرد. ممکن است احساس کنيد سوالی مستقيماً به شما ارتباط ندارد؛ ولی در اين تحقيق، مجموعه ای از سوالات از همة افراد، بصورت يکسان پرسيده می شود.</span></p><p><span style=\"color: #000000;\">اين مصاحبه <u>يک تا دو ساعت</u> طول خواهد کشيد. در هر زمانی از مصاحبه، در صورت خستگی می&shy;توانيد درخواست کنيد تا مصاحبه بطور موقت قطع شود و پس از استراحت ادامه پيدا کند.</span></p>";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvtv.setText(Html.fromHtml(sts, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvtv.setText(HtmlCompat.fromHtml(sts, HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        Button btn = findViewById(R.id.button9);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(getApplicationContext());
                RadioGroup rg = findViewById(R.id.myrgg);
                int isel = rg.getCheckedRadioButtonId();

                if(isel == -1){
                    Toast.makeText(getApplicationContext(),"لطفا پاسخ اجازه را انتخاب کنید" , Toast.LENGTH_LONG).show();
                    return;

                }

                View radioButton = rg.findViewById(isel);
                int idx = rg.indexOfChild(radioButton);
                db.setuseragrement(userid,idx);
                if(idx == 0){
                    Intent ii = new Intent(getApplicationContext(), Main2Activity.class);
                    Main2Activity.UserID = userid;
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ii);
                }else{
                    finish();
                }




            }
        });






    }

}
