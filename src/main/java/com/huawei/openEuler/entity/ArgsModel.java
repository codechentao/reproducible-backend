package com.huawei.openEuler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 交互参数
 *
 * @author zhangshengjie
 * @since 2022-5-27
 * */
@Data
@AllArgsConstructor
public class ArgsModel {
    String term1;
    String term2;
    String architectures;
    String testSuites;
}
