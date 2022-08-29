package com.huawei.openEuler.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.openEuler.controller.EsOpenGaussController;
import com.huawei.openEuler.entity.IssueModel;
import com.huawei.openEuler.utils.HttpRequestUtil;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class IssueService {
    protected final static Logger logger = LoggerFactory.getLogger(IssueService.class);

    private static final String issueApiUrl = "https://gitee.com/api/v5/repos/{owner}/issues";

    public String putIssue(IssueModel issueModel){
        String url = issueApiUrl.replace("{owner}", issueModel.getOwner());
//        String token = getToken();
        Map<String, Object> body = new HashMap<>();
        body.put("access_token",issueModel.getAccessToken());
        body.put("owner",issueModel.getOwner());
        body.put("repo",issueModel.getRepo());
        body.put("title",issueModel.getTitle());
        body.put("issue_type",issueModel.getIssueType());
        body.put("body",issueModel.getBody());
        body.put("assignee",issueModel.getAssignee());
        body.put("collaborators",issueModel.getCollaborators());
        body.put("milestone",issueModel.getMilestone());
        body.put("labels",issueModel.getLabels());
        body.put("program",issueModel.getProgram());
        body.put("security_hole",issueModel.getSecurityHole());
        String bodyStr = JSON.toJSON(body).toString();
        Response response = HttpRequestUtil.postResponse(url, bodyStr);
        return response.message();
    }

    /**
     * 获取token
     *
     * @return token
     */
    public String getToken() {
        String token = "";
        Map<String, String> body = new HashMap<>();
        body.put("userName", "17738417271");
        body.put("passWord", "zr2542646");
        String bodyStr = JSON.toJSON(body).toString();
        Response response = HttpRequestUtil.postResponse("https://cvemanager.test.osinfra.cn/v1/user/login", bodyStr);
        Object object = getDataByResponse("https://cvemanager.test.osinfra.cn/v1/user/login", response);
        if (Objects.nonNull(object)) {
            JSONObject jsonObject = JSON.parseObject(object.toString());
            token = jsonObject.get("Token").toString();
        }
        return token;
    }

    /**
     * 解析Response
     *
     * @param url      路径
     * @param response 请求返回数据
     * @return object
     */
    public Object getDataByResponse(String url, Response response) {
        Object object = null;
        try {
            if (Objects.isNull(response)) {
                logger.error("---------- " + url + "request failed. No parameter is returned. ----------");
                return object;
            }
            if (response.code() != 200) {
                logger.error("---------- " + url + "request failed." + response.message() + " ----------");
                return object;
            }
            if (response.body() != null) {
                String responseStr = response.body().string();
                if (StringUtils.isBlank(responseStr)) {
                    return object;
                }
                JSONObject resultJson = JSON.parseObject(responseStr);
                String code = Objects.nonNull(resultJson.get("errno")) ? resultJson.get("errno").toString()
                        : Objects.nonNull(resultJson.get("code")) ? resultJson.get("code").toString() : "";
                if (StringUtils.isNotBlank(code) && Integer.valueOf(code) != 200) {
                    String errno = code;
                    String errMsg = resultJson.get("errmsg").toString();
                    logger.error("---------- " + url + ",errno:{},errMsg:{} ----------", errno, errMsg);
                    return object;
                }
                object = resultJson.get("body");
            }
        } catch (IOException ioException) {
            logger.error(ioException.getMessage());
        }
        return object;
    }
}
