package com.jd.o2o.enhance.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by wangdongxing on 15-11-13.
 */
public class GizpSerializer {
    /**
     * 将字节数组压缩
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] serialize(byte[] bytes) throws Exception{
        if(bytes == null || bytes.length == 0) return bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(bos);
        gos.write(bytes);
        gos.close();
        return bos.toByteArray();
    }

    /**
     * 将GZIP压缩后的字节数组，解压为正常字节数组
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] deserialize(byte[] bytes) throws Exception{
        if(bytes == null || bytes.length == 0) return bytes;
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len=0;
        while ((len = gzipInputStream.read(buffer)) >0){
            bos.write(buffer,0,len);
        }
        gzipInputStream.close();
        return bos.toByteArray();
    }
}
