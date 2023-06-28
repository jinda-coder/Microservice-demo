package com.hmall.seach.service.impl;

import com.alibaba.fastjson.JSON;
import com.hmall.api.ItemAPI;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import com.hmall.seach.pojo.ItemDoc;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl {

}
