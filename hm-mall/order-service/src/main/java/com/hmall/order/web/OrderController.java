package com.hmall.order.web;

import com.hmall.order.config.SnowFlakeGenerateIdWorker;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.SubmitDto;
import com.hmall.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private IOrderService orderService;
   @GetMapping("{id}")
   public Order queryOrderById(@PathVariable("id") Long orderId) {
      return orderService.getById(orderId);
   }
   @PostMapping
   public Integer submit(@RequestBody SubmitDto dto){
         return orderService.submit(dto);
   }
//   @PostMapping("pay/url/3/{orderId}")
//   public Order pay(@PathVariable Integer orderId){
//      return orderService.pay(orderId);
//   }
}
