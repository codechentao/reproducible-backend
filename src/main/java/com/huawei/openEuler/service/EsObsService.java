package com.huawei.openEuler.service;

import com.alibaba.fastjson.JSONObject;
import com.huawei.openEuler.entity.ArgsModel;
import com.huawei.openEuler.entity.BuildInfo;
import com.huawei.openEuler.entity.DetailTable;
import com.huawei.openEuler.utils.ConstantUtils;
import com.huawei.openEuler.utils.ProjectUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * service
 *
 * @author zhangshengjie
 * @since 2022-5-27
 * */
@Service
public class EsObsService {

    @Autowired(required = false)
    private RestHighLevelClient client;

    private static final String PROJECT_PATH = "";

    /**
     * 单条件搜索
     *
     * @param key1      查询的key
     * @param argsModel 交互参数
     * @return list
     */
    public List<DetailTable> selectById(String key1, ArgsModel argsModel, String index) throws IOException {
        // 指定索引，类似于数据库的表
        SearchRequest searchRequest = new SearchRequest(index);
        // 创建查询对象，相当于写查询sql
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(key1, argsModel.getTerm1()))
                .must(QueryBuilders.termQuery(ConstantUtils.ARCH, argsModel.getArchitectures()))
                .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, argsModel.getTestSuites()))
        );
        // 需要查出的总记录条数
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<DetailTable> list = new ArrayList<>();
        parseHits(hits, list);
        return list;
    }

    /**
     * 双条件搜索
     *
     * @return list
     */
    public List<DetailTable> select(String key1, ArgsModel argsModel, String key2, int type, String index) throws IOException {
        // 指定索引，类似于数据库的表
        SearchRequest searchRequest = new SearchRequest(index);
        // 创建查询对象，相当于写查询sql
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (type == ConstantUtils.TYPE_VALUE_1) {
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery(key1, argsModel.getTerm1()))
                    .must(QueryBuilders.termQuery(ConstantUtils.ARCH, argsModel.getArchitectures()))
                    .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, argsModel.getTestSuites())));
        } else if (type == ConstantUtils.TYPE_VALUE_2) {
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery(key2, argsModel.getTerm2()))
                    .must(QueryBuilders.termQuery(ConstantUtils.ARCH, argsModel.getArchitectures()))
                    .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, argsModel.getTestSuites())));
        } else if (type == ConstantUtils.TYPE_VALUE_3) {
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery(key1, argsModel.getTerm1()))
                    .must(QueryBuilders.termQuery(key2, argsModel.getTerm2()))
                    .must(QueryBuilders.termQuery(ConstantUtils.ARCH, argsModel.getArchitectures()))
                    .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, argsModel.getTestSuites())));
        } else if (type == ConstantUtils.TYPE_VALUE_4) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery(ConstantUtils.ARCH, argsModel.getArchitectures()))
                    .must(QueryBuilders.matchQuery(ConstantUtils.UPSTREAM_BRANCH, argsModel.getTestSuites())));
        }
        // 需要查出的总记录条数
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        List<DetailTable> list = new ArrayList<>();
        parseHits(hits, list);
        return list;
    }


    /**
     * Overview数据聚合
     *
     * @return map
     */
    public LinkedHashMap<String, LinkedHashMap<String, Long>> aggsOverView(QueryBuilder queryBuilder, String index) throws IOException {

        //设置要查询的索引
        SearchRequest request = new SearchRequest().indices(index);
        //构建搜索
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //添加搜索长度
        sourceBuilder.size(0);
        //添加搜索条件
        sourceBuilder.query(queryBuilder);
        TermsAggregationBuilder agg1 = AggregationBuilders
                .terms(ConstantUtils.CATEGORY_LEVEL)
                .field(ConstantUtils.CATEGORY_LEVEL)
                .order(BucketOrder.key(true))
                .size(10000);

        TermsAggregationBuilder agg2 = AggregationBuilders
                .terms(ConstantUtils.TEST_STATUS)
                .field(ConstantUtils.TEST_STATUS)
                .order(BucketOrder.key(true))
                .size(10000);

        //添加聚合查询
        agg1.subAggregation(agg2);
        sourceBuilder.aggregation(agg1);
        //创建请求
        request.source(sourceBuilder);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        LinkedHashMap<String, LinkedHashMap<String, Long>> map = new LinkedHashMap<>();
        Terms terms1 = response.getAggregations().get(ConstantUtils.CATEGORY_LEVEL);
        for (Terms.Bucket bucket1 : terms1.getBuckets()) {
            Terms terms2 = bucket1.getAggregations().get(ConstantUtils.TEST_STATUS);
            LinkedHashMap<String, Long> map1 = new LinkedHashMap<>();
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                map1.put(bucket2.getKeyAsString(), bucket2.getDocCount());
            }
            map.put(bucket1.getKeyAsString(), map1);
        }

        return map;
    }

    /**
     * 日期图聚合
     *
     * @return map
     */
    public LinkedHashMap<String, LinkedHashMap<String, Long>> aggDataArgs(QueryBuilder queryBuilder, String args1, String args2, String index) throws IOException {

        LinkedHashMap<String, LinkedHashMap<String, Long>> map = new LinkedHashMap<>();
        //设置要查询的索引
        SearchRequest request = new SearchRequest().indices(index);
        //构建搜索
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //添加搜索长度
        sourceBuilder.size(0);
        //添加搜索条件
        sourceBuilder.query(queryBuilder);
        //设置要聚合的字段以及条数
        //设置该次聚合的名称 terms(args1)
        //以及要聚合的字段field(args1 + ".keyword") 添加keyword是对字段进行不分词查询。
        DateHistogramAggregationBuilder dateHis = AggregationBuilders
                .dateHistogram(args1)
                .field(args1)
                .format("yyyy-MM-dd")
                .timeZone(ZoneId.of("GMT+8"))
                .order(BucketOrder.key(true))
                .calendarInterval(DateHistogramInterval.DAY);

        TermsAggregationBuilder agg2 = AggregationBuilders
                .terms(args2)
                .field(args2)
                .size(10000);
        //添加聚合查询
        dateHis.subAggregation(agg2);
        sourceBuilder.aggregation(dateHis);
        //创建请求
        request.source(sourceBuilder);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Map<String, Aggregation> a = aggregations.getAsMap();
        Aggregation b = a.get(args1);
        ParsedDateHistogram c = (ParsedDateHistogram) b;
        for (Histogram.Bucket bucket1 : c.getBuckets()) {
            Terms terms2 = bucket1.getAggregations().get(args2);
            LinkedHashMap<String, Long> map1 = new LinkedHashMap<>();
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                map1.put(bucket2.getKeyAsString(), bucket2.getDocCount());
            }
            map.put(bucket1.getKeyAsString(), map1);
        }
        return map;
    }

    /**
     * 日期图聚合
     *
     * @return map
     */
    public LinkedHashMap<String, Long> aggOneArgs(QueryBuilder queryBuilder, String args1, int i, String index) throws IOException {
        LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        //设置要查询的索引
        SearchRequest request = new SearchRequest().indices(index);
        //构建搜索
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //添加搜索长度
        sourceBuilder.size(0);
        //添加搜索条件
        sourceBuilder.query(queryBuilder);
        //设置要聚合的字段以及条数
        //设置该次聚合的名称 terms(args1)
        //以及要聚合的字段field(args1 + ".keyword") 添加keyword是对字段进行不分词查询。
        TermsAggregationBuilder agg1 = AggregationBuilders.terms(args1).field(args1).size(i);
        //添加聚合查询
        sourceBuilder.aggregation(agg1);
        //创建请求
        request.source(sourceBuilder);
        //发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = response.getAggregations();
        Terms terms1 = aggregations.get(args1);
        for (Terms.Bucket bucket1 : terms1.getBuckets()) {
            map.put(bucket1.getKeyAsString(), bucket1.getDocCount());
        }
        return map;
    }

    public void parseHits(SearchHit[] hits, List<DetailTable> list) {
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            DetailTable detailTable = new DetailTable();
            detailTable.setTableId(String.valueOf(sourceAsMap.get(ConstantUtils.ID)));
            detailTable.setCategoryLevel(String.valueOf(sourceAsMap.get(ConstantUtils.CATEGORY_LEVEL)));
            detailTable.setAllPackage(String.valueOf(sourceAsMap.get(ConstantUtils.PKG_NAME)));
            detailTable.setVersion(String.valueOf(sourceAsMap.get(ConstantUtils.VERSION)));

            detailTable.setTestDate(String.valueOf(
                    ProjectUtils.timeTurn(String.valueOf(sourceAsMap.get(ConstantUtils.TASK_START_TIME)))));
            String duration = ProjectUtils.timeCut(String.valueOf(sourceAsMap.get(ConstantUtils.TASK_START_TIME)),
                    String.valueOf(sourceAsMap.get(ConstantUtils.TASK_END_TIME)));
            detailTable.setTestDuration(duration);
            detailTable.setTestStatus(String.valueOf(sourceAsMap.get(ConstantUtils.TEST_STATUS)));
            if (sourceAsMap.get(ConstantUtils.BUILD_LOGS) != null) {
                JSONObject json = (JSONObject) JSONObject.toJSON(sourceAsMap.get(ConstantUtils.BUILD_LOGS));
                JSONObject first = (JSONObject) JSONObject.toJSON(json.get(ConstantUtils.FIRST));
                JSONObject second = (JSONObject) JSONObject.toJSON(json.get(ConstantUtils.SECOND));
                detailTable.setFirstBuildLog(PROJECT_PATH + first.get(ConstantUtils.BUILD_LOG));
                detailTable.setSecondBuildLog(PROJECT_PATH + second.get(ConstantUtils.BUILD_LOG));
                if (first.get(ConstantUtils.SIZE) != null){
                    String firstSize = first.get(ConstantUtils.SIZE).toString();
                    long l;
                    if (firstSize.contains(",")){
                        l = Long.parseLong(firstSize.replaceAll(",",""));
                    }else {
                        l = Long.parseLong(firstSize);
                    }
                    detailTable.setFirstLogSize(ProjectUtils.readableFileSize(l));
                }
                if (second.get(ConstantUtils.SIZE) != null) {
                    String firstSize = second.get(ConstantUtils.SIZE).toString();
                    long l2;
                    if (firstSize.contains(",")){
                        l2 = Long.parseLong(firstSize.replaceAll(",",""));
                    }else {
                        l2 = Long.parseLong(firstSize);
                    }
                    detailTable.setSecondLogSize(ProjectUtils.readableFileSize(l2));
                }
            }
            if (sourceAsMap.get("rpms") != null) {
                JSONObject json = (JSONObject) JSONObject.toJSON(sourceAsMap.get("rpms"));
                for(String key : json.keySet()){
                    JSONObject rpm = (JSONObject) JSONObject.toJSON(json.get(key));
                    JSONObject buildInfos = (JSONObject) JSONObject.toJSON(rpm.get("build_infos"));
                    JSONObject first = (JSONObject) JSONObject.toJSON(buildInfos.get("first"));
                    String firstBuildInfo = first.get("buildinfo").toString();
                    String firstHashKey = first.get("hashkey").toString();
                    JSONObject second = (JSONObject) JSONObject.toJSON(buildInfos.get("second"));
                    String secondBuildInfo = second.get("buildinfo").toString();
                    String secondHashKey = second.get("hashkey").toString();
                    BuildInfo buildInfo = new BuildInfo();
                    buildInfo.setFirstBuildInfo(PROJECT_PATH + firstBuildInfo);
                    buildInfo.setFirstHashkey(firstHashKey);
                    buildInfo.setSecondBuildInfo(PROJECT_PATH + secondBuildInfo);
                    buildInfo.setSecondHashkey(secondHashKey);
                    detailTable.getBuildInfos().add(buildInfo);
                    String rpmName = key;
                    String status = rpm.get("test_status").toString();
                    String result = ProjectUtils.testResult(rpmName, status);
                    detailTable.getTestResult().add(result);
                    List<String> list1 = JSONObject.parseArray(rpm.getJSONArray("diffoscope_logs").toJSONString(), String.class);
                    for (int i=0 ; i<list1.size() ; i++){
                        list1.set(i, PROJECT_PATH + list1.get(i));
                    }
                    detailTable.getDiffoscopeLogs().add(list1);
                    System.out.println(1);
                }
            }else {
                detailTable.getTestResult().add("failing to build");
            }
            list.add(detailTable);
            System.out.println(hit);
        }
    }
}
