package cn.cansluck.utils.net;

import cn.cansluck.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static cn.cansluck.utils.net.SocketPool.*;
import static cn.cansluck.utils.net.SocketHandler.createKey;

// 注入容器
@Component
// 表明这是一个websocket服务的端点
@ServerEndpoint("/net/websocket/{name}")
public class SocketEndPoint {

    private static final Logger log = LoggerFactory.getLogger(SocketEndPoint.class);

    private static IUserService userService;

    @Autowired
    public void setUserService(IUserService userService){
        SocketEndPoint.userService = userService;
    }

    @OnOpen
    public void onOpen(@PathParam("name") String name,  Session session) {
        log.info("有新的连接：{}", session);
        add(createKey(name), session);
        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
            if (item.getKey().equals(name)) {
                SocketHandler.sendMessageAll("<div style='width: 100%; float: left;'>用户【" + name + "】已上线</div>", name);
            }
        }
        log.info("在线人数：{}",count());
        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
            log.info("12: {}", item.getKey());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        if (message.contains("[allUsers]")) {
            String userInfo = message.substring(message.indexOf("[allUsers]")).replace("[allUsers]----------", "");
            SocketHandler.sendMessageAll( "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + userInfo + "群发消息</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + message.substring(0, message.indexOf("[")) + "</div>", userInfo);
        } else {
            String acceptUser = message.substring(message.indexOf("[") + 1, message.lastIndexOf("]"));
            String sendUser = message.substring(message.lastIndexOf("-") + 1, message.length());
            Session userSession;
            for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
                if (item.getKey().equals(acceptUser)) {
                    userSession = item.getValue();
                    String userInfo = message.substring(0, message.indexOf("["));
                    SocketHandler.sendMessage(userSession, "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + sendUser + "</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + userInfo + "</div>");
                }
            }
        }
        log.info("有新消息： {}", message);
    }

    @OnClose
    public void onClose(@PathParam("name") String name,Session session) {
        log.info("连接关闭： {}", session);
        remove(createKey(name));
        log.info("在线人数：{}", count());
        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : sessionMap().entrySet()){
            log.info("12: {}", item.getKey());
        }
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();//可以精确到时分秒
        SocketHandler.sendMessageAll("<div style='width: 100%; float: left;'>[" + df.format(date) + "] " + name + "已离开聊天室</div>", name);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("退出发生异常： {}", e.getMessage());
        }
        log.info("连接出现异常： {}", throwable.getMessage());
    }
}