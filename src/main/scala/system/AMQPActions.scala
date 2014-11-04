package system

import com.rabbitmq.client.Channel
import com.rabbitmq.client.QueueingConsumer
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.MessageProperties

object AMQPActions {
  def sendMessage(msg: String, queue: String, durable: Boolean = true, channel: Channel) {
    channel.queueDeclare(queue, durable, false, false, null)
    channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, queue.getBytes())
  }
  def queueListener(consumerName: String, method: (Array[Byte]) => Unit, queue: String, channel: Channel) {
    channel.queueDeclare(queue, false, false, false, null).getMessageCount()
    println(s"Create Consumer $consumerName")
    channel.basicConsume(queue,
      false,
      consumerName, new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
          try {
            method(body)
            channel.basicAck(envelope.getDeliveryTag(), false)
          } catch {
            case e: Exception => channel.basicNack(envelope.getDeliveryTag(), false, true);
          }
        }
      })
    println("Consumer Created")
  }
}