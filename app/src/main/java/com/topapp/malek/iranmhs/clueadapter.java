package com.topapp.malek.iranmhs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.topapp.malek.Clue;
import com.topapp.malek.Plate;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class clueadapter extends RecyclerView.Adapter<clueadapter.ViewHolder> {

    private List<cluecls> mData;
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
    public clueadapter(Context context,List<cluecls> data,Activity pActivity) {
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

        view = mInflater.inflate(R.layout.clueitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final cluecls data = mData.get(position);


        holder.titletxt.setText(data.title);


        switch (data.cluestatus){
            case 0 :
                holder.sts.setText("در انتظار کروکی");
                holder.plakbtn.setEnabled(true);
                holder.listbtn.setEnabled(false);
                holder.serverbtn.setEnabled(false);
                holder.crokbtn.setEnabled(true);

            break;
            case 1 :
                holder.sts.setText("در انتظار ثبت پلاک");
                holder.plakbtn.setEnabled(true);
                holder.listbtn.setEnabled(false);
                holder.serverbtn.setEnabled(false);
                holder.crokbtn.setEnabled(false);
                break;
            case 2 :
                holder.sts.setText("در انتظار پرسشگری");
                holder.plakbtn.setEnabled(false);
                holder.listbtn.setEnabled(true);
                holder.serverbtn.setEnabled(false);
                holder.crokbtn.setEnabled(false);
                break;
            case 3 :
                holder.sts.setText("درحال نکمیل پرسشگری");
                holder.plakbtn.setEnabled(false);
                holder.listbtn.setEnabled(true);
                holder.serverbtn.setEnabled(false);
                holder.crokbtn.setEnabled(false);
                break;
            case 4 :
                holder.sts.setText("در انتظار ارسال به سرور");
                holder.plakbtn.setEnabled(false);
                holder.listbtn.setEnabled(true);
                holder.serverbtn.setEnabled(true);
                holder.crokbtn.setEnabled(false);
                break;
            case 5 :
                holder.sts.setText("ارشال شده به سرور");
                holder.plakbtn.setEnabled(false);
                holder.listbtn.setEnabled(true);
                holder.serverbtn.setEnabled(false);
                holder.crokbtn.setEnabled(false);
                break;
            default:
                holder.sts.setText("در انتظار کروکی");
                break;



        }


        holder.addresstxt.setText(data.clueaddress);
        holder.platenumbertxt.setText( String.valueOf(  data.clueplatenumbers));
        holder.citytxt.setText(data.cityname);
        holder.proviencetxt.setText(data.provincename);
        holder.postalcodetxt.setText(data.postalcode);

        holder.bakhsh.setText(data.bakhsh);
        holder.deh.setText(data.deh);
        holder.abadi.setText(data.abadi);


        holder.mapbtn.setTag(data.clueloc);
        holder.mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = (String)v.getTag();

                try {
                    JSONArray ja = new JSONArray(loc);
                    String uri = "http://maps.google.com/maps?daddr=" + ja.get(1) + "," + ja.get(0)+ " (" + data.title + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    try
                    {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cnt.startActivity(intent);
                    }
                    catch(ActivityNotFoundException ex)
                    {
                        try
                        {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            unrestrictedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            cnt.startActivity(unrestrictedIntent);
                        }
                        catch(ActivityNotFoundException innerEx)
                        {
                            Toast.makeText(cnt, "Please install a maps application", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.crokbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (ContextCompat.checkSelfPermission(cnt, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED){

                        ActivityCompat.requestPermissions(pActivity, new String[] {Manifest.permission.CAMERA}, 0);
                    }
                    else{
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //getting uri of the file
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_"+ timeStamp + "_";
                        File image_file = File.createTempFile(imageFileName,".jpg",cnt.getFilesDir());

                        //String ppr = cnt.getFilesDir()+"/"+imageFileName +".jpg";
                        Clue.imguri = image_file.getAbsolutePath();
                        Clue.clueid = String.valueOf( data.id);
                        //File f = new File(ppr);
                        Uri photoURI = FileProvider.getUriForFile(cnt.getApplicationContext(),
                                "com.topapp.malek.iranmhs",
                                image_file);
                        //Setting the file Uri to my photo
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                        if(intent.resolveActivity(cnt.getPackageManager())!=null)
                        {
                            pActivity.startActivityForResult(intent, PICTURE_RESULT);
                        }



                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.plakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                   final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
                    builder.setTitle("ورود تعداد پلاک");

                    View viewInflated = LayoutInflater.from(cnt).inflate(R.layout.tedadpelaklayout, (ViewGroup)holder.itemView.getParent(), false);

                    builder.setView(viewInflated);

                   final EditText et = viewInflated.findViewById(R.id.editText);

                    ((Button)viewInflated.findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            et.setText("200");
                        }
                    });

                    et.setFilters(new InputFilter[]{ new CustomRangeInputFilter(30, 200)});


                    et.addTextChangedListener(new TextWatcher() {

                        public void afterTextChanged(Editable s) {}

                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                           if(et.getText().toString().equals("") || Integer.parseInt(et.getText().toString()) <30){
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            et.setError("عدد نمیتواند کمتر از 30 باشد");
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
                            int number = 0;
                            number = Integer.parseInt(et.getText().toString());
                            int i = number / 6;

                            final int min = 1;
                            final int max = 6;
                             int random = 1 ;// new Random().nextInt((max - min) + 1) + min;
                            db.runquery("delete from tblplates  where clueid = " + String.valueOf( data.id));
                            while (random <= number){
                                platecls item = new platecls();
                                item.plateadd = data.clueaddress;
                                item.platepostalcode = data.postalcode;
                                item.platenumber = random;
                                item.clueid = data.id;
                                db.insertplate(item);
                                random += i;
                            }
                            db.runquery("update tblclue set clueplatenumber = "+max+",cluestatus = 2 where clueid = " + String.valueOf( data.id));
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                    mData = db.getclues();
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
        holder.listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plate.clueid = String.valueOf( data.id);
                Intent iii = new Intent(cnt,Plate.class);
                iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cnt.startActivity(iii);
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
        TextView citytxt;
        TextView proviencetxt;
        TextView platenumbertxt;
        TextView postalcodetxt;

        TextView bakhsh;
        TextView deh;
        TextView abadi;

        Button mapbtn;
        Button crokbtn;
        Button plakbtn;
        Button listbtn;
        Button serverbtn;
        TextView sts;

        ViewHolder(View itemView) {
            super(itemView);
            titletxt = itemView.findViewById(R.id.textView3);
            addresstxt = itemView.findViewById(R.id.textView10);


            deh = itemView.findViewById(R.id.textView8);
            bakhsh = itemView.findViewById(R.id.textView6);

            abadi = itemView.findViewById(R.id.textView84);

            citytxt = itemView.findViewById(R.id.textView800);
            proviencetxt = itemView.findViewById(R.id.textView600);

            postalcodetxt = itemView.findViewById(R.id.textView8400);


            platenumbertxt = itemView.findViewById(R.id.textView63);
            sts = itemView.findViewById(R.id.textView831);
            mapbtn = itemView.findViewById(R.id.button2);
            crokbtn = itemView.findViewById(R.id.button3);
            plakbtn = itemView.findViewById(R.id.button4);
            listbtn = itemView.findViewById(R.id.button31);
            serverbtn = itemView.findViewById(R.id.button21);
        }
    }

}