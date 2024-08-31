package com.hncu.util;

import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 带有缓存的查询工具的方法
 * @Author caimeisahng
 * @Date 2024/7/13 3:24
 * @Version 1.0
 */
public class CacheUtils {

    /**
     * 带有缓存的查询工具方法
     * Supplier<T> ：jdk1.8提供的四大功能性函数之一，生产者
     * Consumer<T> ：jdk1.8提供的四大功能性函数之一，消费者
     * @param cacheSelector
     * @param databaseSelector
     * @return
     * @param cacheSave
     * @param <T>
     *
     *
     *
     */

    public static <T> T getCacheData(Supplier<T> cacheSelector, Supplier<T> databaseSelector, Consumer<T> cacheSave){
        //从redis中查询
        T data = cacheSelector.get();
        //如果redis中未查询到
        if (ObjectUtils.isEmpty(data)){
        //if (Objects.isNull(data)){
            //从数据库查询
            data = databaseSelector.get();
            //如果数据库库查询到数据
            //if (!ObjectUtils.isEmpty(data)){
            if (!ObjectUtils.isEmpty(data)){
                //把数据放入到redis中
                cacheSave.accept(data);

            }
        }
        return data;
    }
}
