package com.example.im.client;

import com.example.im.bean.CharType;
import com.example.im.bean.ChatData;

public class Test {
    private static NettyClient mNettyClient;

    public static void main(String[] args) {
        initNettyClient();
//        sendWordMessage();
    }

    private static void initNettyClient() {
        mNettyClient = new NettyClient(8083, "127.0.0.1");
        mNettyClient.start();
    }

    private static void sendWordMessage() {
        ChatData chatData = new ChatData();
        chatData.setId(1);
        chatData.setCharType(CharType.CHANNEL);
        chatData.setName("客户端");
        chatData.setMessage("客户端发送消息");

        for (int i = 0; i <= 30; i++) {
            mNettyClient.sendWordMessage(chatData);
        }

        chatData.setMessage("客户端发送消息1");
        mNettyClient.sendWordMessage(chatData);
        chatData.setMessage("客户端发送消息2");
        mNettyClient.sendWordMessage(chatData);
        chatData.setMessage("客户端发送消息3");
        mNettyClient.sendWordMessage(chatData);
    }


    private void sendPrivateMessage(long id,long toId,String name,String message){
        ChatData chatData = new ChatData();
        chatData.setId(id);
        chatData.setCharType(CharType.PRIVATE_MSG);
        chatData.setToID(toId);
        chatData.setName(name);
        chatData.setMessage(message);
        mNettyClient.sendWordMessage(chatData);
    }



}
