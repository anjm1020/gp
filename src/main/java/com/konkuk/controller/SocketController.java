package com.konkuk.controller;

import com.konkuk.core.logger.DateFormatLogger;
import com.konkuk.core.socket.SimpleMessageConvertor;
import com.konkuk.core.socket.SocketMessageConvertor;
import com.konkuk.core.socket.SocketMeta;
import com.konkuk.core.socket.SocketRunner;

import java.util.ArrayList;
import java.util.List;

public class SocketController {

    private List<SocketMeta> socketMetaList;
    private final SocketMessageConvertor messageConvertor;

    public SocketController() {
        socketMetaList = new ArrayList<>();
        messageConvertor = new SimpleMessageConvertor();
    }

    public void registerSocket(SocketMeta meta) {

        if (hasKey(meta.socketName())) {
            throw new IllegalArgumentException("Conflict socketName:" + meta.socketName());
        }
        socketMetaList.add(meta);
    }

    private boolean hasKey(String key) {
        return socketMetaList.stream()
                .anyMatch(meta -> meta.socketName().equals(key));
    }

    public void run() {
        for (SocketMeta socketMeta : socketMetaList) {
            new Thread(new SocketRunner(socketMeta, messageConvertor, new DateFormatLogger())).start();
        }
    }
}
