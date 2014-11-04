package system

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory

class AMQPConnection(host: String) {
  val factory = new ConnectionFactory
  factory.setHost(host)
  val connection = factory.newConnection()
  
  def createChannel(prefetchCount:Int): Channel = {
    val channel = connection.createChannel()
    channel.basicQos(prefetchCount)
    channel
  }
  
  def terminate(channels : Set[Channel]){
    for(c <- channels) c.close()
    connection.close
  }
}