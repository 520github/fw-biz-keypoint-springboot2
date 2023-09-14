
# 查询本地所有缓存key
curl http://localhost:9091/method/cache/keys/local/caffeine/true


# 删除某个缓存key
curl http://localhost:9091/method/cache/remove/redis/caffeine/true/{key}

http://localhost:9091/method/cache/remove/redis/caffeine/true/cache_method_default_296da80dc988573f381fd778ea267e20


# 清空所有本地缓存key
curl http://localhost:9091/method/cache/clear/local/caffeine/true

# 根据key前缀删除分布式缓存
curl http://localhost:9091/method/cache/remove/distribute/redis/cache_method