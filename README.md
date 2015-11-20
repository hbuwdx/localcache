# localcache
1、目的：实现数据本地存储同时支持多机同步的缓存组件
2、兼容javax.cache 规范
3、支持spring-cache集成
优点：
1、支持 堆内、持久代、堆外三种不同级别的缓存方式
2、使用redis消息队列实现多机数据同步

优化点：
1、序列化方式全部改成protostuff

几种不同缓存策略的对比

JVM缓存	JIMDB缓存	Off-heap缓存
NetIo	无	有	无
序列化	无	有	有
缓存同步	无	有	原生支持单机同步（本项目利用redis构建成多机同步）
依赖JVM	是	否	否
读写速度	快	慢	中
存储位置	JVM堆	集群内存	JVM堆外内存
