package com.hncu.query;

import lombok.Data;

/**
 * @Author caimeisahng
 * @Date 2024/7/21 23:26
 * @Version 1.0
 */

@Data
public class ActivityRemarkQuery extends BaseQuery{
    private Integer id;
    private  Integer activityId;
    private  String noteContent;
}
