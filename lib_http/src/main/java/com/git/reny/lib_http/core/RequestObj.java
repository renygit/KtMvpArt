package com.git.reny.lib_http.core;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/***
 * JSONObject的包装类  方便链式设置POST RequestBody 数据
 */
public class RequestObj extends JSONObject {

    public static final Gson gson = new Gson();

    public RequestObj set(String k, Object v){
        try {
            if(v instanceof List){
                v =  new JSONArray(gson.toJson(v));
            }
            this.put(k, v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

}
