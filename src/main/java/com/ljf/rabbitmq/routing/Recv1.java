package com.ljf.rabbitmq.routing;

import com.ljf.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    private static final String EXCHANGE_NAME = "test_exchange_direct";
    private static final String QUEUE_NAME = "test_queue_direct_1";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String routingKey = "error";
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);

        channel.basicQos(1);  // 保证一次只发送一个

        // 定义一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            // 消息到达 触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("[1] Recv msg: " + msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[1] done.");
                    // 手动回执
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false; // 自动应答 false
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
