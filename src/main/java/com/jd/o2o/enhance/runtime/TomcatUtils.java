package com.jd.o2o.enhance.runtime;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * Created by wangdongxing on 15-11-5.
 */
public class TomcatUtils {

    /**
     * 获取tomcat实例路径
     * @return
     */
    public static String getTomcatCatalinaBase(){
        return System.getProperty("catalina.base");
    }

    /**
     * 获取当前实例的端口,-1代表获取端口失败
     * @return
     */
    public static int getTomcatPort(){
        int port = 0;
        String catalinaBase = getTomcatCatalinaBase();
        String serverXmlPath = catalinaBase+ File.separator+"conf"+File.separator+"server.xml";
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(new File(serverXmlPath));
        } catch (DocumentException e) {
            return -1;
        }
        Element root = document.getRootElement();
        List<Element> connectorList = root.selectNodes("Service/Connector");
        if(connectorList != null && connectorList.size() > 0){
            for(Element element : connectorList){
                String portString = element.attributeValue("port");
                String protocol = element.attributeValue("protocol");
                if(protocol != null && protocol.trim().length() > 0 && protocol.toLowerCase().indexOf("http") >= 0){
                    port = Integer.parseInt(portString);
                    break;
                }
            }
        }
        return port;
    }

}
