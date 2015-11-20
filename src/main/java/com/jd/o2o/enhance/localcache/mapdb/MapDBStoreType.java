package com.jd.o2o.enhance.localcache.mapdb;

/**
 * Created by wangdongxing on 15-10-19.
 * heap 速度最高，无需序列化，被gc影响，初测写入 12w/s 会OutOfMemoryException
 * in_memory 占用jvm老年代，需要序列化,不被young gc影响,但是被full gc影响， 配置-Xmx参数  初测写入 4w/s,会OutOfMemoryException
 * memory_direct 占用堆外内存，脱离JVM，需要序列化，配置 -XX:MaxDirectMemorySize=10G 初测写入  3w/s
 */
public enum MapDBStoreType {
    HEAP,IN_MEMORY,MEMORY_DIRECT
}
