package com.huawei.openEuler.entity;

import lombok.Data;

/**
 * @author zhangshengjie
 */
@Data
public class IssueModel {
    String accessToken;
    String owner;
    String repo;
    String title;
    String issueType;
    String body;
    String assignee;
    String collaborators;
    String milestone;
    String labels;
    String program;
    String securityHole;
}
