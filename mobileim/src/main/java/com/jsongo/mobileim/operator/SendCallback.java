package com.jsongo.mobileim.operator;

/**
 * @author jsongo
 * @date 2019/3/10 22:11
 */
public interface SendCallback {
    default void onSuccess() {
    }

    default void onFailed() {
    }
}
