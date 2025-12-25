package com.example.fraud.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisStateStore {

    private final RedisTemplate<String, Object> redis;

    public RedisStateStore(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    /* -------------------------------------
       VELOCITY CHECKS
       Key: velocity:{accountId}
       Tracks number of txns in last 60 seconds
    -------------------------------------- */
    public int incrementVelocity(String accountId) {
        String key = "velocity:" + accountId;
        Long count = redis.opsForValue().increment(key);
        redis.expire(key, Duration.ofSeconds(60));
        return count != null ? count.intValue() : 0;
    }

    public int getVelocity(String accountId) {
        String key = "velocity:" + accountId;
        Object val = redis.opsForValue().get(key);
        return val == null ? 0 : Integer.parseInt(val.toString());
    }


    /* -------------------------------------
       DEVICE → ACCOUNT RELATIONSHIP
       Key: device:{deviceId}:accounts
       Tracks how many accounts use this device
    -------------------------------------- */
    public int addDeviceAccount(String deviceId, String accountId) {
        String key = "device:" + deviceId + ":accounts";
        redis.opsForSet().add(key, accountId);
        redis.expire(key, Duration.ofDays(1));
        return redis.opsForSet().size(key).intValue();
    }

    public int getDeviceAccountCount(String deviceId) {
        String key = "device:" + deviceId + ":accounts";
        Long size = redis.opsForSet().size(key);
        return size != null ? size.intValue() : 0;
    }


    /* -------------------------------------
       PAST DECLINE COUNT
       Key: declines:{accountId}
       Tracks declines for last 24h
    -------------------------------------- */
    public int incrementDeclines(String accountId) {
        String key = "declines:" + accountId;
        Long count = redis.opsForValue().increment(key);
        redis.expire(key, Duration.ofHours(24));
        return count != null ? count.intValue() : 0;
    }

    public int getPastDeclines(String accountId) {
        String key = "declines:" + accountId;
        Object v = redis.opsForValue().get(key);
        return v == null ? 0 : Integer.parseInt(v.toString());
    }
}