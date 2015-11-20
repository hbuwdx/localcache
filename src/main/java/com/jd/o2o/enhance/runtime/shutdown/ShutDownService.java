package com.jd.o2o.enhance.runtime.shutdown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdongxing on 15-11-5.
 */
public class ShutDownService {

    private List<Hook> hooks;

    public ShutDownService(){
        hooks = new ArrayList<Hook>();
        ShutdownDaemonHook shutdownDaemonHook = new ShutdownDaemonHook();
        Runtime.getRuntime().addShutdownHook(shutdownDaemonHook);
    }

    public Hook addHook(Thread thread){
        thread.setDaemon(true);
        Hook retVal = new Hook(thread);
        hooks.add(retVal);
        return retVal;
    }

    protected class ShutdownDaemonHook extends Thread {
        @Override
        public void run() {
            for (Hook hook : hooks) {
                hook.shutdown();
            }
        }
    }
}
