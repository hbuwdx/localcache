package com.jd.o2o.enhance.zk;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

/**
 * Created by wangdongxing on 15-3-4.
 */
public class LogLevelZKListener implements ZookeeperListener {

    @Override
    public void onCreate(String path, byte[] data) {

    }

    @Override
    public void onUpdate(String path, byte[] data) {
        String levelStr = new String(data);
        Level level = LogManager.getRootLogger().getLevel();
        if(levelStr.toUpperCase().equals("OFF")){
            level = Level.OFF;
        }else if(levelStr.toUpperCase().equals("FATAL")){
            level = Level.FATAL;
        }else if(levelStr.toUpperCase().equals("ERROR")){
            level = Level.ERROR;
        }else if(levelStr.toUpperCase().equals("WARN")){
            level = Level.WARN;
        }else if(levelStr.toUpperCase().equals("INFO")){
            level = Level.INFO;
        }else if(levelStr.toUpperCase().equals("DEBUG")){
            level = Level.DEBUG;
        }else if(levelStr.toUpperCase().equals("TRACE")){
            level = Level.TRACE;
        }else if(levelStr.toUpperCase().equals("ALL")){
            level = Level.ALL;
        }
        LogManager.getRootLogger().setLevel(level);
    }

    @Override
    public void onRemove(String path) {

    }
}
