package org.sunso.keypoint.springboot2.operate.log.biz;

import cn.hutool.json.JSONObject;

/**
 * @author sunso520
 * @Title:AbstractJsonPrimaryKeyBizData
 * @Description: <br>
 * @Created on 2024/5/9 16:23
 */
public abstract class AbstractJsonPrimaryKeyBizData extends AbstractBizData<JSONObject> {

    protected Object doGetBizData(JSONObject primaryValue) {
        return doGetStringPrimaryKeyBizData(primaryValue);
    }

    protected abstract Object doGetStringPrimaryKeyBizData(JSONObject primaryValue);
}
