package com.hncu.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author caimeisahng
 * @Date 2024/8/18 5:24
 * @Version 1.0
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NameValue {
    private String name;
    private Integer value;
}
