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
import com.topapp.malek.clss.shomaretamascls;
import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.R;
import com.topapp.malek.iranmhs.questionnaire;
import com.topapp.malek.iranmhs.questions;
import com.topapp.malek.iranmhs.shomareadapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShomareActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public AlertDialog dialog;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shomare);
        db = new DataBase(getApplicationContext());
        Button btna = findViewById(R.id.button8);
        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder builder = new AlertDialog.Builder(ShomareActivity.this);
                builder.setTitle("افزودن شماره تماس");

                View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addshomare,(ViewGroup) v.getParent().getParent(), false);

                builder.setView(viewInflated);

                final EditText et = viewInflated.findViewById(R.id.editText);
                final EditText et2 = viewInflated.findViewById(R.id.editText1);
                final Spinner sp = viewInflated.findViewById(R.id.spinner2);









                builder.setPositiveButton("افزودن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String  number3 = (et2.getText().toString());
                      //  String  number4 = (et3.getText().toString());
                        String  number = (et.getText().toString());
                        String  number2 = (sp.getSelectedItem().toString());


                        shomaretamascls data = new shomaretamascls();
                        data.clueid = Integer.parseInt( clueid);
                        data.platenumber = Integer.parseInt(plnum);
                        data.noee = number2;
                        data.shomare = number;
                        data.tozihat = number3;
                    //    data.natije = number4;
                        db.insertshomaretamas(data);


                        ((shomareadapter)recyclerView.getAdapter()).updatedb();

//data
//                        // db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));
//
//                        db.runquery("update tblplates set platepostalcode = '"+number+"' where clueid = " + String.valueOf( data.clueid) +" and platenumber =  " + String.valueOf( data.platenumber));
//




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
        shomareadapter adapter = new shomareadapter(this.getApplicationContext(),db.getshomaretamas(clueid,plnum),this,clueid,plnum);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }
}
