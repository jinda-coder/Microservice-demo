package com.hmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmall.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
