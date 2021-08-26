package zxy.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;
import zxy.changgou.config.TokenDecode;


@SpringBootApplication
@EnableEurekaClient
//@EnableScheduling//开启定时任务
@MapperScan(basePackages = {"zxy.yunlian.order.dao"})
@EnableFeignClients(basePackages = "zxy.yunlian.feign")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }
//    @Bean
//    public IdWorker idWorker(){
//        return new IdWorker(1,1);
//    }
//    @Bean
//    public FeignInterceptor feignInterceptor(){
//        return new FeignInterceptor();
//    }
}
