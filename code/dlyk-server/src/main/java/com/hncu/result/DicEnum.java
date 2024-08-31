package com.hncu.result;

/**
 * @Author caimeisahng
 * @Date 2024/7/28 22:42
 * @Version 1.0
 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public enum DicEnum {
    APPELLATION("appellation"),
    SOURCE("source"),

    CLUESTATENAME("clueState"),

    INTENTIONSTATE("intentionState"),

    NEEDLOAN("needLoan"),

    PRODUCT("product"),

    ACTIVITY("activity")


    ;


    @Getter
    @Setter
    private String code;
}
