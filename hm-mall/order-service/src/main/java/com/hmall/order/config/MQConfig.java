package com.hmall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    //延迟队列
    @Bean
    public Queue ttlQueue(){
        return QueueBuilder.durable("ttl.queue")
                .ttl(10000)
                .deadLetterExchange("dl.fanout")
                .build();
    }
    //延迟交换机
    @Bean
    public FanoutExchange ttlExchange(){
        return new FanoutExchange("ttl.fanout");
    }
    //绑定延迟交换机和延迟队列
    @Bean
    public Binding simpleBinding(){
        return BindingBuilder.bind(ttlQueue()).to(ttlExchange());
    }
    //死信队列
    @Bean
    public Queue dlQueue(){
        return QueueBuilder.durable("dl.queue")
                .build();
    }
    //    死信交换机
    @Bean
    public FanoutExchange dlExchange(){
        return new FanoutExchange("dl.fanout");
    }
    @Bean
    public Binding simpleBinding2(){
        return BindingBuilder.bind(dlQueue()).to(dlExchange());
    }

}
