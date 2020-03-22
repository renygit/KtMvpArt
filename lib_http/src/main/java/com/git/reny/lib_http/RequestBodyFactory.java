package com.git.reny.lib_http;

import com.git.reny.lib_http.core.AbstractRequestBodyFactory;
import com.git.reny.lib_http.core.RequestObj;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.internal.Util;

/***
 * POST 或上传文件  时使用的类
 */
public class RequestBodyFactory extends AbstractRequestBodyFactory {

    @Override
    protected JSONObject parseToJSONObject(Object obj) {
        if(obj == null){
            return null;
        }
        if(obj instanceof JSONObject){
            return (JSONObject)obj;
        }else {
            try {
                return new JSONObject(RequestObj.gson.toJson(obj));
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }


    //如果需要压缩数据  可以重写
    @Override
    protected byte[] getRequestByte(JSONObject jsonObject) {
        return jsonObject.toString().getBytes(Util.UTF_8);
    }


    //new MultipartBody.Builder().setType(MultipartBody.FORM)
    // .addFormDataPart(uploadFileKey(file),
    //                  uploadFileName(file),
    //                  RequestBody.create(MediaType.parse(parseMimeType(fileNme)), file)
    @Override
    protected String uploadFileKey(File file) {
        return "file";
    }

    @Override
    protected String uploadFileName(File file) {
        return file.getName();
    }

    @Override
    protected String parseMimeType(String fileName) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

}
