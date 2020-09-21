package com.example.service.impl;

import com.example.service.BookService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    RestHighLevelClient highLevelClient;


    @Override
    public void bookSearch() {
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


    }

    void booksearch(){


    }


}
