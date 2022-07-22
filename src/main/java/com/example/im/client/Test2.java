package com.example.im.client;

import com.example.im.bean.CharType;
import com.example.im.bean.ChatData;

import java.util.Scanner;

public class Test2 {
    private static NettyClient mNettyClient;

    public static void main(String[] args) {
        initNettyClient();
//        sendWordMessage();
    }

    public static void initNettyClient() {
        mNettyClient = new NettyClient(2, "测试2", 8080, "127.0.0.1");
        mNettyClient.start();

        println("请输入指令\n 1.私聊\n 2.群聊\n 3.频道聊天\n");
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();

        switch (next) {
            case "1":
                sendPrivateMessage(2, 2, "测试2", "hello 测试1");
                break;
            case "3":
                sendChannelMessage();
                break;
        }
    }

    private static void println(String message) {
        System.out.println(message);
    }

    public static void sendChannelMessage() {
        ChatData chatData = new ChatData();
        chatData.setId(1);
        chatData.setCharType(CharType.CHAT_ROOM);
        chatData.setName("客户端");
        chatData.setMessage("客户端发送消息");

//        for (int i = 0; i <= 30; i++) {
//            mNettyClient.sendWordMessage(chatData);
//        }

        chatData.setMessage("客户端发送消息1");
        mNettyClient.sendMessage(chatData);
//        chatData.setMessage("客户端发送消息2");
//        mNettyClient.sendWordMessage(chatData);
//        chatData.setMessage("客户端发送消息3");
//        mNettyClient.sendWordMessage(chatData);
    }


    public static void sendPrivateMessage(long id, long toId, String name, String message) {
        ChatData chatData = new ChatData();
        chatData.setId(id);
        chatData.setCharType(CharType.PRIVATE_MSG);
        chatData.setToID(toId);
        chatData.setName(name);
        chatData.setMessage(message);
        mNettyClient.sendMessage(chatData);
    }


}
