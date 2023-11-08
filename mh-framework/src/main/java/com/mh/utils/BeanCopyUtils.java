package com.mh.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dmh
 * @Date 2023/7/16 16:50
 * @Version 1.0
 * @introduce bean拷贝工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
    }
    //通过字节码反射的方式创建对象（参数中的V决定返回值中的V的泛型）
    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());   //收集操作
    }
}

