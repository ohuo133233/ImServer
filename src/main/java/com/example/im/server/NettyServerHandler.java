package com.example.im.server;

import com.example.im.bean.ChatData;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    private HashMap<Long, ArrayList<ChannelId>> mChannelMap = new HashMap();

    private static Map<Long, Channel> mUserMap = new ConcurrentHashMap();
    private static Map<Long, Channel> mGroupMap = new ConcurrentHashMap<>();
    private Gson mGson = new Gson();


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        println("ID: " + ctx.channel().id() + "Name: " + ctx.name() + " ,-----上线了");
    }


    /**
     * 服务端 接收到 客户端 发的数据
     *
     * @param context
     * @param obj
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, Object obj) {
//        System.out.println("服务端接收到客户端的消息：" + obj);

        // 解析客户端发送的数据
        ChatData chatData = mGson.fromJson(obj.toString(), ChatData.class);

        switch (chatData.getCharType()) {
            case ON_LINE:
                onLine(chatData, context.channel());
                break;
            case PRIVATE_MSG:
                privateMessage(chatData);
                break;
            case CHAT_ROOM:
                chatRoomMessage(chatData);
                break;
            case GROUP:
                groupMessage(chatData);
                break;
            default:
                break;
        }
    }

    private void onLine(ChatData chatData, Channel channel) {
        println("ID:" + chatData.getId() + " ---Name:" + chatData.getName() + "----上线");
        addUser(chatData.getId(), channel);
    }

    /**
     * 群聊
     *
     * @param chatData
     */
    private void groupMessage(ChatData chatData) {

    }

    /**
     * 私聊
     *
     * @param chatData 聊天Data
     */
    private void privateMessage(ChatData chatData) {
        println(chatData.toString());
        Channel user = getUser(chatData.getToID());
        println(mUserMap.toString());
        user.writeAndFlush(mGson.toJson(chatData));
    }

    /**
     * 聊天室
     *
     * @param chatData
     */
    private void chatRoomMessage(ChatData chatData) {

    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println(ctx.channel().id() + "下线了");
        removeUser(ctx.channel().id());
    }

    private void addUser(long id, Channel channel) {
        println("id: " + id);
        println("channel: " + channel.remoteAddress());
        mUserMap.put(id, channel);
        println(mUserMap.toString());
        println(mUserMap.size() + "");
    }

    private Channel getUser(long id) {
        return mUserMap.get(id);
    }

    public void removeUser(ChannelId id) {
        mUserMap.remove(id);
    }

    /**
     * 创建聊天室
     * 判断聊天室是否存在
     * 存在结束逻辑，什么也不做
     * 不存在创建，并把创建人加入聊天室
     *
     * @param id        创建人的ID
     * @param channelId 聊天室ID
     */
    public void createChatRoom(ChannelId id, long channelId) {
        if (mChannelMap.containsKey(channelId)) {
            System.out.println("频道已经存在");
        } else {
            addChatRoom(id, channelId);
        }
    }


    /**
     * 加入聊天室
     */
    public void addChatRoom(ChannelId id, long channelId) {
        ArrayList<ChannelId> list = mChannelMap.get(channelId);
        list.add(id);
    }

    /**
     * 加入群组
     */

    public void addGroup(long groupId, Channel channel) {
//        Channel channel1 = mGroupMap.get(groupId);
    }

    private static void println(String message) {
        System.out.println(message);
    }

}