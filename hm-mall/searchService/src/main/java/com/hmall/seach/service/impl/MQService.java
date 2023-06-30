package com.hmall.seach.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hmall.seach.pojo.Item;
import com.hmall.seach.pojo.ItemDoc;
import org.apache.logging.log4j.message.Message;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MQService implements com.hmall.seach.service.MQService {
    @Autowired
    RestHighLevelClient client;
    /**
     * 功能描述:MQ消息监听实现ES索引库文档的新增
     * @return :
     */
    @RabbitListener(queues = {"item.insert.queue"})
    public void insertItem(String message) throws IOException {
        ItemDoc itemDoc = JSONObject.parseObject(message, ItemDoc.class);
        List<String> list = new ArrayList<>();
        list.add(itemDoc.getName());
        list.add(itemDoc.getBrand());
        itemDoc.setSuggestion(list);
//        监听到到新增队列的消息时，调用ES新增文档的接口
//        1.创建request对象  id指的是斜杠后面的id
        IndexRequest request = new IndexRequest("item").id(itemDoc.getId().toString());
        String json = JSON.toJSONString(itemDoc);
        //2.封装信息
        request.source(json, XContentType.JSON);
        //3.发起请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    }
    /**
     * 功能描述:MQ消息监听实现ES索引库文档的删除
     * @return :
     */
    @RabbitListener(queues = "item.delete.queue")
    public void delete(String id) throws IOException {
        //创建请求对象
        DeleteRequest request = new DeleteRequest("item",id);
        //发送请求
        client.delete(request,RequestOptions.DEFAULT);
    }
}
