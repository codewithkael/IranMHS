package com.topapp.malek.iranmhs;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Abolfazl on 8/3/2017.
 */

public class JSDL {
    public static String KPToken ="";
    public static String UserName ="";
    public static String Token ="";
    public static String mUserName ="";
    public static String lanid ="";
    public static String hasdt ="";
    public static JSDLData multipartRequeststatic(final String urlTo, final Map<String, String> parmas, final Map<String, String> headers, final String typ) throws Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        final JSDLData result = new JSDLData();
        result.headers = new HashMap<>();
        try {
            final URL url = new URL(urlTo);

            connection = (HttpURLConnection)url.openConnection();
            if(typ.equals("GET")){
                connection.setDoOutput(false);
            }else{
                connection.setDoOutput(true);
            }
            connection.setDoInput(true);
            connection.setUseCaches(false);

            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestMethod(typ);

            if(!Token.equals("")) {
                connection.addRequestProperty("Authorization", Token);
            }
            if(!KPToken.equals("") && !UserName.equals("")) {
                connection.addRequestProperty("KPToken", Hash(KPToken + UserName + UserName));//
            }
            for (final String keyy : headers.keySet()) {
                final String value = headers.get(keyy);
                connection.addRequestProperty(keyy, value);
            }
            if (!typ.equals( "GET")) {
                final OutputStream os = connection.getOutputStream();
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(parmas));
                writer.flush();
                writer.close();
                os.close();
            }
            final int code = connection.getResponseCode();
            String Bod = "";
            final Map<String, List<String>> mapf = connection.getHeaderFields();
            try {
                boolean first = true;
                final Iterator<String> keyss = mapf.keySet().iterator();
                while (keyss.hasNext()) {
                    if (first) {
                        first = false;
                    }
                    else {
                    }
                    final String key = keyss.next();
                    if (null != key) {
                        final List<String> value2 = mapf.get(key);
                        result.headers.put(key,value2);
                        if(key.equals("KPToken")){
                            KPToken = value2.get(0);
                        }
                    }

                }
            }
            catch (Exception ex) {}
            try {
                inputStream = connection.getInputStream();
                if (null != inputStream) {
                    Bod = convertStreamToString(inputStream);
                    inputStream.close();
                }
            }
            catch (Exception ex2) {
                result.isok = false;
            }
            try {
                inputStream = connection.getErrorStream();
                if (null != inputStream) {
                    Bod = convertStreamToString(inputStream);
                    inputStream.close();
                }
            }
            catch (Exception ex3) {
                result.isok = false;
            }
            result.rcode = code;

            result.body = Bod;
            result.isok = true;
            return result;
        }
        catch (Exception e) {
            result.rcode = 0;
            result.body = e.getMessage();
            result.isok = false;
            return result;
        }
    }
    public static String Hash(String s){
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(s.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static  JSDLData multipartRequest(String urlTo, Map<String, String> parmas, Map<String, String> headers, String typ) throws Exception {



        HttpURLConnection connection = null;

        InputStream inputStream = null;


        JSDLData result = new JSDLData();





        try {

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestMethod(typ);
            //connection.connect();
            Iterator<String> keys = headers.keySet().iterator();
            while (keys.hasNext()) {

                String keyy = keys.next();
                String value = headers.get(keyy);
                connection.addRequestProperty(keyy, value);


            }





            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(parmas));
            writer.flush();
            writer.close();
            os.close();

            int code = connection.getResponseCode();
            String hed = "";
            String Bod = "";
            String Err = "";
            String cod = String.valueOf(code);
            // if (204 == connection.getResponseCode()) {
            Map<String, List<String>> mapf = connection.getHeaderFields();

            try{
                StringBuilder resultr = new StringBuilder();
                boolean first = true;

                Iterator<String> keyss = mapf.keySet().iterator();

                while (keyss.hasNext()) {
                    if (first)
                        first = false;
                    else
                        resultr.append(" \r\n ");

                    String key = keyss.next();
                    if(null != key)
                    {
                        List<String> value = mapf.get(key);
                        resultr.append(URLEncoder.encode(key, "UTF-8"));
                        resultr.append("   =    ");
                        resultr.append(java.net.URLDecoder.decode((URLEncoder.encode(value.get(0), "UTF-8")),"UTF-8"));
                    }


                }
                resultr.append(" \r\n  _______________________________________________________________  \r\n ");

                Collection<List<String>> vals = mapf.values();

                for(List<String> ls : vals){
                    resultr.append(URLEncoder.encode(ls.get(0), "UTF-8"));
                    resultr.append(" \r\n ");
                }
//


//        }
                hed = resultr.toString();
            }catch(Exception ex){

            }

            try {
                inputStream = connection.getInputStream();
                if(null != inputStream ){
                    Bod = convertStreamToString(inputStream);
                    inputStream.close();
                }
            }catch (Exception ex) {

            }
            try {
                inputStream = connection.getErrorStream();
                if(null != inputStream ){
                    Bod = convertStreamToString(inputStream);
                    inputStream.close();
                }


            }catch (Exception ex) {

            }


            result.rcode = code;
            result.headers = connection.getHeaderFields();
            result.body = Bod;
            result.isok = true;
            return result;



            //   return  result;

            //  throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            // }







        } catch (Exception e) {
            result.body = e.getMessage();
            result.isok = false ;

            return  result;
        }

    }
    public static String[] multipartRequest(String urlTo, Map<String, String> parmas) throws Exception {



        HttpURLConnection connection = null;

        InputStream inputStream = null;


        String[] result = {"",""};





        try {

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestMethod("POST");
            //connection.connect();



            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(parmas));
            writer.flush();
            writer.close();
            os.close();


            if (200 != connection.getResponseCode()) {
                inputStream = connection.getErrorStream();
                result[0] = convertStreamToString(inputStream);
                result[1] = "F";

                return  result;

                //  throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();
            try {
                result[0] = convertStreamToString(inputStream);
            }catch (Exception ex) {

                        }

            result[1] = "200";

            inputStream.close();



            return result;
            } catch (Exception e) {
                result[0] = e.getMessage();
                result[1] = "F";

                return  result;
            }

    }
    private static String getQuery(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            if (first)
                first = false;
            else
                result.append("&");

            String key = keys.next();
            String value = params.get(key);
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value, "UTF-8"));


        }






        return result.toString();
    }
    private static String convertStreamToString(InputStream is) {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                try {
                    is.close();
                }
                catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException ex) {

            return "";
        }
    }
    public static String multipartRequest(String urlTo, String post, String filepath, String filefield) throws ParseException, IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary =  "*****"+ Long.toString(System.currentTimeMillis())+"*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] +"\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while(bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            String[] posts = post.split("&");
            int max = posts.length;
            for(int i=0; i<max;i++) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                String[] kv = posts[i].split("=");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain"+lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(kv[1]);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            inputStream = connection.getInputStream();
            result = convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch(Exception e) {
            Log.e("MultipartRequest","Multipart Form Upload Error");
            e.printStackTrace();
            return "error";
        }
    }

}
