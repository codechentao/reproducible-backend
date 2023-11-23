package com.huawei.openEuler.entity.index;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

public class Rpm {
    @JSONField(name = "test_status")
    private String testStatus;

    @JSONField(name = "category_level")
    private String categoryLevel;

    @JSONField(name = "diffoscope_logs")
    private List<Object> diffoscopeLogs;

    @JSONField(name = "build_infos")
    private Map<String, BuildStep> buildInfos;

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public List<Object> getDiffoscopeLogs() {
        return diffoscopeLogs;
    }

    public void setDiffoscopeLogs(List<Object> diffoscopeLogs) {
        this.diffoscopeLogs = diffoscopeLogs;
    }

    public Map<String, BuildStep> getBuildInfos() {
        return buildInfos;
    }

    public void setBuildInfos(Map<String, BuildStep> buildInfos) {
        this.buildInfos = buildInfos;
    }
}