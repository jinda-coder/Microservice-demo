package com.hmall.api;

import com.hmall.common.dto.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice")
public interface UserAPI {
    @GetMapping("address/{id}")
    Address findAddressById(@PathVariable("id") Long id);
}
