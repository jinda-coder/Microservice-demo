//package com.hmall.item.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class MQListener {
//    /**
//     * 功能描述:定义监听延迟消息的死信交换机和死信队列
//     * @return :
//     */
//    @RabbitListener(queues = "dl.queue")
//    public void listenDLQueue(Long id){
//        log.info("收到延迟消息:{}",id);
//    }
//}
