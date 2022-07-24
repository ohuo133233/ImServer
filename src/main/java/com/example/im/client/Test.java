package com.example.im.client;

import com.example.im.bean.CharType;
import com.example.im.bean.ChatData;

import java.util.Scanner;

public class Test {
    private static NettyClient mNettyClient;
    private static long mRoleId = 2;
    private static String mName = "测试1";


    public static void initNettyClient() {
        mNettyClient = new NettyClient(mRoleId, mName, 8082, "127.0.0.1");
        mNettyClient.start();

//        println("请输入指令\n 1.私聊\n 2.群聊\n 3.聊天室聊天\n 4.创建聊天室\n ");
//        Scanner scanner = new Scanner(System.in);
//        String next = scanner.next();
//
//        switch (next) {
//            case "1":
//                sendPrivateMessage(mRoleId, 1, "测试1", "hello 测试2");
//                break;
//            case "3":
//                sendChannelMessage();
//                break;
//            case "4":
//                sendCreateChatRoomMessage(1001L);
//                break;
//        }
    }

    public static void sendCreateChatRoomMessage(long chatRoomId) {
        ChatData chatData = new ChatData();
        chatData.setId(mRoleId);
        chatData.setCharType(CharType.CREATE_CHAT_ROOM);
        chatData.setToID(chatRoomId);
        chatData.setMessage("创建聊天室");
        mNettyClient.sendMessage(chatData);
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

    public static void joinChatRoom() {
        ChatData chatData = new ChatData();
        chatData.setId(mRoleId);
        chatData.setCharType(CharType.JOIN_CHAT_ROOM);
        chatData.setToID(1000L);
        chatData.setName("测试2");
        chatData.setMessage("加入聊天室");
        mNettyClient.sendMessage(chatData);
    }


}
