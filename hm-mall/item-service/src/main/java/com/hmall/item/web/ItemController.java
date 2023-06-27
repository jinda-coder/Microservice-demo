package com.hmall.item.web;

import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {
    /**
     * 功能描述: 商品数据分页查询
     * @return :
     */
    @Autowired
    private IItemService itemService;
    /**
     * 功能描述: 商品数据分页查询
     * @return :
     */
    @GetMapping("/list")
    public PageDTO<Item> findAllByPage(@Param("page") Integer page, @Param("size") Integer size){
        return itemService.findAllByPage(page,size);
    }
    /**
     * 功能描述:根据id查询商品
     * @return :
     */
    @GetMapping("{id}")
    public Item findById(@PathVariable Long id){
        return itemService.findById(id);
    }
}
