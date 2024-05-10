package org.sunso.keypoint.springboot2.operate.log.biz;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author sunso520
 * @Title:AbstractBizData
 * @Description: <br>
 * @Created on 2024/5/9 16:14
 */
public abstract class AbstractBizData<B> implements BizData<B> {

    public String getBizData(B primaryValue) {
        try {
            Object bizData =  doGetBizData(primaryValue);
            return toJsonString(bizData);
        }catch (Exception e) {
            return JSONUtil.toJsonStr(new JSONObject()
                    .set("primaryKey", primaryValue)
                    .set("msg", e.getMessage()));
        }
    }

    protected abstract Object doGetBizData(B primaryValue);


    public String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        return JSONUtil.toJsonStr(obj, JSONConfig.create().setIgnoreNullValue(false));
    }
}
