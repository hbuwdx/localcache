package com.jd.o2o.enhance.localcache.mapdb;

import com.jd.o2o.enhance.localcache.mapdb.sync.MapDBSyncCache;
import org.mapdb.DB;
import org.mapdb.HTreeMap;


/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBFactory {

    private static MapDBFactory instance = null;

    private MapDBContext context;

    private MapDBFactory(){
        this.context = new MapDBContext();
    }

    public static synchronized MapDBFactory getInstance(){
        if(instance == null){
            instance = new MapDBFactory();
        }
        return instance;
    }

    public <K,V> MapDBCache<K,V> createMapdbCache(String cacheName,MapDBCacheConfiguration<K,V> configuration){
        DB db = context.getDb(configuration.getStoreType());
        HTreeMap<K,V> treeMap = db.hashMapCreate(cacheName)
                .keySerializer(db.getDefaultSerializer())
                .valueSerializer(db.getDefaultSerializer())
                .counterEnable()
                .expireStoreSize(configuration.getStoreSizeByG())
                .expireAfterWrite(configuration.getExpireTime(), configuration.getExpireTimeUnit())
                .makeOrGet();

        String cacheId = MapDBContext.getCacheId(cacheName);
        MapDBCache<K,V> cache = null;
        if(configuration.isSyncEnabled() && configuration.getMapdbSyncMQService() != null){
            cache = new MapDBSyncCache<K, V>(cacheId,cacheName,configuration,treeMap);
        }else{
            cache = new MapDBCache<K, V>(cacheId,cacheName,configuration,treeMap);
        }
        return cache;
    }

}
