package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.domain.Page;
import com.example.param.BookParam;
import com.example.service.BookService;
import com.example.vo.BookVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    RestHighLevelClient highLevelClient;


    @Override
    public Page<BookVo> bookSearch(BookParam param) throws IOException {
        Page<BookVo> page = new Page<BookVo>();
        List<BookVo> bookVoList = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("book");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(param.getCurrentPage());
        sourceBuilder.size(param.getPageSize());


        if(param.getAuthor() != null){
            boolQueryBuilder.must(QueryBuilders.matchQuery("author", param.getAuthor().trim()));
        }
        if(param.getTitle() != null){
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", param.getTitle().trim()));
        }
        if(param.getStartDate() != null){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishDate");
            rangeQueryBuilder.gte(param.getStartDate());
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        if(param.getEndDate() != null){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishDate");
            rangeQueryBuilder.lte(param.getEndDate());
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.sort("publishDate", SortOrder.DESC);
        log.info(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = highLevelClient.search(searchRequest);
        searchResponse.getHits().forEach(message -> {
            try {
                String sourceAsString = message.getSourceAsString();
                BookVo bookVo = JSONObject.parseObject(sourceAsString, BookVo.class);
                bookVoList.add(bookVo);
                log.info(sourceAsString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        page.setTotalCount(searchResponse.getHits().getTotalHits());
        page.setCurrentPage(param.getCurrentPage());
        page.setPageSize(param.getPageSize());
        page.setList(bookVoList);
        return page;
    }

    @Override
    public Page<BookVo> searchALL() {

        String indexName ="book";
//        //创建查询请求对象,提供索引和类型,如果建表时规范的话,可以不提供类型的
        SearchRequest searchRequest = new SearchRequest(indexName).types("novel");
        //构建query条件
        QueryBuilder queryBuilder = new MatchAllQueryBuilder();
        //query条件作为查询条件,size表示返回结果的条数
        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(10);
        //请求对象携带条件,查询类型,一般默认即可
        searchRequest.source(builder).searchType(SearchType.DEFAULT);
        try {
            //通过高级客户端执行查询请求,返回响应对象
            SearchResponse searchResponse = highLevelClient.search(searchRequest);

            //拿到响应的匹配结果,遍历
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                //转为String,也可以getSourceAsMap转为map,后续进行操作
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 单条件检索
     * @param fieldKey
     * @param fieldValue
     * @return
     */
    public MatchPhraseQueryBuilder uniqueMatchQuery(String fieldKey,String fieldValue){
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(fieldKey,fieldValue);
        return matchPhraseQueryBuilder;
    }

    /**
     * 多条件检索并集，适用于搜索比如包含腾讯大王卡，滴滴大王卡的用户
     * @param fieldKey
     * @param queryList
     * @return
     */
    public BoolQueryBuilder orMatchUnionWithList(String fieldKey,List<String> queryList){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String fieldValue : queryList){
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(fieldKey,fieldValue));
        }
        return boolQueryBuilder;
    }

    /**
     * 范围查询，左右都是闭集
     * @param fieldKey
     * @param start
     * @param end
     * @return
     */
    public RangeQueryBuilder rangeMathQuery(String fieldKey,String start,String end){
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldKey);
        rangeQueryBuilder.gte(start);
        rangeQueryBuilder.lte(end);
        return rangeQueryBuilder;
    }

    /**
     * 根据中文分词进行查询
     * @param fieldKey
     * @param fieldValue
     * @return
     */
    public MatchQueryBuilder matchQueryBuilder(String fieldKey,String fieldValue){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldKey,fieldValue).analyzer("ik_smart");
        return matchQueryBuilder;
    }


}
