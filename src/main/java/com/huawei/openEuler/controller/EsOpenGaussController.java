package com.huawei.openEuler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.openEuler.entity.ArgsModel;
import com.huawei.openEuler.entity.DetailTable;
import com.huawei.openEuler.entity.StatisticalTable;
import com.huawei.openEuler.service.EsOpenGaussService;
import com.huawei.openEuler.service.EsService;
import com.huawei.openEuler.utils.ConstantUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 接口
 *
 * @author zhangshengjie
 * @since 2022-5-27
 * */
@RestController
@RequestMapping("/api/openGauss")
public class EsOpenGaussController {
    protected final static Logger logger = LoggerFactory.getLogger(EsOpenGaussController.class);

    private static final String INDEX_NAME_LATEST = "latest-repo-opengauss";

    private static final String INDEX_NAME_ALL = "repo-opengauss";

    @Autowired(required = false)
    private EsOpenGaussService service;

    @RequestMapping("/reproducible")
    public List<DetailTable> reproducible(@RequestBody ArgsModel argsModel) throws IOException {
        List<DetailTable> detailTables = service.selectById("id", argsModel, INDEX_NAME_LATEST);
        logger.debug("Reproducible api is called");
        return detailTables;
    }

    @RequestMapping("/select")
    public List<DetailTable> select(@RequestBody ArgsModel argsModel) throws IOException {
        String term1 = argsModel.getTerm1();
        String term2 = argsModel.getTerm2();
        logger.debug("select interface called");
        if (term1!=null && term2!=null){
            return service.select(ConstantUtils.TEST_STATUS,argsModel,ConstantUtils.CATEGORY_LEVEL, 3, INDEX_NAME_LATEST);
        } else if (term1!=null && term2==null){
            return service.select(ConstantUtils.TEST_STATUS,argsModel,ConstantUtils.CATEGORY_LEVEL,1, INDEX_NAME_LATEST);
        } else if (term1==null && term2!=null){
            return service.select(ConstantUtils.TEST_STATUS,argsModel,ConstantUtils.CATEGORY_LEVEL,2, INDEX_NAME_LATEST);
        } else {
            return service.select(ConstantUtils.TEST_STATUS,argsModel,ConstantUtils.CATEGORY_LEVEL,5, INDEX_NAME_LATEST);
        }
    }

    @RequestMapping("/statisticalResult")
    public List<StatisticalTable> statisticalResult(@RequestBody ArgsModel argsModel) throws IOException {
        String architectures = argsModel.getArchitectures();
        String testSuites = argsModel.getTestSuites();
        List<StatisticalTable> list = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, Long>>
                map = service.aggsOverView(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery(ConstantUtils.ARCH, architectures))
                        .must(QueryBuilders.matchQuery(ConstantUtils.UPSTREAM_BRANCH, testSuites)), INDEX_NAME_LATEST);
        for (String key1 : map.keySet()){
            StatisticalTable statisticalTable = new StatisticalTable();
            statisticalTable.setCategoryLevel(key1);
            int sum = 0;
            for (String key2 : map.get(key1).keySet()) {
                sum += map.get(key1).get(key2);
            }
            statisticalTable.setAllPackage(String.valueOf(sum));
            Long ftb = 0L;
            if (map.get(key1).get("failing to build")!=null){
                ftb = map.get(key1).get("failing to build");

            }
            for (String key2 : map.get(key1).keySet()){
                int value = map.get(key1).get(key2).intValue();
                float value2 = (float) (value * 100) / (sum - ftb);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(value2);
                switch (key2){
                    case (ConstantUtils.REPRODUCIBLE):
                        statisticalTable.setReproducible(value+"("+s+"%)");
                        break;
                    case (ConstantUtils.UNREPRODUCIBLE):
                        statisticalTable.setUnreproducible(value+"("+s+"%)");
                        break;
                    case (ConstantUtils.FAILING_TO_BUILD):
//                        statisticalTable.setFailingToBuild(value+"("+s+"%)");
                        statisticalTable.setFailingToBuild(String.valueOf(value));
                        break;
                    case (ConstantUtils.IN_DEPWAIT_STATE):
                        statisticalTable.setInDepwaitState(value+"("+s+"%)");
                        break;
                    case (ConstantUtils.DOWNLOAD_PROBLEMS):
                        statisticalTable.setDownloadProblems(value+"("+s+"%)");
                        break;
                    case (ConstantUtils.BLACKLISTED):
                        statisticalTable.setBlacklisted(value+"("+s+"%)");
                        break;
                    case (ConstantUtils.UNKOWN_STATE):
                        statisticalTable.setUnknownState(value+"("+s+"%)");
                        break;
                    default:
                }
            }
            list.add(statisticalTable);
        }
        logger.debug("statisticalResult api is called");
        return list;
    }

    @RequestMapping("/chartingDate")
    public JSONObject chartingDate(@RequestBody ArgsModel argsModel) throws IOException {
        String term1 = argsModel.getTerm1();
        String architectures = argsModel.getArchitectures();
        String testSuites = argsModel.getTestSuites();
        LinkedHashMap<String, LinkedHashMap<String, Long>> map;
        if (term1 == null){
            map = service
                    .aggDataArgs(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery(ConstantUtils.ARCH, architectures))
                            .must(QueryBuilders.matchQuery(ConstantUtils.UPSTREAM_BRANCH, testSuites)),
                            ConstantUtils.TASK_START_TIME,ConstantUtils.TEST_STATUS, INDEX_NAME_ALL);
        }else {
            map = service
                    .aggDataArgs(QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery(ConstantUtils.CATEGORY_LEVEL, term1))
                            .must(QueryBuilders.termQuery(ConstantUtils.ARCH, architectures))
                            .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, testSuites)),
                            ConstantUtils.TASK_START_TIME,ConstantUtils.TEST_STATUS, INDEX_NAME_ALL);
        }
        List<String> date = new ArrayList<>();
        List<Long> reproducible = new ArrayList<>();
        List<Long> unReproducible = new ArrayList<>();
        List<Long> failingToBuild = new ArrayList<>();
        List<Long> inDepwaitState = new ArrayList<>();
        List<Long> downloadProblems = new ArrayList<>();
        List<Long> blacklisted = new ArrayList<>();
        List<Long> unknownState = new ArrayList<>();
        for (String key : map.keySet()){
            LinkedHashMap<String, Long> value = map.get(key);
            date.add(key);
            reproducible.add(value.getOrDefault(ConstantUtils.REPRODUCIBLE, 0L));
            unReproducible.add(value.getOrDefault(ConstantUtils.UNREPRODUCIBLE, 0L));
            failingToBuild.add(value.getOrDefault(ConstantUtils.FAILING_TO_BUILD, 0L));
            inDepwaitState.add(value.getOrDefault(ConstantUtils.IN_DEPWAIT_STATE, 0L));
            downloadProblems.add(value.getOrDefault(ConstantUtils.DOWNLOAD_PROBLEMS, 0L));
            blacklisted.add(value.getOrDefault(ConstantUtils.BLACKLISTED, 0L));
            unknownState.add(value.getOrDefault(ConstantUtils.UNKOWN_STATE, 0L));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", date);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", ConstantUtils.REPRODUCIBLE);
        jsonObject1.put("data", reproducible);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", ConstantUtils.UNREPRODUCIBLE);
        jsonObject2.put("data", unReproducible);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name", ConstantUtils.FAILING_TO_BUILD);
        jsonObject3.put("data", failingToBuild);
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("name", ConstantUtils.IN_DEPWAIT_STATE);
        jsonObject4.put("data", inDepwaitState);
        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("name", ConstantUtils.DOWNLOAD_PROBLEMS);
        jsonObject5.put("data", downloadProblems);
        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("name", ConstantUtils.BLACKLISTED);
        jsonObject6.put("data", blacklisted);
        JSONObject jsonObject7 = new JSONObject();
        jsonObject7.put("name", ConstantUtils.UNKOWN_STATE);
        jsonObject7.put("data", unknownState);

        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(jsonObject1);
        jsonObjectList.add(jsonObject2);
        jsonObjectList.add(jsonObject3);
        jsonObjectList.add(jsonObject4);
        jsonObjectList.add(jsonObject5);
        jsonObjectList.add(jsonObject6);
        jsonObjectList.add(jsonObject7);

        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(jsonObjectList);
        jsonObject.put("series", jsonArray);
        logger.debug("chartingDate api is called");
        return jsonObject;
    }

    @RequestMapping("/countBranch")
    public List<String> countBranch() throws IOException {
        LinkedHashMap<String, Long> map = service.aggOneArgs(QueryBuilders.matchAllQuery(),
                ConstantUtils.UPSTREAM_BRANCH, 10000, INDEX_NAME_LATEST);
        List<String> list = new ArrayList<>();
        for (String key: map.keySet()){
            if (key.equals("openEuler-20.03-LTS-SP1")){
                continue;
            }
            list.add(key);
        }
        logger.debug("countBranch api is called");
        return list;
    }

    @RequestMapping("/selectAll")
    public List<DetailTable> selectAll() throws IOException {
        logger.debug("selectAll api is called");
        return service.select(ConstantUtils.TEST_STATUS,null,ConstantUtils.CATEGORY_LEVEL, 4, INDEX_NAME_LATEST);
    }
}
