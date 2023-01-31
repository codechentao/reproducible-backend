package com.huawei.openEuler.entity;

import lombok.Data;

/**
 * overview数据
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
@Data
public class StatisticalTable {
    String categoryLevel = "0(0.00%)";
    String allPackage = "0(0.00%)";
    String reproducible = "0(0.00%)";
    String unreproducible = "0(0.00%)";
    String failingToBuild = "0(0.00%)";
    String inDepwaitState = "0(0.00%)";
    String downloadProblems = "0(0.00%)";
    String blacklisted = "0(0.00%)";
    String unknownState = "0(0.00%)";
}
