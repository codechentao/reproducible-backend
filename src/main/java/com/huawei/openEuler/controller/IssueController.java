package com.huawei.openEuler.controller;

import com.huawei.openEuler.entity.IssueModel;
import com.huawei.openEuler.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/issue")
public class IssueController {

    protected final static Logger logger = LoggerFactory.getLogger(EsController.class);

    @Autowired(required = false)
    private IssueService service;

    @RequestMapping("/putIssue")
    @ResponseBody
    public String putIssue(@RequestBody IssueModel issueModel){
        return service.putIssue(issueModel);
    }

}
