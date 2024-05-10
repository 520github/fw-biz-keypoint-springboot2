package org.sunso.keypoint.springboot2.operate.log.biz;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sunso.keypoint.springboot2.operate.log.enums.OperateBiz;

/**
 * @author sunso520
 * @Title:BizDataPortal
 * @Description: <br>
 * @Created on 2024/5/9 16:27
 */
@Slf4j
@Service
public class BizDataPortal {

    public String getBizData(OperateBiz operateBiz, Object primaryValue) {
        String bizBeanName = operateBiz.getBizBeanNameEnum().getBeanName();
        BizData bizDataService = SpringUtil.getBean(bizBeanName, BizData.class);
        if (bizDataService == null) {
            log.error("getBizData not found bizBeanName[{}] class", bizBeanName);
            return null;
        }
        return bizDataService.getBizData(primaryValue);
    }
}
