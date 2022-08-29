package com.huawei.openEuler.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情表数据
 *
 * @author zhangshengjie
 * @since 2022-5-27
 * */
@Data
public class DetailTable {
    String tableId;
    String categoryLevel;
    String allPackage;
    String version;
    List<String> testResult = new ArrayList<>();
    String testDate;
    String testDuration;
    String firstBuildLog;
    String firstLogSize;
    String secondBuildLog;
    String secondLogSize;
    List<BuildInfo> buildInfos = new ArrayList<>();
    String testStatus;
    List<List> diffoscopeLogs = new ArrayList<>();
}
