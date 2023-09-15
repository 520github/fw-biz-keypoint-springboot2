package org.sunso.keypoint.springboot2.biz.keypoint.cache.model;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class ExpireTimeModel {
    private Long expireTime;
    private TimeUnit timeUnit;

    public static ExpireTimeModel newInstance(Long expireTime, TimeUnit timeUnit) {
        ExpireTimeModel expireTimeModel = new ExpireTimeModel();
        expireTimeModel.setExpireTime(expireTime);
        expireTimeModel.setTimeUnit(timeUnit);
        return expireTimeModel;
    }
}
