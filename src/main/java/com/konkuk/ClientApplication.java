package com.konkuk;

import com.konkuk.core.logger.DateFormatLogger;
import com.konkuk.core.logger.SystemLogger;
import com.konkuk.core.socket.SimpleMessageConvertor;
import com.konkuk.data.response.ClientResponse;

import java.io.*;
import java.net.Socket;

public class ClientApplication {

    public static void main(String[] args) {
        SystemLogger logger = new DateFormatLogger();
        try {
            Socket socket = new Socket("127.0.0.1", 8001);
            logger.info("[Connected]");
            logger.info("socket.getPort() = " + socket.getPort());
            logger.info("socket.getLocalPort() = " + socket.getLocalPort());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String data = "TEST MS G2 as";
            out.write(data);
            out.newLine();
            out.flush();
            logger.info("Send data = " + data);

            String input = in.readLine();
            byte[] bytes = input.getBytes();
            SimpleMessageConvertor convertor = new SimpleMessageConvertor();
            ClientResponse obj = convertor.toObject(bytes, ClientResponse.class);
            logger.info("obj.getScript() = " + obj.getScript());
            logger.info("obj.isFinish() = " + obj.isFinish());
            logger.info("obj.getDialog_id() = " + obj.getDialog_id());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
