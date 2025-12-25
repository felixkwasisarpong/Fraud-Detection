package com.example.fraud.service;


import io.micrometer.tracing.Link;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeviceRuleService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_ACCOUNTS_PER_DEVICE = 3;
    private static final int WINDOW_MINUTES = 10;

    public DeviceRuleService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDeviceLinkedToMultipleAccounts(String deviceId, String accountId){
        String key =  "device: " + deviceId + "accounts";
        redisTemplate.opsForSet().add(key,accountId);
        redisTemplate.expire(key, Duration.ofMinutes(WINDOW_MINUTES));
        Long linkedAccounts = redisTemplate.opsForSet().size(key);
        return linkedAccounts != null && linkedAccounts > MAX_ACCOUNTS_PER_DEVICE;
    }

}
