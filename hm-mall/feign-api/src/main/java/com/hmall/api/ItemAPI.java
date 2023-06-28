package com.hmall.api;

import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "itemservice")
public interface ItemAPI {
    @GetMapping("/item/list")
     PageDTO<Item> findAllByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size);
}
