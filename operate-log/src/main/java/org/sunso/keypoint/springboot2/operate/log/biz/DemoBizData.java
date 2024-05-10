package org.sunso.keypoint.springboot2.operate.log.biz;

/**
 * @author sunso520
 * @Title:DemoBizData
 * @Description: <br>
 * @Created on 2024/5/9 16:26
 */
public class DemoBizData extends AbstractStringPrimaryKeyBizData {
    @Override
    protected Object doGetStringPrimaryKeyBizData(String primaryValue) {
        return "demo";
    }
}
