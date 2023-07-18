package com.konkuk.core.socket;

public class SocketMeta {
    private final int port;
    private final String socketName;

    public SocketMeta(int port, String socketName) {
        this.port = port;
        this.socketName = socketName;
    }

    public int getPort() {
        return port;
    }

    public String getSocketName() {
        return socketName;
    }
}
