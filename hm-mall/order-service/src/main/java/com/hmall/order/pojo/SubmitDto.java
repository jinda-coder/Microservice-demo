package com.hmall.order.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_order")
public class SubmitDto {
    private Long addressId;
    private Long itemId;
    private Integer num;
    private Integer paymentType;
    private Long userId;
}
