package org.sunso.keypoint.springboot2.operate.log.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sunso.keypoint.springboot2.operate.log.biz.BizDataPortal;
import org.sunso.keypoint.springboot2.operate.log.data.OperateLogData;
import org.sunso.keypoint.springboot2.operate.log.enums.OperateBiz;
import org.sunso.keypoint.springboot2.operate.log.enums.OperatePrimaryKeySourceEnum;


/**
 * @author sunso520
 * @Title:OperateBizDataServiceImpl
 * @Description: <br>
 * @Created on 2024/5/9 16:54
 */
@Service
@Slf4j
public class OperateBizDataServiceImpl implements OperateBizDataService {
    @Autowired
    private BizDataPortal bizDataPortal;

    @Override
    public String handleBeforeData(OperateLogData operateLogData, OperateBiz operateBiz) {
        Object primaryKeyValue = getPrimaryKeyValue(operateLogData, operateBiz);
        return bizDataPortal.getBizData(operateBiz, primaryKeyValue);
    }

    @Override
    public String handleAfterData(OperateLogData operateLogData, OperateBiz operateBiz) {
        Object primaryKeyValue = getPrimaryKeyValue(operateLogData, operateBiz);
        return bizDataPortal.getBizData(operateBiz, primaryKeyValue);
    }


    private Object getPrimaryKeyValue(OperateLogData operateLogData, OperateBiz operateBiz) {
        //OperateBizEnum operateBizEnum = getOperateBizEnum(operateLog);
        OperatePrimaryKeySourceEnum source = operateBiz.getPrimaryKeySourceEnum();
        Object primaryKeyValue = null;
        // 从方法参数中提取
        if (OperatePrimaryKeySourceEnum.METHOD_PARAMETER == source) {
            primaryKeyValue =  getPrivateKeyValueFromMethodParameter(operateLogData, operateBiz);
        }
        // 从返回结果中提取
        else if (OperatePrimaryKeySourceEnum.METHOD_RESULT == source) {
            primaryKeyValue = getPrivateKeyValueFromMethodResult(operateLogData, operateBiz);
        }
        else if (OperatePrimaryKeySourceEnum.LOGIN_USER_ID == source) {
            Long loginUserId = getLoginUserId();
            if (loginUserId != null) {
                primaryKeyValue = loginUserId;
            }
        }
        if (primaryKeyValue == null || StrUtil.isEmpty(primaryKeyValue.toString())) {
            log.info("getPrimaryKeyValue get primaryKeyValue is empty from operateLogData[{}], operateBiz[{}]", operateLogData, operateBiz);
        }
        return primaryKeyValue;
    }

    private Long getLoginUserId() {
        return null;
    }

    private Object getPrivateKeyValueFromMethodParameter(OperateLogData operateLogData, OperateBiz operateBiz) {
        return getValueByJsonPath(operateLogData.getJavaMethodArgs(), operateBiz.getPrimaryKeyJsonPath());
    }

    private Object getPrivateKeyValueFromMethodResult(OperateLogData operateLogData, OperateBiz operateBiz) {
        String primaryKeyJsonPath = operateBiz.getPrimaryKeyJsonPath();
        // 没有指定jsonPath, 说明返回的data就是需要的值
        if (StrUtil.isEmpty(primaryKeyJsonPath)) {
            return operateLogData.getResultData();
        }
        return getValueByJsonPath(operateLogData.getResultData(), primaryKeyJsonPath);
    }

    private Object getValueByJsonPath(String jsonData, String jsonPath) {
        try {
            if (StrUtil.isEmpty(jsonData)) {
                log.error("getValueByJsonPath find jsonData is empty");
                return null;
            }
            if (StrUtil.isEmpty(jsonPath)) {
                log.error("getValueByJsonPath find jsonPath is empty");
                return null;
            }
            Object value = JSONUtil.parseObj(jsonData).getByPath(jsonPath);
            if (value == null) {
                log.error("getValueByJsonPath not found jsonPath[{}] from jsonData[{}]", jsonPath, jsonData);
            }

            return value;
        }catch (Exception e) {
            log.error("getValueByJsonPath exception get jsonPath[{}] from jsonData[{}]", jsonPath, jsonData, e);
        }
        return null;
    }
}
