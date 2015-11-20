package com.jd.o2o.enhance.localcache.spring;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class SpringCacheManager extends AbstractCacheManager {


    private Collection<? extends Cache> caches;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;
    }
}
