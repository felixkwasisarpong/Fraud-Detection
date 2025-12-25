package com.example.fraud.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class VelocityRuleService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_TXN_PER_MIN = 5;
    private static final int WINDOW_SECONDS = 60;

    public VelocityRuleService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    public boolean isVelocityExceeded(String accountId){
        String key = "velocity" + accountId;
        Long count = redisTemplate.opsForValue().increment(key);
        if(count != null && count == 1){
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }
        return count != null && count > MAX_TXN_PER_MIN;
    }
}
