package global

import com.rabbitmq.client.ConnectionFactory
import system.AMQPConnection
import system.AMQPActions
import com.rabbitmq.client.QueueingConsumer

object Global extends App {
  val QueueName = "test"
  val Host = "localhost"
  val PrefetchCount = 1
  val channel = new AMQPConnection(Host).createChannel(PrefetchCount)
  for(consumer <- 1 to 5){
	  AMQPActions.queueListener(consumer+" worker", (v : Array[Byte]) => {} , QueueName, channel)
  }
  AMQPActions.queueListener("error", (v : Array[Byte]) => throw new Exception("STUFF") , QueueName, channel)
}

 