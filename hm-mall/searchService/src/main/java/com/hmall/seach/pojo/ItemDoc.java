package com.hmall.seach.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDoc {
    private Long id;//商品id
    private String name;//商品名称
    private Long price;//价格（分）
    private String image;//商品图片
    private String category;//分类名称
    private Boolean isAD;
    private String brand;//品牌名称
    private Integer sold;//销量
    private Integer comment_count;//评论数
    private List<String> suggestion = Arrays.asList(name,brand);
}
