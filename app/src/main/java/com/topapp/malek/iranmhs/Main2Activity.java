package com.topapp.malek.iranmhs;

import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

public class Main2Activity extends AppCompatActivity {
    private Boolean exit = false;
    TabLayout tabLayout;
    // ViewPager viewPager;
    Toolbar toolbar;
    RtlViewPager viewPager;
    QViewPager adapter;
    TextView titleTv;
    ImageView titleIv;
    AppBarLayout appBarLayout;
    ArrayList<questionnaire> data;

    DataBase db;
    public static int UserID = 1;
    boolean issending = false;
//    ExpandableLayout expendableLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

//        try {
//            db.updateDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        viewPager = (RtlViewPager) findViewById(R.id.viewpager);
        db = new DataBase(getApplicationContext());
//        expendableLayout = findViewById(R.id.expandable_layout);
        data = db.getquestinares("");
        // Create an adapter that knows which fragment should be shown on each page
        adapter = new QViewPager( getSupportFragmentManager(),data,UserID);


        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {






                if(state == 0){

//                    ((QFragment)viewPager
//                            .getAdapter()
//                            .instantiateItem(viewPager, viewPager.getCurrentItem())).qidm= true;

                    ((QFragment)viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.scrollToPosition(0);

                    ((QFragment)viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.getAdapter().notifyItemChanged(0);





                    //  viewPager.getAdapter().get
                }
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                questionnaire dd = data.get(viewPager.getCurrentItem());
                if(dd.Qdesc != null){

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titleTv.setText(Html.fromHtml(dd.Qdesc , Html.FROM_HTML_MODE_LEGACY));
                    }else{
                        titleTv.setText(HtmlCompat.fromHtml( dd.Qdesc, HtmlCompat.FROM_HTML_MODE_LEGACY));
                    }
                    titleTv.setVisibility(View.VISIBLE);
                }else{
                    titleTv.setVisibility(View.GONE);
                }
                if(dd.imgid > 0){
                    titleIv.setVisibility(View.VISIBLE);
                    switch (dd.imgid){
                        case 100:
                            titleIv.setBackgroundResource(R.drawable.a100);
                            break;
                    }
                }else{
                    titleIv.setVisibility(View.GONE);
                }

                if(dd.Qdesc != null || dd.imgid > 0){
                    toolbar.setVisibility(View.VISIBLE);
                }

                else {
                    toolbar.setVisibility(View.GONE);
                }
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.

            }
        });


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        //  tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //  tabLayout.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        init();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ersal){
            String vvv = db.getanswers().toString();

            HashMap<String,String> needdata =  new HashMap<>();
            HashMap<String,String> params =  new HashMap<>();
            HashMap<String,String> headers =  new HashMap<>();
            needdata.put("Url",CM.getStg(getApplicationContext(),"ServerIP") + "api/excel/savedata");
            needdata.put("type","POST");
            needdata.put("rtype","signin");

            params.put("UserName",vvv);


            issending = true;
            req re = new req();
            re.execute(needdata,params,headers);



            int i = 0;



        }else if (id == R.id.reset){
            db.deleteallanswer();
            viewPager.getAdapter().notifyDataSetChanged();
        }else if(id == R.id.reload){

            ((QFragment)viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.scrollToPosition(0);
            ((QFragment)viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.getAdapter().notifyItemChanged(0);





        }
     else if(id == R.id.khateme){


//    Intent ii = new Intent(getApplicationContext(), Khateme.class);
//    Khateme.userid =UserID;
//    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(ii);
//    finish();


            DataBase db = new DataBase(getApplicationContext());
            db.cpdb();
            //  viewPager.getAdapter().notifyDataSetChanged();

            ((QFragment)viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.scrollToPosition(0);
            ((QFragment)viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem())).recyclerView.getAdapter().notifyItemChanged(0);


        }else if(id == R.id.moshkel){


//    Intent ii = new Intent(getApplicationContext(), Khateme.class);
//    Khateme.userid =UserID;
//    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(ii);
//    finish();


            DataBase db = new DataBase(getApplicationContext());
            db.runquery("update tblQuestions set QuestionID = 1  where QID = 24 and QCode = 1");
            viewPager.getAdapter().notifyDataSetChanged();




        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//
//
//
//        if (exit) {
//            this.finishAffinity();
//        } else {
//            Toast.makeText(this, "برای خروج دکمه برکشت را بزنید",
//                    Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 3 * 1000);
//        }
//
//
//
//
//    }

    private void init(){
        titleIv = findViewById(R.id.imageView3);
        titleTv = findViewById(R.id.textView11);
        toolbar = findViewById(R.id.mainToolbar);
        questionnaire dd = data.get(0);
        if(dd.Qdesc != null){

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                titleTv.setText(Html.fromHtml(dd.Qdesc , Html.FROM_HTML_MODE_LEGACY));
            }else{
                titleTv.setText(HtmlCompat.fromHtml( dd.Qdesc, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
            titleTv.setVisibility(View.VISIBLE);
        }else{
            titleTv.setVisibility(View.GONE);
        }
        if(dd.imgid > 0){
            titleIv.setVisibility(View.VISIBLE);
            switch (dd.imgid){
                case 100:
                    titleIv.setBackgroundResource(R.drawable.a100);
                    break;
            }
        }else{
            titleIv.setVisibility(View.GONE);
        }

        if(dd.Qdesc != null || dd.imgid > 0){
            toolbar.setVisibility(View.VISIBLE);
        }

        else {
            toolbar.setVisibility(View.GONE);
        }



    }

    private void HandelReq(JSDLData data){
        if(data.isok) {
            switch (data.type) {
                case "signin":
                    if (data.rcode == 200) {
                        try {

                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();


                        } catch (Exception ex) {
                            //todo handel ex
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //todo handel bad request
                        Toast.makeText(getApplicationContext(), data.getBody("message"), Toast.LENGTH_LONG).show();
                    }

                    break;
            }
        }
        issending = false;
    }
    private class req extends AsyncTask<HashMap<String,String>,Void,JSDLData> {
        @Override
        protected JSDLData doInBackground(HashMap<String, String>... hashMaps) {
            JSDLData  data  = new JSDLData();
            try {
                data =   JSDL.multipartRequeststatic(hashMaps[0].get("Url"),hashMaps[1],hashMaps[2],hashMaps[0].get("type"));
                data.type =   hashMaps[0].get("rtype");
            }catch (Exception ex){
                data  = new JSDLData();
            }
            return data;
        }

        @Override
        protected void onPostExecute(JSDLData jsdlData) {
            if(jsdlData.rcode == 403){
                finish();
            }
            HandelReq(jsdlData);
            super.onPostExecute(jsdlData);
        }
    }



}
