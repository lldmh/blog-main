package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Author dmh
 * @Date 2023/7/26 19:48
 * @Version 1.0
 * @introduce   后台文件上传接口
 */
@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //上传文件
    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile multipartFile) {
        return uploadService.uploadImg(multipartFile);
    }
}
