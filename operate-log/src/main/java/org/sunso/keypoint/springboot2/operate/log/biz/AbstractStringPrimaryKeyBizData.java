package org.sunso.keypoint.springboot2.operate.log.biz;

/**
 * @author sunso520
 * @Title:AbstractStringPrimaryKeyBizData
 * @Description: <br>
 * @Created on 2024/5/9 16:18
 */
public abstract class AbstractStringPrimaryKeyBizData extends AbstractBizData<String> {

    protected Object doGetBizData(String primaryValue) {
        return doGetStringPrimaryKeyBizData(primaryValue);
    }

    protected abstract Object doGetStringPrimaryKeyBizData(String primaryValue);
}
