package com.example.im.client;

import com.example.im.bean.CharType;
import com.example.im.bean.ChatData;
import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.BootstrapConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;

public class NettyClient {
    private int port;
    private String host;
    private long mRoleId;
    private String mName;
    public SocketChannel socketChannel;
    private Gson mGson = new Gson();

    public NettyClient(int roleId,String name, int port, String host) {
        this.port = port;
        this.host = host;
        this.mRoleId = roleId;
        this.mName = name;
    }

    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });

        ChannelFuture future = bootstrap.connect(host, port);
        try {
            future.sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            ChatData chatData = new ChatData();
            chatData.setId(mRoleId);
            chatData.setName(mName);
            chatData.setMessage("上线消息");
            chatData.setCharType(CharType.ON_LINE);
            socketChannel.writeAndFlush(mGson.toJson(chatData));
            System.out.println("connect server  成功---------");
        }
    }

    public void sendMessage(ChatData chatData) {
        socketChannel.writeAndFlush(mGson.toJson(chatData));
    }


}
