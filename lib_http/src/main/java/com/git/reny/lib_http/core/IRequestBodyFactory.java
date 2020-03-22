package com.git.reny.lib_http.core;

import java.io.File;
import java.util.List;

import okhttp3.RequestBody;

public interface IRequestBodyFactory {

    RequestObj getReq(String key);
    RequestBody build(Object req);//非文件RequestBody，默认为是JSON POST
    RequestBody build(Object req, List<File> files);

}
