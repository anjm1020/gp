package com.konkuk.core.socket;

import com.konkuk.controller.SocketController;
import com.konkuk.data.response.ClientResponse;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SocketRunner Test")
public class SocketRunnerTest {

    static int portNumber;
    static String host;

    @BeforeAll
    static void beforeAll() {
        host = "127.0.0.1";
        SocketController socketController = new SocketController();

//        int start = ((int) (Math.random() * 100000)) % 20000;
//        System.out.println("start = " + start);
//        while(!isAvailable(start)) start++;

        portNumber = 9001;

        SocketMeta testSocket = new SocketMeta(portNumber, "test-socket");
        socketController.registerSocket(testSocket);

        socketController.run();
    }

    static boolean isAvailable(int portNumber) {
        try {
            new Socket(host,portNumber).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Test
    @DisplayName("Connection Test")
    void connectionTest() {
        assertDoesNotThrow(()->new Socket("127.0.0.1",portNumber).close());
    }

    @Nested
    @DisplayName("When connection success")
    class WhenConnectionSuccess {

        Socket socket;

        @BeforeEach
        void beforeAll() {
            try {
                socket = new Socket(host, portNumber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Get Message From Server")
        void getMessageTest() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String data = "TEST_MSG";
                out.write(data);
                out.newLine();
                out.flush();

                String input = in.readLine();
                byte[] bytes = input.getBytes();
                SimpleMessageConvertor convertor = new SimpleMessageConvertor();

                ClientResponse response = convertor.toObject(bytes, ClientResponse.class);
                assertTrue(response.equals(ClientResponse.getTestResponse()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
