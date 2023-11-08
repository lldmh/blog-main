package com.mh.controller;

import com.mh.domain.ResponseResult;
import com.mh.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author dmh
 * @Date 2023/7/21 14:57
 * @Version 1.0
 * @introduce   上传文件接口
 */
@RestController
@Api(tags = "上传",description = "上传相关接口")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
