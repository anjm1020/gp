package com.konkuk.controller;

import com.konkuk.core.socket.SocketMeta;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketController {

    private List<SocketMeta> socketMetaList;

    public SocketController() {
        socketMetaList = new ArrayList<>();
    }

    public void registerSocket(SocketMeta meta) {

        if (hasKey(meta.getSocketName())) {
            throw new IllegalArgumentException("Conflict socketName:" + meta.getSocketName());
        }
        socketMetaList.add(meta);
    }

    private boolean hasKey(String key) {
        return socketMetaList.stream()
                .filter(meta -> meta.getSocketName().equals(key))
                .findAny()
                .isPresent();
    }

    public void run() {
        for (SocketMeta socketMeta : socketMetaList) {
            new Thread(new SocketRunner(socketMeta)).start();
        }
    }

    static class SocketRunner implements Runnable {
        private final SocketMeta socketMeta;

        public SocketRunner(SocketMeta socketMeta) {
            this.socketMeta = socketMeta;
        }

        @Override
        public void run() {
            int port = socketMeta.getPort();

            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println(socketMeta.getSocketName() + " : Listening on " + port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println(new StringBuilder()
                            .append("[Conn Success] : ")
                            .append(socketMeta.getSocketName())
                            .appendCodePoint('\n')
                            .append("port:" + socket.getPort() + '\n')
                            .append("host-port:" + socket.getLocalPort())
                            .toString());
                }
            } catch (BindException e) {
                System.out.println("[Failed to Binding Socket on " + port + "] - " + e.getMessage());
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
