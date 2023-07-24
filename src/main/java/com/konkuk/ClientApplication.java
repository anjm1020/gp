package com.konkuk;

import com.konkuk.core.socket.SimpleMessageConvertor;
import com.konkuk.data.response.ClientResponse;

import java.io.*;
import java.net.Socket;

public class ClientApplication {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8001);
            System.out.println("[Connected]");
            System.out.println("socket.getPort() = " + socket.getPort());
            System.out.println("socket.getLocalPort() = " + socket.getLocalPort());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String data = "TEST MS G2 as";
            out.write(data);
            out.newLine();
            out.flush();
            System.out.println("Send data = " + data);

            String input = in.readLine();
            byte[] bytes = input.getBytes();
            SimpleMessageConvertor convertor = new SimpleMessageConvertor();
            ClientResponse obj = convertor.toObject(bytes, ClientResponse.class);
            System.out.println("obj.getScript() = " + obj.getScript());
            System.out.println("obj.isFinish() = " + obj.isFinish());
            System.out.println("obj.getDialog_id() = " + obj.getDialog_id());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
