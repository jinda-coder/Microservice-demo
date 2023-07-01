package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {
    @Autowired
    ItemMapper itemMapper;
    /**
     * 功能描述: 商品数据分页查询
     * @return :
     */
    @Override
    public PageDTO<Item> findAllByPage(Integer page, Integer size) {
        Page page1 = new Page(page,size);
        itemMapper.selectPage(page1,null);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotal(page1.getTotal());
        pageDTO.setList(page1.getRecords());
        return pageDTO;
}
    /**
     * 功能描述:  根据id查询商品
     * @return :
     */
    @Override
    public Item findById(Long id) {
        return itemMapper.selectById(id);
    }
    /**
     * 功能描述:新增商品
     * @return :
     */
    @Override
    public void addItem(Item item) {
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        itemMapper.insert(item);
    }
    /**
     * 功能描述:商品上架，下架
     * @return :
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        Item item = new Item();
        item.setUpdateTime(new Date());
        item.setId(id);
        item.setStatus(status);
        itemMapper.updateById(item);
    }
    /**
     * 功能描述:修改商品信息
     * @return :
     */
    @Override
    public void updateInfo(Item item) {
        LambdaUpdateWrapper<Item> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Item::getId,item.getId())
                     .set(Item::getName,item.getName())
                     .set(Item::getCategory,item.getCategory())
                     .set(Item::getBrand,item.getBrand())
                     .set(Item::getPrice,item.getPrice())
                     .set(Item::getStock,item.getStock())
                     .set(Item::getSpec,item.getSpec())
                     .set(Item::getImage,item.getImage())
                     .set(Item::getIsAD,item.getIsAD());
        itemMapper.update(null,updateWrapper);
    }
    /**
     * 功能描述:根据id删除商品
     * @return :
     */
    @Override
    public void deleteById(Long id) {
        itemMapper.deleteById(id);
    }

    @Override
    public void stock(Long itemId, Integer  num) {
        Item item = itemMapper.selectById(itemId);
        item.setStock(item.getStock() + num);
        itemMapper.update(item,null);
    }
}
