package com.jd.o2o.enhance.localcache.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import javax.cache.CacheManager;
import javax.cache.Caching;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class CacheManagerFactoryBean implements FactoryBean,DisposableBean {

    private CacheManager cacheManager;

    @Override
    public void destroy() throws Exception {
        if(this.cacheManager != null){
            cacheManager.close();
        }
    }

    @Override
    public Object getObject() throws Exception {
        cacheManager = Caching.getCachingProvider().getCacheManager();
        return cacheManager;
    }

    @Override
    public Class<?> getObjectType() {
        return CacheManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
