package org.sunso.keypoint.springboot2.operate.log.biz;

/**
 * @author sunso520
 * @Title:BizData
 * @Description: <br>
 * @Created on 2024/5/9 16:12
 */
public interface BizData<B> {

    String getBizData(B primaryValue);

}
