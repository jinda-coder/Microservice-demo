package com.hmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.SubmitDto;

public interface IOrderService extends IService<Order> {
    Integer submit(SubmitDto dto);

    Order pay(Integer orderId);
}
