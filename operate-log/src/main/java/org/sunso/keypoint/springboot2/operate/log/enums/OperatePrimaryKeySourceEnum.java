package org.sunso.keypoint.springboot2.operate.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sunso520
 * @Title:OperatePrimaryKeySourceEnum
 * @Description: <br>
 * @Created on 2024/5/8 09:42
 */
@Getter
@AllArgsConstructor
public enum OperatePrimaryKeySourceEnum {
    METHOD_PARAMETER("methodParameter", "从方法参数中提取"),
    METHOD_RESULT("methodResult", "从方法返回结果中提取"),
    LOGIN_USER_ID("loginUserId", "当前登录用户id")
    ;

    /**
     * 主键key提取来源
     */
    private final String source;
    /**
     * 备注说明
     */
    private final String remark;
}
