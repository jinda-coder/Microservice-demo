package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.PageDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.pojo.Item;
import com.hmall.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
