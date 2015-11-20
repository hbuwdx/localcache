package com.jd.o2o.enhance.localcache.mapdb;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBCacheProvider implements CachingProvider {

    private MapDBCacheManager cacheManager;

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return createCacheManager();
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return null;
    }

    @Override
    public URI getDefaultURI() {
        return null;
    }

    @Override
    public Properties getDefaultProperties() {
        return null;
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return createCacheManager();
    }

    @Override
    public CacheManager getCacheManager() {
        return createCacheManager();
    }

    @Override
    public void close() {
        cacheManager.close();
    }

    @Override
    public void close(ClassLoader classLoader) {

    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {

    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return true;
    }

    private MapDBCacheManager createCacheManager(){
        if(this.cacheManager == null){
            this.cacheManager = new MapDBCacheManager(this);
        }
        return this.cacheManager;
    }
}
