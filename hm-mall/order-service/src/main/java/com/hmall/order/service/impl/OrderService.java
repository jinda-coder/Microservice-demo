package com.hmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.api.ItemAPI;
import com.hmall.api.UserAPI;
import com.hmall.common.BaseContext;
import com.hmall.common.dto.Address;
import com.hmall.common.dto.Item;
import com.hmall.order.config.SnowFlakeGenerateIdWorker;
import com.hmall.order.mapper.OrderLogistics;
import com.hmall.order.mapper.OrderMapper;
import com.hmall.order.mapper.OrderDetailMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.pojo.SubmitDto;
import com.hmall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    OrderLogistics orderLogistics;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ItemAPI itemAPI;
    @Autowired
    UserAPI userAPI;
    /**
     * 功能描述:生成订单
     * @return :
     */
    @Override
    public Integer  submit(SubmitDto dto) {
        Order order = new Order();
        //根据雪花算法生成订单id
        SnowFlakeGenerateIdWorker snowFlakeGenerateIdWorker = new SnowFlakeGenerateIdWorker();
        long orderId = snowFlakeGenerateIdWorker.nextId();
        //根据itemId查询商品详情
        Item item = itemAPI.findById(dto.getItemId());
        //计算总价
        Long totalFree = dto.getNum() * item.getPrice();
        //设置订单id
        order.setId(orderId);
        order.setTotalFee(totalFree);
        //设置状态为未付款
        order.setStatus(1);
        order.setPaymentType(dto.getPaymentType());
        Long userId = BaseContext.getCurrentId();
        order.setUserId(userId);
        orderMapper.insert(order);
        //将商品信息写入到order_detail表中
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(orderId)
                .itemId(item.getId())
                .num(dto.getNum())
                .name(item.getName())
                .spec(item.getSpec())
                .price(item.getPrice())
                .image(item.getImage())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        orderDetailMapper.insert(orderDetail);
        //根据addressId查询地址信息
        Long addressId = dto.getAddressId();
        Address address = userAPI.findAddressById(addressId);
        //封装信息添加到order_logistics表中
        com.hmall.order.pojo.OrderLogistics orderLogistics1 = com.hmall.order.pojo.OrderLogistics.builder()
                .orderId(orderId)
                .contact(address.getContact())
                .mobile(address.getMobile())
                .province(address.getProvince())
                .city(address.getCity())
                .town(address.getTown())
                .street(address.getStreet())
                .createTime(new Date())
                .updateTime(new Date()).build();

        orderLogistics.insert(orderLogistics1);
        //扣减库存
        itemAPI.stock(item.getId(),-dto.getNum());
        //生成订单后立即发送一条消息到延迟队列 携带订单id
        rabbitTemplate.convertAndSend("ttl.fanout","",orderId);
        return 123865420;
    }
    @Override
    public Order pay(Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        return order;
    }
    /**
     * 功能描述:监听死信队列消息
     * @return :
     */
    @RabbitListener(queues = "dl.queue")
    public void listenDLQueue(Long id){
        log.info("收到延迟队列消息:{}",id);
        System.out.println("----------------------------华丽的分割线-----------------------------");
        Order order = orderMapper.selectById(id);
        if (order.getStatus() == 1){//状态为1是超时未支付订单
            order.setStatus(5);//设置订单状态为5 交易取消
            //根据订单id查询订单详情表，获取狗购买数量
            QueryWrapper<OrderDetail> queryWrapper4 = new QueryWrapper();
            queryWrapper4.eq("order_id",id);
            List<OrderDetail> orderDetails = orderDetailMapper.selectList(queryWrapper4);
            OrderDetail orderDetail = orderDetails.get(0);
            Integer num = orderDetail.getNum();
            Long itemId = orderDetail.getItemId();
            itemAPI.stock(itemId,num);
        }
    }
}
