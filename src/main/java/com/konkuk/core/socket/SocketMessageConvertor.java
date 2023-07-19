package com.konkuk.core.socket;

public interface SocketMessageConvertor {
    <T> byte[] toByte(T object);
    <T> T toObject(byte[] bytes, Class<T> clazz);
}
