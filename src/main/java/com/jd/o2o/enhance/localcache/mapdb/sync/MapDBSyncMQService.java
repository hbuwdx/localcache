package com.jd.o2o.enhance.localcache.mapdb.sync;

import com.jd.o2o.enhance.localcache.mapdb.MapDBContext;
import com.jd.o2o.enhance.localcache.mapdb.MapDBException;
import com.jd.o2o.enhance.localcache.mapdb.MapDBLogable;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangdongxing on 15-11-20.
 */
public abstract class MapDBSyncMQService extends MapDBLogable implements Closeable {

    private Map<String,MapDBSyncManager> syncManagers =
            new ConcurrentHashMap<String, MapDBSyncManager>();

    private String channel;

    protected MapDBSyncMQService(String channel) {
        this.channel = channel;
    }

    public abstract void publish(MapDBSyncMsg msg);

    public void consumer(MapDBSyncMsg msg){
        if(msg == null) return;
        String cacheName = MapDBContext.getCacheName(msg.getCacheId());
        MapDBSyncManager manager = syncManagers.get(cacheName);
        if(manager == null) return;
        manager.offer(msg);
    }

    public void registerSyncManager(String cacheName,MapDBSyncManager syncManager){
        if(syncManagers.containsKey(cacheName))
            throw new MapDBException("cache ["+cacheName+"] is already registered!");
        syncManagers.put(cacheName,syncManager);
    }

    public MapDBSyncManager getSyncManager(String cacheName){
        return syncManagers.get(cacheName);
    }

    public String getChannel() {
        return channel;
    }
}
