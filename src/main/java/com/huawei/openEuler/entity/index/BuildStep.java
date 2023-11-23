package com.huawei.openEuler.entity.index;

import com.alibaba.fastjson.annotation.JSONField;

public class BuildStep {
    @JSONField(name = "hashkey")
    private String hashkey;

    @JSONField(name = "buildinfo")
    private String buildinfo;

    public String getHashkey() {
        return hashkey;
    }

    public void setHashkey(String hashkey) {
        this.hashkey = hashkey;
    }

    public String getBuildinfo() {
        return buildinfo;
    }

    public void setBuildinfo(String buildinfo) {
        this.buildinfo = buildinfo;
    }
}