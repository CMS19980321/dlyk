package com.hncu.result;

import lombok.*;

/**
 * 枚举类
 *
 * 数得过来的东西，可以用枚举来定义
 *
 * 一年有12个月，一周有7天，那我们的状态码信息是数得过来的，几十个，最多一两个百个
 */

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum CodeEnum {

    OK(200, "成功"),

    FAIL(500, "失败"),


    LOGIN_JWT_IS_EMPTY(901, "请求jwt为空"),

    LOGIN_JWT_IS_ILLEGAL(902, "请求jwt不合法"),

    LOGIN_JWT_IS_EXPIRE(903, "请求jwt已过期"),

    LOGIN_JWT_NO_MATCH(904, "请求jwt不正确"),

    DATA_ACCESS_EXCEPTION(500,"数据库操作失败"),
    ACCESS_DENIED(500,"权限不足"),
    ;


    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;
}
