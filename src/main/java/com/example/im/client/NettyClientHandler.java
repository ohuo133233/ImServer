package com.example.im.client;

import com.example.im.bean.CharType;
import com.example.im.bean.ChatData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    // 利用写空闲发送心跳检测消息
                    ChatData pingDto = new ChatData();
                    pingDto.setId(1);
                    pingDto.setName("客户端");
                    pingDto.setCharType(CharType.HEART_BEAT);
                    pingDto.setMessage("我是心跳包");
//                    ctx.writeAndFlush(pingDto.toString());
//                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 客户端接收到服务端发的数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object obj) {
        System.out.println("客户端接收到消息: " + obj);
        ReferenceCountUtil.release(obj);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channelActive" + ctx.name());
    }
}
