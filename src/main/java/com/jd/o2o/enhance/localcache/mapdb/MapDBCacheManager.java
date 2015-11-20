package com.jd.o2o.enhance.localcache.mapdb;

import com.jd.o2o.enhance.utils.StringUtils;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBCacheManager implements CacheManager {

    private MapDBCacheProvider cacheProvider;

    private ConcurrentHashMap<String,MapDBCache> cacheMap = new ConcurrentHashMap<String, MapDBCache>();

    private MapDBFactory mapdbFactory = MapDBFactory.getInstance();

    public MapDBCacheManager(){

    }

    public MapDBCacheManager(MapDBCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    @Override
    public CachingProvider getCachingProvider() {
        return this.cacheProvider;
    }

    @Override
    public URI getURI() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {

        if(StringUtils.isEmpty(cacheName))
            throw new IllegalArgumentException("argument [cacheName] required !");

        if(!(configuration instanceof MapDBCacheConfiguration))
            throw new IllegalArgumentException("argument [configuration] must be a instance of class["+MapDBCacheConfiguration.class+"]!");

        MapDBCache<K,V> cache = mapdbFactory.createMapdbCache(cacheName,(MapDBCacheConfiguration) configuration);
        cache.setCacheManager(this);
        cacheMap.put(cacheName,cache);
        return cache;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        return getCache(cacheName);
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) {
        if(StringUtils.isEmpty(cacheName)) return null;
        return cacheMap.get(cacheName);
    }

    @Override
    public Iterable<String> getCacheNames() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return cacheMap.keySet().iterator();
            }
        };
    }

    @Override
    public void destroyCache(String cacheName) {
        if(StringUtils.isEmpty(cacheName))
            throw new IllegalArgumentException("argument [cacheName] required !");
        Cache cache = cacheMap.get(cacheName);
        cache.close();
        cacheMap.remove(cacheName);
    }

    @Override
    public void enableManagement(String cacheName, boolean enabled) {

    }

    @Override
    public void enableStatistics(String cacheName, boolean enabled) {

    }

    @Override
    public void close() {
        if(cacheMap.isEmpty()) return;

        Iterator<MapDBCache> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()){
            iterator.next().close();
        }
    }

    @Override
    public boolean isClosed() {
        if(cacheMap.isEmpty()) return true;

        Iterator<MapDBCache> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()){
            if(!iterator.next().isClosed()){
                return false;
            }
        }
        return true;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return null;
    }
}
