package com.jd.o2o.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.cache.Cache;

/**
 * Created by wangdongxing on 15-11-20.
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private Cache cache;

    @Override
    @Cacheable(value = "MainTest")
    public String getName(String who) {
        System.out.println("hello world");
        return "who's name is " + who;
    }

    @Override
    public String getName2(String who) {
        cache.put(who,"hihihihi");
        Object v = cache.get(who);
        return v.toString();
    }

    @Override
    public String info() {
        cache.get("hi");
        cache.put("hello world","dddd");
        return null;
    }
}
