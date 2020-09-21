package com.example.demo;


import net.minidev.json.JSONObject;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootElasticsearchRestDemoApplicationTests {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Test
    public  void paginationSearch2() throws IOException {

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
//
      //  RangeQueryBuilder rangeQuery= QueryBuilders.rangeQuery("count").gte(8);
     //   boolQuery.filter(rangeQuery);

      //  MatchQueryBuilder matchQuery = new MatchQueryBuilder("eventType", "WAN_ONOFF");
    //    boolQuery.must(matchQuery);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQuery)
                .from(0)
                .size(10)
                .trackTotalHits(true);
        SearchResponse response = highLevelClient.search(new SearchRequest("book")
                .source(searchSourceBuilder
                ), RequestOptions.DEFAULT);
        System.out.println(response.getHits().getTotalHits());
        System.out.println("response"+response.getHits().getHits().length);


    }
    @Test
    public  void paginationSearch() throws IOException {
        System.out.println("----");
    }
}
