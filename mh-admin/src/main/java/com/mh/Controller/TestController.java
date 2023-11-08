package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dmh
 * @Date 2023/7/24 15:33
 * @Version 1.0
 * @introduce   测试
 */
@RestController
@RequestMapping("/content/tag")
public class TestController {

//    @Autowired
//    private TagService tagService;
//
//    @GetMapping("/list")
//    public ResponseResult list(){
//        return ResponseResult.okResult(tagService.list());
//    }
}
