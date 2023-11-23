package com.huawei.openEuler.entity.index;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * @author zhanglei
 * @since 2023-11-23
 */
public class ReproduciblePackageInfo {
    @JSONField(name = "arch")
    private String arch;

    @JSONField(name = "submit_time")
    private String submitTime;

    @JSONField(name = "pkg_name")
    private String pkgName;

    @JSONField(name = "pkg_path")
    private String pkgPath;

    @JSONField(name = "category_level")
    private String categoryLevel;

    @JSONField(name = "upstream_repo")
    private String upstreamRepo;

    @JSONField(name = "upstream_branch")
    private String upstreamBranch;

    @JSONField(name = "group_id")
    private String groupId;

    @JSONField(name = "task_start_time")
    private String taskStartTime;

    @JSONField(name = "task_end_time")
    private String taskEndTime;

    @JSONField(name = "commit_id")
    private String commitId;

    @JSONField(name = "build_logs")
    private Map<String, BuildLog> buildLogs;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "build_infos")
    private Map<String, BuildStep> buildInfos;

    @JSONField(name = "rpms")
    private Map<String, Rpm> rpms;

    @JSONField(name = "test_status")
    private String testStatus;

    @JSONField(name = "version")
    private String version;

    @JSONField(name = "test_result")
    private String testResult;

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgPath() {
        return pkgPath;
    }

    public void setPkgPath(String pkgPath) {
        this.pkgPath = pkgPath;
    }

    public String getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(String categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getUpstreamRepo() {
        return upstreamRepo;
    }

    public void setUpstreamRepo(String upstreamRepo) {
        this.upstreamRepo = upstreamRepo;
    }

    public String getUpstreamBranch() {
        return upstreamBranch;
    }

    public void setUpstreamBranch(String upstreamBranch) {
        this.upstreamBranch = upstreamBranch;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Map<String, BuildLog> getBuildLogs() {
        return buildLogs;
    }

    public void setBuildLogs(Map<String, BuildLog> buildLogs) {
        this.buildLogs = buildLogs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, BuildStep> getBuildInfos() {
        return buildInfos;
    }

    public void setBuildInfos(Map<String, BuildStep> buildInfos) {
        this.buildInfos = buildInfos;
    }

    public Map<String, Rpm> getRpms() {
        return rpms;
    }

    public void setRpms(Map<String, Rpm> rpms) {
        this.rpms = rpms;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
