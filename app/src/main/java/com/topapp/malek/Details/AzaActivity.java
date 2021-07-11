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
import com.topapp.malek.clss.azacls;
import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.R;
import com.topapp.malek.iranmhs.azaadapter;
import com.topapp.malek.iranmhs.questionnaire;
import com.topapp.malek.iranmhs.questions;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AzaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public AlertDialog dialog;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aza);
        db = new DataBase(getApplicationContext());
        Button btna = findViewById(R.id.button8);
        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder builder = new AlertDialog.Builder(AzaActivity.this);
                builder.setTitle("افزودن عضو خانوار");

                View viewInflated = LayoutInflater.from(getApplicationContext()).inflate(R.layout.addaza,(ViewGroup) v.getParent().getParent(), false);

                builder.setView(viewInflated);

                final EditText name = viewInflated.findViewById(R.id.editText);
                final EditText sen = viewInflated.findViewById(R.id.editText44);
                final Spinner sp = viewInflated.findViewById(R.id.spinner21);
                final Spinner et2 = viewInflated.findViewById(R.id.spinner22);
                final Spinner et3 = viewInflated.findViewById(R.id.spinner23);
                final Spinner et4 = viewInflated.findViewById(R.id.spinner24);
                final EditText tozih = viewInflated.findViewById(R.id.editText5);
                final Spinner farsi = viewInflated.findViewById(R.id.spinner2);







                builder.setPositiveButton("افزودن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String  number = (name.getText().toString());
                        String  number2 = (sen.getText().toString());



                        azacls data = new azacls();
                        data.clueid = Integer.parseInt( clueid);
                        data.platenumber = Integer.parseInt(plnum);
                        data.name = number;
                        data.sal = Integer.parseInt(  number2 );


                        data.sex = sp.getSelectedItem().toString();
                        data.irani = et2.getSelectedItem().toString();
                        data.farsi = farsi.getSelectedItem().toString();
                        data.hozor = et4.getSelectedItem().toString();
                        data.hozordesc = tozih.getText().toString();
                        data.monaseb = et3.getSelectedItem().toString();
                        db.insertaza(data);


                        ((azaadapter)recyclerView.getAdapter()).updatedb();

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
        azaadapter adapter = new azaadapter(this.getApplicationContext(),db.getaza(clueid,plnum),this,clueid,plnum);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);

    }
}
