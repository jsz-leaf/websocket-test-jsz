package cn.jsz.config;

import cn.jsz.server.WebSocketServer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息监听对象，接收订阅消息
 */
@Component
@Slf4j
public class RedisReceiver implements MessageListener {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 处理接收到的订阅消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 订阅的频道名称
        String channel = new String(message.getChannel());
        String msg = "";
        try {
            msg = new String(message.getBody());
            if (!StringUtils.isEmpty(msg)) {
                if (RedisConfig.REDIS_CHANNEL.endsWith(channel)) {
                    webSocketServer.sendMessage(msg);
                } else {
                    // todo 处理其他订阅的消息
                }
            } else {
                log.info("消息内容为空，不处理。");
            }
        } catch (Exception e) {
            log.error("处理消息异常：" + e);
            e.printStackTrace();
        }
    }
}
