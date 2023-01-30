package com.huawei.openEuler.controller;

import com.alibaba.fastjson.JSONObject;
import com.huawei.openEuler.entity.ArgsModel;
import com.huawei.openEuler.entity.DetailTable;
import com.huawei.openEuler.entity.StatisticalTable;
import com.huawei.openEuler.service.EsMindSporeService;
import com.huawei.openEuler.utils.ConstantUtils;
import com.huawei.openEuler.utils.ProjectUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
@RestController
@RequestMapping("/api/mindSpore")
public class EsMindSporeController {
    protected final static Logger logger = LoggerFactory.getLogger(EsMindSporeController.class);

    private static final String INDEX_NAME_LATEST = "latest-repo-mindspore";

    private static final String INDEX_NAME_ALL = "repo-mindspore";

    @Autowired(required = false)
    private EsMindSporeService service;

    @RequestMapping("/reproducible")
    public List<DetailTable> reproducible(@RequestBody ArgsModel argsModel) throws IOException {
        List<DetailTable> detailTables = service.selectById("id", argsModel, INDEX_NAME_LATEST);
        logger.debug("Reproducible api is called");
        return detailTables;
    }

    @RequestMapping("/select")
    public Map<String, Object> select(@RequestBody ArgsModel argsModel, Integer pageNum, Integer pageSize) throws IOException {
        String term1 = argsModel.getTerm1();
        String term2 = argsModel.getTerm2();
        logger.debug("select interface called");
        if (term1 != null && term2 != null) {
            return service.select(ConstantUtils.TEST_STATUS, argsModel, ConstantUtils.CATEGORY_LEVEL, 3, INDEX_NAME_LATEST, pageNum, pageSize);
        } else if (term1 != null && term2 == null) {
            return service.select(ConstantUtils.TEST_STATUS, argsModel, ConstantUtils.CATEGORY_LEVEL, 1, INDEX_NAME_LATEST, pageNum, pageSize);
        } else if (term1 == null && term2 != null) {
            return service.select(ConstantUtils.TEST_STATUS, argsModel, ConstantUtils.CATEGORY_LEVEL, 2, INDEX_NAME_LATEST, pageNum, pageSize);
        } else {
            return service.select(ConstantUtils.TEST_STATUS, argsModel, ConstantUtils.CATEGORY_LEVEL, 5, INDEX_NAME_LATEST, pageNum, pageSize);
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
        ProjectUtils.statisticalResult(map, list);
        logger.debug("statisticalResult api is called");
        return list;
    }

    @RequestMapping("/chartingDate")
    public JSONObject chartingDate(@RequestBody ArgsModel argsModel) throws IOException {
        String term1 = argsModel.getTerm1();
        String architectures = argsModel.getArchitectures();
        String testSuites = argsModel.getTestSuites();
        LinkedHashMap<String, LinkedHashMap<String, Long>> map;
        if (term1 == null) {
            map = service
                .aggDataArgs(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery(ConstantUtils.ARCH, architectures))
                        .must(QueryBuilders.matchQuery(ConstantUtils.UPSTREAM_BRANCH, testSuites)),
                    ConstantUtils.TASK_START_TIME, ConstantUtils.TEST_STATUS, INDEX_NAME_ALL);
        } else {
            map = service
                .aggDataArgs(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(ConstantUtils.CATEGORY_LEVEL, term1))
                        .must(QueryBuilders.termQuery(ConstantUtils.ARCH, architectures))
                        .must(QueryBuilders.termQuery(ConstantUtils.UPSTREAM_BRANCH, testSuites)),
                    ConstantUtils.TASK_START_TIME, ConstantUtils.TEST_STATUS, INDEX_NAME_ALL);
        }
        JSONObject jsonObject = ProjectUtils.chartingDate(map);
        logger.debug("chartingDate api is called");
        return jsonObject;
    }

    @RequestMapping("/countBranch")
    public List<String> countBranch() throws IOException {
        logger.debug("----------------------start------------------");
        try {
            LinkedHashMap<String, Long> map = service.aggOneArgs(QueryBuilders.matchAllQuery(),
                ConstantUtils.UPSTREAM_BRANCH, 10000, INDEX_NAME_LATEST);
            List<String> list = new ArrayList<>();
            for (String key : map.keySet()) {
                if (key.equals("openEuler-20.03-LTS-SP1")) {
                    continue;
                }
                list.add(key);
            }
            logger.debug("countBranch api is called");
            logger.debug("----------------------end------------------");
            return list;

        } catch (Exception e) {
            logger.error("error", e);
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/selectAll")
    public HashMap<String, Object> selectAll() throws IOException {
        logger.debug("selectAll api is called");
        return service.select(ConstantUtils.TEST_STATUS, null, ConstantUtils.CATEGORY_LEVEL, 4, INDEX_NAME_ALL, 1, 10000);
    }
}
