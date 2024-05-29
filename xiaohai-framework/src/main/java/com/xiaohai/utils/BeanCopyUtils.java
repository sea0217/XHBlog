package com.xiaohai.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {}


    public static <V> V copyBean(Object source, Class<V> clazz) {
        // 创建目标对象
        V target = null;
        try {
            target = clazz.newInstance();
            // 属性拷贝
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target;
    }

    public static <O,V> List<V> copyBeanList(List<O> sourceList, Class<V> clazz) {
        return sourceList.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
