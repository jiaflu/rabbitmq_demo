package com.ljf.rabbitmq.routing;

import com.ljf.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        // exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "hello direct.";

        String routingKey = "warning";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        System.out.println("send: " + msg);

        channel.close();
        connection.close();
    }

}
