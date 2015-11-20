package com.jd.o2o.enhance.zk;

import java.util.List;

/**
 * Created by wangdongxing on 15-10-26.
 */
public class ZookeeperPathBind {

    private String path;

    private List<ZookeeperListener> pathListenerList;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ZookeeperListener> getPathListenerList() {
        return pathListenerList;
    }

    public void setPathListenerList(List<ZookeeperListener> pathListenerList) {
        this.pathListenerList = pathListenerList;
    }
}
