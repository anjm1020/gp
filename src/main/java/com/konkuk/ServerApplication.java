package com.konkuk;

import com.konkuk.controller.SocketController;
import com.konkuk.core.banner.BannerPrinter;
import com.konkuk.core.socket.SocketMeta;

public class ServerApplication {
    public static void main(String[] args) {
        SocketController socketController = new SocketController();

        SocketMeta clientSocket = new SocketMeta(8001, "client-socket");
        SocketMeta aiSocket = new SocketMeta(8002, "ai-socket");

        socketController.registerSocket(clientSocket);
        socketController.registerSocket(aiSocket);

        BannerPrinter.run();
        socketController.run();
    }
}
