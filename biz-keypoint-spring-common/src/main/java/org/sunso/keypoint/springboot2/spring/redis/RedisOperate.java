package org.sunso.keypoint.springboot2.spring.redis;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisOperate {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisOperate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置含有过期时间的缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间
     * @param timeUnit  过期时间单位
     */
    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 设置含有过期时间的缓存（默认时间单位为秒）
     *
     * @param key
     * @param value
     * @param time
     */
    public void setExpire(final String key, final Object value, final long time) {
        this.setExpire(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 添加到缓存
     *
     * @param key   the key
     * @param value the value
     */
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 判断某个主键是否存在
     *
     * @param key the key
     * @return the boolean
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除key
     *
     * @param keys the keys
     * @return the long
     */
    public boolean del(final String... keys) {
        boolean result = false;
        for (String key : keys) {
            result = redisTemplate.delete(key);
            log.info("delete redis key[{}]", key);
        }
        return result;
    }

    /**
     * 查询匹配keyPatten的所有key
     *
     * @param keyPatten the key patten
     * @return the set
     */
    public Set<String> keys(final String keyPatten) {
        return redisTemplate.keys(keyPatten + "*");
    }

    /**
     * 查询匹配keyPatten的所有key
     * @param keyPattern
     * @return
     */
    public Set<String> keysPatternByScan(final String keyPattern) {
        return keysPatternByScan(keyPattern, 100000);
    }

    /**
     * 查询匹配keyPatten的所有key
     * @param keyPattern
     * @return
     */
    public Set<String> keysPatternByScan(final String keyPattern, final long count) {
        Set<String> keys = new HashSet<>();
        redisTemplate.execute(connection -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(keyPattern.endsWith("*") ? keyPattern : keyPattern + "*")
                    .count(count)
                    .build());
            while (cursor.hasNext() && binaryKeys.size() < count) {
                binaryKeys.add(new String(cursor.next()));
            }
            keys.addAll(binaryKeys);
            return binaryKeys;
        }, true);
        log.info("keysPatternByScan size[{}] by keyPattern[{}]", keys.size(), keyPattern);
        return keys;
    }

    public int delByKeyPattern(final String keyPattern) {
        Set<String> set = keysPatternByScan(keyPattern);
        del(set.toArray(new String[]{}));
        return set.size();
    }

    /**
     * 根据key获取对象
     *
     * @param key the key
     * @return the string
     */
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getString(final String key) {
        return get(key, String.class);
    }

    public <T> T get(final String key, Class<T> tClass) {
        Object result = redisTemplate.opsForValue().get(key);
        log.info("get redis value[{}] by key[{}]", result, key);
        return convertObject(result, tClass);
    }

    public Object getAndSet(final String key, final String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    public <T> T getAndSet(final String key, final String value, Class<T> tClass) {
        Object result = redisTemplate.opsForValue().getAndSet(key, value);
        return convertObject(result, tClass);
    }

    private HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * 对HashMap操作
     *
     * @param key       the key
     * @param hashKey   the hash key
     * @param hashValue the hash value
     */
    public void putHashValue(String key, String hashKey, Object hashValue) {
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取单个field对应的值
     *
     * @param key     the key
     * @param hashKey the hash key
     * @return the hash values
     */
    public Object getHashValues(String key, String hashKey) {
        return opsForHash().get(key, hashKey);
    }

    /**
     * 根据hashKey值删除
     *
     * @param key      the key
     * @param hashKeys the hash keys
     */
    public void delHashValues(String key, Object... hashKeys) {
        opsForHash().delete(key, hashKeys);
    }

    /**
     * key只匹配map
     *
     * @param key the key
     * @return the hash value
     */
    public Map<String, Object> getHashValue(String key) {
        return opsForHash().entries(key);
    }

    /**
     * 批量添加
     *
     * @param key the key
     * @param map the map
     */
    public void putHashValues(String key, Map<String, Object> map) {
        opsForHash().putAll(key, map);
    }

    /**
     * 对某个主键对应的值加一,value值必须是全数字的字符串
     *
     * @param key the key
     * @return the long
     */
    public long incr(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * redis List 引擎
     *
     * @return the list operations
     */
    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * redis List数据结构 : 将一个 value 插入到列表 key 的表头
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long listLeftPush(String key, Object value) {
        return opsForList().leftPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的头元素
     *
     * @param key the key
     * @return the string
     */
    public Object listLeftPop(String key) {
        return opsForList().leftPop(key);
    }

    /**
     * redis List数据结构 :将一个个值 value 插入到列表 key 的表尾(最右边)。
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long listRightPush(String key, Object value) {
        return opsForList().rightPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     *
     * @param key the key
     * @return the string
     */
    public Object listRightPop(String key) {
        return opsForList().rightPop(key);
    }

    /**
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     *
     * @param key the key
     * @return the long
     */
    public Long listSize(String key) {
        return opsForList().size(key);
    }

    /**
     * redis List数据结构 : 根据参数count的值，移除列表中与参数 value 相等的元素
     *
     * @param key   the key
     * @param count    the
     * @param value the value
     */
    public void listRemove(String key, long count, Object value) {
        opsForList().remove(key, count, value);
    }

    /**
     * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void listSet(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     *
     * @param key   the key
     * @param start the start
     * @param end   the end
     * @return the list
     */
    public List<Object> listGet(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    /**
     * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
     *
     * @param key   the key
     * @param index the index
     * @param value the value
     */
    public void listInsert(String key, long index, Object value) {
        opsForList().set(key, index, value);
    }

    private <T> T convertObject(Object result, Class<T> tClass) {
        if (result == null) {
            return null;
        }
        if (result instanceof String) {
            return (T)result;
        }
        if (result instanceof Map) {
            log.info("get redis value[{}] parse object[{}] by JSON ", result, tClass.getName());
            return JSON.parseObject(JSON.toJSONString(result), tClass);
        }
        return (T)result;
    }
}
