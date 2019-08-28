package com.web;

import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude= DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class WebVmallApplication {

    public static void main(String[] args) throws Exception {

//        Log4jUtils.init();
        SpringApplication.run(WebVmallApplication.class, args);
    }

}
