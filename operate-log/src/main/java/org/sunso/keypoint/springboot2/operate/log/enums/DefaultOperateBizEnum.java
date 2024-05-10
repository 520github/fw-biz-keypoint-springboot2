package org.sunso.keypoint.springboot2.operate.log.enums;

import lombok.AllArgsConstructor;

/**
 * @author sunso520
 * @Title:DefaultOperateBizEnum
 * @Description: <br>
 * @Created on 2024/5/8 09:48
 */
@AllArgsConstructor
public enum DefaultOperateBizEnum implements OperateBiz {
    EMPTY(null, "", null, "主要用于OperateLog注解的默认值，不需要提取主键值"),
    ;

    private OperateBizBeanNameEnum bizBeanNameEnum;
    private String primaryKeyJsonPath;
    private OperatePrimaryKeySourceEnum primaryKeySourceEnum;
    private String remark;

    @Override
    public String getPrimaryKeyJsonPath() {
        return null;
    }

    @Override
    public OperateBizBeanNameEnum getBizBeanNameEnum() {
        return null;
    }

    @Override
    public OperatePrimaryKeySourceEnum getPrimaryKeySourceEnum() {
        return null;
    }
}
