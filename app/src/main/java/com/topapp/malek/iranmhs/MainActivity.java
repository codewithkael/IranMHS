package com.topapp.malek.iranmhs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.topapp.malek.MainMenu;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn;
    boolean issending = false;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase db = new DataBase(getApplicationContext(),true);
        db.cpdb();
     //   db.copydbfile();
        setContentView(R.layout.activity_main);
        btn =  (Button) findViewById(R.id.signupbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendLogin();


//                DataBase db = new DataBase(getApplicationContext());
//                try {
//
//                    db.updateDataBase();
//
//                    Toast.makeText(getApplicationContext(),"دیتابیس با موفقیت آپدیت شد",Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(getApplicationContext(),Main2Activity.class);
//                    startActivity(i);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }




            }
        });

       FloatingActionButton btn2 =  (FloatingActionButton) findViewById(R.id.floatingActionButton);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendLogi2n();

//                Intent i = new Intent(getApplicationContext(),Main2Activity.class);
//                startActivity(i);

            }
        });


        final String username = CM.getStg(getApplicationContext(),"username");
        if(null != username && !username.equals("")){
            ((TextView) findViewById(R.id.Userlogintxt)).setText(username);
        }
        String password = CM.getStg(getApplicationContext(),"password");
        if(null != password && !password.equals("")){
            ((TextView) findViewById(R.id.PassLogintxt)).setText(password);
        }


        if(CM.getbStg(getApplicationContext(),"example_switch")){

            Switch sw = (Switch)findViewById(R.id.switch1);
            sw.setChecked(true);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    btn.performClick();
                }
            }, 500);
        }else{
            Switch sw = (Switch)findViewById(R.id.switch1);
            sw.setChecked(false);

        }


        ((TextView)findViewById(R.id.forgetpassbtn) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase db = new DataBase(getApplicationContext());
//                try {
//
////                    db.updateDataBase();
////
////                    Toast.makeText(getApplicationContext(),"دیتابیس با موفقیت آپدیت شد",Toast.LENGTH_LONG).show();
////                    Intent i = new Intent(getApplicationContext(),Main2Activity.class);
////                    startActivity(i);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        ((TextView)findViewById(R.id.textView) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent ii = new Intent(getApplicationContext(),Main2Activity.class);
               startActivity(ii);
            }
        });



//        ((TextView)findViewById(R.id.forgetpassbtn)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),PassForget.class);
//                startActivity(i);
//            }
//        });

    }

    private void SendLogi2n() {
        Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
    }

    private void HandelReq(JSDLData data){

        if(data.isok) {
            switch (data.type) {
                case "login":
                    if (data.rcode == 200) {
                        try {
                            JSONObject jo = new JSONObject(data.body);
                            JSDL.KPToken = jo.getString("token");
                            JSDL.Token = jo.getString("token");

                            JSDL.mUserName = jo.getString("username");
                            JSDL.lanid = jo.getString("lanid");

                            Intent i = new Intent(getApplicationContext(), MainMenu.class);
                            startActivity(i);


                           // Toast.makeText(getApplicationContext(), JSDL.Token, Toast.LENGTH_LONG).show();




                        } catch (Exception ex) {
                            //todo handel ex
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //todo handel bad request
                        Toast.makeText(getApplicationContext(), data.getBody("message"), Toast.LENGTH_LONG).show();
                    }
                    btn.setText("ورود");
                    break;
            }
        }
        else{

            Toast.makeText(getApplicationContext(),"اتصال به سرور برقرار نیست", Toast.LENGTH_LONG).show();

        }
        issending = false;
        btn.setText("ورود");
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
        if(issending){
            Toast.makeText(getApplicationContext(),"لطفا کمی صبر کنید",Toast.LENGTH_LONG).show();

        }
        EditText usrtxt = (EditText)findViewById(R.id.Userlogintxt);
        EditText pastxt = (EditText)findViewById(R.id.PassLogintxt);
        String usert = String.valueOf(usrtxt.getText());
        String passt = String.valueOf(pastxt.getText());
        btn.setText("درحال ارسال ..");
        HashMap<String,String> needdata =  new HashMap<>();
        HashMap<String,String> params =  new HashMap<>();
        HashMap<String,String> headers =  new HashMap<>();
        needdata.put("Url",CM.getStg(getApplicationContext(),"ServerIP") + "authentication/loginclient");
        needdata.put("type","POST");
        needdata.put("rtype","login");
        if(usert .equals("")  || passt .equals("")   ){
            Toast.makeText(getApplicationContext(),"نام کاربری یا رمز عبور وارد نشده است",Toast.LENGTH_LONG).show();
            btn.setText("ورود");

            return;
        }
        params.put("Username",usert);
        params.put("Password",passt);
        params.put("LanID",passt);

        issending = true;
        req re = new req();
        re.execute(needdata,params,headers);


    }

    @Override
    public void onBackPressed() {



        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "برای خروج دکمه برکشت را بزنید",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }




    }

}
