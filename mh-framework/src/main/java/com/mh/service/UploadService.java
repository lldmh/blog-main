package com.mh.service;

import com.mh.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author dmh
 * @Date 2023/7/21 15:08
 * @Version 1.0
 * @introduce  图片上传
 */
public interface UploadService {
    //图片上传
    ResponseResult uploadImg(MultipartFile img);
}
