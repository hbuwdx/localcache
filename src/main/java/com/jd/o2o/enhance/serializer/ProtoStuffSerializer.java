package com.jd.o2o.enhance.serializer;

import com.dyuproject.protostuff.*;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wangdongxing on 15-11-11.
 * 特性:
 * 支持pojo,collection,map
 * pojo可以嵌套如Collection、Map
 * 不支持集合嵌套,如Collection<Map> Map<Collection>
 */
public class ProtoStuffSerializer {

    public static <T> byte[] serializePojo(T message,Class<T> pojoClazz){
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = RuntimeSchema.getSchema(pojoClazz);
            return ProtostuffIOUtil.toByteArray(message, schema, buffer);
        } catch (Exception e) {
        }finally {
            buffer.clear();
        }
        return null;
    }

    public static <T> byte[] serializeCollection(Collection<T> message,Class<T> pojoClazz){
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> pojoSchema = RuntimeSchema.getSchema(pojoClazz);
            MessageCollectionSchema<T> schema = new MessageCollectionSchema<T>(pojoSchema);
            return ProtostuffIOUtil.toByteArray(message,schema,buffer);
        } catch (Exception e) {
        }finally {
            buffer.clear();
        }
        return null;
    }

    public static <K,V> byte[] serializeMap(Map<K,V> message,Class<K> kClazz,Class<V> vClazz){
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<K> kSchema = RuntimeSchema.getSchema(kClazz);
            Schema<V> vSchema = RuntimeSchema.getSchema(vClazz);
            MessageMapSchema<K,V> schema = new MessageMapSchema<K, V>(kSchema,vSchema);
            return ProtostuffIOUtil.toByteArray(message,schema,buffer);
        } catch (Exception e) {
        }finally {
            buffer.clear();
        }
        return null;
    }

    public static <T> T deserializePojo(byte[] bytes,Class<T> pojoClazz){
        try {
            Schema<T> schema = RuntimeSchema.getSchema(pojoClazz);
            T message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> Collection<T> deserializeCollection(byte[] bytes,Class<T> pojoClazz){
        try {
            Schema<T> pojoSchema = RuntimeSchema.getSchema(pojoClazz);
            MessageCollectionSchema<T> schema = new MessageCollectionSchema<T>(pojoSchema);
            Collection<T> message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes,message, schema);
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    public static <K,V> Map<K,V> deserializeMap(byte[] bytes,Class<K> kClazz,Class<V> vClazz){
        try {
            Schema<K> kSchema = RuntimeSchema.getSchema(kClazz);
            Schema<V> vSchema = RuntimeSchema.getSchema(vClazz);
            MessageMapSchema<K,V> schema = new MessageMapSchema<K, V>(kSchema,vSchema);
            Map<K,V> message = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bytes,message,schema);
            return message;
        } catch (Exception e) {
            return null;
        }
    }

}
