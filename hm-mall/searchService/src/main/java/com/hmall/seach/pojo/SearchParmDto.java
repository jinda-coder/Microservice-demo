package com.hmall.seach.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchParmDto {
    private String key;
    private String category;//分类名称
    private String brand;//品牌名称
    private Boolean isAD;//商品状态 1-正常，2-下架
    private Integer page;
    private Integer size;
    private Integer minPrice;
    private Integer maxPrice;
    private String sortBy;
    private Integer sold;

}
