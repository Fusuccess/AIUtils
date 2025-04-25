package top.fusuccess.aidemo.demos.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class PreWarmCache {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void preWarmCache() {
        // 从数据库或其他地方预加载数据并缓存
        redisTemplate.opsForValue().set("你好", "你好，我是AIUtils工具搭建的AI智能聊天机器人，请问有什么可以帮助到您的呢？", 3600, TimeUnit.SECONDS);
    }
}
