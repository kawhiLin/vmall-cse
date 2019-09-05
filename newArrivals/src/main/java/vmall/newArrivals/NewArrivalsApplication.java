package vmall.newArrivals;

import org.apache.servicecomb.springboot.starter.provider.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@EnableServiceComb
public class NewArrivalsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewArrivalsApplication.class, args);
    }
}
