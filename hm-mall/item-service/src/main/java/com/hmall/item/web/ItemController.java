package com.hmall.item.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
public class ItemController {
    /**
     * 功能描述: 商品数据分页查询
     * @return :
     */
    @Autowired
    private IItemService itemService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 功能描述: 商品数据分页查询
     * @return :
     */
    @GetMapping("/list")
    public PageDTO<Item> findAllByPage(@Param("page") Integer page, @Param("size    ") Integer size){
        return itemService.findAllByPage(page,size);
    }
    /**
     * 功能描述:根据id查询商品
     * @return :
     */
    @GetMapping("{id}")
    public Item findById(@PathVariable(name = "id") Long id){
        return itemService.findById(id);
    }
    /**
     * 功能描述:新增商品
     * @return :
     */
    @PostMapping
    public void addItem(@RequestBody Item item){
        itemService.addItem(item);
        String json = JSON.toJSONString(item);
        String queue = "item.insert.queue";
        rabbitTemplate.convertAndSend(queue,json);
    }
    /**
     * 功能描述:商品上架，下架
     * @return :
     */
    @PutMapping("/status/{id}/{status}")
    public void updateStatus(@PathVariable Long id,@PathVariable Integer status){
        itemService.updateStatus(id,status);
    }
    /**
     * 功能描述:修改商品信息
     * @return :
     */
    @PutMapping
    public void updateInfo(@RequestBody Item item){
        itemService.updateInfo(item);
        String json = JSON.toJSONString(item);
        String queue = "item.insert.queue";
        rabbitTemplate.convertAndSend(queue,json);
    }
    /**
     * 功能描述:根据id删除商品
     * @return :
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        itemService.deleteById(id);
        String queue = "item.delete.queue";
        rabbitTemplate.convertAndSend(queue,id);
    }
    /**
     * 功能描述:扣减库存接口
     * @return :
     */
    @PutMapping("stock/{itemId}/{num}")
    public void stock(@PathVariable("itemId") Long itemId,@PathVariable("num") Integer  num){
        itemService.stock(itemId,num);
    }
}
