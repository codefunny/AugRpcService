package com.yzf.controller;

import com.yzf.domain.ActionRequest;
import com.yzf.domain.ActionResponse;
import com.yzf.service.ActionClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActionController {

    @Value("${zookeeper.action-name}")
    private String actionService;

    @Autowired
    private ActionClientService clientService;

    @PostMapping("/action.cgi")
    @ResponseBody
    public ActionResponse action(ActionRequest request) {
        return clientService.action(request,actionService);
    }
}
