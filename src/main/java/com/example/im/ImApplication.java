package com.example.im;

import com.example.im.client.Test;
import com.example.im.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
        NettyServer nettyServer = new NettyServer(8082);
        try {
            nettyServer.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Test.initNettyClient();
        Test.sendCreateChatRoomMessage(1001L);
    }

}
