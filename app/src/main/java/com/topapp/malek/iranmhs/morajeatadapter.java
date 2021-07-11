package com.topapp.malek.iranmhs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.topapp.malek.clss.morajeeecls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class morajeatadapter extends RecyclerView.Adapter<morajeatadapter.ViewHolder> {

    private List<morajeeecls> mData;
    private DataBase db;
    private LayoutInflater mInflater;
    private int lastquestionid = 0;
    private int Userid = 1;
    private String cluid = "";
    private String plnum = "";


    private Context cnt;
    RecyclerView mRecyclerView;
    static final int PICTURE_RESULT = 1;
    Activity pActivity;
    public AlertDialog dialog;

    // data is passed into the constructor
    public morajeatadapter(Context context, List<morajeeecls> data, Activity pActivity,String clid,String plid) {
        this.mInflater = LayoutInflater.from(context);
        cnt = context;
        mData = data;
        cluid = clid;
        plnum = plid;
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
    public void updatedb(){
        mData = db.getmorajee(cluid,plnum);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = mInflater.inflate(R.layout.morajeeitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final morajeeecls data = mData.get(position);


        holder.titletxt.setText(  "نوبت مراجعه ی " +  " "+ String.valueOf(position+1));


//        switch (data.platestatus){
//            case 0 :
//                holder.sts.setText("در انتظار مراجعه");
//            break;
//            case 1 :
//                holder.sts.setText("در انتظار ثبت خانوار");
//                break;
//            case 2 :
//                holder.sts.setText("در انتظار ثبت شماره تماس");
//                break;
//            case 3 :
//                holder.sts.setText("درحال انتظار هماهنگی پرسشگری");
//                break;
//            case 4 :
//                holder.sts.setText("در انتظار پرسشگری");
//                break;
//            case 5 :
//                holder.sts.setText("در انتظار ارسال به سرور");
//                break;
//            case 6 :
//                holder.sts.setText("ارسال شده به سرور");
//                break;
//            default:
//                holder.sts.setText("در انتظار مراجعه");
//                break;
//
//
//
//        }
//
//


        holder.tarikhtxt.setText(data.tarikh);
        holder.vaziat.setText(data.vaziat);
//
//        holder.postalcodetxt.setText(data.platepostalcode);
//
//       // holder.mapbtn.setTag(data.clueloc);
////        holder.mapbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String loc = (String)v.getTag();
////
////                try {
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
////        holder.crokbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                try {
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
        holder.hazfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    db.runquery("delete from  tblmorajee  where morajeeid = " + String.valueOf( data.morajeeid)  );

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    mData = db.getmorajee( cluid,plnum);
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//
//        holder.changepostal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//
//
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
//                    builder.setTitle("تغییر کد پستی");
//
//                    View viewInflated = LayoutInflater.from(cnt).inflate(R.layout.changetxtlyt, (ViewGroup)holder.itemView.getParent(), false);
//
//                    builder.setView(viewInflated);
//
//                    final EditText et = viewInflated.findViewById(R.id.editText);
//
//                    TextView et2 =   (viewInflated.findViewById(R.id.textView12));
//                    et2.setText("کد پستی جدید : ");
//                   // et.setFilters(new InputFilter[]{ new CustomRangeInputFilter(30, 200)});
//
//
//                    et.addTextChangedListener(new TextWatcher() {
//
//                        public void afterTextChanged(Editable s) {}
//
//                        public void beforeTextChanged(CharSequence s, int start,
//                                                      int count, int after) {
//                        }
//                        public void onTextChanged(CharSequence s, int start,
//                                                  int before, int count) {
//                            if(et.getText().toString().equals("") || (et.getText().toString().length()) != 10){
//                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                                et.setError("عدد باید 10 رقمی باشد");
//                            }
//                            else{
//                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                                et.setError(null);
//                            }
//                        }
//                    });
//                    builder.setPositiveButton("قبول", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                         String  number = (et.getText().toString());
//
//                          // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));
//
//                            db.runquery("update tblplates set platepostalcode = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));
//
//                            mRecyclerView.post(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    mData = db.getcplates( String.valueOf(data.clueid));
//                                    mRecyclerView.getAdapter().notifyDataSetChanged();
//                                }
//                            });
//
//
//
//                        }
//                    });
//                    builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    dialog =  builder.show();
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        });
//        holder.changeadres.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    int id = data.clueid;
//                    int y = data.platenumber;
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
//                    builder.setTitle("تغییر آدرس");
//
//                    View viewInflated = LayoutInflater.from(cnt).inflate(R.layout.changenumlyt, (ViewGroup)holder.itemView.getParent(), false);
//
//                    builder.setView(viewInflated);
//
//                    final EditText et = viewInflated.findViewById(R.id.editText);
//
//                    TextView et2 =   (viewInflated.findViewById(R.id.textView12));
//                    et2.setText("آدرس جدید : ");
//                    // et.setFilters(new InputFilter[]{ new CustomRangeInputFilter(30, 200)});
//
//
//                    et.addTextChangedListener(new TextWatcher() {
//
//                        public void afterTextChanged(Editable s) {}
//
//                        public void beforeTextChanged(CharSequence s, int start,
//                                                      int count, int after) {
//                        }
//                        public void onTextChanged(CharSequence s, int start,
//                                                  int before, int count) {
//                            if(et.getText().toString().equals("") ){
//                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                                et.setError("آدرس نمیتواند خالی باشد");
//                            }
//                            else{
//                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                                et.setError(null);
//                            }
//                        }
//                    });
//                    builder.setPositiveButton("قبول", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            String  number = (et.getText().toString());
//
//                            // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));
//
//                            db.runquery("update tblplates set plateadd = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));
//
//                            mRecyclerView.post(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    mData = db.getcplates( String.valueOf(data.clueid));
//                                    mRecyclerView.getAdapter().notifyDataSetChanged();
//                                }
//                            });
//
//
//
//                        }
//                    });
//                    builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    dialog =  builder.show();
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    // stores and recycles views as they are scrolled off screena
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titletxt;
        TextView tarikhtxt;

        TextView vaziat;
     //   Button mapbtn;
        Button hazfbtn;

        TextView sts;

        ViewHolder(View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.textView3);
            tarikhtxt = itemView.findViewById(R.id.textView8);

            vaziat = itemView.findViewById(R.id.textView84);

            hazfbtn = itemView.findViewById(R.id.hazfmorajeee);

        }
    }

}