package com.example.im;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: WebSocketServer
 * @Author: zcq
 * @Date: 2019-06-14 14:37
 */

@Component
@ServerEndpoint("/index/{sid}")  //该注解表示该类被声明为一个webSocket终端
public class WebSocket {

    /**
     * 初始在线人数
     */
    private static AtomicInteger online_num = new AtomicInteger(0);
    /**
     * 线程安全的socket集合，存储在线用户实例
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();
    /**
     * 当前会话
     */
    private Session session;

    /**
     * 接收用户id
     */
    private String sid = "";

    /**
     * 链接创建成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.sid = sid;
        this.session = session;
        webSocketSet.add(this);
        // 在线人数+1
        addOnlineCount();
        System.out.println("有连接加入，当前人数为:" + getOnline_num());
        sendMessageBasic("有连接加入，当前人数为:" + getOnline_num());
    }

    /**
     * 链接关闭调用的方法
     */
    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount(); //在线数减1
        System.out.println("有一链接关闭！当前在线人数为:" + getOnline_num());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("来自客户端的消息:" + message);
        for (WebSocket item : webSocketSet) {
            item.sendMessageBasic(message);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误:" + error);
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessageBasic(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessageAsync(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        System.out.println("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocket item : webSocketSet) {
            //这里能够设定只推送给这个sid的，为null则所有推送
            if (sid == null || "all".equals(sid)) {
                item.sendMessageBasic(message);
            } else if (item.sid.equals(sid)) {
                item.sendMessageBasic(message);
            }
        }
    }

    public static WebSocket getWebSocket(String sid) {
        if (webSocketSet == null || webSocketSet.size() <= 0) {
            return null;
        }
        for (WebSocket item : webSocketSet) {
            if (sid.equals(item.sid)) {
                return item;
            }
        }
        return null;
    }

    public AtomicInteger getOnline_num() {
        return WebSocket.online_num;
    }

    public int subOnlineCount() {
        return WebSocket.online_num.addAndGet(-1);
    }

    public int addOnlineCount() {
        return WebSocket.online_num.addAndGet(1);
    }
}
