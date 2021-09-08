package zxy.changgou;

import com.github.wxpay.sdk.WXPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import com.github.wxpay.sdk.MyConfig;

@SpringBootApplication
@EnableEurekaClient

public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Bean
    public WXPay wxPay() {
        try {
            return new WXPay(new MyConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
