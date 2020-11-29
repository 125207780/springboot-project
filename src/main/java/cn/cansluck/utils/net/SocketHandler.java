package cn.cansluck.utils.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static cn.cansluck.utils.net.SocketPool.sessionMap;

/**
 * WebSocket动作类
 *
 * @author Cansluck
 */
public class SocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    /**
     * 根据key和用户名生成一个key值，简单实现下
     * @param name 发送人
     * @return 返回值
     */
    public static String createKey(String name){
        return name;
    }

    /**
     * 给指定用户发送信息
     * @param session session
     * @param msg 发送的消息
     */
    public static void sendMessage(Session session, String msg) {
        if (session == null)
            return;
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null)
            return;
        try {
            basic.sendText(msg);
        } catch (IOException e) {
            log.error("消息发送异常，异常情况: {}", e.getMessage());
        }
    }

    /**
     * 给所有的在线用户发送消息
     * @param message  发送的消息
     * @param username 发送人
     */
    public static void sendMessageAll(String message, String username) {
        log.info("广播：群发消息");
        // 遍历map，只输出给其他客户端，不给自己重复输出
        sessionMap().forEach((key, session) -> {
            if (!username.equals(key)) {
                sendMessage(session, message);
            }
        });
    }
}