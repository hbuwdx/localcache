package com.jd.o2o.enhance.localcache.mapdb.sync;

import java.io.Serializable;

/**
 * Created by wangdongxing on 15-10-23.
 */
public class MapDBSyncMsg implements Serializable{

    private static final long serialVersionUID = 2071052294255443249L;

    public static final int TYPE_PUT = 1;
    public static final int TYPE_REMOVE = 2;
    public static final int TYPE_CLEAR = 3;

    private int type; //1:put 2:remove 3:CLEAR
    private Object key;
    private Object value;
    private String cacheId;

    public MapDBSyncMsg(int type, Object key, Object value, String cacheId) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.cacheId = cacheId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCacheId() {
        return cacheId;
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }

    public static MapDBSyncMsg build(int type,Object key,Object value,String cacheId){
        return new MapDBSyncMsg(type,key,value,cacheId);
    }

    @Override
    public String toString() {
        return "MapDBSyncMsg{" +
                "type=" + type +
                ", key='" + key + '\'' +
                ", value=" + value +
                ", cacheId='" + cacheId + '\'' +
                '}';
    }
}
