package com.topapp.malek.iranmhs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.topapp.ShoroPor;
import com.topapp.malek.Details.AzaActivity;
import com.topapp.malek.Details.HamahangiActivity;
import com.topapp.malek.Details.Morajeat;
import com.topapp.malek.Details.ShomareActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class plateadapter extends RecyclerView.Adapter<plateadapter.ViewHolder> {

    private List<platecls> mData;
    private DataBase db;
    private LayoutInflater mInflater;
    private int lastquestionid = 0;
    private int Userid = 1;

    private Context cnt;
    RecyclerView mRecyclerView;
    static final int PICTURE_RESULT = 1;
    Activity pActivity;
    public AlertDialog dialog;

    // data is passed into the constructor
    public plateadapter(Context context, List<platecls> data, Activity pActivity) {
        this.mInflater = LayoutInflater.from(context);
        cnt = context;
        mData = data;
        this.pActivity = pActivity;
        db = new DataBase(cnt);

    }

    @Override
    public int getItemViewType(int position) {

        return 1;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = mInflater.inflate(R.layout.plateitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final platecls data = mData.get(position);


        holder.titletxt.setText(  "شماره خانوار" +  " "+ data.platenumber);


        switch (data.platestatus){
            case 0 :
                holder.sts.setText("در انتظار مراجعه");
            break;
            case 1 :
                holder.sts.setText("در انتظار ثبت خانوار");
                break;
            case 2 :
                holder.sts.setText("در انتظار ثبت شماره تماس");
                break;
            case 3 :
                holder.sts.setText("درحال انتظار هماهنگی پرسشگری");
                break;
            case 4 :
                holder.sts.setText("در انتظار پرسشگری");
                break;
            case 5 :
                holder.sts.setText("در انتظار ارسال به سرور");
                break;
            case 6 :
                holder.sts.setText("ارسال شده به سرور");
                break;
            default:
                holder.sts.setText("در انتظار مراجعه");
                break;



        }


        holder.addresstxt.setText(data.plateadd);

        holder.postalcodetxt.setText(data.platepostalcode);

       // holder.mapbtn.setTag(data.clueloc);
//        holder.mapbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String loc = (String)v.getTag();
//
//                try {
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        holder.morajeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent ii = new Intent(cnt, Morajeat.class);
                    Morajeat.clueid = String.valueOf( data.clueid);
                    Morajeat.plnum = String.valueOf( data.platenumber);
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cnt.startActivity(ii);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.shomare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent ii = new Intent(cnt, ShomareActivity.class);
                    ShomareActivity.clueid = String.valueOf( data.clueid);
                    ShomareActivity.plnum = String.valueOf( data.platenumber);
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cnt.startActivity(ii);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.aza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent ii = new Intent(cnt, AzaActivity.class);
                    AzaActivity.clueid = String.valueOf( data.clueid);
                    AzaActivity.plnum = String.valueOf( data.platenumber);
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cnt.startActivity(ii);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.hamahangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent ii = new Intent(cnt, HamahangiActivity.class);
                    HamahangiActivity.clueid = String.valueOf( data.clueid);
                    HamahangiActivity.plnum = String.valueOf( data.platenumber);
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cnt.startActivity(ii);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.porsesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    if(db.isregisteduser(data.userid)){
                        Intent ii = new Intent(cnt, ShoroPor.class);
                        ShoroPor.userid = data.userid;
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cnt.startActivity(ii);
                    }else{
                        Intent ii = new Intent(cnt, Main2Activity.class);
                        Main2Activity.UserID = data.userid;
                        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cnt.startActivity(ii);
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.changepostal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
                    builder.setTitle("تغییر کد پستی");

                    View viewInflated = LayoutInflater.from(cnt).inflate(R.layout.changetxtlyt, (ViewGroup)holder.itemView.getParent(), false);

                    builder.setView(viewInflated);

                    final EditText et = viewInflated.findViewById(R.id.editText);

                    TextView et2 =   (viewInflated.findViewById(R.id.textView12));
                    et2.setText("کد پستی جدید : ");
                   // et.setFilters(new InputFilter[]{ new CustomRangeInputFilter(30, 200)});


                    et.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {}

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(et.getText().toString().equals("") || (et.getText().toString().length()) != 10){
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                et.setError("عدد باید 10 رقمی باشد");
                            }
                            else{
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                et.setError(null);
                            }
                        }
                    });
                    builder.setPositiveButton("قبول", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                         String  number = (et.getText().toString());

                          // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));

                            db.runquery("update tblplates set platepostalcode = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    mData = db.getcplates( String.valueOf(data.clueid));
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });



                        }
                    });
                    builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog =  builder.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
        holder.changeadres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    int id = data.clueid;
                    int y = data.platenumber;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
                    builder.setTitle("تغییر آدرس");

                    View viewInflated = LayoutInflater.from(cnt).inflate(R.layout.changenumlyt, (ViewGroup)holder.itemView.getParent(), false);

                    builder.setView(viewInflated);

                    final EditText et = viewInflated.findViewById(R.id.editText);

                    TextView et2 =   (viewInflated.findViewById(R.id.textView12));
                    et2.setText("آدرس جدید : ");
                    // et.setFilters(new InputFilter[]{ new CustomRangeInputFilter(30, 200)});


                    et.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {}

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(et.getText().toString().equals("") ){
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                et.setError("آدرس نمیتواند خالی باشد");
                            }
                            else{
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                et.setError(null);
                            }
                        }
                    });
                    builder.setPositiveButton("قبول", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String  number = (et.getText().toString());

                            // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));

                            db.runquery("update tblplates set plateadd = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    mData = db.getcplates( String.valueOf(data.clueid));
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });



                        }
                    });
                    builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog =  builder.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    // stores and recycles views as they are scrolled off screena
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titletxt;
        TextView addresstxt;
     //   TextView citytxt;
      //  TextView proviencetxt;
      //  TextView platenumbertxt;
        TextView postalcodetxt;
       Button morajeat;
       Button hamahangi;
       Button aza;
       Button shomare;




        Button porsesh;
        Button changeadres;
        Button changepostal;
     //   Button plakbtn;
        TextView sts;

        ViewHolder(View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.textView3);
            addresstxt = itemView.findViewById(R.id.textView10);
            // citytxt = itemView.findViewById(R.id.textView8);
            //  proviencetxt = itemView.findViewById(R.id.textView6);
            //  platenumbertxt = itemView.findViewById(R.id.textView63);
            postalcodetxt = itemView.findViewById(R.id.textView84);
            sts = itemView.findViewById(R.id.textView831);
            morajeat = itemView.findViewById(R.id.button2);
            hamahangi = itemView.findViewById(R.id.button21);
            aza = itemView.findViewById(R.id.button3);
            shomare = itemView.findViewById(R.id.button4);




            porsesh = itemView.findViewById(R.id.button31);
            changeadres = itemView.findViewById(R.id.button7);
            changepostal = itemView.findViewById(R.id.button6);
          //  plakbtn = itemView.findViewById(R.id.button4);
        }
    }

}