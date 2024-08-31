package com.hncu.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author caimeisahng
 * @Date 2024/7/15 5:09
 * @Version 1.0
 */
@Data
public class ActivityQuery extends BaseQuery{

    private Integer id;

    private Integer ownerId;

    private String name;

    /*
    *  前端传入一个格式为:YYYY-MM-DD HH:mm:ss 的字符串日期，后台接收需要把日期转为java.util.Date类日期，需使用@DateTimeFormat注解
    *
    * */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    //涉及金钱使用BigDecimal类型
    private BigDecimal cost;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 活动描述
     */
    private String description;

}
