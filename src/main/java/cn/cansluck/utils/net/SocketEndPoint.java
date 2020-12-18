package cn.cansluck.utils.net;

import cn.cansluck.model.SocketMsg;
import cn.cansluck.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * 用户连接方法
     * @param name 连接用户
     * @param session session
     */
    @OnOpen
    public void onOpen(@PathParam("name") String name,  Session session) {
        log.info("有新的连接：{}", session);
        // SocketPool类add方法，有新的连接，将新的连接加进来
        add(createKey(name), session);
        // 调用群发消息方法，告知所有人有人上线了
        SocketHandler.sendMessageAll("<div style='width: 100%; float: left;'>用户【" + name + "】已上线</div>", name);
        log.info("在线人数：{}",count());

        // 往控制台输出提示信息：在线用户 和 哪些用户
        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
            log.info("{}", item.getKey());
        }
    }

    /**
     * 发送消息方法（私聊和群聊）
     * @param message 消息对象
     * @throws IOException 异常
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        // 获取到前端返回的消息，消息是JSON字符串，将消息转换为SocketMsg实体类对象
        ObjectMapper objectMapper = new ObjectMapper();
        SocketMsg socketMsg = objectMapper.readValue(message, SocketMsg.class);

        // 群发
        if (socketMsg.getSendType().equals("1")) {
            // 群发给除了自己以外的用户
            SocketHandler.sendMessageAll( "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + socketMsg.getSendUser() + "群发消息</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + socketMsg.getMsg() + "</div>", socketMsg.getSendUser());
        }
        // 私聊
        else {
            Session userSession;
            /*
             * 1. 遍历WebSocket连接用户池
             * 2. 判断如果是接收方，则调用私聊方法，并退出循环
             */
            for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
                if (item.getKey().equals(socketMsg.getAcceptUser())) {
                    userSession = item.getValue();
                    // 调用单一消息发送方法，给对应用户发送消息
                    SocketHandler.sendMessage(userSession, "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + socketMsg.getSendUser() + "</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + socketMsg.getMsg() + "</div>");
                    // 只给某一个发送之后，就不需要再循环发送了
                    break;
                }
            }
        }
        log.info("有新消息： {}", message);
    }

    /**
     * 连接关闭方法
     * @param name 关闭用户
     * @param session session
     */
    @OnClose
    public void onClose(@PathParam("name") String name,Session session) {
        log.info("连接关闭： {}", session);
        // 将退出连接的用户调用remove方法，将其从WebSocket连接池中去掉，聊天里面就不会给这个对象发消息了
        remove(createKey(name));
        // 下面是在控制台的提示输出信息
        log.info("在线人数：{}", count());
        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : sessionMap().entrySet()){
            log.info("12: {}", item.getKey());
        }
        // 给所有人群发某用户退出连接
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        SocketHandler.sendMessageAll("<div style='width: 100%; float: left;'>[" + df.format(date) + "] " + name + "已离开聊天室</div>", name);
    }

    /**
     * 聊天室异常方法
     * @param session session
     * @param throwable 异常类
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            // session关闭
            session.close();
        } catch (IOException e) {
            // 异常捕获及提示
            log.error("退出发生异常： {}", e.getMessage());
        }
        // 给出异常提示
        log.info("连接出现异常： {}", throwable.getMessage());
    }
}