package org.sunso.keypoint.springboot2.biz.keypoint.cache.enums;

import lombok.Getter;

@Getter
public enum DistributeCacheTypeEnum {
    empty("empty", "无"),
    redis("redis", "redis")
    ;
    private String type;
    private String remark;

    DistributeCacheTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
