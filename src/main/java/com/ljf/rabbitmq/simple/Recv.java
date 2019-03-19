package com.ljf.rabbitmq.simple;

import com.ljf.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者获取消息
 */
public class Recv {
    private static final String QUEUE_NAME = "test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();

        // 创建通道
        Channel channel = connection.createChannel();

        // 队列声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 定义消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取到达的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("recv: " + msg);
            }
        };

        boolean autoAck = true;
        // 监听队列
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

}
