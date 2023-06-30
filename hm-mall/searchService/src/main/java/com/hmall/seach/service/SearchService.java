package com.hmall.seach.service;

import com.hmall.common.dto.PageDTO;
import com.hmall.seach.pojo.Item;
import com.hmall.seach.pojo.SearchParmDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    PageDTO<Item> find(SearchParmDto dto) throws IOException;

    Map<String, List<String>> bucket(SearchParmDto dto) throws IOException;

    List<String> suggestion(String key) throws IOException;
}
