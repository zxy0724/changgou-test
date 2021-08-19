package zxy.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "zxy.yunlian.feign")
public class PageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageApplication.class);
    }
}
