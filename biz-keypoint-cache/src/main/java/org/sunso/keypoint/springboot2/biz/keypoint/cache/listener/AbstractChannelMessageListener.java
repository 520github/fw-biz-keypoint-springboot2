package org.sunso.keypoint.springboot2.biz.keypoint.cache.listener;

import cn.hutool.core.util.TypeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.sunso.keypoint.springboot2.common.utils.JacksonJsonUtils;

import java.lang.reflect.Type;

@Slf4j
public abstract class AbstractChannelMessageListener<T extends AbstractChannelMessage> implements MessageListener {

    private final Class<T> messageType;
    private final String channel;

    @Autowired
    private RedisTemplate redisTemplate;

    @SneakyThrows
    protected AbstractChannelMessageListener() {
        this.messageType = getMessageClass();
        this.channel = messageType.newInstance().getChannel();
    }

    public final void onMessage(Message message, byte[] bytes) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        String key = String.valueOf(message.getChannel());
        T value = (T)valueSerializer.deserialize(message.getBody());
        log.info("AbstractChannelMessageListener key[{}], value[{}]", key, value);
        log.info("AbstractChannelMessageListener onMessage message[{}]", message);
        //T messageObject = JacksonJsonUtils.parseObject(message.getBody(), messageType);
        onMessage(value);
    }

    public abstract void onMessage(T message);

    private Class<T> getMessageClass() {
        Type type = TypeUtil.getTypeArgument(getClass(), 0);
        if (type == null) {
            throw new IllegalStateException(String.format("类[%s]需要设置消息类型", getClass().getName()));
        }
        return (Class<T>)type;
    }
}
