package org.sunso.keypoint.springboot2.biz.keypoint.cache.enums;

import lombok.Getter;

@Getter
public enum LocalCacheTypeEnum {
    empty("empty", "无"),
    caffeine("caffeine", "caffeine")

    ;
    private String type;
    private String remark;

    private LocalCacheTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
