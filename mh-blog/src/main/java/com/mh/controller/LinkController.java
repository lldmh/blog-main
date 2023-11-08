package com.mh.controller;

import com.mh.domain.ResponseResult;
import com.mh.service.LinkService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dmh
 * @Date 2023/7/17 19:32
 * @Version 1.0
 * @introduce
 */
@RestController
@RequestMapping("/link")
@Api(tags = "友联",description = "友联相关接口")
public class LinkController {

    @Autowired
    private LinkService linkService;

    //查询所有友联
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
