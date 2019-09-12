package weben;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;

@SpringBootApplication(exclude= DispatcherServletAutoConfiguration.class)
@EnableServiceComb
public class WebEnVmallApplication {

    public static void main(String[] args) throws Exception {

//        Log4jUtils.init();
        SpringApplication.run(WebEnVmallApplication.class, args);
    }

}
