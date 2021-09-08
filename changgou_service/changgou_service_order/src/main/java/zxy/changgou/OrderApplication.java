package zxy.changgou;

import com.changgou.intercepter.FeignInterceptor;
import com.changgou.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import zxy.changgou.config.TokenDecode;


@SpringBootApplication
@EnableEurekaClient
@EnableScheduling//开启定时任务
@MapperScan(basePackages = {"zxy.changgou.dao"})
@EnableFeignClients(basePackages = {"zxy.changgou.order.feign", "zxy.yunlian.feign","zxy.changgou.pay.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }
}
