package com.ybj.utils.exception;

/**
 * Redis 连接异常
 * @createTime 2019-10-21 15:38:13
 */

public class RedisConnectException extends Exception {
    public RedisConnectException(String message) {
        super(message);
    }
}
