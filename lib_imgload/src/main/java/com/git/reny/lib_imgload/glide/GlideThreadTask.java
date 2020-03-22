package com.git.reny.lib_imgload.glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by reny on 2017/8/10.
 */

public class GlideThreadTask {

    private static ExecutorService singleExecutor = null;

    public static void addTask(Runnable runnable){
        if (null == singleExecutor) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.submit(runnable);
    }

    public static void clearTask(){
        if (null != singleExecutor && !singleExecutor.isShutdown()) {
            try {
                singleExecutor.shutdown();
            }catch (Exception e){e.printStackTrace();}
        }
    }

}
