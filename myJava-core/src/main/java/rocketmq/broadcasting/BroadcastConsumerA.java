package rocketmq.broadcasting;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import rocketmq.connection.RocketMQPushConsumer;

import java.util.List;

public class BroadcastConsumerA {

	public static void main(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		// 消息模型，支持以下两种：集群消费(clustering)，广播消费(broadcasting)
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.setConsumerGroup("broadcast");
		consumer.subscribe("topicTest", "TagA || TagC || TagD");
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf(Thread.currentThread().getName() + " consumerA Receive New Messages: " + msgs + "%n");
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.out.printf("Broadcast ConsumerA Started.%n");
	}

}
