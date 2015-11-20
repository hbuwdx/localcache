package com.jd.o2o.enhance.localcache.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class CacheFactoryBean<K,V> implements FactoryBean,DisposableBean {

    private Cache<K,V> cache;

    private String cacheName;

    private Configuration<K,V> configuration;

    @Override
    public void destroy() throws Exception {
        if(cache != null){
            cache.close();
        }
    }

    @Override
    public Object getObject() throws Exception {
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cache = cacheManager.createCache(cacheName,configuration);
        return cache;
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public void setConfiguration(Configuration<K, V> configuration) {
        this.configuration = configuration;
    }
}
