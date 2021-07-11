package com.topapp.malek.iranmhs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import saman.zamani.persiandate.PersianDate;

public class questionadapter extends RecyclerView.Adapter<questionadapter.ViewHolder> {

    private int SpinnerTextSize = 12;
    private List<questions> mData;
    private DataBase db;
    private LayoutInflater mInflater;
    private QFragment qfrag;
    private int lastquestionid = 0;
    private int Userid = 1;
    public int cqid = 0;
    private  Boolean stopaction = false;
    private Context cnt;
    RecyclerView mRecyclerView;
    //  QFragment qFragment;
//  1.2
    // data is passed into the constructor
    questionadapter(Context context, List<questions> data, int qid,QFragment qf) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.qfrag = qf;
        cnt = context;
        db = new DataBase(cnt);
        Userid = qid;
    }

    @Override
    public int getItemViewType(int position) {
        questions data = mData.get(position);
        return data.QType;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 1:
            case 4:
            case 5:
                view = mInflater.inflate(R.layout.type1layout, parent, false);
                break;
            case 2:
                view = mInflater.inflate(R.layout.type2layout, parent, false);
                break;
            case 3:
                view = mInflater.inflate(R.layout.type3layout, parent, false);
                break;
            case 6:
                view = mInflater.inflate(R.layout.type4layout, parent, false);
                break;
            default:
                view = mInflater.inflate(R.layout.questionrowlayout, parent, false);
                break;
        }


        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        //  holder.setIsRecyclable(false);

//if(!this.qfrag.qidm){
//
//  //  holder.pva.setVisibility(View.GONE);
//    return;
//
//}

        questions data = mData.get(position);
        boolean isready = false;
        boolean isNR = false;
        Typeface typeface = ResourcesCompat.getFont(cnt, R.font.myfont);
        TextView titletv = holder.pva.findViewById(R.id.qtitle);
        TextView destv = holder.pva.findViewById(R.id.description);
        TextView errtvtv = holder.pva.findViewById(R.id.error);
        if(errtvtv!=null){
            errtvtv.setTextColor(0);
            errtvtv.setVisibility(View.GONE);
        }

        TextView untv = holder.pva.findViewById(R.id.unitlbl);
        View restrictview = holder.pva.findViewById(R.id.view);
        View restrictviewNR = holder.pva.findViewById(R.id.viewNR);
        restrictviewNR.setVisibility(View.GONE);
        titletv.setTypeface(typeface);
        if (untv != null) untv.setTypeface(typeface);
        Button dkbtn = holder.pva.findViewById(R.id.DKbtn);
        Button rfbtn = holder.pva.findViewById(R.id.RFbtn);
        dkbtn.setTypeface(typeface);
        rfbtn.setTypeface(typeface);
        dkbtn.setBackgroundResource(R.drawable.btngray);
        rfbtn.setBackgroundResource(R.drawable.btngray);
        answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
        // holder.pva.setBackground(ContextCompat.getDrawable(cnt,R.drawable.roundwhite));
        Drawable background =  holder.pva.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor("#FFFFFF"));
        }
        if(position == 0){
            ExpandableLayout el =  ((ExpandableLayout)((View)mRecyclerView.getParent().getParent().getParent()).findViewById(R.id.expandable_layout));
            //   el.setTag(1);
            if(!el.isExpanded()) el.expand();
        }

//        if(position == 11){
//            ExpandableLayout el =  ((ExpandableLayout)((View)mRecyclerView.getParent().getParent().getParent()).findViewById(R.id.expandable_layout));
//            //   el.setTag(1);
//            if(el.isExpanded()) el.collapse();
//        }


        Log.d("scoroll",data.QID+")))    onBindViewHolder("+position+") ===    scoroll => " + data.QnID);

        //mlch//       restrictviewNR.setTag(data.QnID);
        try {
            if ((data.QnID <= (lastquestionid + 1) && (data.QnID != 1 || db.isquestinareready(String.valueOf(data.QID - 1))))) {
                restrictview.setVisibility(View.GONE);
                isready = true;
            } else {
                restrictview.setVisibility(View.VISIBLE);
            }
            switch (data.QType) {
                case 1:
                    //region case1
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    }
                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }
                    LinearLayout pviw = holder.pva.findViewById(R.id.parentview);
                    pviw.removeAllViews();
                    LinearLayout parent = new LinearLayout(cnt);
                    parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    LinearLayout.LayoutParams pr1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pr1.setMargins(0, 0, 0, 10);
                    parent.setLayoutParams(pr1);
                    parent.setOrientation(LinearLayout.HORIZONTAL);
//mlch//            parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                    if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {
                        isNR = false;
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                            db.deleteanswer(String.valueOf(ans.answerid));
                            ans.answerid = -1;
                        }

                    } else {
                        isNR = true;
                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = "";
                            ans.AnswerMeta = "RN";
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = "";
                            ans.AnswerMeta = "RN";
                            db.updateanswer(ans);
                        }
                        restrictviewNR.setVisibility(View.VISIBLE);
                    }
                    try {
                        JSONObject mdata = new JSONObject(data.MetaData);
                        if(mdata.has("bcolor")){
                            if (background instanceof GradientDrawable) {
                                ((GradientDrawable)background).setColor(Color.parseColor(mdata.getString("bcolor")));
                            }
                            // .setBackgroundColor(Color.parseColor(mdata.getString("bcolor")));
                        }
                        JSONArray ansdata = new JSONArray();
                        if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                            if (ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                                dkbtn.setBackgroundResource(R.drawable.btngreen);

                                //mlch//  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }
                            if (ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                                rfbtn.setBackgroundResource(R.drawable.btngreen);

                                //mlch//   if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }

                            if (!ans.AnswerMeta.equals("RN")) {
                                JSONObject answermdata = new JSONObject(ans.AnswerMeta);
                                if (answermdata.has("answers")) {
                                    ansdata = answermdata.getJSONArray("answers");
                                }
                            }
                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                        }


                        JSONArray mlist = mdata.getJSONArray("options");

                        if (mdata.has("hasDK")) {
                            dkbtn.setVisibility(View.VISIBLE);
                            dkbtn.setOnClickListener(new MyOnClickListener());
                            if (mdata.has("hastxt")) {
                                dkbtn.setText(mdata.getString("hastxt"));
                            } else {
                                dkbtn.setText("نمیداند");
                            }

                        } else {
                            dkbtn.setVisibility(View.GONE);
                        }
                        if (mdata.has("hasRF")) {
                            rfbtn.setVisibility(View.VISIBLE);
                            rfbtn.setOnClickListener(new MyOnClickListener2());

                        } else {
                            rfbtn.setVisibility(View.GONE);
                        }

                        int spliter = 0;
                        for (int i = 0; i < mlist.length(); i++) {
                            final JSONObject st = mlist.getJSONObject(i);
                            TextView tv1 = new TextView(cnt);
                            tv1.setText(st.getString("t") + " : ");
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 10, 0);
                            tv1.setTextColor(Color.parseColor("#000000"));
                            tv1.setLayoutParams(params);
                            tv1.setTag("inplab-" + String.valueOf(st.getInt("c")));
                            tv1.setTypeface(typeface);
                            parent.addView(tv1);

                            if(st.has("iscombo")){



                                Spinner tv2 = new Spinner(cnt);
                                tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));


                                tv2.setTag("inp-" + String.valueOf(st.getInt("c")));


                                JSONArray dd =  st.getJSONArray("cdata");
                                ArrayList<String> spinnerArray = new ArrayList<String>();
                                spinnerArray.add( "_"  );


                                for (int i1 = 0; i1 < dd.length(); i1++) {
                                    if(st.getString("t").equals("PRB") || st.getString("t").equals("PRN")){
                                        spinnerArray.add( (dd.getString(i1) )   );
                                    }else{
                                        spinnerArray.add( replaceparams2(dd.getString(i1) )   );
                                    }


                                }



                                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(cnt, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                                // final SpinAdapter<spval> dataAdapter = new SpinAdapter<spval>(cnt, android.R.layout.simple_spinner_item, vals);

                                tv2.setAdapter(spinnerArrayAdapter);
                                if (ansdata.length() > 0) {
                                    JSONObject ansst = ansdata.getJSONObject(i);

                                    if(st.getString("t").equals("PRB") || st.getString("t").equals("PRN")){
                                        tv2.setSelection(  spinnerArray.indexOf( (ansst.getString("val"))));
                                    }else{
                                        tv2.setSelection(  spinnerArray.indexOf( replaceparams2(ansst.getString("val"))));
                                    }


                                }

                                tv2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        LinearLayout pviw3 = (LinearLayout) view.getParent().getParent();
                                        int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent().getParent());
                                        questions data = mData.get(pos);
                                        DoValidate(null, data, (View) pviw3.getParent(), pos, true, false,true);
                                        try{
                                            if(st.has("showpop") && st.getJSONObject("showpop").getString("V").equals(parent.getItemAtPosition(position).toString()) ){
//                                                AlertDialog alertDialog = new AlertDialog.Builder(cnt.getApplicationContext()).create();
//
//                                                alertDialog.setTitle("خطا");
//                                                alertDialog.setMessage();
//                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "فهمیدم",
//                                                        new DialogInterface.OnClickListener() {
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                dialog.dismiss();
//                                                            }
//                                                        });
//                                                alertDialog.show();
                                                Toast.makeText(cnt, "خطا  :   " +  st.getJSONObject("showpop").getString("t"),Toast.LENGTH_LONG).show();
                                            }
                                            if(st.has("releaseon") || st.getString("t").equals("PRB")  ){
                                                if(st.getString("t").equals("PRB") && !st.has("releaseon")){
                                                    JSONObject mdata0 = new JSONObject(data.MetaData);
                                                    JSONArray mlist = mdata0.getJSONArray("options");

                                                    for (int ie = 1; ie < mlist.length(); ie++) {
                                                        final JSONObject ste = mlist.getJSONObject(ie);
                                                        EditText et0 =  ((View) pviw3.getParent()).findViewWithTag("inp-" + String.valueOf(ste.getInt("c")));
                                                        et0.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                            @Override
                                                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                                                View nextView = v.focusSearch(View.FOCUS_DOWN);
                                                                if (nextView != null) {
                                                                    nextView.requestFocus(View.FOCUS_DOWN);
                                                                }
                                                                return true;
                                                            }
                                                        });
                                                        TextView etm =  ((View) pviw3.getParent()).findViewWithTag("inplab-" + String.valueOf(ste.getInt("c")));
                                                        if(parent.getItemAtPosition(position).toString().split("-")[0].equals( "3") || parent.getItemAtPosition(position).toString().split("-")[0].equals( "4" )|| parent.getItemAtPosition(position).toString().split("-")[0].equals( "5") ){
                                                            et0.setVisibility(View.VISIBLE);
                                                            etm.setVisibility(View.VISIBLE);
                                                        }else{
                                                            //  et0.setText(" ");
                                                            et0.setVisibility(View.GONE);
                                                            etm.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                                else{
                                                    EditText et =  ((View)pviw3.getParent()).findViewWithTag("inp-" + String.valueOf(st.getJSONObject("releaseon").getInt("c")));
                                                    TextView etm =  ((View)pviw3.getParent()).findViewWithTag("inplab-" + String.valueOf(st.getJSONObject("releaseon").getInt("c")));
                                                    if(st.getJSONObject("releaseon").getString("val").equals(parent.getItemAtPosition(position).toString().split("-")[0])){
                                                        //  et.setText("");
                                                        et.setVisibility(View.VISIBLE);
                                                        etm.setVisibility(View.VISIBLE);
                                                    }else{
                                                        //   et.setText(" ");
                                                        et.setVisibility(View.GONE);
                                                        etm.setVisibility(View.GONE);
                                                    }

                                                }


                                            }
                                        }catch (Exception ex){

                                            int i = 0;
                                        }
                                        DoValidate(null, data, (View) pviw3.getParent(), pos, true, false,true);



                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });







                                parent.addView(tv2);




                            }else{
                                EditText tv2 = new EditText(cnt);
                                tv2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        View nextView = v.focusSearch(View.FOCUS_DOWN);
                                        if (nextView != null) {
                                            nextView.requestFocus(View.FOCUS_DOWN);
                                        }
                                        return true;
                                    }
                                });
                                tv2.setImeOptions(EditorInfo.IME_ACTION_NONE);
                                tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                                tv2.setTextColor(Color.parseColor("#5e6362"));
                                tv2.setTypeface(typeface);
                                tv2.setTag("inp-" + String.valueOf(st.getInt("c")));
                                if (st.has("isnumber") ||st.has("issen")  ) {
                                    tv2.setInputType(InputType.TYPE_CLASS_NUMBER);
                                }
                                if(st.has("issen")){
                                    int min = 1;
                                    int max = Integer.parseInt( getparam("@@c1"));

                                    if(st.getInt("issen") == 2){
                                        answer anse = db.getanswer(String.valueOf(Userid),String.valueOf( data.QID) , String.valueOf (data.QnID-1));
                                        if (anse.answerid == -1) min =1;
                                        else{
                                            try {

                                                min = Integer.parseInt(anse.AnswerData.split(" ")[anse.AnswerData.split(" ").length-1]);
                                            } catch (Exception ex) {
                                                // res = "";
                                            }
                                        }

                                        //  min = 0;
                                    }

                                    CustomRangeInputFilter rangeFilter = new CustomRangeInputFilter(min, max);
                                    tv2.setFilters(new InputFilter[]{rangeFilter});
                                    tv2.setOnFocusChangeListener(rangeFilter);





                                }



                                if (ansdata.length() > 0) {
                                    JSONObject ansst = ansdata.getJSONObject(i);
                                    tv2.setText(ansst.getString("val"));
                                }

                                if(st.getString("t").trim().equals("پزشک") || st.getString("t").trim().equals("غیره") ){
                                    tv2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View v, boolean hasFocus) {
                                            if(hasFocus) return;
                                            LinearLayout pviw3 = (LinearLayout) v.getParent().getParent();
                                            int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                            questions data = mData.get(pos);
                                            DoValidate(null, data, (View) pviw3.getParent(), pos, true, false,true);
                                        }
                                    });
                                }
                                else{
                                    tv2.addTextChangedListener(new MyTextWatcher(pviw, data, position, holder) {
                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                        }
                                    });
                                }





                                parent.addView(tv2);
                            }


                            spliter++;
                            if (spliter == 2 || st.getString("t").equals("PRN")  || st.getString("t").equals("PRB") || st.has("iscombo") ) {
                                pviw.addView(parent);
                                parent = new LinearLayout(cnt);
                                parent.setLayoutParams(pr1);
                                parent.setOrientation(LinearLayout.HORIZONTAL);
                                parent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                spliter = 0;
                            }
                        }
                        if (spliter > 0) {
                            pviw.addView(parent);
                        }

                        final JSONObject st = mlist.getJSONObject(0);
                        if(st.has("iscombo")){
                            Spinner et = pviw.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));

                            if( et.getSelectedItem() == null && (   st.has("releaseon") ||st.getString("t").equals("PRB")) ){
                                if(st.getString("t").equals("PRB") && !st.has("releaseon")){
                                    for (int ie = 1; ie < mlist.length(); ie++) {
                                        final JSONObject ste = mlist.getJSONObject(ie);
                                        EditText et0 =  pviw.findViewWithTag("inp-" + String.valueOf(ste.getInt("c")));

                                        TextView etm =  pviw.findViewWithTag("inplab-" + String.valueOf(ste.getInt("c")));
                                        if(et.getSelectedItem().toString().split("-")[0].equals( "3") || et.getSelectedItem().toString().split("-")[0] .equals("4") || et.getSelectedItem().toString().split("-")[0].equals("5") ){
                                            et0.setVisibility(View.VISIBLE);
                                            etm.setVisibility(View.VISIBLE);
                                        }else{
                                            et0.setText("");
                                            et0.setVisibility(View.GONE);
                                            etm.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                else{
                                    EditText et0 =  pviw.findViewWithTag("inp-" + String.valueOf(st.getJSONObject("releaseon").getInt("c")));

                                    TextView etm =  pviw.findViewWithTag("inplab-" + String.valueOf(st.getJSONObject("releaseon").getInt("c")));
                                    if(st.getJSONObject("releaseon").getString("val").equals(et.getSelectedItem().toString().split("-")[0])){
                                        //  et0.setText("");
                                        et0.setVisibility(View.VISIBLE);
                                        etm.setVisibility(View.VISIBLE);
                                    }else{
                                        et0.setText("");
                                        et0.setVisibility(View.GONE);
                                        etm.setVisibility(View.GONE);
                                    }

                                }

                            }
                        }


                        if (ansdata.length() > 0 || isNR) {

                            DoValidate(holder, data, null, position, false, isNR,false); //mlch//

                        }

                    } catch (Exception ex) {
                        int i = 0;
                    }
                    //endregion
                    break;
                case 2:
                    //region case2

                    LinearLayout pviw2 = holder.pva.findViewById(R.id.parentview);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    }
                    EditText dt = holder.pva.findViewById(R.id.numbericf);
                    dt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                            View nextView = v.focusSearch(View.FOCUS_DOWN);
                            if (nextView != null) {
                                nextView.requestFocus(View.FOCUS_DOWN);
                            }
                            return true;
                        }
                    });
                    if (holder.tw != null) dt.removeTextChangedListener(holder.tw);
                    dt.setTypeface(typeface);
                    if (null != data.MetaData && !data.MetaData.equals("")) {
                        try {
                            JSONObject mdata = new JSONObject(data.MetaData);
                            if(mdata.has("bcolor")){
                                if (background instanceof GradientDrawable) {
                                    ((GradientDrawable)background).setColor(Color.parseColor(mdata.getString("bcolor")));
                                }
                            }
                            if (mdata.has("hasDK")) {
                                dkbtn.setVisibility(View.VISIBLE);
                                dkbtn.setOnClickListener(new MyOnClickListener());
                                if (mdata.has("hastxt")) {
                                    dkbtn.setText(mdata.getString("hastxt"));
                                } else {
                                    dkbtn.setText("نمیداند");
                                }
                            } else {
                                dkbtn.setVisibility(View.GONE);
                            }
                            if (mdata.has("hasRF")) {
                                rfbtn.setVisibility(View.VISIBLE);
                                rfbtn.setOnClickListener(new MyOnClickListener2());

                            } else {
                                rfbtn.setVisibility(View.GONE);
                            }
                            if (mdata.has("hint")) {
                                dt.setHint(mdata.getString("hint"));
                            } else {
                                dt.setHint("");
                            }
                            if (mdata.has("unit")) {

                                untv.setVisibility(View.VISIBLE);
                                untv.setText(mdata.getString("unit"));
                            } else {
                                untv.setVisibility(View.GONE);
                            }
                            if (mdata.has("maxlength")) {
                                dt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mdata.getInt("maxlength"))});
                            } else {

                                int min = -999999999;
                                int max = 999999999;

                                if (mdata.has("max")) {
                                    if (mdata.getString("max").startsWith("@@")) {
                                        if (!getparam(mdata.getString("max")).equals(""))
                                            max = Integer.parseInt(getparam(mdata.getString("max")));
                                    } else
                                        max = mdata.getInt("max");
                                }
                                if (mdata.has("min")) {
                                    if (mdata.getString("min").startsWith("@@"))
                                        min = Integer.parseInt(getparam(mdata.getString("min")));
                                    else
                                        min = mdata.getInt("min");
                                }
                                final CustomRangeInputFilter rangeFilter = new CustomRangeInputFilter(min, max);
                                dt.setFilters(new InputFilter[]{rangeFilter});
                                dt.setOnFocusChangeListener(rangeFilter);
                            }

                            if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {
                                isNR = false;
                                if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                                    db.deleteanswer(String.valueOf(ans.answerid));
                                    ans.answerid = -1;
                                }

                            } else {
                                isNR = true;
                                if (ans.answerid == -1) {
                                    ans.QID = data.QID;
                                    ans.QuestionID = data.QnID;
                                    ans.UserID = Userid;
                                    ans.AnswerData = "";
                                    ans.AnswerMeta = "RN";
                                    db.insertanswer(ans);
                                } else {
                                    ans.AnswerData = "";
                                    ans.AnswerMeta = "RN";
                                    db.updateanswer(ans);
                                }
                                restrictviewNR.setVisibility(View.VISIBLE);
                            }

                            if (mdata.has("defval") && isready) {
                                dt.setText(getparam(mdata.getString("defval")));
                                if (!dt.getText().toString().equals("")) {
                                    lastquestionid = data.QnID;

                                    DoValidate(holder, data, pviw2, position, true, false,false);
                                }

                            } else if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                                if (ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                                    dkbtn.setBackgroundResource(R.drawable.btngreen);
                                }
                                if (ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                                    rfbtn.setBackgroundResource(R.drawable.btngreen);
                                }
                                dt.setText(ans.AnswerData);


                                if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                                DoValidate(holder, data, null, position, false, isNR,false);

                            } else {
                                dt.setText("");
                            }

                            holder.tw = new MyTextWatcher(pviw2, data, position, holder) {
                                @Override
                                public void afterTextChanged(Editable s) {
                                    DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                }
                            };
                            dt.addTextChangedListener(holder.tw);
                            dt.setEnabled(!mdata.has("readonly"));
                        } catch (Exception ex) {
                            int i = 0;
                            int ii = 0;
                        }
                    }
                    dt.setTextColor(Color.parseColor("#5e6362"));
                    LinearLayout.LayoutParams pr12 = (LinearLayout.LayoutParams) dt.getLayoutParams();
                    pr12.setMargins(0, 0, 0, 10);
                    dt.setLayoutParams(pr12);
                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }
                    //endregion
                    break;
                case 3:
                    //region case3
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    }
                    RadioGroup rg = holder.pva.findViewById(R.id.radiogrop);
                    rg.setOnCheckedChangeListener(null);
                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }

                    if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {

                        isNR = false;
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                            db.deleteanswer(String.valueOf(ans.answerid));
                            ans.answerid = -1;
                        }

                    } else {
                        isNR = true;
                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = "";
                            ans.AnswerMeta = "RN";
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = "";
                            ans.AnswerMeta = "RN";
                            db.updateanswer(ans);
                        }
                        restrictviewNR.setVisibility(View.VISIBLE);
                    }


                    try {
                        final JSONObject mdata = new JSONObject(data.MetaData);
                        if(mdata.has("bcolor")){
                            if (background instanceof GradientDrawable) {
                                ((GradientDrawable)background).setColor(Color.parseColor(mdata.getString("bcolor")));
                            }
                        }
                        JSONArray ops = mdata.getJSONArray("options");
                        if (mdata.has("hasDK")) {
                            dkbtn.setVisibility(View.VISIBLE);
                            dkbtn.setOnClickListener(new MyOnClickListener());
                            if (mdata.has("hastxt")) {
                                dkbtn.setText(mdata.getString("hastxt"));
                            } else {
                                dkbtn.setText("نمیداند");
                            }
                        } else {
                            dkbtn.setVisibility(View.GONE);
                        }
                        if (mdata.has("hasRF")) {
                            rfbtn.setVisibility(View.VISIBLE);
                            rfbtn.setOnClickListener(new MyOnClickListener2());

                        } else {
                            rfbtn.setVisibility(View.GONE);
                        }
                        if (mdata.has("orientation")) {
                            rg.setOrientation(mdata.getString("orientation").equals("horizontal") ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
                        }
                        rg.removeAllViews();


                        for (int i = 0; i < ops.length(); i++) {
                            JSONObject el = ops.getJSONObject(i);
                            RadioButton rb = new RadioButton(cnt);
                            if (el.has("con") && !getV(el.getString("con")).equals("1")) {
                                break;
                            }
                            rb.setText(el.getString("t"));
                            rb.setTypeface(typeface);
                            rb.setId(el.getInt("v"));
                            rb.setTextColor(Color.parseColor("#5e6362"));
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(15, 15, 15, 15);
                            rb.setLayoutParams(params);
                            if (ans.answerid != -1 && isready && String.valueOf(el.getInt("v")).equals(ans.AnswerData)) {
                                rb.setChecked(true);
                            }
                            rg.addView(rb);
                        }
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                LinearLayout pviw3 = (LinearLayout) group.getParent();
                                int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                questions data = mData.get(pos);
                                DoValidate(null, data, pviw3, pos, true, false,true);
                            }
                        });
                        if (mdata.has("secq")){
                            errtvtv.setVisibility(View.VISIBLE);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                errtvtv.setText(Html.fromHtml(mdata.getString("secq"), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                errtvtv.setText(HtmlCompat.fromHtml(mdata.getString("secq"), HtmlCompat.FROM_HTML_MODE_LEGACY));

                            }
                        }
                        if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                            if (ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                                dkbtn.setBackgroundResource(R.drawable.btngreen);

                                //  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }
                            if (ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                                rfbtn.setBackgroundResource(R.drawable.btngreen);

                                //  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }


                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            DoValidate(holder, data, null, position, false, isNR,false);

                        }


                    } catch (Exception ex) {
                        int i = 0;
                    }


                    //endregion
                    break;
                case 4:
                    //region case4
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    }
                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }
                    LinearLayout pviw4 = holder.pva.findViewById(R.id.parentview);
                    pviw4.removeAllViews();
                    LinearLayout parent4 = new LinearLayout(cnt);
                    LinearLayout.LayoutParams pr14 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pr14.setMargins(0, 0, 0, 10);
                    parent4.setLayoutParams(pr14);
                    parent4.setOrientation(LinearLayout.HORIZONTAL);
                    parent4.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    try {


                        JSONObject mdata = new JSONObject(data.MetaData);
                        if(mdata.has("bcolor")){
                            if (background instanceof GradientDrawable) {
                                ((GradientDrawable)background).setColor(Color.parseColor(mdata.getString("bcolor")));
                            }
                        }
                        JSONArray ops = mdata.getJSONArray("options");
                        JSONArray ansdata = new JSONArray();


                        if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {
                            isNR = false;
                            if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                                db.deleteanswer(String.valueOf(ans.answerid));
                                ans.answerid = -1;
                            }

                        } else {
                            isNR = true;
                            if (ans.answerid == -1) {
                                ans.QID = data.QID;
                                ans.QuestionID = data.QnID;
                                ans.UserID = Userid;
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.insertanswer(ans);
                            } else {
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.updateanswer(ans);
                            }
                            restrictviewNR.setVisibility(View.VISIBLE);
                        }


//                    if ((ans.answerid != -1  ||  isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
//                        if (ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
//                            dkbtn.setBackgroundResource(R.drawable.btngreen);
//
//                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
//                        }
//                        if (ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
//                            rfbtn.setBackgroundResource(R.drawable.btngreen);
//
//                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
//                        }
//
//                        JSONObject answermdata = new JSONObject(ans.AnswerMeta);
//                        if (answermdata.has("answers")) {
//                            ansdata = answermdata.getJSONArray("answers");
//
//                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
//
//                        }
//                    }
                        if (mdata.has("hasDK")) {
                            dkbtn.setVisibility(View.VISIBLE);
                            dkbtn.setOnClickListener(new MyOnClickListener());
                            if (mdata.has("hastxt")) {
                                dkbtn.setText(mdata.getString("hastxt"));
                            } else {
                                dkbtn.setText("نمیداند");
                            }
                        } else {
                            dkbtn.setVisibility(View.GONE);
                        }
                        if (mdata.has("hasRF")) {
                            rfbtn.setVisibility(View.VISIBLE);
                            rfbtn.setOnClickListener(new MyOnClickListener2());

                        } else {
                            rfbtn.setVisibility(View.GONE);
                        }
                        int spliter4 = 0;
                        if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                            if (ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                                dkbtn.setBackgroundResource(R.drawable.btngreen);


                            }
                            if (ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                                rfbtn.setBackgroundResource(R.drawable.btngreen);
                                //if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }


                            if (!ans.AnswerMeta.equals("RN")) {
                                JSONObject answermdata = new JSONObject(ans.AnswerMeta);

                                if (answermdata.has("answers")) {
                                    ansdata = answermdata.getJSONArray("answers");
                                }
                            }
                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                        }
                        for (int i = 0; i < ops.length(); i++) {
                            JSONObject st = ops.getJSONObject(i);
                            TextView tv1 = new TextView(cnt);
                            tv1.setText(st.getString("t") + " : ");
                            tv1.setTypeface(typeface);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 0, 10, 0);
                            tv1.setTextColor(Color.parseColor("#5e6362"));
                            tv1.setLayoutParams(params);

                            EditText tv2 = new EditText(cnt);
                            tv2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                    View nextView = v.focusSearch(View.FOCUS_DOWN);
                                    if (nextView != null) {
                                        nextView.requestFocus(View.FOCUS_DOWN);
                                    }
                                    return true;
                                }
                            });
                            tv2.setImeOptions(EditorInfo.IME_ACTION_NONE);
                            tv2.setTag("inp-" + String.valueOf(st.getInt("v")));
                            tv2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            tv2.setTextColor(Color.parseColor("#5e6362"));
                            tv2.setTypeface(typeface);
                            int min = -999999999;
                            int max = 999999999;

                            if (st.has("max")) {
                                max = st.getInt("max");
                            }
                            if (st.has("min")) {
                                min = st.getInt("min");
                            }
                            if (st.has("hint")) {
                                tv2.setHint(st.getString("hint"));
                            }

                            CustomRangeInputFilter rangeFilter = new CustomRangeInputFilter(min, max);
                            tv2.setFilters(new InputFilter[]{rangeFilter});
                            tv2.setOnFocusChangeListener(rangeFilter);
                            tv2.setInputType(InputType.TYPE_CLASS_PHONE);
                            tv2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            if (ansdata.length() > 0) {
                                JSONObject ansst = ansdata.getJSONObject(i);
                                tv2.setText(ansst.getString("val"));

                            } else if (st.has("defval")) {
                                tv2.setText(st.getString("defval"));
                            }
                            tv2.addTextChangedListener(new MyTextWatcher(pviw4, data, position, holder) {
                                @Override
                                public void afterTextChanged(Editable s) {
                                    DoValidate(holder, this.data, this.view, this.posi, true, false,true);
                                }
                            });

                            parent4.addView(tv1);
                            parent4.addView(tv2);
                            spliter4++;

                            if (spliter4 == 2) {
                                pviw4.addView(parent4);
                                parent4 = new LinearLayout(cnt);
                                parent4.setLayoutParams(pr14);
                                parent4.setOrientation(LinearLayout.HORIZONTAL);
                                parent4.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                spliter4 = 0;
                            }
                        }
                        if (spliter4 > 0) {
                            pviw4.addView(parent4);
                        }
                        if (ansdata.length() > 0 || isNR) {

                            DoValidate(holder, data, null, position, false, false,false);

                        }

                    } catch (Exception ex0) {
                        int i = 0;
                    }

                    //endregion case4
                    break;
                case 5:
                    //region case5
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    }
                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }
                    LinearLayout pviw0 = holder.pva.findViewById(R.id.parentview);
                    pviw0.removeAllViews();
                    LinearLayout parent0 = new LinearLayout(cnt);
                    parent0.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    LinearLayout.LayoutParams pr10 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    pr10.setMargins(0, 0, 0, 10);
                    parent0.setLayoutParams(pr10);
                    parent0.setOrientation(LinearLayout.HORIZONTAL);
                    parent0.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


                    try {
                        JSONObject mdata5 = new JSONObject(data.MetaData);
                        if(mdata5.has("bcolor")){
                            holder.pva.setBackgroundColor(Color.parseColor(mdata5.getString("bcolor")));
                        }
                        JSONArray ops5 = mdata5.getJSONArray("options");
                        JSONArray ansdata = new JSONArray();


                        if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {
                            isNR = false;
                            if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                                db.deleteanswer(String.valueOf(ans.answerid));
                                ans.answerid = -1;
                            }

                        } else {
                            isNR = true;
                            if (ans.answerid == -1) {
                                ans.QID = data.QID;
                                ans.QuestionID = data.QnID;
                                ans.UserID = Userid;
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.insertanswer(ans);
                            } else {
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.updateanswer(ans);
                            }
                            restrictviewNR.setVisibility(View.VISIBLE);
                        }


                        if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                            if (ans.AnswerMeta.equals("DK") && mdata5.has("hasDK")) {
                                dkbtn.setBackgroundResource(R.drawable.btngreen);

                                //  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }
                            if (ans.AnswerMeta.equals("RF") && mdata5.has("hasRF")) {
                                rfbtn.setBackgroundResource(R.drawable.btngreen);

                                // if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }

                            if (!ans.AnswerMeta.equals("RN")) {
                                JSONObject answermdata = new JSONObject(ans.AnswerMeta);
                                if (answermdata.has("answers")) {
                                    ansdata = answermdata.getJSONArray("answers");


                                }
                            }
                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                        }
                        if (mdata5.has("hasDK")) {
                            dkbtn.setVisibility(View.VISIBLE);
                            dkbtn.setOnClickListener(new MyOnClickListener());
                            if (mdata5.has("hastxt")) {
                                dkbtn.setText(mdata5.getString("hastxt"));
                            } else {
                                dkbtn.setText("نمیداند");
                            }
                        } else {
                            dkbtn.setVisibility(View.GONE);
                        }
                        if (mdata5.has("hasRF")) {
                            rfbtn.setVisibility(View.VISIBLE);
                            rfbtn.setOnClickListener(new MyOnClickListener2());

                        } else {
                            rfbtn.setVisibility(View.GONE);
                        }
                        boolean flag = mdata5.has("forced") ? false: true;
                        boolean flag2 =  true;
                        for (int i = 0; i < ops5.length(); i++) {
                            JSONObject st = ops5.getJSONObject(i);
                            Boolean falsecon = false;
                            Boolean isinputready = false;
                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {

                                falsecon = true;
                                //tv.setVisibility(View.GONE);
                            }
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0.8f
                            );
                            String inputval = "";





                            if (st.has("hasinput") && false) {
                                EditText tv = new EditText(cnt);
                                tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        View nextView = v.focusSearch(View.FOCUS_DOWN);
                                        if (nextView != null) {
                                            nextView.requestFocus(View.FOCUS_DOWN);
                                        }
                                        return true;
                                    }
                                });
                                if (falsecon) {

                                    tv.setVisibility(View.GONE);

                                }
                                tv.setTypeface(typeface);

                                params.setMargins(0, 10, 10, 10);
                                tv.setLayoutParams(params);
                                tv.setTextColor(Color.parseColor("#000000"));

                                tv.setTag("inpt-" + String.valueOf(st.getInt("c")));
                                tv.setHint(st.getString("t"));
                                tv.setText(inputval);

                                tv.addTextChangedListener(new MyTextWatcher(pviw0, data, position, holder) {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                    }
                                });


                                parent0.addView(tv);
                            }else{
                                TextView tv = new TextView(cnt);
                                if (falsecon) {

                                    tv.setVisibility(View.GONE);

                                }

                                tv.setTypeface(typeface);
                                if(st.has("fontsize")){
                                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,st.getInt("fontsize"));
                                }
                                else if(mdata5.has("fontsize")){
                                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mdata5.getInt("fontsize"));
                                }

                                //
                                params.setMargins(0, 10, 10, 10);
                                tv.setLayoutParams(params);
                                tv.setTextColor(Color.parseColor("#000000"));
                                parent0.addView(tv);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    tv.setText(Html.fromHtml( replaceparams( st.getString("t")), Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv.setText(HtmlCompat.fromHtml(replaceparams(st.getString("t")), HtmlCompat.FROM_HTML_MODE_LEGACY));
                                }
                            }














                            if(mdata5.has("forceswitch")){
                                Switch sw = new Switch(cnt);
                                if (falsecon) {
                                    sw.setVisibility(View.GONE);
                                    // falsecon = true;
                                    //tv.setVisibility(View.GONE);
                                }
                                if (mdata5.has("maxsel")) {

                                    sw.setTag(R.id.t2, "2");
                                }else
                                if (st.has("oof")) {

                                    sw.setTag(R.id.t2, "1");
                                }  else {
                                    sw.setTag(R.id.t2, "-1");
                                }
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    sw.setShowText(true);
                                    sw.setTextOn("بله");
                                    sw.setTextOff("خیر");
                                }
                                sw.setTag("inp-" + String.valueOf(st.getInt("c")));
                                //tv.setTypeface(typeface);

                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        0.2f
                                );
                                //   params.setMargins(0, 0, 10, 0);
                                // tv.setLayoutParams(params);
                                //  tv.setTextColor(Color.parseColor("#5e6362"));
                                params2.setMargins(0, 10, 40, 10);
                                // params2.width = 120;
//                            sw.setScaleX((float) 1.5);
//                            sw.setScaleY((float) 1.5);
                                sw.setLayoutParams(params2);
                                //    sw.setSwitchMinWidth(70);
                                if (ansdata.length() > 0) {
                                    for (int i1 = 0; i1 < ansdata.length(); i1++) {
                                        JSONObject objt = ansdata.getJSONObject(i1);
                                        if ( String.valueOf(  objt.getString("c")).equals( String.valueOf ( st.getInt("c")))) {
                                            if (sw.getVisibility() != View.GONE) {
                                                sw.setChecked(objt.get("val").equals("5") ? true : false);
                                                if(objt.get("val").equals("5"))
                                                    flag = true;
                                            } else {
                                                sw.setChecked(false);
                                            }

                                            if(st.has("hasinput")){
                                                inputval = objt.getString(  "valt");
                                            }
                                        }

                                    }


                                }
                                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (!buttonView.isPressed()) {
                                            return;
                                        }
                                        LinearLayout pviw3 = (LinearLayout) buttonView.getParent().getParent();
                                        int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                        questions data = mData.get(pos);
                                        if (((String) buttonView.getTag(R.id.t2)).equals("2") && isChecked) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                int count =mdata5.getInt("maxsel");
                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Switch et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if (et.isChecked()) {
                                                        count--;
                                                        if(count <0) et.setChecked(false);
                                                    }
                                                }
                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }
                                        if (((String) buttonView.getTag(R.id.t2)).equals("1") && isChecked) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Switch et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if (!et.getTag().toString().equals(buttonView.getTag().toString())) {
                                                        et.setChecked(false);
                                                    }
                                                }
                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }   else if (isChecked) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Switch et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));



                                                    if (et.getTag(R.id.t2).toString().equals("1")) {
                                                        et.setChecked(false);
                                                    }
                                                }
                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }
                                        DoValidate(null, data, pviw3, pos, true, false,true);
                                        try {
                                            JSONObject mdata5 = new JSONObject(data.MetaData);
                                            JSONArray ops5 = mdata5.getJSONArray("options");
                                            for (int i = 0; i < ops5.length(); i++) {
                                                JSONObject st = ops5.getJSONObject(i);
                                                Switch et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                if (st.has("con") && getV(st.getString("con")).equals("1")) {
                                                    et.setVisibility(View.VISIBLE);
                                                    ViewGroup  container = (ViewGroup)et.getParent();
                                                    View nextView = container.getChildAt(container.indexOfChild(et)-1);
                                                    nextView.setVisibility(View.VISIBLE);

                                                }else if(st.has("con") && !getV(st.getString("con")).equals("1")){
                                                    et.setVisibility(View.GONE);
                                                    ViewGroup  container = (ViewGroup)et.getParent();
                                                    View nextView = container.getChildAt(container.indexOfChild(et)-1);
                                                    nextView.setVisibility(View.GONE);
                                                }
                                            }
                                        } catch (Exception ex) {
                                            int iii = 0;
                                        }
                                        DoValidate(null, data, pviw3, pos, true, false,true);
                                    }
                                });
                                parent0.addView(sw);
                            }else{
                                Spinner spn = new Spinner(cnt);

                                if (falsecon) {
                                    spn.setVisibility(View.GONE);

                                    //tv.setVisibility(View.GONE);
                                }
                                if (mdata5.has("maxsel")) {
                                    spn.setTag(R.id.t2, "2");
                                }else
                                if (st.has("oof")) {

                                    spn.setTag(R.id.t2, "1");
                                }  else {
                                    spn.setTag(R.id.t2, "-1");
                                }


                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        0.2f
                                );
                                params2.setMargins(0, 10, 40, 10);
                                spn.setTag("inp-" + String.valueOf(st.getInt("c")));
                                spval[] vals = new spval[3];
                                vals[0] = new spval();
                                vals[0].ID = 0;
                                vals[0].text = "-";
                                vals[1] = new spval();
                                vals[1].ID = 1;
                                vals[1].text = "خیر";
                                vals[2] = new spval();
                                vals[2].ID = 5;
                                vals[2].text = "بله";
                                params2.gravity = Gravity.CENTER_VERTICAL;
                                spn.setMinimumWidth(200);
                                // spn.se(200);
                                spn.setLayoutParams(params2);
                                final SpinAdapter<spval> dataAdapter = new SpinAdapter<spval>(cnt, R.layout.support_simple_spinner_dropdown_item, vals);
                                spn.setAdapter(dataAdapter);
                                spn.setDropDownWidth(300);
                                if (ansdata.length() > 0) {
                                    for (int i1 = 0; i1 < ansdata.length(); i1++) {
                                        JSONObject objt = ansdata.getJSONObject(i1);
                                        if ( String.valueOf(  objt.getString("c")).equals( String.valueOf ( st.getInt("c")))) {
                                            if (spn.getVisibility() != View.GONE) {
                                                if(objt.get("val").equals("5")){
                                                    flag = true;
                                                    spn.setSelection(2,false);
                                                    isinputready = true;



                                                }else if(objt.get("val").equals("1")){
                                                    spn.setSelection(1,false);
                                                    isinputready = false;
                                                }
                                                else{
                                                    if(mdata5.has("gforced")){
                                                        flag2 = false;
                                                    }
                                                }








                                            } else {
                                                spn.setSelection(0,false);
                                                isinputready = false;
                                            }

                                            if(st.has("hasinput")){
                                                inputval = objt.getString(  "valt");

                                            }
                                        }

                                    }
                                }
                                spn.setTag(R.id.t3,0);

                                spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        if(((int) ((Spinner)view.getParent()).getTag(R.id.t3)) == 1){
                                            ((Spinner)view.getParent()).setTag(R.id.t3,0) ;
                                            return;
                                        }
                                        LinearLayout pviw3 = (LinearLayout) view.getParent().getParent().getParent();
                                        int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                        questions data = mData.get(pos);


                                        if (((String) ((Spinner)view.getParent()).getTag(R.id.t2)).equals("1") && position == 2) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Spinner et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if (!et.getTag().toString().equals(((Spinner)view.getParent()).getTag().toString())) {
                                                        et.setTag(R.id.t3,1);
                                                        et.setSelection(0);
                                                        et.setTag(R.id.t3,0);
                                                        ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
                                                    }else{
                                                        ((View)et.getParent()).setBackgroundColor(0x00000000);
                                                    }
                                                }
                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }
                                        else if (((spval)((Spinner)view.getParent()).getSelectedItem()).ID == 5) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Spinner et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if (et.getTag(R.id.t2).toString().equals("1")) {
                                                        et.setTag(R.id.t3,1);
                                                        et.setSelection(0);
                                                        et.setTag(R.id.t3,0);
                                                        ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
                                                    }else{
                                                        ((View)et.getParent()).setBackgroundColor(0x00000000);
                                                    }
                                                }
                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }
                                        if (((String) ((Spinner)view.getParent()).getTag(R.id.t2)).equals("2") //&& position == 2
                                        ) {
                                            try {
                                                JSONObject mdata5 = new JSONObject(data.MetaData);
                                                JSONArray ops5 = mdata5.getJSONArray("options");
                                                int count =mdata5.getInt("maxsel");

                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Spinner et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if (((spval)et.getSelectedItem()).ID == 5) {
                                                        count--;
                                                    }
                                                    if(count <0  && ((spval)et.getSelectedItem()).ID == 5   ){
                                                        et.setTag(R.id.t3,1);
                                                        et.setSelection(0);
                                                        et.setTag(R.id.t3,0);
                                                    }
                                                }

                                                for (int i = 0; i < ops5.length(); i++) {
                                                    JSONObject st = ops5.getJSONObject(i);
                                                    Spinner et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                    if(count == 0   && ((spval)et.getSelectedItem()).ID != 5 ){
                                                        ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
                                                        et.setEnabled(false);
                                                    }else{
                                                        ((View)et.getParent()).setBackgroundColor(0x00000000);
                                                        et.setEnabled(true);
                                                    }
                                                }



                                            } catch (Exception ex) {

                                                int iii = 0;
                                            }
                                        }
                                        DoValidate(null, data, pviw3, pos, true, false,true);
                                        try {
                                            JSONObject mdata5 = new JSONObject(data.MetaData);
                                            JSONArray ops5 = mdata5.getJSONArray("options");
                                            for (int i = 0; i < ops5.length(); i++) {
                                                JSONObject st = ops5.getJSONObject(i);
                                                Spinner et = pviw3.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                                                if (st.has("con") && getV(st.getString("con")).equals("1")) {
                                                    et.setVisibility(View.VISIBLE);
                                                    ViewGroup  container = (ViewGroup)et.getParent();
                                                    View nextView = container.getChildAt(container.indexOfChild(et)-1);
                                                    nextView.setVisibility(View.VISIBLE);

                                                }else if(st.has("con") && !getV(st.getString("con")).equals("1")){
                                                    et.setVisibility(View.GONE);
                                                    //stopaction = true;
                                                    //  et.setSelection(0,false);
                                                    ViewGroup  container = (ViewGroup)et.getParent();
                                                    View nextView = container.getChildAt(container.indexOfChild(et)-1);
                                                    nextView.setVisibility(View.GONE);
                                                }
                                                else if(et.getSelectedItemPosition() == 2 && st.has("hasinput")){
                                                    EditText tv = pviw3.findViewWithTag("inpt-" + String.valueOf(st.getInt("c")));
                                                    tv.setEnabled(true);
                                                }
                                                else if(st.has("hasinput")){
                                                    EditText tv = pviw3.findViewWithTag("inpt-" + String.valueOf(st.getInt("c")));
                                                    tv.setText("");
                                                    tv.setEnabled(false);
                                                }
                                            }
                                        } catch (Exception ex) {
                                            int iii = 0;
                                        }
                                        DoValidate(null, data, pviw3, pos, true, false,true);

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                parent0.addView(spn);
                            }













                            //   tv.setText(getparam(st.getString("t"));

                            pviw0.addView(parent0);
                            parent0 = new LinearLayout(cnt);
                            parent0.setLayoutParams(pr10);
                            parent0.setOrientation(LinearLayout.HORIZONTAL);
                            parent0.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);






                            if (st.has("hasinput")) {


                                TextView tv = new TextView(cnt);
                                if (falsecon) {

                                    tv.setVisibility(View.GONE);

                                }

                                tv.setTypeface(typeface);
                                if(st.has("fontsize")){
                                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,st.getInt("fontsize"));
                                }
                                else if(mdata5.has("fontsize")){
                                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mdata5.getInt("fontsize"));
                                }

                                //
                                params.setMargins(0, 10, 10, 10);
                                tv.setLayoutParams(params);
                                tv.setTextColor(Color.parseColor("#000000"));
                                parent0.addView(tv);
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    tv.setText(Html.fromHtml( replaceparams( "بنویسید : "), Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv.setText(HtmlCompat.fromHtml(replaceparams("بنویسید : "), HtmlCompat.FROM_HTML_MODE_LEGACY));
                                }



                                EditText tvm = new EditText(cnt);
                                tvm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        View nextView = v.focusSearch(View.FOCUS_DOWN);
                                        if (nextView != null) {
                                            nextView.requestFocus(View.FOCUS_DOWN);
                                        }
                                        return true;
                                    }
                                });
                                if (falsecon) {

                                    tvm.setVisibility(View.GONE);

                                }
                                tvm.setTypeface(typeface);

                                params.setMargins(0, 10, 10, 10);
                                tvm.setLayoutParams(params);
                                tvm.setTextColor(Color.parseColor("#000000"));

                                tvm.setTag("inpt-" + String.valueOf(st.getInt("c")));
                                //   tvm.setHint(st.getString("t"));
                                //  tvm.setText(inputval);
                                if( isinputready ){
                                    tvm.setText(inputval);
                                    // tvm.setEnabled(true);
                                }else{
                                    tvm.setText("");
                                    //  tvm.setEnabled(false);
                                }
                                tvm.addTextChangedListener(new MyTextWatcher(pviw0, data, position, holder) {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                    }
                                });


                                parent0.addView(tvm);


                                pviw0.addView(parent0);
                                parent0 = new LinearLayout(cnt);
                                parent0.setLayoutParams(pr10);
                                parent0.setOrientation(LinearLayout.HORIZONTAL);
                                parent0.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);




                            }








                        }


//                        if (ansdata.length() > 0) {
//                            for (int i1 = 0; i1 < ansdata.length(); i1++) {
//                                JSONObject objt = ansdata.getJSONObject(i1);
//
//                                Spinner spn =pviw0.findViewWithTag("inp-" + objt.getString("c"));
//
//                                if (((String) spn.getTag(R.id.t2)).equals("2") &&((spval)(spn).getSelectedItem()).ID == 5 ) {
//                                    try {
//                                        //   JSONObject mdata5 = new JSONObject(data.MetaData);
//                                        //  JSONArray ops5 = mdata5.getJSONArray("options");
//                                        int count =mdata5.getInt("maxsel");
//                                        for (int i0 = 0; i0 < ops5.length(); i0++) {
//                                            JSONObject st0 = ops5.getJSONObject(i0);
//                                            Spinner et = pviw0.findViewWithTag("inp-" + String.valueOf(st0.getInt("c")));
//                                            if (((spval)et.getSelectedItem()).ID == 5) {
//                                                count--;
//                                            }
//
//                                            if(count <0){
//                                                et.setTag(R.id.t3,1);
//                                                et.setSelection(0);
//                                                et.setEnabled(false);
//                                                et.setTag(R.id.t3,0);
//                                                ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
//                                            }else{
//                                                ((View)et.getParent()).setBackgroundColor(0x00000000);
//                                            }
//                                        }
//                                    } catch (Exception ex) {
//
//                                        int iii = 0;
//                                    }
//                                }
//                                if (((String) spn.getTag(R.id.t2)).equals("1") &&((spval)(spn).getSelectedItem()).ID == 5 ) {
//                                    try {
//
//                                        for (int i0 = 0; i0 < ops5.length(); i0++) {
//                                            JSONObject st0 = ops5.getJSONObject(i0);
//                                            Spinner et = pviw0.findViewWithTag("inp-" + String.valueOf(st0.getInt("c")));
//                                            if (!et.getTag().toString().equals(spn.getTag().toString())) {
//                                                et.setTag(R.id.t3,1);
//                                                et.setSelection(0);
//                                                et.setTag(R.id.t3,0);
//                                                ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
//                                            }else{
//                                                ((View)et.getParent()).setBackgroundColor(0x00000000);
//                                            }
//                                        }
//                                    } catch (Exception ex) {
//
//                                        int iii = 0;
//                                    }
//                                }
//
//                                if (((spval)(spn).getSelectedItem()).ID == 5) {
//                                    try {
//
//                                        for (int i0 = 0; i0 < ops5.length(); i0++) {
//                                            JSONObject st0 = ops5.getJSONObject(i0);
//                                            Spinner et = pviw0.findViewWithTag("inp-" + String.valueOf(st0.getInt("c")));
//                                            if (et.getTag(R.id.t2).toString().equals("1")) {
//                                                et.setTag(R.id.t3,1);
//                                                et.setSelection(0);
//                                                et.setTag(R.id.t3,0);
//                                                ((View)et.getParent()).setBackgroundColor(Color.parseColor("#A0A0A0"));
//                                            }else{
//                                                ((View)et.getParent()).setBackgroundColor(0x00000000);
//                                            }
//                                        }
//                                    } catch (Exception ex) {
//
//                                        int iii = 0;
//                                    }
//                                }
//
//
//                            }
//                        }



                        if ((flag && flag2) || isNR) {
                            DoValidate(holder, data, null, position, true, isNR,false);
                        } else if (ans.answerid != -1 && isready) {

                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                        }


                    } catch (Exception ex5) {
                        int ii = 0;
                    }


                    //endregion case5
                    break;
                case 6:
                    //region case6
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        titletv.setText(Html.fromHtml((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) + replaceparams( data.QTitle), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        titletv.setText(HtmlCompat.fromHtml(((data.Qorder.trim().equals("") ? "" :  (data.Qorder +  ". ")) +replaceparams( data.QTitle)), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    }

                    if (data.QDesctiption != null && !data.QDesctiption.equals("")) {
                        destv.setVisibility(View.VISIBLE);
                        destv.setText(data.QDesctiption.replaceAll("\\\\n", "\\\n"));
                    } else {
                        destv.setVisibility(View.GONE);
                    }
                    TableLayout tbl = holder.pva.findViewById(R.id.parentview);

                    JSONArray rowsval = new JSONArray();
                    JSONObject rowtxt = new JSONObject();


                    // String ss = "";

                    try {
                        JSONObject mdata5 = new JSONObject(data.MetaData);
                        if(mdata5.has("bcolor")){
                            holder.pva.setBackgroundColor(Color.parseColor(mdata5.getString("bcolor")));
                        }
                        JSONArray ops5 = mdata5.getJSONArray("rows");
                        JSONArray ops5c = mdata5.getJSONArray("cols");
                        JSONObject qdata = mdata5.getJSONObject("qdata");
                        JSONObject ansdata = new JSONObject();

                        if (data.Prerequisites == null || data.Prerequisites.equals("") || !isready || getV(data.Prerequisites).equals("1")) {
                            isNR = false;
                            if (ans.answerid != -1 && ans.AnswerMeta.equals("RN") && isready) {
                                db.deleteanswer(String.valueOf(ans.answerid));
                                ans.answerid = -1;
                            }

                        } else {
                            isNR = true;
                            if (ans.answerid == -1) {
                                ans.QID = data.QID;
                                ans.QuestionID = data.QnID;
                                ans.UserID = Userid;
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.insertanswer(ans);
                            } else {
                                ans.AnswerData = "";
                                ans.AnswerMeta = "RN";
                                db.updateanswer(ans);
                            }
                            restrictviewNR.setVisibility(View.VISIBLE);
                        }


                        if ((ans.answerid != -1 || isNR) && isready && (!ans.AnswerMeta.equals("RN") || ((ans.AnswerMeta.equals("RN") && isNR)))) {
                            if (ans.AnswerMeta.equals("DK") && mdata5.has("hasDK")) {
                                dkbtn.setBackgroundResource(R.drawable.btngreen);

                                //  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }
                            if (ans.AnswerMeta.equals("RF") && mdata5.has("hasRF")) {
                                rfbtn.setBackgroundResource(R.drawable.btngreen);

                                //  if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                            }

                            if (!ans.AnswerMeta.equals("RN")) {
                                JSONObject answermdata = new JSONObject(ans.AnswerMeta);
                                if (answermdata.has("answers")) {
                                    ansdata = answermdata.getJSONObject("answers");
                                    if (ansdata.has("rowtxt")) rowtxt = ansdata.getJSONObject("rowtxt");
                                    if (ansdata.has("rowsval")) rowsval = ansdata.getJSONArray("rowsval");
                                }
                            }
                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;

//                        JSONObject answermdata = new JSONObject(ans.AnswerMeta);
//                        if (answermdata.has("answers")) {
//                            ansdata = answermdata.getJSONObject("answers");
//                            if(ansdata.has("rowtxt")) rowtxt = ansdata.getJSONObject("rowtxt");
//                            if(ansdata.has("rowsval")) rowsval = ansdata.getJSONArray("rowsval");
////                            if(ansdata.length() > 0)
////                            if (lasqid <= data.QID) lasqid = data.QID;
////                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
//                        }
                        }
                        if (mdata5.has("hasDK")) {
                            dkbtn.setVisibility(View.VISIBLE);
                            dkbtn.setOnClickListener(new MyOnClickListener());
                            if (mdata5.has("hastxt")) {
                                dkbtn.setText(mdata5.getString("hastxt"));
                            } else {
                                dkbtn.setText("نمیداند");
                            }
                        } else {
                            dkbtn.setVisibility(View.GONE);
                        }
                        if (mdata5.has("hasRF")) {
                            rfbtn.setVisibility(View.VISIBLE);
                            rfbtn.setOnClickListener(new MyOnClickListener2());

                        } else {
                            rfbtn.setVisibility(View.GONE);
                        }

                        if (mdata5.has("secq")){
                            errtvtv.setVisibility(View.VISIBLE);
                            errtvtv.setTextColor(Color.parseColor("#000000"));
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                errtvtv.setText(Html.fromHtml(mdata5.getString("secq"), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                errtvtv.setText(HtmlCompat.fromHtml(mdata5.getString("secq"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                            }
                        }
                        TableLayout.LayoutParams tableRowParams=
                                new TableLayout.LayoutParams
                                        (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                        tbl.removeAllViews();
                        TableRow header = new TableRow(cnt);
                        TextView tv = new TextView(cnt);
                        TableRow.LayoutParams param = new TableRow.LayoutParams(
                                0,
                                TableRow.LayoutParams.WRAP_CONTENT,
                                mdata5.has("qw") ? Float.parseFloat(mdata5.getString("qw")) : 2f
                        );
                        tv.setLayoutParams(param);
                        if (mdata5.has("qtext")) {
                            tv.setText(mdata5.getString("qtext"));
                        } else {
                            tv.setText("سوالات");
                        }

                        tv.setTextColor(Color.parseColor("#000000"));
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv.setTypeface(typeface);
                        header.addView(tv);
                        for (int i = 0; i < ops5c.length(); i++) {
                            JSONObject st = ops5c.getJSONObject(i);
                            TextView col = new TextView(cnt);
                            col.setTextColor(Color.parseColor("#000000"));
                            TableRow.LayoutParams paramm = new TableRow.LayoutParams(
                                    0,
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    st.has("w") ? Float.parseFloat(st.getString("w")) : 1f
                            );
                            col.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            col.setLayoutParams(paramm);
                            col.setTypeface(typeface);
                            col.setTag("col-" + st.getString("v"));
                            if(st.has("ishtml")){
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    col.setText(Html.fromHtml( st.getString("t"), Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    col.setText(HtmlCompat.fromHtml( st.getString("t"), HtmlCompat.FROM_HTML_MODE_LEGACY));
                                }
                            }else{
                                col.setText( replaceparams(  st.getString("t")));
                            }
                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                col.setVisibility(View.GONE);
                            }
                            header.addView(col);
                        }
                        tbl.addView(header);
                        boolean flag = mdata5.has("forced") ? false : true;
                        boolean flag2 = true;
                        final boolean relatedstep =  mdata5.has("relatedstep");
                        for (int i = 0; i < ops5.length(); i++) {
                            JSONObject st = ops5.getJSONObject(i);
                            TableRow row = new TableRow(cnt);


                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                row.setVisibility(View.GONE);
                            }

                            tableRowParams.setMargins(10, 15, 10, 15);

                            row.setLayoutParams(tableRowParams);


                            if (mdata5.has("bottomborder")) {
                                row.setBackgroundResource(R.drawable.borderbn);
                            }
                            if (st.has("hasinput")) {
                                EditText tv0 = new EditText(cnt);
                                tv0.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                        View nextView = v.focusSearch(View.FOCUS_DOWN);
                                        if (nextView != null) {
                                            nextView.requestFocus(View.FOCUS_DOWN);
                                        }
                                        return true;
                                    }
                                });
                                tv0.setTextColor(Color.parseColor("#000000"));
                                tv0.setTag("inp-" + st.getString("v"));
                                TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                                        0,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        mdata5.has("qw") ? Float.parseFloat(mdata5.getString("qw")) : 2f
                                );
                                tv0.setLayoutParams(param2);
                                tv0.setHint(st.getString("t"));
                                tv0.setTypeface(typeface);
                                tv0.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                tv0.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);

                                if (rowtxt.has(st.getString("v")))
                                    tv0.setText(rowtxt.getString(st.getString("v")));

                                tv0.addTextChangedListener(new MyTextWatcher(tbl, data, position, holder) {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                    }
                                });
                                row.addView(tv0);
                            } else {
                                TextView tv0 = new TextView(cnt);
                                TableRow.LayoutParams param2 = new TableRow.LayoutParams(
                                        0,
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        mdata5.has("qw") ? Float.parseFloat(mdata5.getString("qw")) : 2f
                                );
                                param2.setMargins(0,0,0,20);

                                tv0.setLayoutParams(param2);
                                if(st.has("fontsize")){
                                    tv0.setTextSize(TypedValue.COMPLEX_UNIT_DIP,st.getInt("fontsize"));
                                }
                                else if(mdata5.has("fontsize")){
                                    tv0.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mdata5.getInt("fontsize"));
                                }

                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    tv0.setText(Html.fromHtml(replaceparams( st.getString("t")), Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv0.setText(HtmlCompat.fromHtml(replaceparams( st.getString("t")), HtmlCompat.FROM_HTML_MODE_LEGACY));
                                }

                                tv0.setTypeface(typeface);
                                tv0.setTextColor(Color.parseColor("#000000"));
                                tv0.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                tv0.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                                row.addView(tv0);
                            }


                            for (int i2 = 0; i2 < ops5c.length(); i2++) {
                                JSONObject st2 = ops5c.getJSONObject(i2);
                                if (st2.has("con") && !getV(st2.getString("con")).equals("1")) continue;

                                String inputtype = "";

                                if (st2.has("qdata")) {
                                    inputtype = st2.getJSONObject("qdata").getString("type");
                                } else if (st.has("qdata")) {
                                    inputtype = st.getJSONObject("qdata").getString("type");
                                } else {
                                    inputtype = qdata.getString("qtype");
                                }

                                switch (inputtype) {
                                    case "switch":


                                        if(mdata5.has("forceswitch")){
                                            Switch sw = new Switch(cnt);
                                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                sw.setShowText(true);
                                                sw.setTextOn("بله");
                                                sw.setTextOff("خیر");
                                            }
                                            TableRow.LayoutParams paramm = new TableRow.LayoutParams(
                                                    0,
                                                    TableRow.LayoutParams.WRAP_CONTENT,
                                                    st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                            );
                                            sw.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                            sw.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                            paramm.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            paramm.gravity = Gravity.CENTER_VERTICAL;
                                            sw.setLayoutParams(paramm);

                                            if (ans.AnswerData != (null) && !ans.AnswerData.equals("") && ans.AnswerData.contains(st.getString("v") + "-" + st2.getString("v")))
                                                sw.setChecked(true);

                                            final int size = ops5c.length();
                                            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (!buttonView.isPressed()) {
                                                        return;
                                                    }
                                                    TableRow pviw3 = (TableRow) buttonView.getParent();
                                                    int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent().getParent());
                                                    questions data = mData.get(pos);
                                                    int po = Integer.parseInt(buttonView.getTag(R.id.t2).toString().split("-")[3]) - 1;
                                                    if (isChecked) {
                                                        if (relatedstep) {
                                                            for (int i2 = po; i2 >= 1; i2--) {
                                                                ((Switch) pviw3.findViewWithTag(buttonView.getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2)))).setChecked(isChecked);
                                                            }
                                                        } else {
                                                            ((Switch) pviw3.findViewWithTag(buttonView.getTag().toString().replace("-" + String.valueOf(po + 1), "-1"))).setChecked(isChecked);
                                                        }
                                                    } else {
                                                        if (relatedstep || po == 0) {

                                                            for (int i2 = (size); i2 > po; i2--) {
                                                                View vvs =  pviw3.findViewWithTag(buttonView.getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2)));
                                                                if(vvs instanceof  Switch )
                                                                    ((Switch) pviw3.findViewWithTag(buttonView.getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2)))).setChecked(isChecked);
                                                            }
                                                        }
                                                    }
                                                    DoValidate(null, data, (View) pviw3.getParent(), pos, true, false,true);
                                                    try {
                                                        JSONObject mdata5 = new JSONObject(data.MetaData);
                                                        // JSONArray cols = mdata5.getJSONArray("cols");
                                                        JSONArray rows = mdata5.getJSONArray("rows");
                                                        TableLayout tbl = (TableLayout) pviw3.getParent();// .findViewById(R.id.parentview);
                                                        for (int i = 0; i < rows.length(); i++) {
                                                            JSONObject st = rows.getJSONObject(i);
                                                            TableRow tr = (TableRow) tbl.getChildAt(i+1);

                                                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                                tr.setVisibility(View.GONE);
                                                            }
                                                            else{
                                                                tr.setVisibility(View.VISIBLE);
                                                            }

//                                                        for (int j = 0; j < cols.length(); j++) {
//                                                            JSONObject st2 = cols.getJSONObject(j);
//                                                            String inputtype = "";
//
//                                                            if(st2.has("qdata")){
//                                                                inputtype =   st2.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else if (st.has("qdata")){
//                                                                inputtype =   st.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else{
//                                                                inputtype =   mdata5.getString("qtype");
//                                                            }
//
//                                                            switch (inputtype){
//                                                                case "switch":
//                                                                    Switch et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
//
//
//
//                                                                    break;
//                                                                case "number":
//                                                                case "text":
//
//                                                                    break;
//                                                                case "combo":
//
//                                                                    break;
//                                                                case "multicheck":
//
//
//
//                                                                    break;
//                                                            }
//                                                        }
                                                        }
                                                    } catch (Exception ex) {

                                                        int iii = 0;
                                                    }

                                                }
                                            });
                                            row.addView(sw);
                                        }else{
                                            Spinner sw = new Spinner(cnt);

                                            TableRow.LayoutParams paramm = new TableRow.LayoutParams(
                                                    0,
                                                    TableRow.LayoutParams.MATCH_PARENT,
                                                    st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                            );
                                            sw.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                            sw.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                            //   paramm.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            //   sw.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                            //  sw.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                                            //   paramm.gravity = Gravity.CENTER;
                                            //  paramm.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            //   sw.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                                            //   sw.setTextDirection(View.TEXT_DIRECTION_LTR);

                                            paramm.gravity = Gravity.CENTER;
                                            //  paramm.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                            sw.setLayoutParams(paramm);
                                            spval[] vals = new spval[3];
                                            vals[0] = new spval();
                                            vals[0].ID = 0;
                                            vals[0].text = "-";
                                            vals[1] = new spval();
                                            vals[1].ID = 1;
                                            vals[1].text = "خیر";
                                            vals[2] = new spval();
                                            vals[2].ID = 5;
                                            vals[2].text = "بله";
                                            sw.setMinimumWidth(200);
                                            final SpinAdapter<spval> dataAdapter = new SpinAdapter<spval>(cnt, R.layout.support_simple_spinner_dropdown_item, vals);

                                            sw.setAdapter(dataAdapter);
                                            sw.setDropDownWidth(300);
                                            sw.setSelection(0,false);

//                                            if (ans.AnswerData != (null) && !ans.AnswerData.equals("") && ans.AnswerData.contains(st.getString("v") + "-" + st2.getString("v")))
//                                                sw.setSelection(2);

                                            if (rowsval.length() > 0){
                                                for (int i1 = 0; i1 < rowsval.length(); i1++) {
                                                    JSONObject objt = rowsval.getJSONObject(i1);
                                                    if ( String.valueOf(  objt.getString("c")).equals( st.getString("v") + "-" + st2.getString("v") )) {
                                                        if (sw.getVisibility() != View.GONE) {
                                                            if(objt.get("val").equals("5")){
                                                                sw.setSelection(2,false);
                                                                flag = true;
                                                            }else if(objt.get("val").equals("1")){
                                                                sw.setSelection(1,false);
                                                                flag = true;
                                                            }
//                                                            if(objt.get("val").equals("5"))
//                                                                flag = true;
                                                        } else {
                                                            sw.setSelection(0,false);
                                                        }


                                                    }

                                                }
                                            }




                                            final int size = ops5c.length();
                                            sw.setTag(R.id.t3,0);
                                            sw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                    if(((int) ((Spinner)view.getParent()).getTag(R.id.t3)) == 1){
                                                        ((Spinner)view.getParent()).setTag(R.id.t3,0) ;
                                                        return;
                                                    }

//                                                    int size = 0;
//
//                                                    for (int iu = 1; iu < pviw3.getChildCount(); iu++) {
//
//                                                        final View child = pviw3.getChildAt(iu);
//
//                                                        if (child != null && child.getVisibility() == View.VISIBLE) {
//                                                            size++;
//                                                        }
//                                                    }

                                                    LinearLayout pviw3 = (LinearLayout) view.getParent().getParent().getParent();
                                                    int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                                    questions data = mData.get(pos);
                                                    int po = Integer.parseInt(((Spinner)view.getParent()).getTag(R.id.t2).toString().split("-")[3]) - 1;
                                                    if (position == 2) {
                                                        if (relatedstep) {
                                                            for (int i2 = po; i2 >= 1; i2--) {
                                                                Spinner spn=   ((Spinner ) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace(((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + String.valueOf(po + 1), ((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + (i2))));
                                                                spn.setTag(R.id.t3,1);
                                                                spn.setSelection(2);
                                                                spn.setTag(R.id.t3,0);
                                                            }
                                                        } else {
                                                            Spinner spn=   ((Spinner) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace(((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + String.valueOf(po + 1), ((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-1")));
                                                            spn.setTag(R.id.t3,1);
                                                            spn.setSelection(2);
                                                            spn.setTag(R.id.t3,0);
                                                        }
                                                    } else {
                                                        if (relatedstep || po == 0) {

                                                            for (int i2 = (size); i2 > po; i2--) {


                                                                View vvs =  pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace(((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + String.valueOf(po + 1), ((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + (i2)));
                                                                if(vvs instanceof  Spinner  ){
                                                                    Spinner spn=((Spinner) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace(((Spinner)view.getParent()).getTag().toString().split("-")[1]+"-" + String.valueOf(po + 1),((Spinner)view.getParent()).getTag().toString().split("-")[1]+ "-" + (i2))));
                                                                    spn.setTag(R.id.t3,1);
                                                                    spn.setSelection(1);
                                                                    spn.setTag(R.id.t3,0);

                                                                }
                                                                //.setChecked(isChecked);
                                                            }
                                                        }
                                                    }
                                                    DoValidate(null, data, (View) pviw3, pos, true, false,true);
                                                    try {
                                                        JSONObject mdata5 = new JSONObject(data.MetaData);
                                                        JSONArray rows = mdata5.getJSONArray("rows");
                                                        JSONArray cols = mdata5.getJSONArray("cols");
                                                        TableLayout tbl = (TableLayout) pviw3;
                                                        for (int i = 0; i < cols.length(); i++) {
                                                            JSONObject st = cols.getJSONObject(i);
                                                            TextView tr = (TextView) tbl.findViewWithTag("col-" + st.getString("v"));
                                                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                                tr.setVisibility(View.GONE);
                                                            }
                                                            else{
                                                                tr.setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                        for (int i = 0; i < rows.length(); i++) {
                                                            JSONObject st = rows.getJSONObject(i);
                                                            TableRow tr = (TableRow) tbl.getChildAt(i+1);
                                                            if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                                tr.setVisibility(View.GONE);
                                                            }
                                                            else{
                                                                tr.setVisibility(View.VISIBLE);
//                                                                for (int iu = 1; iu < tr.getChildCount(); iu++) {
//                                                                    JSONObject st2 = cols.getJSONObject(iu -1);
//                                                                    final View child = tr.getChildAt(iu);
//
//                                                                        if (child != null) {
//                                                                            if (st2.has("con") && !getV(st2.getString("con")).equals("1")) {
//                                                                                child.setVisibility(View.GONE);
//                                                                            }
//                                                                            else{
//                                                                                child.setVisibility(View.VISIBLE);
//                                                                            }
//                                                                        }
//                                                                    }
                                                            }

//                                                        for (int j = 0; j < cols.length(); j++) {
//                                                            JSONObject st2 = cols.getJSONObject(j);
//                                                            String inputtype = "";
//
//                                                            if(st2.has("qdata")){
//                                                                inputtype =   st2.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else if (st.has("qdata")){
//                                                                inputtype =   st.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else{
//                                                                inputtype =   mdata5.getString("qtype");
//                                                            }
//
//                                                            switch (inputtype){
//                                                                case "switch":
//                                                                    Switch et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
//
//
//
//                                                                    break;
//                                                                case "number":
//                                                                case "text":
//
//                                                                    break;
//                                                                case "combo":
//
//                                                                    break;
//                                                                case "multicheck":
//
//
//
//                                                                    break;
//                                                            }
//                                                        }
                                                        }

                                                    } catch (Exception ex) {

                                                        int iii = 0;
                                                    }
                                                    DoValidate(null, data, (View) pviw3, pos, true, false,true);
                                                    ((TextView)view).setTextSize(SpinnerTextSize);

                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });



//
                                            row.addView(sw);
                                        }

                                        break;
                                    case "number":
                                    case "text":
                                        EditText ed = new EditText(cnt);
                                        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                            @Override
                                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                                                View nextView = v.focusSearch(View.FOCUS_DOWN);
                                                if (nextView != null) {
                                                    nextView.requestFocus(View.FOCUS_DOWN);
                                                }
                                                return true;
                                            }
                                        });
                                        ed.setTypeface(typeface);
                                        ed.setImeOptions(EditorInfo.IME_ACTION_NONE);
                                        ed.setTextColor(Color.parseColor("#000000"));
                                        ed.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                        ed.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                        TableRow.LayoutParams parammc = new TableRow.LayoutParams(
                                                0,
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                        );
                                        if (inputtype.equals("number")) {
                                            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        } else {
                                            ed.setBackgroundResource(R.drawable.border);
                                        }

                                        if(st.has("qdata") &&  st.getJSONObject("qdata").has("hint")){
                                            ed.setHint(st.getJSONObject("qdata").getString("hint"));
                                        }

                                        for (int i1 = 0; i1 < rowsval.length(); i1++) {
                                            if (((JSONObject) rowsval.get(i1)).getString("c").equals(st.getString("v") + "-" + st2.getString("v"))) {
                                                ed.setText(((JSONObject) rowsval.get(i1)).getString("val"));
                                                break;
                                            }

                                        }
                                        //  if(st.has("forced") && (String.valueOf(ed.getText())).length() == 0){
                                        //     flag= false;
                                        //  ed.setError("پرکردن این فیلد الزامی است");
                                        //   }else{

                                        //   ed.setError(null);
                                        //      }
                                        if(ed.getText().toString().equals("") && row.getVisibility() == View.VISIBLE){
                                            flag2 = false;
                                        }



                                        ed.addTextChangedListener(new MyTextWatcher(tbl, data, position, holder) {
                                            @Override
                                            public void afterTextChanged(Editable s) {




                                                DoValidate(this.holder, this.data, this.view, this.posi, true, false,true);
                                                try {
                                                    JSONObject mdata5 = new JSONObject(data.MetaData);
                                                    JSONArray rows = mdata5.getJSONArray("rows");
                                                    JSONArray cols = mdata5.getJSONArray("cols");
                                                    TableLayout tbl = (TableLayout) this.view;
                                                    for (int i = 0; i < cols.length(); i++) {
                                                        JSONObject st = cols.getJSONObject(i);
                                                        TextView tr = (TextView) tbl.findViewWithTag("col-" + st.getString("v"));
                                                        if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                            tr.setVisibility(View.GONE);
                                                        }
                                                        else{
                                                            tr.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                    for (int i = 0; i < rows.length(); i++) {
                                                        JSONObject st = rows.getJSONObject(i);
                                                        TableRow tr = (TableRow) tbl.getChildAt(i+1);
                                                        if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                            tr.setVisibility(View.GONE);
                                                        }
                                                        else{
                                                            tr.setVisibility(View.VISIBLE);
                                                            for (int iu = 1; iu < tr.getChildCount(); iu++) {
                                                                JSONObject st2 = cols.getJSONObject(iu -1);
                                                                final View child = tr.getChildAt(iu);

                                                                if (child != null) {
                                                                    if (st2.has("con") && !getV(st2.getString("con")).equals("1")) {
                                                                        child.setVisibility(View.GONE);
                                                                    }
                                                                    else{
                                                                        child.setVisibility(View.VISIBLE);
                                                                    }
                                                                }
                                                            }
                                                        }

//                                                        for (int j = 0; j < cols.length(); j++) {
//                                                            JSONObject st2 = cols.getJSONObject(j);
//                                                            String inputtype = "";
//
//                                                            if(st2.has("qdata")){
//                                                                inputtype =   st2.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else if (st.has("qdata")){
//                                                                inputtype =   st.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else{
//                                                                inputtype =   mdata5.getString("qtype");
//                                                            }
//
//                                                            switch (inputtype){
//                                                                case "switch":
//                                                                    Switch et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
//
//
//
//                                                                    break;
//                                                                case "number":
//                                                                case "text":
//
//                                                                    break;
//                                                                case "combo":
//
//                                                                    break;
//                                                                case "multicheck":
//
//
//
//                                                                    break;
//                                                            }
//                                                        }
                                                    }

                                                } catch (Exception ex) {

                                                    int iii = 0;
                                                }


                                            }
                                        });
                                        parammc.gravity = Gravity.CENTER;
                                        ed.setLayoutParams(parammc);



                                        row.addView(ed);
                                        break;


                                    case "combo":
                                        Spinner spn = new Spinner(cnt);
                                        TableRow.LayoutParams parammc0 = new TableRow.LayoutParams(
                                                0,
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                        );
                                        spn.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                        spn.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                        spn.setTag(R.id.t3,0);

                                        JSONArray dd = new JSONArray();
                                        if(st2.has("forcecd")){
                                            dd =  st2.getJSONArray("cdata");
                                        }else{
                                            dd = st.has("cdata") ? st.getJSONArray("cdata") : st2.getJSONArray("cdata");
                                        }



                                        spval[] vals = new spval[dd.length() + 1];
                                        vals[0] = new spval();
                                        vals[0].ID = 0;
                                        vals[0].text = "-";

                                        for (int i1 = 0; i1 < dd.length(); i1++) {
                                            vals[i1 + 1] = new spval();
                                            vals[i1 + 1].ID = dd.getJSONObject(i1).getInt("v");
                                            vals[i1 + 1].text = dd.getJSONObject(i1).getString("t");
                                        }
                                        parammc0.gravity = Gravity.CENTER;
                                        //    parammc0.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                        spn.setLayoutParams(parammc0);
                                        //   spn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                        //   spn.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
                                        //  spn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        final SpinAdapter<spval> dataAdapter = new SpinAdapter<spval>(cnt, R.layout.support_simple_spinner_dropdown_item, vals);
                                        final int size = ops5c.length();
                                        spn.setAdapter(dataAdapter);

                                        for (int i1 = 0; i1 < rowsval.length(); i1++) {
                                            if (((JSONObject) rowsval.get(i1)).getString("c").equals(st.getString("v") + "-" + st2.getString("v"))) {
                                                spn.setSelection(dataAdapter.getPos(((JSONObject) rowsval.get(i1)).getInt("val")));
                                                break;
                                            }
                                        }
                                        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                if(((int) ((Spinner)view.getParent()).getTag(R.id.t3)) == 1){
                                                    ((Spinner)view.getParent()).setTag(R.id.t3,0) ;
                                                    return;
                                                }
                                                LinearLayout pviw3 = (LinearLayout) view.getParent().getParent().getParent();
                                                int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent());
                                                questions data = mData.get(pos);
                                                int po = Integer.parseInt(((Spinner)view.getParent()).getTag(R.id.t2).toString().split("-")[3]) - 1;
//                                                if (position == 2) {
//                                                    if (relatedstep) {
//                                                        for (int i2 = po; i2 >= 1; i2--) {
//                                                            Spinner spn=   ((Spinner ) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2))));
//                                                            spn.setTag(R.id.t3,1);
//                                                            spn.setSelection(2);
//                                                            spn.setTag(R.id.t3,0);
//                                                        }
//                                                    } else {
//                                                        Spinner spn=   ((Spinner) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace("-" + String.valueOf(po + 1), "-1")));
//                                                        spn.setTag(R.id.t3,1);
//                                                        spn.setSelection(2);
//                                                        spn.setTag(R.id.t3,0);
//                                                    }
//                                                } else {
//                                                    if (relatedstep ) {
//
//                                                        for (int i2 = (size); i2 > po; i2--) {
//                                                            View vvs =  pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2)));
//                                                            if(vvs instanceof  Spinner  ){
//                                                                Spinner spn=((Spinner) pviw3.findViewWithTag(((Spinner)view.getParent()).getTag().toString().replace("-" + String.valueOf(po + 1), "-" + (i2))));
//                                                                spn.setTag(R.id.t3,1);
//                                                                spn.setSelection(1);
//                                                                spn.setTag(R.id.t3,0);
//
//                                                            }
//                                                            //.setChecked(isChecked);
//                                                        }
//                                                    }
//                                                }
                                                DoValidate(null, data, (View) pviw3, pos, true, false,true);
                                                try {
                                                    JSONObject mdata5 = new JSONObject(data.MetaData);
                                                    JSONArray rows = mdata5.getJSONArray("rows");
                                                    JSONArray cols = mdata5.getJSONArray("cols");
                                                    TableLayout tbl = (TableLayout) pviw3;
                                                    for (int i = 0; i < cols.length(); i++) {
                                                        JSONObject st = cols.getJSONObject(i);
                                                        TextView tr = (TextView) tbl.findViewWithTag("col-" + st.getString("v"));
                                                        if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                            tr.setVisibility(View.GONE);
                                                        }
                                                        else{
                                                            tr.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                    for (int i = 0; i < rows.length(); i++) {
                                                        JSONObject st = rows.getJSONObject(i);
                                                        TableRow tr = (TableRow) tbl.getChildAt(i+1);
                                                        if (st.has("con") && !getV(st.getString("con")).equals("1")) {
                                                            tr.setVisibility(View.GONE);
                                                        }
                                                        else{
                                                            tr.setVisibility(View.VISIBLE);
                                                            for (int iu = 1; iu < tr.getChildCount(); iu++) {
                                                                JSONObject st2 = cols.getJSONObject(iu -1);
                                                                final View child = tr.getChildAt(iu);

                                                                if (child != null) {
                                                                    if (st2.has("con") && !getV(st2.getString("con")).equals("1")) {
                                                                        child.setVisibility(View.GONE);
                                                                    }
                                                                    else{
                                                                        child.setVisibility(View.VISIBLE);
                                                                    }
                                                                }
                                                            }
                                                        }

//                                                        for (int j = 0; j < cols.length(); j++) {
//                                                            JSONObject st2 = cols.getJSONObject(j);
//                                                            String inputtype = "";
//
//                                                            if(st2.has("qdata")){
//                                                                inputtype =   st2.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else if (st.has("qdata")){
//                                                                inputtype =   st.getJSONObject("qdata").getString("type");
//                                                            }
//                                                            else{
//                                                                inputtype =   mdata5.getString("qtype");
//                                                            }
//
//                                                            switch (inputtype){
//                                                                case "switch":
//                                                                    Switch et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
//
//
//
//                                                                    break;
//                                                                case "number":
//                                                                case "text":
//
//                                                                    break;
//                                                                case "combo":
//
//                                                                    break;
//                                                                case "multicheck":
//
//
//
//                                                                    break;
//                                                            }
//                                                        }
                                                    }

                                                } catch (Exception ex) {

                                                    int iii = 0;
                                                }
                                                DoValidate(null, data, (View) pviw3, pos, true, false,true);
                                                ((TextView)view).setTextSize(SpinnerTextSize);
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });

                                        row.addView(spn);
                                        break;
                                    case "multicheck":
                                        LinearLayout ll = new LinearLayout(cnt);
                                        ll.setOrientation(LinearLayout.VERTICAL);
                                        ll.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                        ll.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                        JSONArray cdata =

                                                st2.getJSONArray("cdata");
                                        JSONArray ansoj = new JSONArray();
                                        for (int i1 = 0; i1 < rowsval.length(); i1++) {
                                            if (((JSONObject) rowsval.get(i1)).getString("c").equals(st.getString("v") + "-" + st2.getString("v"))) {
                                                ansoj = ((JSONObject) rowsval.get(i1)).getJSONArray("val");
                                                break;
                                            }
                                        }
                                        for (int i1 = 0; i1 < cdata.length(); i1++) {
                                            JSONObject item = cdata.getJSONObject(i1);
                                            CheckBox sw0 = new CheckBox(cnt);
                                            sw0.setTextColor(Color.parseColor("#000000"));
                                            sw0.setText(item.getString("t"));
                                            sw0.setTag(item.getString("c"));
                                            for (int i10 = 0; i10 < ansoj.length(); i10++) {
                                                if (ansoj.getJSONObject(i10).getString("c").equals(item.getString("c")))
                                                    sw0.setChecked(ansoj.getJSONObject(i10).getString("v").equals("5"));
                                            }
                                            sw0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    TableRow pviw3 = (TableRow) buttonView.getParent().getParent();
                                                    int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent().getParent());
                                                    questions data = mData.get(pos);
                                                    DoValidate(null, data, (View) pviw3.getParent().getParent(), pos, true, false,true);
                                                }
                                            });

                                            ll.addView(sw0);
                                        }
                                        TableRow.LayoutParams parammc01 = new TableRow.LayoutParams(
                                                0,
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                        );
                                        parammc01.gravity = Gravity.CENTER;
                                        ll.setLayoutParams(parammc01);


                                        row.addView(ll);
                                        break;
                                    case "check":

                                        CheckBox sw0 = new CheckBox(cnt);
                                        sw0.setTextColor(Color.parseColor("#000000"));
                                        sw0.setText( "" );
                                        // sw0.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                        sw0.setTag(R.id.t2, "inp-" + st.getString("v") + "-" + st2.getString("v") + "-" + String.valueOf(i2 + 1));
                                        sw0.setTag("inp-" + st.getString("v") + "-" + String.valueOf(i2 + 1));
                                        for (int i1 = 0; i1 < rowsval.length(); i1++) {
                                            if (((JSONObject) rowsval.get(i1)).getString("c").equals(st.getString("v") + "-" + st2.getString("v"))) {
                                                sw0.setChecked(true);
                                                break;
                                            }

                                        }
                                        sw0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if(!buttonView.isPressed()) return;
                                                TableRow pviw3 = (TableRow) buttonView.getParent();
                                                int pos = mRecyclerView.getLayoutManager().getPosition((View) pviw3.getParent().getParent());
                                                questions data = mData.get(pos);
                                                DoValidate(null, data, (View) pviw3.getParent().getParent(), pos, true, false,true);
                                            }
                                        });



                                        TableRow.LayoutParams parammc01a = new TableRow.LayoutParams(
                                                0,
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                st2.has("w") ? Float.parseFloat(st2.getString("w")) : 1f
                                        );
                                        parammc01a.gravity = Gravity.CENTER;
                                        sw0.setLayoutParams(parammc01a);


                                        row.addView(sw0);
                                        break;
                                }
                            }


                            tbl.addView(row,tableRowParams);


                        }
                        if ((flag && flag2 ) || isNR) {
                            DoValidate(holder, data, null, position, true, isNR,false);
                        } else if (ans.answerid != -1 && isready) {

                            if (lastquestionid <= data.QnID) lastquestionid = data.QnID;
                        }


                    } catch (Exception ex5) {
                        int ii = 0;
                    }


                    //endregion case6
                    break;

            }
        } catch(Exception exr){

            int iiii = 0;
        }
    }




    private String replaceparams2(String qTitle) {

        switch (qTitle){
            case "1":
                qTitle +="-در دو هفته اخير";
                break;
            case "2":
                qTitle +="-از دو هفته تا كمتر از يك ماه قبل";
                break;
            case "3":
                qTitle +="-از يك ماه تا كمتر از شش ماه قبل";
                break;
            case "4":
                qTitle +="-از شش ماه تا كمتر از يك سال قبل";
                break;
            case "5":
                qTitle +="-در دوازده ماه گذشته، نميدانم چه وقت";
                break;
            case "6":
                qTitle +="-بيش از يك سال قبل";
                break;
        }


        return qTitle;
    }
    private String replaceparams(String qTitle) {
        if(qTitle.contains("@@")){
            String str=qTitle;
            while (str.contains("@@"))
                str = str.replaceFirst(qTitle.substring(qTitle.indexOf("@@"),qTitle.indexOf("@@")+4) ,getparam(qTitle.substring(qTitle.indexOf("@@"),qTitle.indexOf("@@")+4)));
            return str;
        }
        return qTitle;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screena
    public class ViewHolder extends RecyclerView.ViewHolder {

        View pva;
        MyTextWatcher tw;
        ArrayList<HashMap<String, MyTextWatcher>> twd;

        ViewHolder(View itemView) {
            super(itemView);
            pva = itemView;
        }
    }

    public int DoValidate(ViewHolder holder, questions data, View parentview, final int position, boolean nc, boolean nr,boolean fromuser) {

        int ress = -1;
        //   boolean nc = fromuser;
        Log.d("scoroll",data.QID+")))   DoValidate ("+position+") ===    last => " + data.QnID);

        boolean isok = true;
        boolean isok2 = true;
        if(nc && data.RQ != null){
            handelrelated(data);
        }
        answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
        if (null == parentview)
            parentview = mRecyclerView.getLayoutManager().findViewByPosition(position);
        if (null == parentview) {
            parentview = holder.pva.findViewById(R.id.parentview);
        }
        if (parentview == null) return -1;
        View res = ((ViewGroup) parentview.getParent()).findViewById(R.id.view);
        View res2 = ((ViewGroup) parentview.getParent()).findViewById(R.id.viewNR);
        //    int dda = (int) res2.getTag();


        if (res.getVisibility() == View.VISIBLE) return -1;
        if (res2.getVisibility() == View.VISIBLE) {
            if (nc) lastquestionid = data.QnID;

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                }
            });
            return -1;
        }

        switch (data.QType) {
            case 1:
                //region case1
                try {
                    String res1 = "";
                    JSONObject ansdata = new JSONObject();
                    JSONArray jans = new JSONArray();
                    JSONObject mdata = new JSONObject(data.MetaData);
                    JSONArray mlist = mdata.getJSONArray("options");


                    if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }

                    for (int i = 0; i < mlist.length(); i++) {
                        JSONObject st = mlist.getJSONObject(i);

                        String txt = "";
                        if(st.has("iscombo")){

                            Spinner et = parentview.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                            txt = et.getSelectedItem().toString().split("-")[0];

                            if (et.getSelectedItem().toString().equals("")) {
                                isok = false;
                                //   et.setError("پر کردن این فیلد الزامی است!");
                            } else {
                                //  et.setError(null);
                                res1 += et.getSelectedItem().toString().split("-")[0] + " ";
                                JSONObject jb = new JSONObject();
                                jb.put("c", st.getInt("c"));
                                jb.put("val", et.getSelectedItem().toString().split("-")[0]);
                                jans.put(jb);
                            }


                        }
                        else{

                            EditText et = parentview.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));




                            //    txt = et.getText().toString();

                            if (et.getText().toString().replace(" ","").equals("") && (et.getVisibility() == View.VISIBLE && ((View)et.getParent()).getVisibility() == View.VISIBLE)) {
                                isok = false;
                                et.setError("پر کردن این فیلد الزامی است!");
                            }
                            else if(st.has("issen") && st.getInt("issen") == 2 && et.getVisibility() == View.VISIBLE){
                                int min = 1;
                                answer anse = db.getanswer(String.valueOf(Userid),String.valueOf( data.QID) , String.valueOf (data.QnID-1));
                                if (anse.answerid == -1) min =1;
                                else{
                                    try {

                                        min = Integer.parseInt(anse.AnswerData.split(" ")[anse.AnswerData.split(" ").length-1]);
                                    } catch (Exception ex) {
                                        // res = "";
                                    }
                                }
                                if(min > Integer.parseInt(et.getText().toString())){
                                    isok = false;
                                    et.setError("سن آخرین بار نمیتواند کمتر از سن اولین بار باشد!");
                                }else{
                                    et.setError(null);
                                    res1 += et.getText().toString() + " ";
                                    JSONObject jb = new JSONObject();
                                    jb.put("c", st.getInt("c"));
                                    jb.put("val", et.getText().toString());
                                    jans.put(jb);
                                }

                            }
                            else if( et.getVisibility() == View.GONE   ){
                                et.setError(null);
                                res1 += "0" + " ";
                                JSONObject jb = new JSONObject();
                                jb.put("c", st.getInt("c"));
                                jb.put("val", et.getText().toString());
                                jans.put(jb);
                            }
                            else {
                                et.setError(null);
                                res1 += et.getText().toString() + " ";
                                JSONObject jb = new JSONObject();
                                jb.put("c", st.getInt("c"));
                                jb.put("val", et.getText().toString());
                                jans.put(jb);
                            }
                        }




                    }

                    if (isok) {
                        ansdata.put("answers", jans);

                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.GONE);
                        //  ress = position +1;
                        if ((nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK"))  || (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) ) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
//                        if ((nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF"))) {
//                            mRecyclerView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
//                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
//                                }
//                            });
//                        }

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });

                        //  this.notifyItemChanged(position + 1);
                    } else {
                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("لطفا تمام فیلد های اجباری را پر کنید");
                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;

                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });

                        }

                    }


                } catch (Exception ex) {
                    int i = 0;
                }

                //endregion case1
                break;
            case 2:
                //region case2
                try {
                    String res1 = "";
                    String err = "";
                    JSONObject mdata = new JSONObject(data.MetaData);
                    EditText et = parentview.findViewById(R.id.numbericf);


                    res1 = et.getText().toString();
                    if (res1.equals("")) {
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                                }
                            });
                            return ress;
                        }
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                                }
                            });
                            return ress;
                        }
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("NR") && nr) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                                }
                            });
                            return ress;
                        }
                        isok = false;
                    }


                    if (mdata.has("minlength")) {
                        if (res1.length() < mdata.getInt("minlength")) {
                            isok = false;
                            err += "حداقل تعداد کارکتر برای این فیلد " + mdata.getInt("minlength") + " میباشد " + "\n";
                        }
                    }
                    if (mdata.has("maxlength")) {
                        if (res1.length() > mdata.getInt("maxlength")) {
                            isok = false;
                            err += "حداکثر تعداد کارکتر برای این فیلد " + mdata.getInt("maxlength") + " میباشد " + "\n";
                        }
                    }
                    if (isok) {


                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = res1;
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = res1;
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.GONE);
                        if (nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        if (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });


                    } else if (res1 != "") {
                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(err);
                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;

                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });


                        }

                    }


                } catch (Exception ex) {
                    int i = 0;

                }

                //endregion case2
                break;
            case 3:
                //region case3
                try {
                    String res1 = "";
                    String err = "";
                    JSONObject mdata = new JSONObject(data.MetaData);
                    RadioGroup radioGroup = (RadioGroup) parentview.findViewById(R.id.radiogrop);
                    int resi = radioGroup.getCheckedRadioButtonId();
                    if (resi != -1) {
                        isok = true;
                        res1 = String.valueOf(resi);
                    } else {


                        if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                                }
                            });
                            return ress;
                        }
                        if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                                }
                            });
                            return ress;
                        }


                        isok = false;
                    }
                    //  answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
                    if (isok) {
                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = res1;
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = res1;
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        //  TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        //  tv.setVisibility(View.GONE);
                        //    ress = position +1;
                        if (nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        if (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                    } else if (res1 != "") {
                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(err);
                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;
                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });


                        }

                    }


                } catch (Exception ex) {
                    int i = 0;
                }

                //endregion case3
                break;
            case 4:
                //region case4
                try {
                    String res1 = "";
                    String err = "";
                    JSONObject ansdata = new JSONObject();


                    JSONArray jans = new JSONArray();

                    JSONObject mdata = new JSONObject(data.MetaData);
                    JSONArray mlist = mdata.getJSONArray("options");


                    if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }

                    for (int i = 0; i < mlist.length(); i++) {
                        JSONObject st = mlist.getJSONObject(i);
                        EditText et = parentview.findViewWithTag("inp-" + String.valueOf(st.getInt("v")));
                        String txt = et.getText().toString();
                        if (et.getText().toString().equals("")) {
                            isok = false;
                            et.setError("پر کردن این فیلد الزامی است!");
                        } else {
                            int mi = Integer.parseInt(txt);
                            if (st.has("min")) {
                                if (mi < st.getInt("min")) {
                                    isok = false;
                                    err += "حداقل مقدار برای " + st.getString("t") + " " + st.getInt("min") + " میباشد " + "\n";
                                }
                            }
                            if (st.has("max")) {
                                if (mi > st.getInt("max")) {
                                    isok = false;
                                    err += "حداقل مقدار برای " + st.getString("t") + " " + st.getInt("max") + " میباشد " + "\n";
                                }
                            }
                            if (isok) {
                                et.setError(null);
                                res1 += et.getText().toString() + ",";
                                JSONObject jb = new JSONObject();
                                jb.put("v", st.getInt("v"));
                                jb.put("val", et.getText().toString());
                                jans.put(jb);
                            } else {
                                et.setError(err);

                            }

                        }
                    }
                    //  answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
                    if (isok) {
                        ansdata.put("answers", jans);

                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.GONE);
                        //    ress = position +1;
                        if (nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        if (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });

                    } else {
                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("لطفا تمام فیلد های اجباری را پر کنید");
                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;

                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });


                        }

                    }


                } catch (Exception ex) {
                    int i = 0;
                }

                //endregion case4
                break;
            case 5:
                //region case5
                try {
                    String res1 = "";
                    String err = " ";
                    JSONObject ansdata = new JSONObject();


                    JSONArray jans = new JSONArray();

                    JSONObject mdata = new JSONObject(data.MetaData);
                    JSONArray mlist = mdata.getJSONArray("options");


                    if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    isok = mdata.has("forced") ? false: true;
                    for (int i = 0; i < mlist.length(); i++) {
                        JSONObject st = mlist.getJSONObject(i);
                        if(mdata.has("forceswitch")){
                            Switch et = parentview.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                            if (et.isChecked() && et.getVisibility() == View.VISIBLE) {
                                isok = true;
                                res1 += String.valueOf(st.getInt("c")) + ",";
                                JSONObject jb = new JSONObject();
                                jb.put("c", st.getInt("c"));
                                jb.put("val", et.isChecked() ? "5" : "1");

                                if(st.has("hasinput")){
                                    EditText tt = parentview.findViewWithTag("inpt-" + String.valueOf(st.getInt("c")));
                                    jb.put("valt", String.valueOf(tt.getText()));
                                }

                                jans.put(jb);
                            }
                        }else{
                            Spinner et = parentview.findViewWithTag("inp-" + String.valueOf(st.getInt("c")));
                            if ( et != null && et.getVisibility() == View.VISIBLE) {


                                if( ((spval)et.getSelectedItem()).ID == 0 && mdata.has("gforced")) isok2 = false;

                                if( ((spval)et.getSelectedItem()).ID == 1) isok = true;

                                if(((spval)et.getSelectedItem()).ID == 5 ) { res1 += String.valueOf(st.getInt("c")) + ","; isok = true;}
                                JSONObject jb = new JSONObject();
                                jb.put("c", st.getInt("c"));
                                jb.put("val", String.valueOf(((spval)et.getSelectedItem()).ID) );
                                if(st.has("hasinput")){
                                    EditText tt = parentview.findViewWithTag("inpt-" + String.valueOf(st.getInt("c")));
                                    if( ((spval)et.getSelectedItem()).ID == 5 &&   tt.getText().toString().equals("")){
                                        isok = false;
                                        tt.setError("پر کردن این قسمت الزامی است");
                                    }else {

                                        //  isok = true;
                                        tt.setError(null);
                                    }
                                    jb.put("valt", String.valueOf(tt.getText()));
                                }

                                jans.put(jb);
                            }
                        }



                    }
                    //  answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
                    if (isok && isok2) {
                        ansdata.put("answers", jans);

                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.GONE);
                        //    ress = position +1;
                        if (nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        if (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });

                    } else {
                        ansdata.put("answers", jans);
                        TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        tv.setVisibility(View.VISIBLE);
                        tv.setTextColor(Color.parseColor("#ff0000"));
                        if(!isok2){

                            tv.setText("جواب به همه ی گزینه ها اجباری میباشد");

                            if (ans.answerid == -1) {
                                ans.QID = data.QID;
                                ans.QuestionID = data.QnID;
                                ans.UserID = Userid;
                                ans.AnswerData = res1;
                                ans.AnswerMeta = ansdata.toString();
                                db.insertanswer(ans);
                            } else {
                                ans.AnswerData = res1;
                                ans.AnswerMeta = ansdata.toString();
                                db.updateanswer(ans);
                            }


                        }else{
                            tv.setText("جواب به حداقل یکی از گزینه ها اجباری میباشد");
                        }

                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;
                            final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;

                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });


                        }


//                        if (ans.answerid != -1) {
//                            if (nc) lastquestionid = data.QnID - 1;
//
//                            final int _pos = position + 1;
//                            final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;
//
//                            //  this.notifyItemRangeChanged(_pos, last - _pos);
//                            if (nc)
//                                mRecyclerView.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
//                                    }
//                                });
//
//
//                        }

                    }


                } catch (Exception ex) {
                    int i = 0;
                }

                //endregion case5
                break;
            case 6:
                //region case6
                try {
                    String res1 = " ";
                    String err = " ";
                    JSONObject ansdata = new JSONObject();

                    JSONObject jans = new JSONObject();
                    JSONArray sws = new JSONArray();

                    JSONObject mdata = new JSONObject(data.MetaData);
                    JSONArray cols = mdata.getJSONArray("cols");
                    JSONArray rows = mdata.getJSONArray("rows");
                    JSONObject qdata = mdata.getJSONObject("qdata");


                    if (ans.answerid != -1 && ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    if (ans.answerid != -1 && ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });
                        return ress;
                    }
                    isok = mdata.has("forced") ? false: true;
                    isok2 =  true;
                    TableLayout tbl = parentview.findViewById(R.id.parentview);
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject st = rows.getJSONObject(i);
                        if(st.has("hasinput")){
                            JSONObject jb = new JSONObject();
                            EditText tty = parentview.findViewWithTag("inp-"+st.getString("v"));
                            //   if(tty.getText().equals("")) isok = false;

                            jb.put(st.getString("v"), String.valueOf(tty.getText()));
                            jans.put("rowtxt",jb);
                        }
                        TableRow tr = (TableRow) tbl.getChildAt(i+1);

                        if( tr.getVisibility() == View.VISIBLE)

                            for (int j = 0; j < cols.length(); j++) {
                                JSONObject st2 = cols.getJSONObject(j);

                                String inputtype = "";

                                if(st2.has("qdata")){
                                    inputtype =   st2.getJSONObject("qdata").getString("type");
                                }
                                else if (st.has("qdata")){
                                    inputtype =   st.getJSONObject("qdata").getString("type");
                                }
                                else{
                                    inputtype =   qdata.getString("qtype");
                                }


                                switch (inputtype){
                                    case "switch":
                                        if(mdata.has("forceswitch")){
                                            Switch et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
                                            if (et.isChecked()) {
                                                isok = true;
                                                res1 += (st.getString("v")) +"-"+ (st2.getString("v")) + ",";
                                                JSONObject jb = new JSONObject();
                                                jb.put("c", st.getString("v") +"-" + st2.getString("v"));
                                                jb.put("val", et.isChecked() ? qdata.getString("qvaly") : qdata.getString("qvaln"));
                                                sws.put(jb);
                                            }
                                        }else{
                                            Spinner et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));

                                            if (  et.getVisibility() == View.VISIBLE) {

                                                if(((spval)et.getSelectedItem()).ID == 5) {
                                                    isok = true;
                                                    res1 += (st.getString("v")) +"-"+ (st2.getString("v")) + ",";

                                                }

                                                if(((spval)et.getSelectedItem()).ID == 1) {
                                                    isok = true;
                                                }

                                                if(mdata.has("gforced") && ((spval)et.getSelectedItem()).ID == 0 && j ==0){
                                                    isok2 = false;
                                                }

                                                JSONObject jb = new JSONObject();
                                                jb.put("c", st.getString("v") +"-" + st2.getString("v"));
                                                jb.put("val", String.valueOf(((spval)et.getSelectedItem()).ID));
                                                sws.put(jb);
                                            }

//
                                        }




                                        break;
                                    case "number":
                                    case "text":
                                        EditText et0 = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));

                                        if (!et0.getText().toString().equals("")) {

                                            //  isok = true;
                                            et0.setError(null);
                                            res1 += (st.getString("v")) +"-"+ (st2.getString("v")) + "-" +et0.getText().toString() + ",";
                                            JSONObject jb = new JSONObject();
                                            jb.put("c", st.getString("v") +"-" + st2.getString("v"));
                                            jb.put("val", et0.getText().toString() );
                                            sws.put(jb);
                                        }else if(st.has("forced") && j ==0){
                                            isok2 = false;
                                        }else  if(mdata.has("gforced") && j ==0){
                                            isok2 = false;
                                        }



                                        boolean isminok = true;
                                        if( et0.getVisibility() == View.VISIBLE && !et0.getText().toString().equals("") &&   inputtype.equals("number") &&  st.has("min") && st.getInt("min") > Integer.parseInt(et0.getText().toString()) ){
                                            isok2 = false;
                                            if(st.has("minhint")){
                                                et0.setError(st.getString("minhint"));
                                            }else{
                                                et0.setError("این قسمت نمیتواند کمتر از " + st.getInt("min") + "باشد");
                                            }
                                            isminok = false;

                                        }else if(inputtype.equals("number") ){
                                            et0.setError(null);
                                        }

                                        if( et0.getVisibility() == View.VISIBLE && !et0.getText().toString().equals("") &&  inputtype.equals("number") &&  st.has("max") && st.getInt("max") < Integer.parseInt(et0.getText().toString()) ){
                                            isok2 = false;
                                            if(st.has("maxhint")){
                                                et0.setError(st.getString("maxhint"));
                                            }else{
                                                et0.setError("این قسمت نمیتواند یشتر از " + st.getInt("max") + "باشد");
                                            }


                                        }else if(inputtype.equals("number") && isminok){
                                            et0.setError(null);
                                        }

//                                    else if(st.has("forced") && j == 0 && tr.getVisibility() == View.VISIBLE){
//                                        et0.setError("پرکردن این فیلد الزامی است");
//                                        isok = false;
//                                    }
                                        break;
                                    case "combo":
                                        Spinner com = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));


                                        if(mdata.has("gforced") && ((spval)com.getSelectedItem()).ID == 0 && j ==0){
                                            isok2 = false;
                                        }


                                        res1 += (st.getString("v")) +"-"+ (st2.getString("v")) + "-" +((spval)com.getSelectedItem()).ID + ",";
                                        JSONObject jb5 = new JSONObject();
                                        jb5.put("c", st.getString("v") +"-" + st2.getString("v"));
                                        jb5.put("val", ((spval)com.getSelectedItem()).ID);
                                        sws.put(jb5);

                                        break;
                                    case "multicheck":
                                        LinearLayout ll = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
                                        JSONObject jb = new JSONObject();
                                        jb.put("c", st.getString("v") +"-" + st2.getString("v"));
                                        JSONArray jsj = new JSONArray();
                                        for (int a = 0; a < ll.getChildCount(); a++) {
                                            JSONObject jbb = new JSONObject();
                                            CheckBox ch = (CheckBox) ll.getChildAt(a);
                                            jbb.put("c",ch.getTag());

                                            jbb.put("v",ch.isChecked() ? "5" :"1");


                                            jsj.put(jbb);
                                        }
                                        jb.put("val", jsj );
                                        sws.put(jb);


                                        break;
                                    case "check":
                                        CheckBox et = tr.findViewWithTag("inp-"+st.getString("v")  + "-"+String.valueOf(j +1));
                                        if (et.isChecked()) {
                                            isok = true;
                                            res1 += (st.getString("v")) +"-"+ (st2.getString("v")) + ",";
                                            JSONObject jb0 = new JSONObject();
                                            jb0.put("c", st.getString("v") +"-" + st2.getString("v"));
                                            jb0.put("val", et.isChecked() ? qdata.getString("qvaly") : qdata.getString("qvaln"));
                                            sws.put(jb0);
                                        }


                                        break;
                                }
                                // if(j >0) isok2 = true;
                            }
                    }
                    jans.put("rowsval",sws);
                    //  answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));



                    if (isok && isok2 ) {
                        ansdata.put("answers", jans);
                        if (mdata.has("secq")){
                            TextView errtvtv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                            errtvtv.setTextColor(Color.parseColor("#000000"));
                            errtvtv.setVisibility(View.VISIBLE);
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                errtvtv.setText(Html.fromHtml(mdata.getString("secq"), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                errtvtv.setText(HtmlCompat.fromHtml(mdata.getString("secq"), HtmlCompat.FROM_HTML_MODE_LEGACY));

                            }
                        }else{
                            TextView errtvtv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                            errtvtv.setVisibility(View.GONE);
                        }
                        if (ans.answerid == -1) {
                            ans.QID = data.QID;
                            ans.QuestionID = data.QnID;
                            ans.UserID = Userid;
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.insertanswer(ans);
                        } else {
                            ans.AnswerData = res1;
                            ans.AnswerMeta = ansdata.toString();
                            db.updateanswer(ans);
                        }
                        if (nc) lastquestionid = data.QnID;

                        // TextView tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        //   tv.setVisibility(View.GONE);
                        //    ress = position +1;
                        if (nc && !ans.AnswerMeta.equals("DK") && mdata.has("hasDK")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        if (nc && !ans.AnswerMeta.equals("RF") && mdata.has("hasRF")) {
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.DKbtn).setBackgroundResource(R.drawable.btngray);
                                    mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.RFbtn).setBackgroundResource(R.drawable.btngray);
                                }
                            });
                        }
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getAdapter().notifyItemChanged(position + 1);
                            }
                        });

                    } else {
                        TextView tv;
                        ansdata.put("answers", jans);
                        if(parentview instanceof TableLayout){
                            tv = ((ViewGroup) parentview.getParent()).findViewById(R.id.error);
                        }else{
                            tv = ((ViewGroup) parentview).findViewById(R.id.error);
                        }

                        tv.setVisibility(View.VISIBLE);
                        tv.setTextColor(Color.parseColor("#ff0000"));
                        if(!isok2){
                            tv.setText("جواب به همه ی گزینه ها اجباری میباشد");
                            ansdata.put("answers", jans);
                            if (ans.answerid == -1) {
                                ans.QID = data.QID;
                                ans.QuestionID = data.QnID;
                                ans.UserID = Userid;
                                ans.AnswerData = res1;
                                ans.AnswerMeta = ansdata.toString();
                                db.insertanswer(ans);
                            } else {
                                ans.AnswerData = res1;
                                ans.AnswerMeta = ansdata.toString();
                                db.updateanswer(ans);
                            }


                        }else{
                            tv.setText("جواب به حداقل یکی از گزینه ها اجباری میباشد");
                        }

                        if (ans.answerid != -1) {
                            if (nc) lastquestionid = data.QnID - 1;

                            final int _pos = position + 1;
                            final int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2;

                            //  this.notifyItemRangeChanged(_pos, last - _pos);
                            if (nc)
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerView.getAdapter().notifyItemRangeChanged(_pos, (last - _pos + 2));
                                    }
                                });


                        }

                    }


                } catch (Exception ex) {
                    String ss = ex.getMessage();
                    int i = 0;
                }

                //endregion case6
                break;
        }
        return ress;
    }

    public String getparam(String par) {
        String res = "";
        if (par.startsWith("@@")) {
            if (par.substring(2).startsWith("c")) {
                switch (par.substring(3)) {
                    case "1":
                        res = "0";
                        answer ans = db.getanswer(String.valueOf(Userid), "1", "4");
                        if (ans.answerid == -1) return res;
                        try {
                            // JSONArray ja = new JSONArray(ans.AnswerMeta);
                            PersianDate pdate = new PersianDate();
                            int rrr = pdate.getShYear() - Integer.parseInt(ans.AnswerData.split(",")[1]);
                            if (Integer.parseInt(ans.AnswerData.split(",")[0]) > pdate.getShMonth())
                                rrr--;
                            res = String.valueOf(rrr);
                        } catch (Exception ex) {
                            res = "";
                        }
                        break;
                    case "B":
                        answer ans00 = db.getanswer(String.valueOf(Userid), "16", "14");
                        if (ans00.answerid == -1) return res;
                        try {
                            res = (ans00.AnswerData);
                        } catch (Exception ex) {
                            res = "";
                        }
                        break;
                    case "2":
                        answer ans0 = db.getanswer(String.valueOf(Userid), "1", "4");
                        if (ans0.answerid == -1) return res;
                        try {
                            // JSONArray ja = new JSONArray(ans.AnswerMeta);
                            PersianDate pdate = new PersianDate();
                            int rrr = pdate.getShYear() - Integer.parseInt(ans0.AnswerData.split(",")[1]);
                            if (Integer.parseInt(ans0.AnswerData.split(",")[0]) > pdate.getShMonth())
                                rrr--;
                            res = String.valueOf(rrr);
                            if(Integer.parseInt(res) > 35) res = "35";
                        } catch (Exception ex) {
                            res = "";
                        }
                        break;
                    case "3":
                        answer ans3 = db.getanswer(String.valueOf(Userid), "10", "1");
                        if (ans3.answerid == -1) return " - ";
                        questions q3 = db.getquestion( "10", "1");
                        try {
                            JSONObject mdata5 = new JSONObject(q3.MetaData);
                            JSONArray ops5c =   mdata5.getJSONArray("rows");
                            res += "<span style=\"color: #ff00ff;\"  >";
                            for (int i = 0; i < ops5c.length(); i++) {
                                JSONObject st = ops5c.getJSONObject(i);

                                if(ans3.AnswerMeta.contains( "{\"c\":\""+st.getString("v") +"-A"+"\",\"val\":\"5\"}") ){
                                    if( st.has("tm") &&  !res.contains( " , " + st.getString("tm") ))
                                        res += " , " + st.getString("tm");
                                }
                            }
                            res += "</span>";

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "4":
                        answer ans4 = db.getanswer(String.valueOf(Userid), "11", "1");
                        if (ans4.answerid == -1) return " - ";
                        questions q4 = db.getquestion( "11", "1");
                        try {
                            JSONObject mdata5 = new JSONObject(q4.MetaData);
                            JSONArray ops5c =   mdata5.getJSONArray("rows");
                            res += "<span style=\"color: #ff00ff;\"  >";
                            for (int i = 0; i < ops5c.length(); i++) {
                                JSONObject st = ops5c.getJSONObject(i);
                                if(ans4.AnswerMeta.contains( "{\"c\":\""+st.getString("v") +"-A"+"\",\"val\":\"5\"}")){
                                    if( st.has("tm") &&  !res.contains( " , " + st.getString("tm") ))
                                        res += " , " + st.getString("tm");
                                }
                            }
                            res += "</span>";

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "5":
                        answer ans5 = db.getanswer(String.valueOf(Userid), "11", "10");
                        if (ans5.answerid == -1) return " - ";
                        questions q5 = db.getquestion( "11", "10");
                        try {
                            JSONObject mdata5 = new JSONObject(q5.MetaData);
                            JSONArray ops5c =   mdata5.getJSONArray("rows");
                            res += "<span style=\"color: #ff00ff;\"  >";
                            for (int i = 0; i < ops5c.length(); i++) {
                                JSONObject st = ops5c.getJSONObject(i);

                                if(ans5.AnswerMeta.contains( "{\"c\":\""+st.getString("v") +"-A"+"\",\"val\":\"5\"}")){
                                    if( st.has("tm") &&  !res.contains( " , " + st.getString("tm") ))
                                        res += " , " + st.getString("tm");
                                }
                            }
                            res += "</span>";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "6":
                        answer ans6 = db.getanswer(String.valueOf(Userid), "14", "3");
                        if (ans6.answerid == -1) return " - ";
                        questions q6 = db.getquestion( "14", "3");
                        try {
                            JSONObject mdata5 = new JSONObject(q6.MetaData);
                            JSONArray ops5c =   mdata5.getJSONArray("rows");
                            res += "<span style=\"color: #ff00ff;\"  >";
                            for (int i = 0; i < ops5c.length(); i++) {
                                JSONObject st = ops5c.getJSONObject(i);

                                if(   ans6.AnswerMeta.contains( "{\"c\":\""+st.getString("v") +"-A"+"\",\"val\":\"5\"}")  && Integer.parseInt( st.getString("v")) <= 29  ){
                                    if( st.has("tm") &&  !res.contains( " , " + st.getString("tm") ))
                                        res += " , " + st.getString("tm");
                                }
                            }
                            res += "</span>";

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // res = "";
                        break;

                    case "7":
                        answer ans7 = db.getanswer(String.valueOf(Userid), "14", "10");
                        if (ans7.answerid == -1) return res;
                        try {
                            // JSONArray ja = new JSONArray(ans.AnswerMeta);

                            res = (ans7.AnswerData.split(" ")[1]);
                        } catch (Exception ex) {
                            res = "";
                        }
                        break;

                    case "8":

                        res =  " بین سال گذشته تا آخرين ماه دوره افسردگي ";

                        answer ans8 = db.getanswer(String.valueOf(Userid), "15", "1");
                        if (ans8.answerid == -1) return res;
                        try {

                            res =  " بین سال گذشته تا آخرين ماه دوره افسردگي ";
                            if(ans8.AnswerData.contains("6-A-0")){
                                res = "در 12 ماه گذشته";
                            }
                        } catch (Exception ex) {
                            res = "";
                        }
                        break;
                    case "9":
                        answer ans9 = db.getanswer(String.valueOf(Userid), "16", "3");
                        if (ans9.answerid == -1) return " - ";
                        questions q9 = db.getquestion( "16", "3");
                        try {
                            JSONObject mdata9 = new JSONObject(q9.MetaData);
                            JSONArray ops5c =   mdata9.getJSONArray("rows");
                            res += "<span style=\"color: #ff00ff;\"  >";
                            for (int i = 0; i < ops5c.length(); i++) {
                                JSONObject st = ops5c.getJSONObject(i);
                                if(ans9.AnswerMeta.contains( "{\"c\":\""+st.getString("v") +"-A"+"\",\"val\":\"5\"}"   )){
                                    if( st.has("tm") &&  !res.contains( " , " + st.getString("tm") ))
                                        res += " , " + st.getString("tm");
                                    //  res += " <br> " + st.getString("t");
                                }
                            }
                            res += "</span>";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "A":
//                        answer ans10 = db.getanswer(String.valueOf(Userid), "19", "1");
//                        if (ans10.answerid == -1) return " - ";
//                        if(!ans10.AnswerData.contains("-B")) return  "-";
//                        questions q10 = db.getquestion( "19", "1");
//                        try {
//                            JSONObject mdata5 = new JSONObject(q10.MetaData);
//                            JSONArray ops5c =   mdata5.getJSONArray("rows");
//                            for (int i = 0; i < ops5c.length(); i++) {
//                                JSONObject st = ops5c.getJSONObject(i);
//                                if(ans10.AnswerData.contains(st.getString("v") +"-B")){
//                                    res += " <br> " + st.getString("t");
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        res = "<span style=\"color: #0000ff;\">آن واقعه</span>";
                        break;
                    case "C":
                        answer anss = db.getanswer(String.valueOf(Userid), "24", "7");
                        if (anss.answerid == -1) return "0";
                        try {
                            // JSONArray ja = new JSONArray(ans.AnswerMeta);
                            res = String.valueOf(anss.AnswerData);
                        } catch (Exception ex) {
                            res = "";
                        }

                        break;

                    case "E":
                        answer ansse = db.getanswer(String.valueOf(Userid), "23", "16");
                        if (ansse.answerid == -1) return "0";
                        try {
                            // JSONArray ja = new JSONArray(ans.AnswerMeta);

                            res = String.valueOf(ansse.AnswerData);
                        } catch (Exception ex) {
                            res = "0";
                        }

                        break;

                    case "F":

                        res += "<span style=\"color: #ff00ff;\"  >";
                        try {
                            answer ansk9 = db.getanswer(String.valueOf(Userid), "18", "11");
                            if(ansk9.AnswerData != null  && ansk9.AnswerData.contains("1-A") ){
                                res += " , " +"تکرار کارها";
                            }
                        } catch (Exception ex) { }
                        try {
                            answer ansk9 = db.getanswer(String.valueOf(Userid), "18", "12");
                            if(ansk9.AnswerData != null  && ansk9.AnswerData.contains("1-A") ){
                                res += " , " +"نجام کارها به ترتیب خاص";
                            }
                        } catch (Exception ex) { }
                        try {
                            answer ansk9 = db.getanswer(String.valueOf(Userid), "18", "13");
                            if(ansk9.AnswerData != null  && ansk9.AnswerData.contains("1-A") ){
                                res += " , " +"شمردن چیزها";
                            }
                        } catch (Exception ex) { }
                        try {
                            answer ansk9 = db.getanswer(String.valueOf(Userid), "18", "14");
                            if(ansk9.AnswerData != null  && ansk9.AnswerData.contains("1-A") ){
                                res += " , " +"تکرار کلمات";
                            }
                        } catch (Exception ex) { }
                        res += "</span>";
                        break;


                }
            } else {

            }
        } else {
            res = par;
        }
        return res;
    }

    public String getV(String mdt) {
        String res = "";
        try {
            JSONArray ifs = new JSONArray(mdt);
            for (int mi = 0; mi < ifs.length(); mi++) {
                JSONObject inobj = ifs.getJSONObject(mi);
                if (handel(inobj.getJSONObject("con")).equals("1"))
                    return handel(inobj.getJSONObject("res"));
            }
        } catch (Exception ex) {
            Toast.makeText(cnt, String.valueOf( lastquestionid) +  ex.getMessage(),Toast.LENGTH_LONG).show();

            int i = 0;
        }
        return res;
    }

    public String handel(JSONObject obj) throws JSONException {
        String res = "";
        if (obj.has("type")) {
            switch (obj.getString("type")) {
                case "val":
                    return obj.getString("value");
                case "dbv":
                    return db.getanswer(String.valueOf(Userid), obj.getString("value").split("-")[0], obj.getString("value").split("-")[1]).AnswerData;
                case "dbm":
                    return db.getanswer(String.valueOf(Userid), obj.getString("value").split("-")[0], obj.getString("value").split("-")[1]).AnswerMeta;
                case "dba": {
                    String rs = "";
                    if(db.getanswer(String.valueOf(Userid), obj.getString("value").split("-")[0], obj.getString("value").split("-")[1]).answerid == -1) return  "-1";
                    if(db.getanswer(String.valueOf(Userid), obj.getString("value").split("-")[0], obj.getString("value").split("-")[1]).AnswerMeta.equals("RN")) return  "-1";


                    ReadContext docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), obj.getString("value").split("-")[0], obj.getString("value").split("-")[1]).AnswerMeta);
                    switch (obj.getString("dbatype")) {
                        case "val": {
                            net.minidev.json.JSONArray r = ((net.minidev.json.JSONArray) docCtx.read(obj.getString("path")));
                            return r.size() > 0 ? r.get(0).toString() : "";
                        }
                        case "int": {

                            return  docCtx.read(obj.getString("path")).toString();
                        }
                        case "length":
                            return String.valueOf(((net.minidev.json.JSONArray) docCtx.read(obj.getString("path"))).size());
                    }
                    return rs;
                }
                case "costom1": {
                    int ssum = 0;
                    ReadContext docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "14", "1").AnswerMeta);
                    net.minidev.json.JSONArray r = ((net.minidev.json.JSONArray) docCtx.read("$.answers[?(@.c == '1')][?(@.val == 5)]"));
                    if (r.size() > 0)
                        ssum++;
                    docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "14", "2").AnswerMeta);
                    net.minidev.json.JSONArray r2 = ((net.minidev.json.JSONArray) docCtx.read("$.answers[?(@.c == '1')][?(@.val == 5)]"));
                    if (r2.size() > 0)
                        ssum++;
                    docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "14", "3").AnswerMeta);
                    net.minidev.json.JSONArray r3 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c == '1-A')][?(@.val == 5)].val"));
                    if (r3.size() > 0)
                        ssum++;
                    net.minidev.json.JSONArray r4 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['2-A','3-A','5-A','6-A'])][?(@.val == 5)].val"));
                    if (r4.size() > 0)
                        ssum++;
                    net.minidev.json.JSONArray r5 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['8-A','9-A','10-A','11-A','12-A','13-A'])][?(@.val == 5)].val"));
                    if (r5.size() > 0)
                        ssum++;
                    net.minidev.json.JSONArray r6 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['15-A','16-A','17-A','19-A'])][?(@.val == 5)].val"));
                    if (r6.size() > 0)
                        ssum++;
                    net.minidev.json.JSONArray r7 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['20-A','21-A'])][?(@.val == 5)].val"));
                    if (r7.size() > 0)
                        ssum++;
                    net.minidev.json.JSONArray r8 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['22-A','23-A','24-A','25-A'])][?(@.val == 5)].val"));
                    if (r8.size() > 0)
                        ssum++;

                    net.minidev.json.JSONArray r9 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c in ['26-A','27-A','28-A','29-A'])][?(@.val == 5)].val"));
                    if (r9.size() > 0)
                        ssum++;
                    //


                    return String.valueOf(ssum);
                }
                case "costom2": {

                    String ress = "1";
                    for (int ii =2;ii<19;ii++)
                        if(db.getanswer(String.valueOf(Userid), "15", String.valueOf( ii)).AnswerData.equals("5")) ress = "0";

                    return ress;
                }
                case "costom3": {
                    String ress = "0";
                    for (int ii =14;ii<23;ii++)
                        if(db.getanswer(String.valueOf(Userid), "19", String.valueOf( ii)).AnswerMeta.contains("5")) ress = "1";
                    return ress;
                }
                case "costom4": {
                    String ress = "0";
                    for (int ii =24;ii<42;ii++)
                        if(db.getanswer(String.valueOf(Userid), "19", String.valueOf( ii)).AnswerData.contains("5")) ress = "1";
                    return ress;
                }
                case "costom5": {

                    int ssum = 0;
                    ReadContext docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "28", "1").AnswerMeta);
                    net.minidev.json.JSONArray r = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c == '0-A' || @.c == '1-A' || @.c == '2-A' )][?(@.val == 5)]"));
                    if (r.size() > 0)
                        ssum++;
                    docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "28", "1").AnswerMeta);
                    net.minidev.json.JSONArray r2 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c == '03-A' || @.c == '3-A' || @.c == '4-A'|| @.c == '5-A' || @.c == '6-A' || @.c == '7-A' )][?(@.val == 5)]"));
                    if (r2.size() > 0)
                        ssum++;
                    docCtx = JsonPath.parse(db.getanswer(String.valueOf(Userid), "28", "1").AnswerMeta);
                    net.minidev.json.JSONArray r3 = ((net.minidev.json.JSONArray) docCtx.read("$.answers.rowsval[?(@.c == '08-A' || @.c == '8-A' || @.c == '9-A'|| @.c == '10-A' || @.c == '11-A' || @.c == '12-A' || @.c == '13-A'|| @.c == '14-A' || @.c == '15-A' || @.c == '16-A' || @.c == '17-A' )][?(@.val == 5)].val"));
                    if (r3.size() > 0)
                        ssum++;

                    return String.valueOf( ssum);
                }
                default:
                    break;
            }
        } else {
            String m1 = handel(obj.getJSONObject("mVal1"));
            String m2 = handel(obj.getJSONObject("mVal2"));
            switch (obj.getString("op")) {
                case "+":
                    return (m1 + m2);
                case "i+":
                    return String.valueOf(Integer.parseInt(m1) + Integer.parseInt(m2));
                case "-":
                    return (m1.replace(m2, ""));
                case "i-":
                    return String.valueOf(Integer.parseInt(m1) - Integer.parseInt(m2));
                case "i*":
                    return String.valueOf(Integer.parseInt(m1) * Integer.parseInt(m2));
                case "i/":
                    return String.valueOf(Integer.parseInt(m1) / Integer.parseInt(m2));
                case "!=":
                    return (!m1.equals(m2)) ? "1" : "0";
                case "==":
                    return m1.equals(m2) ? "1" : "0";

                case "i>":
                    return (Integer.parseInt(m1) > Integer.parseInt(m2) ? "1" : "0");
                case "i<":
                    return (Integer.parseInt(m1) < Integer.parseInt(m2) ? "1" : "0");
                case "i>=":
                    return (Integer.parseInt(m1) >= Integer.parseInt(m2) ? "1" : "0");
                case "i<=":
                    return (Integer.parseInt(m1) <= Integer.parseInt(m2) ? "1" : "0");

                case "&&":
                    return (m1.equals("1")) && (m2.equals("1")) ? "1" : "0";
                case "||":
                    return (m1.equals("1")) || (m2.equals("1")) ? "1" : "0";
                default:
                    break;
            }
        }
        return res;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            View par = (View) v.getParent();
            int pos = mRecyclerView.getLayoutManager().getPosition(par);
            questions data = mData.get(pos);
            answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
            if (ans.answerid == -1) {
                ans.QID = data.QID;
                ans.QuestionID = data.QnID;
                ans.UserID = Userid;
                ans.AnswerData = "";
                ans.AnswerMeta = "DK";
                db.insertanswer(ans);
            } else {
                ans.AnswerData = "";
                ans.AnswerMeta = "DK";
                db.updateanswer(ans);
            }
            mRecyclerView.getAdapter().notifyItemChanged(pos);
            // DoValidate(null, data, par, pos, true);
        }
    }

    private class MyOnClickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            View par = (View) v.getParent();
            int pos = mRecyclerView.getLayoutManager().getPosition(par);
            questions data = mData.get(pos);
            answer ans = db.getanswer(String.valueOf(Userid), String.valueOf(data.QID), String.valueOf(data.QnID));
            if (ans.answerid == -1) {
                ans.QID = data.QID;
                ans.QuestionID = data.QnID;
                ans.UserID = Userid;
                ans.AnswerData = "";
                ans.AnswerMeta = "RF";
                db.insertanswer(ans);
            } else {
                ans.AnswerData = "";
                ans.AnswerMeta = "RF";
                db.updateanswer(ans);
            }
            mRecyclerView.getAdapter().notifyItemChanged(pos);
            // DoValidate(null, data, par, pos, true);
        }
    }

    private void handelrelated(questions q){
//        for (String s : q.RQ.split(",")) {
//            if(s.equals("")) continue;
//            questions inq = db.getquestion( s.split("-")[0],s.split("-")[1]);
//            db.deleteanswer( s.split("-")[0],s.split("-")[1]);
//            if( inq.RQ != null){
//                handelrelated(inq);
//            }
//        }
    }

}