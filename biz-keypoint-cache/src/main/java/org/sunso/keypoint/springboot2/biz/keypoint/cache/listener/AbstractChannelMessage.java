package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractChannelMessage {
    @JsonIgnore
    public abstract String getChannel();
}
