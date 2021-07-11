package com.topapp.malek;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.topapp.malek.iranmhs.CM;
import com.topapp.malek.iranmhs.DataBase;
import com.topapp.malek.iranmhs.JSDL;
import com.topapp.malek.iranmhs.JSDLData;
import com.topapp.malek.iranmhs.R;
import com.topapp.malek.iranmhs.clueadapter;
import com.topapp.malek.iranmhs.cluecls;
import com.topapp.malek.iranmhs.questionnaire;
import com.topapp.malek.iranmhs.questions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Clue extends AppCompatActivity {
    private int qid;
    private questionnaire qdata;
    private ArrayList<questions> mquestions;
    public RecyclerView recyclerView;
    boolean issending = false;
    private Boolean exit = false;
    static final int PICTURE_RESULT = 1;
    public static  String imguri;
    public static  String clueid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue);
        SendLogin();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICTURE_RESULT) {
            if(resultCode == Activity.RESULT_OK) {
                try {

                    DataBase db = new DataBase(getApplicationContext());
                    db.runquery("update tblclue set imgadd = '"+imguri+"',cluestatus = 1 where clueid = " + clueid);
                    clueadapter adapter = new clueadapter(this.getApplicationContext(),db.getclues(),this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    Log.e("URI",imguri);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startlist(){
        recyclerView = findViewById(R.id.recycleclue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        DataBase db = new DataBase(getApplicationContext());
        clueadapter adapter = new clueadapter(this.getApplicationContext(),db.getclues(),this);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);

    }
    private void HandelReq(JSDLData data){

        if(data.isok) {
            switch (data.type) {
                case "clue":
                    if (data.rcode == 200) {
                        try {
                            JSONObject dtb = new JSONObject(data.body);
                            JSONArray dt =dtb.getJSONArray("data");
                            DataBase db = new DataBase(getApplicationContext());
                            db.updatecluessts();
                            for(int i = 0;i<dt.length();i++){
                                JSONObject jb = dt.getJSONObject(i);
                                cluecls item = new cluecls();


                                item.id = jb.getInt("ClueID");
                                item = db.getclue( String.valueOf( item.id));
                                item.id = jb.getInt("ClueID");
                                item.title = jb.getString("ClueTitle");
                                item.cityname = jb.getString("City");

                                item.bakhsh = jb.getString("Bakhsh");
                                item.deh = jb.getString("Deh");
                                item.abadi = jb.getString("Abadi");//CluePostalCode

                                item.postalcode = jb.getString("CluePostalCode");//
                                item.cityid = jb.getInt("CityID");
                                item.clueloc = jb.getString("ClueLocation");
                                item.provinceid = jb.getInt("ProvinceID");
                                item.provincename = jb.getString("ProvinceName");
                              //  item.postalcode = jb.getString("ProvinceName");
                             //   item.cluestatus = 0;
                               // item.clueplatenumbers = jb.getInt("CluePlateNumbers");
                                item.clueaddress = jb.getString("ClueAdd");
                                db.insertorupdateanswer(item);
                            }

                            startlist();
//                            Intent i = new Intent(getApplicationContext(), MainMenu.class);
//                            startActivity(i);


                            // Toast.makeText(getApplicationContext(), JSDL.Token, Toast.LENGTH_LONG).show();




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
    public void SendLogin(){

        HashMap<String,String> needdata =  new HashMap<>();
        HashMap<String,String> params =  new HashMap<>();
        HashMap<String,String> headers =  new HashMap<>();

        needdata.put("Url", CM.getStg(getApplicationContext(),"ServerIP") + "clue/getcluedata");
        needdata.put("type","GET");
        needdata.put("rtype","clue");

        issending = true;
        req re = new req();
        re.execute(needdata,params,headers);

    }


}
