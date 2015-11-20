package com.jd.o2o.enhance.localcache.mapdb.sync;

import com.jd.cachecloud.driver.jedis.ChannelEvent;
import com.jd.cachecloud.driver.jedis.ShardedXCommands;
import com.jd.o2o.enhance.serializer.ProtoStuffSerializer;
import com.jd.o2o.enhance.utils.ThreadUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wangdongxing on 15-11-5.
 */
public class RedisMQClient extends MapDBSyncMQService {

    private ShardedXCommands redisClient;

    private BlockingDeque<ChannelEvent.BytesChannelEvent> eventQueue;

    private final long sleepDuration = 50l;

    private boolean isRunning = false;

    public RedisMQClient(String channel, ShardedXCommands redisClient) {
        super(channel);
        this.redisClient = redisClient;
        eventQueue = new LinkedBlockingDeque<ChannelEvent.BytesChannelEvent>();
        subscribe();
    }

    @Override
    public void publish(MapDBSyncMsg msg) {
        byte[] data = serialize(msg);
        if(data == null || data.length == 0) return;
        redisClient.publish(getChannelBytes(super.getChannel()), data);
    }

    public byte[] getChannelBytes(String channel){
        try {
            return channel.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void subscribe(){
        isRunning = true;
        redisClient.subscribe(new byte[][]{getChannelBytes(super.getChannel())},eventQueue);
        new Thread(dealQueueRunnable,"MapDB-Sync-Listener").start();
    }

    private Runnable dealQueueRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning){
                try {
                    ChannelEvent.BytesChannelEvent event = null;
                    if(eventQueue.size() > 0)
                        event = eventQueue.pop();

                    if(event == null){
                        ThreadUtils.sleep(sleepDuration);
                        continue;
                    }
                    MapDBSyncMsg msg = doSerialize(event.getMessage());
                    consumer(msg);
                }catch (Exception e){
                    logger.error("deal msg error", e);
                }
            }
        }
    };

    @Override
    public void close() throws IOException {
        isRunning = false;
    }

    private byte[] serialize(MapDBSyncMsg syncMsg){
        if(syncMsg == null) return null;
        try {
            return ProtoStuffSerializer.serializePojo(syncMsg,MapDBSyncMsg.class);
        } catch (Exception e) {
        }
        return null;
    }

    private MapDBSyncMsg doSerialize(byte[] data){
        if(data == null) return null;
        try {
            return ProtoStuffSerializer.deserializePojo(data, MapDBSyncMsg.class);
        } catch (Exception e) {
        }
        return null;
    }

}
