package com.topapp.malek.Details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.topapp.malek.clss.morajeeecls;
import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.R;
import com.topapp.malek.iranmhs.morajeatadapter;
import com.topapp.malek.iranmhs.questionnaire;
import com.topapp.malek.iranmhs.questions;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Morajeat extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public AlertDialog dialog;
DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morajeat);
db = new DataBase(getApplicationContext());
        Button btna = findViewById(R.id.button8);
        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder builder = new AlertDialog.Builder(Morajeat.this);
                builder.setTitle("افزودن مراجعه");

                View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addmorajee,(ViewGroup) v.getParent().getParent(), false);

                builder.setView(viewInflated);

                final EditText et = viewInflated.findViewById(R.id.editText);
                final Spinner sp = viewInflated.findViewById(R.id.spinner2);

                et.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersianCalendar persianCalendar = new PersianCalendar();
                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                                Morajeat.this,
                                persianCalendar.getPersianYear(),
                                persianCalendar.getPersianMonth(),
                                persianCalendar.getPersianDay()
                        );
                        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
                    }
                });



                builder.setPositiveButton("افزودن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String  number = (et.getText().toString());
                        String  number2 = (sp.getSelectedItem().toString());


                        morajeeecls data = new morajeeecls();
                        data.clueid = Integer.parseInt( clueid);
                        data.platenumber = Integer.parseInt(plnum);
                        data.vaziat = number2;
                        data.tarikh = number;
                        db.insertmorajee(data);



                        ((morajeatadapter)recyclerView.getAdapter()).updatedb();
//data
//                        // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));
//
//                        db.runquery("update tblplates set platepostalcode = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));
//
//                        mRecyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                mData = db.getcplates( String.valueOf(data.clueid));
//                                mRecyclerView.getAdapter().notifyDataSetChanged();
//                            }
//                        });



                    }
                });
                builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog =  builder.show();
              //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);





            }
        });
        startlist();
    }




    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
       // String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        final EditText et = dialog.findViewById(R.id.editText);
        et.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
    }

    private int qid;
    private questionnaire qdata;
    private ArrayList<questions> mquestions;
    public RecyclerView recyclerView;
    boolean issending = false;
    private Boolean exit = false;
    static final int PICTURE_RESULT = 1;
    public static  String imguri;
    public static  String clueid;
    public static  String plnum;



    private void startlist(){
        recyclerView = findViewById(R.id.recycleclue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        DataBase db = new DataBase(getApplicationContext());
        morajeatadapter adapter = new morajeatadapter(this.getApplicationContext(),db.getmorajee(clueid,plnum),this,clueid,plnum);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);

    }

}
