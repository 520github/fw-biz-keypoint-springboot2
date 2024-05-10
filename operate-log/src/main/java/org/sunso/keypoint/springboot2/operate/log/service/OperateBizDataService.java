package org.sunso.keypoint.springboot2.operate.log.service;

import org.sunso.keypoint.springboot2.operate.log.data.OperateLogData;
import org.sunso.keypoint.springboot2.operate.log.enums.OperateBiz;

/**
 * @author sunso520
 * @Title:OperateBizDataService
 * @Description: <br>
 * @Created on 2024/5/9 16:53
 */
public interface OperateBizDataService {

    String handleBeforeData(OperateLogData operateLogData, OperateBiz operateBiz);

    String handleAfterData(OperateLogData operateLogData, OperateBiz operateBiz);
}
