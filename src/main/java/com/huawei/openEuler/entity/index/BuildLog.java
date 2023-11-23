package com.huawei.openEuler.entity.index;

import com.alibaba.fastjson.annotation.JSONField;

public class BuildLog {
    @JSONField(name = "size")
    private int size;

    @JSONField(name = "buildlog")
    private String buildlog;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBuildlog() {
        return buildlog;
    }

    public void setBuildlog(String buildlog) {
        this.buildlog = buildlog;
    }
}