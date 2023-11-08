package com.mh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author dmh
 * @Date 2023/7/21 15:14
 * @Version 1.0
 * @introduce 文件上传路径：时间+UUID+文件名.jpg
 */
public class PathUtils {
    public static String generateFilePath(String fileName){
        //根据日期生成路径   2023/7/21/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}
