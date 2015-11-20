package com.jd.o2o.enhance.zk;

/**
 * Created by wangdongxing on 15-3-4.
 */
public class LocalCacheZKListener implements ZookeeperListener {

    @Override
    public void onCreate(String path, byte[] data) {

    }

    @Override
    public void onUpdate(String path, byte[] data) {
        String str = new String(data);
        if(str.equals("on")){
            //启动本地缓存
        }else if(str.equals("off")){
            //关闭本地缓存
        }
    }

    @Override
    public void onRemove(String path) {

    }
}
