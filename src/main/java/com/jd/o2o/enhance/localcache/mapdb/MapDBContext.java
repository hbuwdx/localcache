package com.jd.o2o.enhance.localcache.mapdb;

import com.jd.o2o.enhance.runtime.JvmUtils;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBContext {

    private String ip;
    private String pid;
    private DB heapDB;
    private DB memoryDB;
    private DB memoryDirectDB;

    public String getIp() {
        if(ip == null){
            ip = JvmUtils.IP;
        }
        return ip;
    }


    public String getPid() {
        if(pid == null){
            pid = JvmUtils.PID;
        }
        return pid;
    }

    public DB getDb(MapDBStoreType storeType) {
        if(MapDBStoreType.HEAP == storeType){
            return getHeapDB();
        }else if(MapDBStoreType.IN_MEMORY == storeType){
            return getMemoryDB();
        }else if(MapDBStoreType.MEMORY_DIRECT == storeType){
            return getMemoryDirectDB();
        }else{
            throw new MapDBException("not supported store type ["+storeType+"]");
        }
    }

    private DB createDB(DBMaker.Maker maker){
        return maker == null ? null : maker.transactionDisable()
                .cacheLRUEnable()
                .cacheExecutorEnable()
                .closeOnJvmShutdown()
                .make();
    }

    public DB getHeapDB() {
        if(heapDB == null){
            heapDB = createDB(DBMaker.heapDB());
        }
        return heapDB;
    }

    public DB getMemoryDB() {
        if(memoryDB == null){
            memoryDB = createDB(DBMaker.memoryDB());
        }
        return memoryDB;
    }

    public DB getMemoryDirectDB() {
        if(memoryDirectDB == null){
            memoryDirectDB = createDB(DBMaker.memoryDirectDB());
        }
        return memoryDirectDB;
    }

    /**
     * 123@127.0.0.1:/cacheName
     * @param cacheName
     * @return
     */
    public static String getCacheId(String cacheName){
        return JvmUtils.PID +"@"+JvmUtils.IP+":/"+cacheName;
    }

    public static String getCacheName(String cacheId){
        return cacheId.substring(cacheId.lastIndexOf(":/")+2);
    }

}
