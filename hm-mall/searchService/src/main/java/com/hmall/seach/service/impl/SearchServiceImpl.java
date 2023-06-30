package com.hmall.seach.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hmall.api.ItemAPI;
import com.hmall.common.dto.PageDTO;
import com.hmall.seach.pojo.Item;
import com.hmall.seach.pojo.ItemDoc;
import com.hmall.seach.pojo.SearchParmDto;
import com.hmall.seach.service.SearchService;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    RestHighLevelClient client;

    @Override
    public PageDTO<Item> find(SearchParmDto dto) throws IOException {
        //1.创建request请求
        SearchRequest request = new SearchRequest("item");
        SearchSourceBuilder source = request.source();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //如果搜索框内容为空，默认查询所有
        String key = dto.getKey();
        if (key != null && !"".equals(key)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("name",key));
        }else {
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }
        String category = dto.getCategory();
        //分类条件
        if (category != null && !"".equals(category)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("category",category));
        }
        //品牌条件
        String brand = dto.getBrand();
        if (brand != null && !"".equals(brand)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand",brand));
        }
        //价格判断
        Integer minPrice = dto.getMinPrice();
        Integer maxPrice = dto.getMaxPrice();
        if (minPrice != null && maxPrice != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(minPrice*100).lte(maxPrice*100));
        }
        //排序判断
        String sortBy = dto.getSortBy();
        if (sortBy.equals("price")){
            source.sort(SortBuilders.fieldSort(sortBy).order(SortOrder.ASC));
        }else if (sortBy.equals("sold")){
            source.sort(SortBuilders.fieldSort("sold").order(SortOrder.DESC));
        }
        Integer page = dto.getPage();
        Integer  size = dto.getSize();
        if (page != null && size != null){
            source.query(boolQueryBuilder).from(page).size(size);
        }
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("name").requireFieldMatch(false).preTags("<span style=\"color: red;\">").postTags("</span>");
        source.highlighter(highlightBuilder);
        //竞价排名
        //定义filter
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = {new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("isAD",true), ScoreFunctionBuilders.weightFactorFunction(1000000))};
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder,filterFunctionBuilders);
        //计算模式
        functionScoreQueryBuilder.boostMode(CombineFunction.MULTIPLY);
        request.source().query(functionScoreQueryBuilder).from(dto.getPage()).size(dto.getSize());
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(response);
        SearchHit[] hits = response.getHits().getHits();
        PageDTO<Item> pageDTO = new PageDTO<>();
        pageDTO.setTotal(response.getHits().getTotalHits().value);
        List<Item> list = new ArrayList<>();
        String name = "";
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
           if (highlightFields.size() != 0){
               HighlightField highlightField = highlightFields.get("name");
               if (highlightField != null){
                    name = highlightField.getFragments()[0].toString();
               }
           }
            String json = hit.getSourceAsString();
            Item item = JSON.parseObject(json, Item.class);
            item.setName(name);
            list.add(item);
        }
        pageDTO.setList(list);
        return pageDTO;
    }

    @Override
    public Map<String, List<String>> bucket(SearchParmDto dto) throws IOException {
        //创建请求对象
        SearchRequest request = new SearchRequest("item");
        SearchSourceBuilder source = request.source();
        if (dto.getBrand() == null && dto.getCategory() == null){
            source.aggregation(AggregationBuilders.terms("brand_list").size(10).field("brand"));
            source.aggregation(AggregationBuilders.terms("category_list").size(10).field("category"));
        }
        //brand聚合
        String brand = dto.getBrand();
        if (brand != null && ! "".equals(brand)){
            source.aggregation(AggregationBuilders.terms("brand_list").size(10).field("brand"));
        }
        //分类聚合
        String category = dto.getCategory();
        if (category != null && !"".equals(category)){
            source.aggregation(AggregationBuilders.terms("category_list").size(10).field("category"));
        }
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Map<String,List<String>> map = new HashMap<>();
        List<String> brandList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();
        //获取brand聚合
        Aggregations aggregations1 = response.getAggregations();
        if (aggregations1 != null) {
            Terms aggregation = aggregations1.get("brand_list");
            if (aggregation != null) {
                for (Terms.Bucket bucket : aggregation.getBuckets()) {
                    String keyAsString = bucket.getKeyAsString();
                    brandList.add(keyAsString);
                }
                map.put("brand", brandList);
            }
        }
        //获取category聚合
        Aggregations aggregations2 = response.getAggregations();
        if (aggregations2 != null) {
            Terms aggregation = aggregations2.get("category_list");
            if (aggregation != null) {
                for (Terms.Bucket bucket : aggregation.getBuckets()) {
                    String keyAsString = bucket.getKeyAsString();
                    categoryList.add(keyAsString);
                }
                map.put("分类", categoryList);
            }
        }
        return map;
    }

    @Override
    public List<String> suggestion(String key) throws IOException {
        //1.创建请求对象
        SearchRequest request = new SearchRequest("item");
        SearchSourceBuilder source = request.source();
        source.suggest(new SuggestBuilder().addSuggestion("suggestion",SuggestBuilders.completionSuggestion("suggestion").prefix(key).skipDuplicates(true).size(10)));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Suggest suggest = response.getSuggest();
        CompletionSuggestion mySuggestion = suggest.getSuggestion("suggestion");
        List<String> list = new ArrayList<>();
        List<CompletionSuggestion.Entry.Option> options = mySuggestion.getOptions();
        for (CompletionSuggestion.Entry.Option option : options) {
            //获取补全词条
            String text = option.getText().string();
            list.add(text);
        }
        return list;
    }
}
