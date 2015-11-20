package com.jd.o2o.enhance.localcache.mapdb;

import org.mapdb.HTreeMap;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.event.CacheEntryListener;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.util.*;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBCache<K,V> extends MapDBLogable implements Cache<K,V>{

    private String cacheId;

    private String name;

    private MapDBCacheConfiguration<K,V> configuration;

    private HTreeMap<K,V> treeMap;

    private boolean isClosed = false;

    private MapDBCacheManager cacheManager;

    private MapDBListener<K,V> mapdbListener;

    private List<CacheEntryListener<K,V>> listeners;

    public MapDBCache(String cacheId, String name, MapDBCacheConfiguration<K, V> configuration, HTreeMap<K, V> treeMap) {
        this.cacheId = cacheId;
        this.name = name;
        this.configuration = configuration;
        this.treeMap = treeMap;
        this.mapdbListener = new MapDBListener<K, V>(this);
        this.treeMap.modificationListenerAdd(this.mapdbListener);
        this.listeners = new Vector<CacheEntryListener<K, V>>();

    }

    public void setCacheManager(MapDBCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public List<CacheEntryListener<K, V>> getListeners() {
        return listeners;
    }

    public String getCacheId() {
        return cacheId;
    }

    @Override
    public V get(K key) {
        return treeMap.get(key);
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        Map<K,V> map = new HashMap<K, V>();
        if(keys == null || keys.size() == 0) return map;

        for(K key : keys){
            map.put(key,treeMap.get(key));
        }
        return map;
    }

    @Override
    public boolean containsKey(K key) {
        return treeMap.containsKey(key);
    }

    @Override
    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, CompletionListener completionListener) {
        throw new UnsupportedOperationException("not support method[loadAll]");
    }

    @Override
    public void put(K key, V value) {
        treeMap.put(key,value);
    }

    @Override
    public V getAndPut(K key, V value) {
        V v = treeMap.get(key);
        treeMap.put(key,value);
        return v;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        treeMap.putAll(map);
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        put(key,value);
        return true;
    }

    @Override
    public boolean remove(K key) {
        return treeMap.remove(key) != null;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        return treeMap.remove(key,oldValue);
    }

    @Override
    public V getAndRemove(K key) {
        V v = treeMap.get(key);
        treeMap.remove(key);
        return v;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return treeMap.replace(key,oldValue,newValue);
    }

    @Override
    public boolean replace(K key, V value) {
        return treeMap.replace(key,value) != null;
    }

    @Override
    public V getAndReplace(K key, V value) {
        V v = treeMap.get(key);
        treeMap.replace(key,value);
        return v;
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        if(keys == null || keys.size() == 0) return;
        for(K key : keys){
            treeMap.remove(key);
        }
    }

    @Override
    public void removeAll() {
        Set<K> keys = treeMap.keySet();
        removeAll(keys);
    }

    @Override
    public void clear() {
        treeMap.clear();
    }

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        throw new UnsupportedOperationException("not support method[getConfiguration]");
    }

    @Override
    public <T> T invoke(K key, EntryProcessor<K, V, T> entryProcessor, Object... arguments) throws EntryProcessorException {
        throw new UnsupportedOperationException("not support method[invoke]");
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        throw new UnsupportedOperationException("not support method[invokeAll]");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override
    public void close() {
        if(!isClosed){
            treeMap.modificationListenerRemove(this.mapdbListener);
            treeMap.close();
            isClosed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return null;
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        CacheEntryListener listener = cacheEntryListenerConfiguration.getCacheEntryListenerFactory().create();
        listeners.add(listener);
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        CacheEntryListener listener = cacheEntryListenerConfiguration.getCacheEntryListenerFactory().create();
        listeners.remove(listener);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new UnsupportedOperationException("not support method[iterator]");
    }

}
