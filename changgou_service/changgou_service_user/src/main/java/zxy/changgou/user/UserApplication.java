package zxy.changgou.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;
import zxy.changgou.user.congfig.TokenDecode;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "zxy.changgou.user.dao")
@EnableFeignClients(basePackages = "zxy.changgou.user.feign")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }
}
