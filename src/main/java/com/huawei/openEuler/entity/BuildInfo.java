package com.huawei.openEuler.entity;


import lombok.Data;

@Data
public class BuildInfo {
    String firstBuildInfo;
    String firstHashkey;
    String secondBuildInfo;
    String secondHashkey;
}
