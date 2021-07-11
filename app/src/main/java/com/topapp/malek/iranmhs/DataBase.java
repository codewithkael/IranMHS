package com.topapp.malek.iranmhs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.topapp.malek.clss.azacls;
import com.topapp.malek.clss.hamahangicls;
import com.topapp.malek.clss.morajeeecls;
import com.topapp.malek.clss.shomaretamascls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    private static String DB_NAME = "iranmhs.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;

        this.getReadableDatabase();
    }

    public DataBase(Context context,Boolean copdb) {
        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;

        this.getReadableDatabase();

    }

    public  void cpdb(){
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = this.mContext.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + this.mContext.getPackageName() + "/databases/";

        copyDataBase();
    }

    public  void copydbfile(){
      //  openDataBase();
        copyDataBase();
//        this.getReadableDatabase();
//        this.close();
//        try {
//            copyDBFile();
//        } catch (IOException mIOException) {
//            throw new Error("ErrorCopyingDataBase");
//        }

    }

    public void updateDataBase() throws IOException {
        mNeedUpdate = true;
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        dbFile.delete();
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        //    InputStream mInput = mContext.getAssets().open(DB_NAME);
        InputStream mInput = mContext.getResources().openRawResource(R.raw.iranmhs);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
    //____________helpers_____________//

    public ArrayList<questionnaire> getquestinares(String serach) {
        ArrayList<questionnaire> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblQuestionnaire  ";//where QuestionnaireID in (27)
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    questionnaire item = new questionnaire();

                    item.QID = cursor.getInt(cursor.getColumnIndex("QuestionnaireID"));
                    item.QTitle = cursor.getString(cursor.getColumnIndex("QTitle"));
                    item.QCode = cursor.getString(cursor.getColumnIndex("QCode"));
                    item.Qdesc = cursor.getString(cursor.getColumnIndex("qdesc"));
                    item.imgid = cursor.getInt(cursor.getColumnIndex("qpicnom"));

                    if (serach.equals("") || (item.QCode.contains(serach)) || (item.QTitle.contains(serach)))
                        data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public questionnaire getquestinare(String qid) {
        ArrayList<questionnaire> data = new ArrayList<>();
        questionnaire item = new questionnaire();
        try {
            String selectQuery = " select * from tblQuestionnaire where QuestionnaireID = " + qid;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    item.QID = cursor.getInt(cursor.getColumnIndex("QuestionnaireID"));
                    item.QTitle = cursor.getString(cursor.getColumnIndex("QTitle"));
                    item.QCode = cursor.getString(cursor.getColumnIndex("QCode"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return item;
    }

    public ArrayList<questions> getquestions(String qid) {
        ArrayList<questions> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblQuestions where QID = " + qid + " order by QuestionID ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    questions item = new questions();

                    item.QID = cursor.getInt(cursor.getColumnIndex("QID"));
                    item.QType = cursor.getInt(cursor.getColumnIndex("QType"));
                    item.QnID = cursor.getInt(cursor.getColumnIndex("QuestionID"));
                    item.QTitle = cursor.getString(cursor.getColumnIndex("QTitle"));
                    item.QCode = cursor.getString(cursor.getColumnIndex("QCode"));
                    item.Qorder = cursor.getString(cursor.getColumnIndex("Qorder"));
                    item.QDesctiption = cursor.getString(cursor.getColumnIndex("QDesctiption"));
                    item.Prerequisites = cursor.getString(cursor.getColumnIndex("Prerequisites"));
                    item.MetaData = cursor.getString(cursor.getColumnIndex("MetaData"));
                    item.RQ = cursor.getString(cursor.getColumnIndex("RQ"));


                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public questions getquestion(String qid,String QUID) {

        questions item = new questions();
        try {
            String selectQuery = " select * from tblQuestions where QID = " + qid+" and QuestionID = "+QUID;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    item.QID = cursor.getInt(cursor.getColumnIndex("QID"));
                    item.QType = cursor.getInt(cursor.getColumnIndex("QType"));
                    item.QnID = cursor.getInt(cursor.getColumnIndex("QuestionID"));
                    item.QTitle = cursor.getString(cursor.getColumnIndex("QTitle"));
                    item.QCode = cursor.getString(cursor.getColumnIndex("QCode"));
                    item.QDesctiption = cursor.getString(cursor.getColumnIndex("QDesctiption"));
                    item.Prerequisites = cursor.getString(cursor.getColumnIndex("Prerequisites"));
                    item.MetaData = cursor.getString(cursor.getColumnIndex("MetaData"));
                    item.RQ = cursor.getString(cursor.getColumnIndex("RQ"));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return item;
    }

    public answer getanswer(String userid, String qid, String questionid) {
        answer item = new answer();
        item.answerid = -1;
        item.AnswerData ="";
        item.AnswerMeta = "";
        try {
            String selectQuery = "select * from tblanswer where UserID = " + userid + " and QID = " + qid + " and QuestionID = " + questionid;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    item.QID = cursor.getInt(cursor.getColumnIndex("QID"));
                    item.UserID = cursor.getInt(cursor.getColumnIndex("UserID"));
                    item.answerid = cursor.getInt(cursor.getColumnIndex("answerid"));
                    item.QuestionID = cursor.getInt(cursor.getColumnIndex("QuestionID"));
                    item.AnswerData = cursor.getString(cursor.getColumnIndex("AnswerData"));
                    item.AnswerMeta = cursor.getString(cursor.getColumnIndex("AnswerMeta"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return item;
    }

    public void insertanswer(answer ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("QID", ans.QID);
            cv.put("QuestionID", ans.QuestionID);
            cv.put("UserID", ans.UserID);
            cv.put("AnswerData", ans.AnswerData);
            cv.put("AnswerMeta", ans.AnswerMeta);
            mm = String.valueOf(mydb.insert("tblanswer", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }

    public void updateanswer(answer ans) {
        SQLiteDatabase mydb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("QID", ans.QID);
        cv.put("QuestionID", ans.QuestionID);
        cv.put("UserID", ans.UserID);
        cv.put("AnswerData", ans.AnswerData);
        cv.put("AnswerMeta", ans.AnswerMeta);
      //  mydb.insertWithOnConflict(SQLiteDatabase.CONFLICT_REPLACE)
        mydb.update("tblanswer",
                cv,
                "answerid = ?",
                new String[]{String.valueOf(ans.answerid)});
    }

    public cluecls getclue(String cid) {

        cluecls item = new cluecls();
        try {
            String selectQuery = " select * from tblclue where clueid = " + cid;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    item.id = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.title = cursor.getString(cursor.getColumnIndex("title"));
                    item.cityname = cursor.getString(cursor.getColumnIndex("cityname"));
                    item.cityid = cursor.getInt(cursor.getColumnIndex("cityid"));
                    item.clueloc = cursor.getString(cursor.getColumnIndex("clueloc"));
                    item.provinceid = cursor.getInt(cursor.getColumnIndex("provinceid"));
                    item.provincename = cursor.getString(cursor.getColumnIndex("provincename"));
                    item.cluestatus =cursor.getInt(cursor.getColumnIndex("cluestatus"));
                    item.clueplatenumbers = cursor.getInt(cursor.getColumnIndex("clueplatenumber"));
                    item.clueaddress = cursor.getString(cursor.getColumnIndex("clueaddress"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return item;
    }

    public void insertorupdateanswer(cluecls ans) {
        SQLiteDatabase mydb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("clueid", ans.id);
        cv.put("title", ans.title);
        cv.put("cityname", ans.cityname);
        cv.put("cityid", ans.cityid);
        cv.put("provinceid", ans.provinceid);
        cv.put("provincename", ans.provincename);
        cv.put("clueaddress", ans.clueaddress);
        cv.put("clueplatenumber", ans.clueplatenumbers);
        cv.put("cluestatus", ans.cluestatus);
        cv.put("clueloc", ans.clueloc);
        cv.put("postalcode", ans.postalcode);

        cv.put("deh", ans.deh);
        cv.put("bakhsh", ans.bakhsh);
        cv.put("abadi", ans.abadi);
        cv.put("isactive", true);

        mydb.insertWithOnConflict("tblclue",
                null,cv,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean isquestinareready(String qid) {

        return true;
//        boolean res = false;
//        if(qid.equals("0")) return true;
//        try {
//            String selectQuery = "select count(*) cnt from  tblQuestions a left join tblanswer b on a.QID = b.QID and a.QuestionID = b.QuestionID where a.QID = "+qid+" and b.answerid is null";
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            if (cursor.moveToFirst()) {
//                do {
//                    res = (cursor.getInt(cursor.getColumnIndex("cnt")) == 0) ;
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//            db.close();
//        } catch (Exception ex) {
//            String ss = ex.getMessage();
//        }
//        return res;
    }

    public  void deleteanswer(String ansid){
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.execSQL("delete from tblanswer where answerid = " + ansid);
            mydb.close();
        }catch(Exception exs){

        }
    }

    public  void updatecluessts(){
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.execSQL("update tblclue set  isactive = 0");
            mydb.close();
        }catch(Exception exs){

        }
    }

    public  void deleteanswer(String QID,String QnID){
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.execSQL("delete from tblanswer where QID = " + QID + " and QuestionID = " + QnID);
            mydb.close();
        }catch(Exception exs){

        }
    }
    public  void runquery(String Q){
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.execSQL(Q);
            mydb.close();
        }catch(Exception exs){
int iii = 0;
        }
    }

    public  void deleteallanswer(){
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            mydb.execSQL("delete from tblanswer");
            mydb.close();
        }catch(Exception exs){

        }
    }

    public JSONArray getanswers() {
        JSONArray data = new JSONArray();

        try {
            String selectQuery = " select * from tblanswer ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject item = new JSONObject();

                    item.put("QID", cursor.getInt(cursor.getColumnIndex("QID")));
                    item.put("UserID", cursor.getInt(cursor.getColumnIndex("UserID")));
                    item.put("answerid", cursor.getInt(cursor.getColumnIndex("answerid")));
                    item.put("QuestionID", cursor.getInt(cursor.getColumnIndex("QuestionID")));
                    item.put("AnswerData" , cursor.getString(cursor.getColumnIndex("AnswerData")));
                    item.put("AnswerMeta" , cursor.getString(cursor.getColumnIndex("AnswerMeta")));



                        data.put(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }
    public List<cluecls> getclues() {
        ArrayList<cluecls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblclue where isactive = 1";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    cluecls item = new cluecls();
                    item.id = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.title = cursor.getString(cursor.getColumnIndex("title"));
                    item.cityname = cursor.getString(cursor.getColumnIndex("cityname"));
                    item.cityid = cursor.getInt(cursor.getColumnIndex("cityid"));
                    item.clueloc = cursor.getString(cursor.getColumnIndex("clueloc"));
                    item.provinceid = cursor.getInt(cursor.getColumnIndex("provinceid"));
                    item.provincename = cursor.getString(cursor.getColumnIndex("provincename"));
                    item.cluestatus =cursor.getInt(cursor.getColumnIndex("cluestatus"));
                    item.clueplatenumbers = cursor.getInt(cursor.getColumnIndex("clueplatenumber"));
                    item.clueaddress = cursor.getString(cursor.getColumnIndex("clueaddress"));


                    item.postalcode = cursor.getString(cursor.getColumnIndex("postalcode"));
                    item.bakhsh = cursor.getString(cursor.getColumnIndex("bakhsh"));
                    item.deh = cursor.getString(cursor.getColumnIndex("deh"));
                    item.abadi = cursor.getString(cursor.getColumnIndex("abadi"));

                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }
    public List<platecls> getcplates(String clid) {
        ArrayList<platecls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblplates where clueid = "+ clid;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    platecls item = new platecls();
                    item.clueid = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.platenumber = cursor.getInt(cursor.getColumnIndex("platenumber"));
                    item.platepostalcode = cursor.getString(cursor.getColumnIndex("platepostalcode"));
                    item.userid = cursor.getInt(cursor.getColumnIndex("userid"));
                    item.platestatus = cursor.getInt(cursor.getColumnIndex("platestatus"));
                    item.plateadd = cursor.getString(cursor.getColumnIndex("plateadd"));


                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }
    public void insertplate(platecls ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("plateadd", ans.plateadd);
            cv.put("platenumber", ans.platenumber);
            cv.put("platepostalcode", ans.platepostalcode);
            cv.put("platestatus", ans.platestatus);
          //  cv.put("userid", ans.userid);
            cv.put("clueid", ans.clueid);
            mm = String.valueOf(mydb.insert("tblplates", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }

    public void insertshomaretamas(shomaretamascls ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();


            cv.put("clueid", ans.clueid);
            cv.put("platenumber", ans.platenumber);
            cv.put("noee", ans.noee);
            cv.put("shomare", ans.shomare);
            cv.put("tozihat", ans.tozihat);

//            cv.put("monaseb", ans.monaseb);
//            cv.put("hozor", ans.hozor);
//            cv.put("hozordesc", ans.hozordesc);
//            cv.put("farsi", ans.farsi);
//            cv.put("selected", ans.selected);



            mm = String.valueOf(mydb.insert("tblshomaretamas", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }
    public List<shomaretamascls> getshomaretamas(String clid,String pnum) {
        ArrayList<shomaretamascls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblshomaretamas where clueid = "+ clid +" and platenumber = "+pnum;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    shomaretamascls item = new shomaretamascls();
                    item.clueid = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.platenumber = cursor.getInt(cursor.getColumnIndex("platenumber"));
                    item.noee    = cursor.getString(cursor.getColumnIndex("noee"));
                    item.shomare = cursor.getString(cursor.getColumnIndex("shomare"));
                    item.tozihat = cursor.getString(cursor.getColumnIndex("tozihat"));
                  //  item.tarikh = cursor.getString(cursor.getColumnIndex("tarikh"));
//                    item.monaseb = cursor.getString(cursor.getColumnIndex("monaseb"));
//                    item.hozor = cursor.getString(cursor.getColumnIndex("hozor"));
//                    item.hozordesc = cursor.getString(cursor.getColumnIndex("hozordesc"));
//                    item.farsi = cursor.getString(cursor.getColumnIndex("farsi"));
                    item.shomareid = cursor.getInt(cursor.getColumnIndex("shomareid"));





                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public void inserthamahangi(hamahangicls ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();


            cv.put("clueid", ans.clueid);
            cv.put("platenumber", ans.platenumber);
            cv.put("tarikh", ans.tarikh);
            cv.put("type", ans.type);
            cv.put("nesbat", ans.nesbat);
            cv.put("natije", ans.natije);
//            cv.put("monaseb", ans.monaseb);
//            cv.put("hozor", ans.hozor);
//            cv.put("hozordesc", ans.hozordesc);
//            cv.put("farsi", ans.farsi);
//            cv.put("selected", ans.selected);



            mm = String.valueOf(mydb.insert("tblhamahangi", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }
    public List<hamahangicls> gethamahangi(String clid,String pnum) {
        ArrayList<hamahangicls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblhamahangi where clueid = "+ clid +" and platenumber = "+pnum;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    hamahangicls item = new hamahangicls();
                    item.clueid = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.platenumber = cursor.getInt(cursor.getColumnIndex("platenumber"));
                    item.type    = cursor.getString(cursor.getColumnIndex("type"));
                    item.nesbat = cursor.getString(cursor.getColumnIndex("nesbat"));
                    item.natije = cursor.getString(cursor.getColumnIndex("natije"));
                    item.tarikh = cursor.getString(cursor.getColumnIndex("tarikh"));
//                    item.monaseb = cursor.getString(cursor.getColumnIndex("monaseb"));
//                    item.hozor = cursor.getString(cursor.getColumnIndex("hozor"));
//                    item.hozordesc = cursor.getString(cursor.getColumnIndex("hozordesc"));
//                    item.farsi = cursor.getString(cursor.getColumnIndex("farsi"));
                    item.hamid = cursor.getInt(cursor.getColumnIndex("hamid"));





                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public void insertaza(azacls ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();


            cv.put("clueid", ans.clueid);
            cv.put("platenumber", ans.platenumber);
            cv.put("name", ans.name);
            cv.put("sal", ans.sal);
            cv.put("irani", ans.irani);
            cv.put("sex", ans.sex);
            cv.put("monaseb", ans.monaseb);
            cv.put("hozor", ans.hozor);
            cv.put("hozordesc", ans.hozordesc);
            cv.put("farsi", ans.farsi);
            cv.put("selected", ans.selected);



            mm = String.valueOf(mydb.insert("tblaza", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }
    public List<azacls> getaza(String clid,String pnum) {
        ArrayList<azacls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblaza where clueid = "+ clid +" and platenumber = "+pnum;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    azacls item = new azacls();
                    item.clueid = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.azaid = cursor.getInt(cursor.getColumnIndex("azaid"));
                    item.platenumber = cursor.getInt(cursor.getColumnIndex("platenumber"));
                    item.name = cursor.getString(cursor.getColumnIndex("name"));
                    item.sal = cursor.getInt(cursor.getColumnIndex("sal"));
                    item.irani = cursor.getString(cursor.getColumnIndex("irani"));
                    item.sex = cursor.getString(cursor.getColumnIndex("sex"));
                    item.monaseb = cursor.getString(cursor.getColumnIndex("monaseb"));
                    item.hozor = cursor.getString(cursor.getColumnIndex("hozor"));
                    item.hozordesc = cursor.getString(cursor.getColumnIndex("hozordesc"));
                    item.farsi = cursor.getString(cursor.getColumnIndex("farsi"));
                    item.selected = cursor.getInt(cursor.getColumnIndex("selected"));





                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public void insertmorajee(morajeeecls ans) {
        String mm;
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            // cv.put("morajeeid", ans.morajeeid);
            cv.put("clueid", ans.clueid);
            cv.put("platenumber", ans.platenumber);
            cv.put("tarikh", ans.tarikh);
            //  cv.put("userid", ans.userid);
            cv.put("vaziat", ans.vaziat);
            mm = String.valueOf(mydb.insert("tblmorajee", null, cv));
            mydb.close();
        } catch (Exception exs) {
            String ex = exs.getMessage();
            mm = "-1";
        }
        return;
    }
    public List<morajeeecls> getmorajee(String clid,String pnum) {
        ArrayList<morajeeecls> data = new ArrayList<>();

        try {
            String selectQuery = " select * from tblmorajee where clueid = "+ clid +" and platenumber = "+pnum;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    morajeeecls item = new morajeeecls();
                    item.clueid = cursor.getInt(cursor.getColumnIndex("clueid"));
                    item.platenumber = cursor.getInt(cursor.getColumnIndex("platenumber"));
                    item.tarikh = cursor.getString(cursor.getColumnIndex("tarikh"));
                    item.vaziat = cursor.getString(cursor.getColumnIndex("vaziat"));
                    item.morajeeid = cursor.getInt(cursor.getColumnIndex("morajeeid"));



                    data.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return data;
    }

    public void setuseragrement(int userid, int idx) {
        try {
            SQLiteDatabase mydb = this.getWritableDatabase();

            mydb.execSQL("delete from  ShoroPayan where UserID = "+userid);

            mydb.execSQL("INSERT INTO ShoroPayan (\n" +
                    "                           UserID,\n" +
                    "                           indexno\n" +
                    "                       )\n" +
                    "                       VALUES (\n" +
                    "                           "+userid+",\n" +
                    "                           "+idx+"\n" +
                    "                       );");
            mydb.close();
        }catch(Exception exs){
            int iii = 0;
        }
    }

    public boolean isregisteduser(int userid) {

        boolean res = false;

        try {
            String selectQuery = "select count(*) cnt from  ShoroPayan where UserID = "+userid+" and indexno = 0";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    res = (cursor.getInt(cursor.getColumnIndex("cnt")) == 0) ;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            String ss = ex.getMessage();
        }
        return res;


    }
}
