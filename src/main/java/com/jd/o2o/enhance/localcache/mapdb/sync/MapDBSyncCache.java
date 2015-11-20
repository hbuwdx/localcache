package com.jd.o2o.enhance.localcache.mapdb.sync;

import com.jd.o2o.enhance.localcache.mapdb.MapDBCache;
import com.jd.o2o.enhance.localcache.mapdb.MapDBCacheConfiguration;
import org.mapdb.HTreeMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class MapDBSyncCache<K,V> extends MapDBCache<K,V> {

    private MapDBSyncManager<K,V> syncManager;

    public MapDBSyncCache(String cacheId, String name, MapDBCacheConfiguration<K, V> configuration, HTreeMap<K, V> treeMap) {
        super(cacheId, name, configuration, treeMap);
        this.syncManager = new MapDBSyncManager<K, V>(this,configuration.getMapdbSyncMQService());
    }

    @Override
    public void put(K key, V value) {
        super.put(key, value);
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_PUT, key, value, getCacheId()));
    }

    @Override
    public V getAndPut(K key, V value) {
        V ret =  super.getAndPut(key, value);
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_PUT, key, value, getCacheId()));
        return ret;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        super.putAll(map);
        for(K key : map.keySet()){
            syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_PUT, key, map.get(key), getCacheId()));
        }
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        boolean ret =  super.putIfAbsent(key, value);
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_PUT, key, value, getCacheId()));
        return ret;
    }

    @Override
    public boolean remove(K key) {
        boolean ret =  super.remove(key);
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_REMOVE, key, null, getCacheId()));
        return ret;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        boolean ret = super.remove(key, oldValue);
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_REMOVE, key, oldValue, getCacheId()));
        return ret;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        put(key,newValue);
        return true;
    }

    @Override
    public boolean replace(K key, V value) {
        put(key,value);
        return true;
    }

    @Override
    public V getAndReplace(K key, V value) {
        V ret =  super.getAndReplace(key, value);
        put(key,value);
        return ret;
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        super.removeAll(keys);
        for(K key : keys){
            syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_REMOVE, key, null, getCacheId()));
        }
    }

    @Override
    public void removeAll() {
        super.removeAll();
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_CLEAR, null, null, getCacheId()));
    }

    @Override
    public void clear() {
        super.clear();
        syncManager.publish(MapDBSyncMsg.build(MapDBSyncMsg.TYPE_CLEAR, null, null, getCacheId()));
    }

    /**
     * 不需要同步的接口
     * @param key
     * @param value
     */
    public void superPut(K key, V value){
        super.put(key,value);
    }

    public void superRemove(K key){
        super.remove(key);
    }

    public void superClear(){
        super.clear();
    }
}
