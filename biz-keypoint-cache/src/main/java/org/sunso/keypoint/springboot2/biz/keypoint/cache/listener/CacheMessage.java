package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import lombok.Data;

import java.io.Serializable;

@Data
public class CacheMessage extends AbstractChannelMessage implements Serializable {

    private String cacheName;
    private Object key;
    private Object value;
    private Integer type = 0;

    @Override
    public String getChannel() {
        return cacheName + "_topic";
    }
}
