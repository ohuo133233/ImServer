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


    private static Map<Long, Channel> mUserMap = new ConcurrentHashMap();

    private static Map<Long, ArrayList<Long>> mChatRoom = new ConcurrentHashMap<>();
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
            case CREATE_CHAT_ROOM:
                createChatRoom(chatData.getId(), chatData.getToID());
                break;
            default:
                break;
        }
    }


    private void onLine(ChatData chatData, Channel channel) {
        println("ID:" + chatData.getId() + " ---Name:" + chatData.getName() + "----上线");
        addUser(chatData.getId(), channel);
    }

    // ****************** 用户***********************/
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

    // ****************** 用户***********************/

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

    // ****************** 聊天室 ***********************/

    /**
     * 创建聊天室
     * 判断聊天室是否存在
     * 存在结束逻辑，什么也不做
     * 不存在创建，并把创建人加入聊天室
     *
     * @param channelId  创建人的ID
     * @param chatRoomId 聊天室ID
     */
    public void createChatRoom(Long channelId, long chatRoomId) {
        println("查询聊天室列表: " + mChatRoom.toString());
        boolean isExist = mChatRoom.containsKey(chatRoomId);
        println("创建的聊天室是否存在: " + isExist);
        if (isExist) {
            println("聊天室已经存在");
        } else {
            ArrayList<Long> chatRoomUserList = new ArrayList<>();
            mChatRoom.put(chatRoomId, chatRoomUserList);
            println("创建聊天室成功");
            println("mChatRoom: " + mChatRoom.toString());
        }
        joinChatRoom(channelId, chatRoomId);
    }


    /**
     * 进入聊天室
     *
     * @param channelId
     * @param chatRoomId
     */
    private void joinChatRoom(Long channelId, long chatRoomId) {
        println("加入聊天室: channelId: " + channelId);
        ArrayList<Long> chatRoomList = mChatRoom.get(chatRoomId);
        if (chatRoomList == null) {
            println("没有这个聊天室");
        } else {
            if (chatRoomList.contains(chatRoomId)) {
                println("已经加入过聊天室了");
            } else {
                chatRoomList.add(channelId);
                println("这个聊天室的人列表:" + chatRoomList.toString());
            }

        }

    }


    /**
     * 发送消息到聊天室
     *
     * @param chatData
     */
    private void chatRoomMessage(ChatData chatData) {
        long toID = chatData.getToID();
        ArrayList<Long> channels = mChatRoom.get(toID);
        for (Long channelId : channels) {
            getUser(channelId).writeAndFlush(chatData.getMessage());
        }
    }

    /**
     * 退出聊天室
     *
     * @param channelId
     * @param chatRoomId
     */
    private void quitRoomMessage(Long channelId, long chatRoomId) {
        ArrayList<Long> chatRoomList = mChatRoom.get(chatRoomId);
        if (chatRoomList == null) {
            println("没有这个群");
            return;
        }
        if (chatRoomList.contains(channelId)) {
            chatRoomList.remove(channelId);
            println("退出聊天室成功");
        } else {
            println("没有加入该聊天室");
        }
    }

    // ****************** 聊天室 ***********************/

    /**
     * 群聊
     *
     * @param chatData
     */
    private void groupMessage(ChatData chatData) {

    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println(ctx.channel().id() + "下线了");
        removeUser(ctx.channel().id());
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