package com.jd.o2o.enhance.localcache.mapdb.sync;

import com.jd.o2o.enhance.localcache.mapdb.MapDBLogable;
import com.jd.o2o.enhance.utils.ThreadUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wangdongxing on 15-11-20.
 */
public class MapDBSyncManager<K,V> extends MapDBLogable implements Closeable {

    private MapDBSyncCache<K,V> cache;

    private MapDBSyncMQService mqService;

    private BlockingDeque<MapDBSyncMsg> msgQueue;

    private boolean isRunning = false;

    private Long dealDuration = 20l;

    private int threadNum = 1;

    public MapDBSyncManager(MapDBSyncCache<K, V> cache, MapDBSyncMQService mqService) {
        this.cache = cache;
        this.msgQueue = new LinkedBlockingDeque<MapDBSyncMsg>();
        this.mqService = mqService;
        this.mqService.registerSyncManager(cache.getName(),this);
        startSyncThreads();
    }

    public void startSyncThreads(){
        for(int i = 0 ; i<threadNum ; i++){
            new Thread(new DealSyncMsgRunnable(),"MapDB-"+cache.getCacheId()+"-sync-thread-"+i).start();
        }
        isRunning = true;

        if(logger.isDebugEnabled()){
            logger.debug(getMapDBPrefix()+getLogCacheName(cache.getCacheId()) +
                    "MapDBSyncManager started sync threads.");
        }
    }

    public void publish(final MapDBSyncMsg msg){
        final MapDBSyncMQService service = this.mqService;
        ThreadUtils.startNewThread("MapDB-MQSERVICE-PUBLISHER",new Runnable() {
            @Override
            public void run() {
                service.publish(msg);
            }
        });
    }

    public void offer(MapDBSyncMsg msg){
        // 抛弃自己发布自己收到的消息
        if(cache.getCacheId().equals(msg.getCacheId()))
            return;

        msgQueue.offer(msg);

        if(logger.isDebugEnabled()){
            logger.debug(getMapDBPrefix()+getLogCacheName(cache.getCacheId()) +
                    "MapDBSyncManager offer a msg to queue.");
        }
    }

    private class DealSyncMsgRunnable implements Runnable{

        @Override
        public void run() {
            while (isRunning){
                try {
                    MapDBSyncMsg syncMsg = null;
                    if(msgQueue.size() > 0)
                        syncMsg = msgQueue.pop();

                    if(syncMsg == null){
                        ThreadUtils.sleep(dealDuration);
                        continue;
                    }

                    onMessage(syncMsg);

                }catch (Exception e){
                    logger.error(getMapDBPrefix()+getLogCacheName(cache.getCacheId()) +
                            "syncManager consumes error.",e);
                }
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void close() throws IOException {
        isRunning = false;
        this.mqService.close();
    }

    private void onMessage(MapDBSyncMsg syncMsg) {

        if(logger.isDebugEnabled())
            logger.debug(getMapDBPrefix()+getLogCacheName(cache.getCacheId())+getLogThreadName() +
                    " onMessage:{}",syncMsg.toString());

        switch (syncMsg.getType()){
            case MapDBSyncMsg.TYPE_PUT:
                cache.superPut((K) syncMsg.getKey(), (V) syncMsg.getValue());
                break;
            case MapDBSyncMsg.TYPE_REMOVE:
                cache.superRemove((K) syncMsg.getKey());
                break;
            case MapDBSyncMsg.TYPE_CLEAR:
                cache.superClear();
                break;
            default:
                logger.error(getMapDBPrefix()+getLogCacheName(cache.getCacheId())+getLogThreadName() +
                        "do not support MapDBSyncMsg.Type:{}.",syncMsg.getType());
        }
    }

    public MapDBSyncMQService getMqService() {
        return mqService;
    }
}
