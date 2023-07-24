package com.konkuk.core.socket;

import com.konkuk.data.response.ClientResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketRunner implements Runnable {
    private final SocketMeta socketMeta;
    private final SocketMessageConvertor messageConvertor;

    public SocketRunner(SocketMeta socketMeta, SocketMessageConvertor messageConvertor) {
        this.socketMeta = socketMeta;
        this.messageConvertor = messageConvertor;
    }

    @Override
    public void run() {
        int port = socketMeta.port();

        try {
            ServerSocket socket = new ServerSocket(port);
            System.out.println(socketMeta.socketName() + " : Listening on " + port);

            while (true) {

                Socket client = socket.accept();
                System.out.println("client.getInetAddress() = " + client.getInetAddress());
                System.out.println("client.getPort() = " + client.getPort());

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                String received = in.readLine();
                System.out.println("receivedByte = " + received);

                ClientResponse response = ClientResponse.builder()
                        .script("mockup script")
                        .active(true)
                        .finish(false)
                        .dialog_id(1)
                        .type("daily")
                        .build();

                byte[] responseBytes = messageConvertor.toByte(response);
                String data = new String(responseBytes);
                System.out.println("Ready for send : " + data);
                out.write(data);
                System.out.println("Send Finish");
                out.flush();
                client.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
