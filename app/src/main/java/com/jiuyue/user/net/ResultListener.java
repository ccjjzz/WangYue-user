package com.jiuyue.user.net;

public interface ResultListener<T> {
    void onSuccess(T data);
    void onError(String msg, int code);
}
