package com.hmall.seach.web;

import com.hmall.common.dto.PageDTO;
import com.hmall.seach.pojo.Item;
import com.hmall.seach.pojo.ItemDoc;
import com.hmall.seach.pojo.SearchParmDto;
import com.hmall.seach.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService service;
    @PostMapping("/list")
    public PageDTO<Item> find(@RequestBody SearchParmDto dto) throws IOException {
        return service.find(dto);
    }
    @PostMapping("/filters")
    public Map<String, List<String>> bucket(@RequestBody SearchParmDto dto) throws IOException {
        return service.bucket(dto);
    }
    @GetMapping("/suggestion")
    public List<String> suggestion(String key) throws IOException {
        return service.suggestion(key);
    }
}
