package com.hmall.seach;

import com.hmall.api.ItemAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.hmall.api")
public class SearchApplication {
    public static void main(String[] args)   {
        SpringApplication.run(SearchApplication.class,args);
    }
}
