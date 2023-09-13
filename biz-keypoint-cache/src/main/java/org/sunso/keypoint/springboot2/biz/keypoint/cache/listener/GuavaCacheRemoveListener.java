package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaCacheRemoveListener implements RemovalListener {
    @Override
    public void onRemoval(RemovalNotification rn) {
        log.info("[移除缓存] key:[{}], value[{}], reason:[{}]", rn.getKey(), rn.getValue(), rn.getCause());
    }
}
