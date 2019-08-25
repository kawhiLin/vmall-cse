package com.record;

import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication(exclude= DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class RecordVmallApplication {

    public static void main(String[] args) throws Exception {
        Log4jUtils.init();
        SpringApplication.run(RecordVmallApplication.class, args);
    }

}
