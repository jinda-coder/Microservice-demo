package com.hmall.item.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    //声明新增，需修改的队列
    @Bean
    public Queue insert(){
        return new Queue("item.insert.queue",true);
    }
    //声明删除队列
    @Bean
    public Queue delete(){
        return new Queue("item.delete.queue",true);
    }
}
