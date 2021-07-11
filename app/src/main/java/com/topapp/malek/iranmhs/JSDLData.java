package com.topapp.malek.iranmhs;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class JSDLData {
    public JSDLData(){

    }
    public Map<String, List<String>> headers;
    public String body;
    public int rcode;
    public boolean isok;
    public String type;
    public String getBody(String key){
        String res = "";
        try {
            JSONObject jObject = new JSONObject(body);
            res = jObject.getString(key);
        }
        catch (Exception ex){
             res = ex.getMessage();
        }
        finally {
            return res;
        }
    }
}
