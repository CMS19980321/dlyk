package com.hncu.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础查询父类
 * @Author caimeisahng
 * @Date 2024/6/26 4:06
 * @Version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseQuery {
    private String Token;//jwt

    private String filterSQL; //数据权限的SQL过滤条件:具体到例子中为 tu.id = 2
}
