package com.topapp.malek.iranmhs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.topapp.malek.clss.azacls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class azaadapter extends RecyclerView.Adapter<azaadapter.ViewHolder> {

    private List<azacls> mData;
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
    public azaadapter(Context context, List<azacls> data, Activity pActivity, String clid, String plid) {
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
        mData = db.getaza(cluid,plnum);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = mInflater.inflate(R.layout.azaitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final azacls data = mData.get(position);


        holder.titletxt.setText(  "نوبت مراجعه ی " +  " "+ String.valueOf(position+1));
        holder.farsi.setText( data.farsi );
        holder.hozor.setText( data.hozor );
        holder.sen.setText( String.valueOf( data.sal) );
        holder.sex.setText( data.sex );

        holder.desc.setText( data.hozordesc );
        holder.esm.setText( data.name );
        holder.vaziat.setText( data.monaseb);
        holder.irani.setText( data.irani );


        holder.hazfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    db.runquery("delete from  tblaza  where azaid = " + String.valueOf( data.azaid)  );

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    mData = db.getaza( cluid,plnum);
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });

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
        TextView esm;
        TextView sen;
        TextView sex;
        TextView irani;
        TextView vaziat;
        TextView hozor;
        TextView desc;
        TextView farsi;


        Button hazfbtn;



        ViewHolder(View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.textView3);

            esm = itemView.findViewById(R.id.textView8);
            sen = itemView.findViewById(R.id.textView84);
            sex = itemView.findViewById(R.id.textView81);
            irani = itemView.findViewById(R.id.textView841);

            vaziat  = itemView.findViewById(R.id.textView844);
            hozor = itemView.findViewById(R.id.textView845);
            desc = itemView.findViewById(R.id.textView8450);
            farsi  = itemView.findViewById(R.id.textView816);

            hazfbtn = itemView.findViewById(R.id.hazfmorajeee);

        }
    }

}