package com.ljf.rabbitmq.workfair;

import com.ljf.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = ConnectionUtils.getConnection();

        // 获取 channel
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);  // 保证一次只发送一个

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("[2] Recv msg: " + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[2] done.");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        boolean autoAck = false; // 自动应答 false
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

}
