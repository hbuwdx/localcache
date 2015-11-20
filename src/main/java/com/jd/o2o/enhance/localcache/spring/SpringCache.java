package com.jd.o2o.enhance.localcache.spring;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class SpringCache implements Cache {

    private javax.cache.Cache<Object,Object> cache;

    public SpringCache(javax.cache.Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = cache.get(key);
        if(value == null) return null;
        return new SimpleValueWrapper(value);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key,value);
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

}
