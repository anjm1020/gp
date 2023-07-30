package com.konkuk.core.socket;

import com.konkuk.core.logger.SystemLogger;
import com.konkuk.data.response.ClientResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketRunner implements Runnable {
    private final SocketMeta socketMeta;
    private final SocketMessageConvertor messageConvertor;
    private final SystemLogger logger;

    public SocketRunner(SocketMeta socketMeta, SocketMessageConvertor messageConvertor, SystemLogger logger) {
        this.socketMeta = socketMeta;
        this.messageConvertor = messageConvertor;
        this.logger = logger;
    }

    @Override
    public void run() {
        int port = socketMeta.port();

        try {
            ServerSocket socket = new ServerSocket(port);
            logger.info(socketMeta.socketName() + " : Listening on " + port);

            while (true) {

                Socket client = socket.accept();
                logger.info("client.getInetAddress() = " + client.getInetAddress());
                logger.info("client.getPort() = " + client.getPort());

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                String received = in.readLine();
                logger.info("receivedByte = " + received);

                ClientResponse response = ClientResponse.builder()
                        .script("mockup script")
                        .active(true)
                        .finish(false)
                        .dialog_id(1)
                        .type("daily")
                        .build();

                byte[] responseBytes = messageConvertor.toByte(response);
                String data = new String(responseBytes);
                logger.info("Ready for send : " + data);
                out.write(data);
                logger.info("Send Finish");
                out.flush();
                client.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
