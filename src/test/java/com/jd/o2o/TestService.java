package com.jd.o2o;

import com.jd.o2o.enhance.utils.ThreadUtils;
import com.jd.o2o.service.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wangdongxing on 15-11-20.
 */
@ContextConfiguration({"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {

    @Autowired
    private HelloService helloService;

    @Test
    public void testCache(){
        String result = helloService.getName("hi");
        System.out.println(result);
        result = helloService.getName("hi");
        System.out.println(result);
    }

    @Test
    public void testCache2(){
        for(int i=0;i<1000;i++){
            String result = helloService.getName2("hi"+i);
            ThreadUtils.sleep(50l);
        }
    }

    @Test
    public void testCache3(){
        for(int i=0;i<100;i++){
            String result = helloService.info();
            ThreadUtils.sleep(5000l);
        }
    }
}
