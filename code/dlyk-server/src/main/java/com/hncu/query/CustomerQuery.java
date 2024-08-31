package com.hncu.query;

import lombok.Data;

import java.util.Date;

/**
 * @Author caimeisahng
 * @Date 2024/8/5 10:49
 * @Version 1.0
 */

@Data
public class CustomerQuery extends BaseQuery{
    /**
     * 线索ID
     */
    private Integer clueId;

    /**
     * 选购产品
     */
    private Integer product;

    /**
     * 客户描述
     */
    private String description;

    /**
     * 下次联系时间
     */
    private Date nextContactTime;
}
