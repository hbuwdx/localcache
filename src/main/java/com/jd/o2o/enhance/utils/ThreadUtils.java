package com.jd.o2o.enhance.utils;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class ThreadUtils {

    public static void sleep(Long sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

    public static String getName(){
        return Thread.currentThread().getName();
    }

    public static Thread startNewThread(String name,Runnable runnable){
        Thread thread =  new Thread(runnable,name);
        thread.start();
        return thread;
    }
}
