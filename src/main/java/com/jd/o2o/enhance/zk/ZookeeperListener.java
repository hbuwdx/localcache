package com.jd.o2o.enhance.zk;

/**
 * Created by wangdongxing on 15-3-4.
 */
public interface ZookeeperListener {

    void onCreate(String path, byte[] data);

    void onUpdate(String path, byte[] data);

    void onRemove(String path);
}
