package com.git.reny.lib_http.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

public abstract class AbstractRequestBodyFactory implements IRequestBodyFactory {

    private static final MediaType MT_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MT_FORM = MultipartBody.FORM;

    private Map<String, RequestObj> reqMap;

    @Override
    public RequestObj getReq(String key) {
        if(reqMap == null){
            reqMap = new HashMap<>();
        }
        if(reqMap.containsKey(key) && reqMap.get(key) != null){
            return reqMap.get(key);
        }
        RequestObj obj = new RequestObj();
        reqMap.put(key, obj);
        return obj;
    }

    @Override
    public RequestBody build(Object req) {
        JSONObject jsonObject = parseToJSONObject(req);
        RequestBody requestBody = null;
        if(null != jsonObject) {
            try {
                requestBody = RequestBody.create(MT_JSON, getRequestByte(jsonObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return requestBody;
    }

    //这里req只能是<String, String>  如果有其它需求  需要重写这段代码
    @Override
    public RequestBody build(Object req, List<File> files) {
        JSONObject jsonObject = parseToJSONObject(req);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MT_FORM);
        if(null != jsonObject) {
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                try {
                    String value = jsonObject.getString(key);
                    builder.addFormDataPart(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < files.size(); i++) { //对文件进行遍历
            //根据文件的后缀名，获得文件类型
            File file = files.get(i);
            if(file != null) {
                String fileName = file.getName();
                String fileType = parseMimeType(fileName);
                builder.addFormDataPart( //给Builder添加上传的文件
                        uploadFileKey(file),  //请求的名字
                        uploadFileName(file), //文件的文字，服务器端用来解析的
                        RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
                );
            }
        }
        return builder.build();
    }

    protected abstract JSONObject parseToJSONObject(Object obj);

    protected abstract byte[] getRequestByte(JSONObject jsonObject);

    protected abstract String uploadFileKey(File file);
    protected abstract String uploadFileName(File file);
    protected abstract String parseMimeType(String fileName);

}
