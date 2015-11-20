package com.jd.o2o.enhance.zk;

import com.jd.registry.RegistryException;
import com.jd.registry.listener.ConnectionEvent;
import com.jd.registry.listener.ConnectionListener;
import com.jd.registry.listener.PathEvent;
import com.jd.registry.listener.PathListener;
import com.jd.registry.util.URL;
import com.jd.registry.zookeeper.ZKRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdongxing on 15-3-4.
 */
public class ZookeeperWatcher {

    private static final Logger logger =
            LoggerFactory.getLogger(ZookeeperWatcher.class);

    private String zkAddresses;

    private ZKRegistry registry;

    private boolean started = false;

    private List<ZookeeperPathBind> zookeeperPathBindList;

    private Map<String,List<ZookeeperListener>> listenerMap =
            new HashMap<String, List<ZookeeperListener>>();


    public ZookeeperWatcher(String zkAddresses,List<ZookeeperPathBind> zookeeperPathBindList) {
        this.zkAddresses = zkAddresses;
        this.zookeeperPathBindList = zookeeperPathBindList;
        start();
    }

    private PathListener pathListener = new PathListener() {
        @Override
        public void onEvent(PathEvent event) {
            logger.debug(
                    "received children event:type={},path={},data={}",
                    new Object[]{
                            event.getType(),
                            event.getPath(),
                            event.getData() == null ? "" : new String(event
                                    .getData())});

            if(listenerMap.size() == 0) return;
            List<ZookeeperListener> listeners = listenerMap.get(event.getPath());
            if(listeners == null || listeners.size() == 0) return;
            switch (event.getType()) {
                case CREATED: {
                    for(ZookeeperListener listener:listeners){
                        try {
                            listener.onCreate(event.getPath(), event.getData());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    break;
                }
                case UPDATED: {
                    for(ZookeeperListener listener:listeners){
                        try {
                            listener.onUpdate(event.getPath(),
                                    event.getData());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    break;
                }
                case REMOVED: {
                    for(ZookeeperListener listener:listeners){
                        try {
                            listener.onRemove(event.getPath());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    break;
                }
            }
        }
    };

    public void start(){
        if (started) {
            return;
        }
        registry = new ZKRegistry();
        registry.setUrl(URL.valueOf(zkAddresses));
        registry.addListener(new ConnectionListener() {
            public void onEvent(ConnectionEvent event) {
                logger.debug("received connect event: type={}", event.getType());
            }
        });

        try {
            registry.open();
            started = true;
        } catch (RegistryException e) {
            logger.error("Failed to started the Zookeeper client for "
                    + zkAddresses);
            return;
        }
        //注册路径的监听
        copyPathBindToListenerMap();

        for(String path:this.listenerMap.keySet()){
            registry.addListener(path,pathListener);
        }
    }

    private void copyPathBindToListenerMap(){
        for(ZookeeperPathBind bind : zookeeperPathBindList){
            this.listenerMap.put(bind.getPath(),bind.getPathListenerList());
        }
    }

    public void setZookeeperPathBindList(List<ZookeeperPathBind> zookeeperPathBindList) {
        this.zookeeperPathBindList = zookeeperPathBindList;
    }

    public String getZkAddresses() {
        return zkAddresses;
    }

    public void setZkAddresses(String zkAddresses) {
        this.zkAddresses = zkAddresses;
    }

    public ZKRegistry getRegistry() {
        return registry;
    }
}
