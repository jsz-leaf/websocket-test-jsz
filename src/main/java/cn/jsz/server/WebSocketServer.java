package cn.jsz.server;

import cn.jsz.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket处理创建、推送、接受、关闭类
 * ServerEndpoint 定义websocket的监听连接地址
 *
 * @author hecai
 * @date 2021/10/24
 */

@Component
@ServerEndpoint("/websocket/{id}")
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 用来存放每个客户端对应的 Session 对象, session对象存储着连接信息
     */
    private static final ConcurrentHashMap<Integer, Session> webSocketMap = new ConcurrentHashMap<>();

    private static StringRedisTemplate template;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate template) {
        WebSocketServer.template = template;
    }

    /**
     * 创建连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Integer id) {
        if (Objects.nonNull(webSocketMap.get(id))) {
            // 如果存在就先移除
            webSocketMap.remove(id);
        }
        webSocketMap.put(id, session);
        sendMessage(String.format("%s,%s", id, "连接成功"));
        logger.info(String.format("用户【%s】创建连接成功！", id));
    }

    /**
     * 根据消息体内容发送消息
     */
    public void sendMessage(String messageBody) {
        String[] split = messageBody.split(",");
        Integer id = Integer.parseInt(split[0]);
        String message = split[1];
        Session session = webSocketMap.get(id);
        try {
            // 服务端推送消息给监听的客户端
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受消息
     */
    @OnMessage
    public void onMessage(String message) {
        template.convertAndSend(RedisConfig.REDIS_CHANNEL, message);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("id") Integer id) {
        try {
            webSocketMap.remove(id).close();
            logger.info(String.format("用户【%s】关闭连接成功！", id));
        } catch (IOException e) {
            logger.error(String.format("用户【%s】关闭连接失败！", id));
        }
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
    }
}
