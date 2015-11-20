package com.jd.o2o.enhance.runtime.shutdown;

/**
 * Created by wangdongxing on 15-11-5.
 */
public class Hook {

    private boolean keepRunning = true;

    private Thread thread;

    public Hook(Thread thread){
        this.thread = thread;
    }

    public boolean isKeepRunning(){
        return keepRunning;
    }

    public void shutdown(){
        keepRunning = false;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }
}
