package com.jd.o2o.enhance.localcache.mapdb;

import com.jd.o2o.enhance.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class MapDBLogable {

    public final static Logger logger = LoggerFactory.getLogger("MapDB-LOGGER");

    public String getMapDBPrefix(){
        return "[MapDB]";
    }

    public String getLogCacheName(String cacheId){
        return "[Cache-"+cacheId+"]";
    }

    public String getLogThreadName(){
        return "[Thread-"+ ThreadUtils.getName()+"]";
    }

}
