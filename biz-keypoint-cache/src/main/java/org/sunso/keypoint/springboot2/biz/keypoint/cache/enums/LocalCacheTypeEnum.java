package org.sunso.keypoint.springboot2.biz.keypoint.cache.enums;

import lombok.Getter;

@Getter
public enum LocalCacheTypeEnum {
    empty("empty", "无"),
    caffeine("caffeine", "caffeine"),
    guava("guava", "guava")

    ;
    private String type;
    private String remark;

    LocalCacheTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public static LocalCacheTypeEnum getByCacheType(String cacheType) {
        for(LocalCacheTypeEnum cacheTypeEnum: LocalCacheTypeEnum.values()) {
            if (cacheTypeEnum.getType().equalsIgnoreCase(cacheType)) {
                return cacheTypeEnum;
            }
        }
        return null;
    }
}
