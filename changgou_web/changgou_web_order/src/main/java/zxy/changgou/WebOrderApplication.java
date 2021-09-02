package zxy.changgou;

import com.changgou.entity.Result;
import com.changgou.intercepter.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"zxy.changgou.feign", "zxy.changgou.user.feign"})
public class WebOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebOrderApplication.class, args);
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}