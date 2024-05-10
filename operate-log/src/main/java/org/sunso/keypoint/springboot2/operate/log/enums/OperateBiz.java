package org.sunso.keypoint.springboot2.operate.log.enums;

/**
 * @author sunso520
 * @Title:OperateBiz
 * @Description: <br>
 * @Created on 2024/5/8 09:47
 */
public interface OperateBiz {

    String getPrimaryKeyJsonPath();

    OperateBizBeanNameEnum getBizBeanNameEnum();

    OperatePrimaryKeySourceEnum getPrimaryKeySourceEnum();
}
