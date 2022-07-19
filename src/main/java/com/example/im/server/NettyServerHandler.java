package com.example.im.server;

import com.example.im.bean.ChatData;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.LinkedList;


public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
    // 世界消息
    private LinkedList<ChatData> mWordMessageList = new LinkedList<>();

    private HashMap mUserMap = new HashMap();
    private Gson mGson = new Gson();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive ：channel id" + ctx.channel().id());
        // channel失效，从Map中移除
        mUserMap.remove(ctx.channel().id());
    }


    /**
     * 服务端 接收到 客户端 发的数据
     *
     * @param context
     * @param obj
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object obj) {
        System.out.println("服务端接收到客户端的消息：" + obj);

        // 解析客户端发送的数据
        ChatData chatData = mGson.fromJson(obj.toString(), ChatData.class);
        System.out.println("chatData：" + chatData.toString());

        SocketChannel socketChannel = (SocketChannel) context.channel();
//        socketChannel.writeAndFlush("服务端回复消息");

        switch (chatData.getCharType()) {
            case CHANNEL:
                channelMessage(chatData);
                break;
            case PRIVATE_MSG:
                privateMessage(chatData, socketChannel);
        }


//        ChatData chatData = gson.fromJson(obj.toString(), ChatData.class);
//
//        // 客户端ID
//        long clientId = chatData.getId();
//
//        /**
//         * 心跳包处理
//         */
//        ChatData pingDto = new ChatData();
//        pingDto.setId(clientId);
//        pingDto.setMessage("服务端收到心跳包，返回响应");
//        socketChannel.writeAndFlush(gson.toJson(pingDto));

//
//        Channel channel = NettyChannelMap.get(clientId);
//
//        if (channel==null){
//            /**
//             * 存放所有连接客户端
//             */
//            NettyChannelMap.add(clientId, socketChannel);
//            channel=socketChannel;
//        }
//
//
//        /**
//         * 服务器返回客户端消息
//         */
//        ChatDto returnDto=new ChatDto();
//        returnDto.setClientId(clientId).setMsg("我是服务端，收到你的消息了");
//        channel.writeAndFlush(JSON.toJSONString(returnDto));
//        ReferenceCountUtil.release(obj);
    }

    private void privateMessage(ChatData chatData, SocketChannel socketChannel) {
        socketChannel.parent().remoteAddress();
    }

    private void channelMessage(ChatData chatData) {
        if (mWordMessageList.size() == 10) {
            mWordMessageList.removeFirst();
        }
        mWordMessageList.add(chatData);
        System.out.println("mWordMessageList: " + mWordMessageList);
        System.out.println("mWordMessageList size: " + mWordMessageList.size());
    }
}