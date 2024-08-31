package com.hncu.query;

import lombok.Data;

/**
 * @Author caimeisahng
 * @Date 2024/8/4 20:12
 * @Version 1.0
 */

@Data
public class ClueRemarkQuery extends BaseQuery{
    /**
     * 线索ID
     */
    private Integer clueId;

    /**
     * 跟踪方式
     */
    private Integer noteWay;

    /**
     * 跟踪内容
     */
    private String noteContent;
}
