package rocketmq.simple;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import rocketmq.connection.RocketMQPushConsumer;

import java.util.List;

public class SyncConsumer {
	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		consumer.setConsumerGroup("sync");
		consumer.subscribe("TopicTest-sync", "TagA || TagC || TagD");

		consumer.registerMessageListener(new MessageListenerOrderly() {


			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				context.setAutoCommit(true);
				System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
				System.out.println(new String(msgs.get(0).getBody()));
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		consumer.start();

		System.out.printf("Consumer Started.%n");
	}
}
