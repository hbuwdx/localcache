package com.jd.o2o.enhance.localcache.mapdb;

import com.jd.o2o.enhance.localcache.mapdb.sync.MapDBSyncMQService;

import javax.cache.configuration.Configuration;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBCacheConfiguration<K,V> implements Configuration<K,V> {

    private final static double STORESIZEBYG_DEFAULT = 0.1;
    private final static long EXPIRETIME_DEFAULT = 30;
    private final static TimeUnit EXPIRETIMEUNIT_DEFAULT = TimeUnit.SECONDS;
    private final static MapDBStoreType STORETYPE_DEFAULT = MapDBStoreType.MEMORY_DIRECT;
    private final static boolean SYNCENABLED_DEFAULT = false;

    private Class<K> keyClazz;
    private Class<V> valueClazz;

    private double storeSizeByG;
    private long expireTime;
    private TimeUnit expireTimeUnit;
    private MapDBStoreType storeType;
    private boolean syncEnabled;
    private MapDBSyncMQService mapdbSyncMQService;

    public MapDBCacheConfiguration() {

    }

    public MapDBCacheConfiguration(Class<K> keyClazz, Class<V> valueClazz) {
        this.keyClazz = keyClazz;
        this.valueClazz = valueClazz;
    }

    public MapDBCacheConfiguration(Class<K> keyClazz, Class<V> valueClazz, boolean syncEnabled, MapDBSyncMQService mapdbSyncMQService) {
        this.keyClazz = keyClazz;
        this.valueClazz = valueClazz;
        this.syncEnabled = syncEnabled;
        this.mapdbSyncMQService = mapdbSyncMQService;
    }

    public MapDBCacheConfiguration(Class<K> keyClazz, Class<V> valueClazz,
                                   double storeSizeByG, long expireTime,
                                   TimeUnit expireTimeUnit, MapDBStoreType storeType,
                                   boolean syncEnabled, MapDBSyncMQService mapdbSyncMQService) {
        this.keyClazz = keyClazz;
        this.valueClazz = valueClazz;
        this.storeSizeByG = storeSizeByG;
        this.expireTime = expireTime;
        this.expireTimeUnit = expireTimeUnit;
        this.storeType = storeType;
        this.syncEnabled = syncEnabled;
        this.mapdbSyncMQService = mapdbSyncMQService;
    }

    @Override
    public Class<K> getKeyType() {
        return this.keyClazz;
    }

    @Override
    public Class<V> getValueType() {
        return this.valueClazz;
    }

    @Override
    public boolean isStoreByValue() {
        return false;
    }

    public double getStoreSizeByG() {
        return storeSizeByG <= 0 ? STORESIZEBYG_DEFAULT : storeSizeByG;
    }

    public long getExpireTime() {
        return expireTime <= 0 ? EXPIRETIME_DEFAULT : expireTime;
    }

    public TimeUnit getExpireTimeUnit() {
        return expireTimeUnit == null ? EXPIRETIMEUNIT_DEFAULT : expireTimeUnit;
    }

    public MapDBStoreType getStoreType() {
        return storeType == null ? STORETYPE_DEFAULT : storeType;
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public MapDBSyncMQService getMapdbSyncMQService() {
        return mapdbSyncMQService;
    }

    public void setKeyClazz(Class<K> keyClazz) {
        this.keyClazz = keyClazz;
    }

    public void setValueClazz(Class<V> valueClazz) {
        this.valueClazz = valueClazz;
    }

    public void setStoreSizeByG(double storeSizeByG) {
        this.storeSizeByG = storeSizeByG;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public void setExpireTimeUnit(TimeUnit expireTimeUnit) {
        this.expireTimeUnit = expireTimeUnit;
    }

    public void setStoreType(MapDBStoreType storeType) {
        this.storeType = storeType;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public void setMapdbSyncMQService(MapDBSyncMQService mapdbSyncMQService) {
        this.mapdbSyncMQService = mapdbSyncMQService;
    }
}
