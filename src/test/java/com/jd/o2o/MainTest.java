package com.jd.o2o;

import com.jd.o2o.enhance.localcache.mapdb.MapDBCacheConfiguration;
import com.jd.o2o.enhance.localcache.mapdb.sync.RedisMQClient;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class MainTest {
    public static void main(String[] args) {

        RedisMQClient client = null;

        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();

        String cacheName = "testCache";
        Configuration<String,Stu> configuration = new MapDBCacheConfiguration<String, Stu>(
                String.class,Stu.class,true,client
        );
        Cache<String,Stu> cache = cacheManager.createCache(cacheName,configuration);

        List<Stu> list = new ArrayList<Stu>();
        list.add(buildStu());
        list.add(buildStu());
        Map<Stu,Stu> map = new HashMap<Stu,Stu>();
        map.put(buildStu(),buildStu());
        map.put(buildStu(),buildStu());
        map.put(buildStu(),buildStu());

        cache.put("key1",buildStu());
        cache.put("key2",buildStu());
        cache.put("key3", buildStu());
        cache.put("key2", buildStu());

        Object v = cache.get("key2");
        v = cache.get("key5");
        v = cache.get("key6");
        System.out.println(v);



    }

    public static Stu buildStu(){
        Stu stu = new Stu();
        stu.setAge(10);
        stu.setName("stu");
        return stu;
    }
}
