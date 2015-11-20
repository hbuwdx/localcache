package com.jd.o2o.enhance.runtime;

import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by wangdongxing.
 * 获取系统的一些相关信息
 */
public class JvmUtils {

    //本地IP
    public static String IP = null;
    //进程PID
    public static String PID = null;
    //操作系统名称
    public static String OS_NAME = null;
    //行分页符
    public static String OS_LINE_SEPARATOR = null;
    //文件分隔符号
    public static String OS_FILE_SEPARATOR = null;
    //获得系统属性集
    public static final Properties props=System.getProperties();


    static {
        IP = getLocalIp();
        PID = ManagementFactory.getRuntimeMXBean().getName();
        OS_NAME = getProperty("os.name");
        OS_LINE_SEPARATOR = getProperty("line.separator");
        OS_FILE_SEPARATOR = getProperty("file.separator");

    }

    public static String getProperty(String propertyName){
        return props.getProperty(propertyName);
    }

    public static String getLocalIp(){
        try {
            return getSystemLocalIp().getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * 根据系统的类型获取本服务器的ip地址
     *
     * InetAddress inet = InetAddress.getLocalHost();
     * 但是上述代码在Linux下返回127.0.0.1。
     * 主要是在linux下返回的是/etc/hosts中配置的localhost的ip地址，
     * 而不是网卡的绑定地址。后来改用网卡的绑定地址，可以取到本机的ip地址：）：
     * @throws java.net.UnknownHostException
     */
    public static InetAddress getSystemLocalIp() throws Exception{
        InetAddress inet = null;
        String osName = getSystemOSName();
        if(osName.toLowerCase().contains("windows")){
            inet = getWinLocalIp();
        }else{
            inet = getUnixLocalIp();
        }
        return inet;
    }
    public static String getSystemOSName() {
        return props.getProperty("os.name");
    }
    /**
     * 获取window 本地ip地址
     * @throws java.net.UnknownHostException
     */
    private static InetAddress getWinLocalIp() throws UnknownHostException{
        return InetAddress.getLocalHost();
    }

    /**
     * 获取unixIP地址，因为存在多个网卡，选出非loopback地址
     * @throws java.net.SocketException
     */
    private static InetAddress getUnixLocalIp() throws SocketException{
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while(netInterfaces.hasMoreElements())
        {
            NetworkInterface ni= netInterfaces.nextElement();
            if(!ni.isUp()) continue;
            if(ni.isLoopback()) continue;

            for(Enumeration inet = ni.getInetAddresses();inet.hasMoreElements();){
                InetAddress nip=(InetAddress) inet.nextElement();
                if(nip instanceof Inet4Address){
                    ip = nip;
                    break;
                }else if(nip instanceof Inet6Address){

                }else{

                }
            }
            if(ip != null){
                return ip;
            }
        }
        return null;
    }

    public static long getMaxMemory(){
        return Runtime.getRuntime().maxMemory();
    }

    public static long getTotalMemory(){
        return Runtime.getRuntime().totalMemory();
    }

    public static void freeMemeory(){
        Runtime.getRuntime().freeMemory();
    }

    public static void addShutdownHook(Thread hook){
        Runtime.getRuntime().addShutdownHook(hook);
    }
    public static void removeShutdownHook(Thread hook){
        Runtime.getRuntime().removeShutdownHook(hook);
    }
}
