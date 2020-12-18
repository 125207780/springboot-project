package cn.cansluck.utils.net;


import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket连接池类
 *
 * @author Cansluck
 */
public class SocketPool {

    // 在线用户websocket连接池
    private static final Map<String, Session> ONLINE_USER_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 新增一则连接
     * @param key 设置主键
     * @param session 设置session
     */
    public static void add(String key, Session session) {
        if (!key.isEmpty() && session != null){
            ONLINE_USER_SESSIONS.put(key, session);
        }
    }

    /**
     * 根据Key删除连接
     * @param key 主键
     */
    public static void remove(String key) {
        if (!key.isEmpty()){
            ONLINE_USER_SESSIONS.remove(key);
        }
    }

    /**
     * 获取在线人数
     * @return 返回在线人数
     */
    public static int count(){
        return ONLINE_USER_SESSIONS.size();
    }

    /**
     * 获取在线session池
     * @return 获取session池
     */
    public static Map<String, Session> sessionMap() {
        return ONLINE_USER_SESSIONS;
    }
}