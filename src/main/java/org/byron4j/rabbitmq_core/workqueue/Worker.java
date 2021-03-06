package org.byron4j.rabbitmq_core.workqueue;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * @author Byron.Y.Y
 * @date 2016年10月1日
 */
public class Worker {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		// 创建连接工厂类
		ConnectionFactory factory = new ConnectionFactory();
		// 设置需要连接到的RabbitMQ主机地址
		factory.setHost("localhost");
		// 创建一个连接
		Connection connection = factory.newConnection();
		// 创建一个信道
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		final Consumer consumer = new DefaultConsumer(channel) {
			  @Override
			  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
			    String message = new String(body, "UTF-8");

			    System.out.println(" [x] Received '" + message + "'");
			    try {
			      doWork(message);
			    } catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
			      System.out.println(" [x] Done");
			    }
			  }
			};
			boolean autoAck = true; // acknowledgment is covered below
			channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}
	
	private static void doWork(String task) throws InterruptedException {
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}
}
