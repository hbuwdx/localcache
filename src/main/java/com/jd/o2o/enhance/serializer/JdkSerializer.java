package com.jd.o2o.enhance.serializer;

import java.io.*;

/**
 * Created by wangdongxing on 15-11-13.
 */
public class JdkSerializer {
    /**
     * 对象转成字节数组
     * @param o
     * @return
     */
    public static byte[] serialize(Object o){
        if(o == null) return null;
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(bos);
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(objectOutputStream != null) objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    /**
     * 字节数组转成对象
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes){
        Object resultObject = null;
        if(bytes == null || bytes.length == 0) return resultObject;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            resultObject = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultObject != null){
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  resultObject;
    }

}
