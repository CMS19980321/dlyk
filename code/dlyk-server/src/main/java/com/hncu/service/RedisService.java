package com.hncu.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void setValue(String key, Object value);

    Object getValue(String key);

    Boolean delete(String key);

    Boolean expire(String key, long timeout, TimeUnit unit);
}
