package com.jd.o2o.enhance.localcache.mapdb;

import org.mapdb.Bind;

import javax.cache.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBListener<K,V> extends MapDBLogable implements Bind.MapListener<K,V> {

    private MapDBCache<K,V> cache;

    public MapDBListener(MapDBCache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * when a entry expired ,the listener will fire twice
     * @param key
     * @param oldVal
     * @param newVal
     */
    @Override
    public void update(K key, V oldVal, V newVal) {
        if(newVal == null){
            //remove or expired
            call(EventType.REMOVED,key,oldVal,newVal);
        }else if(oldVal == null){
            //put
            call(EventType.CREATED,key,oldVal,newVal);
        }else{
            //update
            call(EventType.UPDATED,key,oldVal,newVal);
        }
    }

    private CacheEntryEvent<K,V> createCacheEntryEvent(EventType eventType,final K key, final V oldVal,final V newVal){
        return new CacheEntryEvent<K, V>(this.cache,eventType) {
            @Override
            public V getOldValue() {
                return oldVal;
            }

            @Override
            public boolean isOldValueAvailable() {
                return oldVal != null;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return newVal;
            }

            @Override
            public <T> T unwrap(Class<T> clazz) {
                return  null;
            }
        };
    }

    private void call(EventType eventType,K key, V oldVal, V newVal){

        if(logger.isDebugEnabled()){
            logger.debug(getMapDBPrefix()+getLogCacheName(cache.getCacheId()) +
                    " fired listener , key:{}, type:{}",key,eventType);
        }

        List<CacheEntryEvent<K,V>> eventList = new ArrayList<CacheEntryEvent<K, V>>();
        eventList.add(createCacheEntryEvent(eventType,key,oldVal,newVal));

        Iterator<CacheEntryListener<K,V>> iterator = cache.getListeners().iterator();
        while (iterator.hasNext()){
            CacheEntryListener<K,V> listener = iterator.next();

            if(eventType == EventType.CREATED && listener instanceof CacheEntryCreatedListener){
                ((CacheEntryCreatedListener) listener).onCreated(eventList);
            }
            if(eventType == EventType.UPDATED && listener instanceof CacheEntryUpdatedListener){
                ((CacheEntryUpdatedListener) listener).onUpdated(eventList);
            }
            if(eventType == EventType.REMOVED && listener instanceof CacheEntryRemovedListener){
                ((CacheEntryRemovedListener) listener).onRemoved(eventList);
            }
            if(eventType == EventType.EXPIRED && listener instanceof CacheEntryExpiredListener){
                ((CacheEntryExpiredListener) listener).onExpired(eventList);
            }
        }

    }

}
