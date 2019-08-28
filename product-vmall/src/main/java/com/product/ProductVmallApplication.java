package com.product;

import org.apache.log4j.PropertyConfigurator;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.apache.servicecomb.foundation.common.utils.Log4jUtils;

@EnableJpaRepositories
@SpringBootApplication(exclude= DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class ProductVmallApplication {
    public static void main(String[] args) throws Exception {
//        Log4jUtils.init();

        SpringApplication.run(ProductVmallApplication.class, args);
    }
}
