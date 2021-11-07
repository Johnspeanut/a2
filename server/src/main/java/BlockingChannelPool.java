import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

public class BlockingChannelPool {
  private static int CHANNELS = 32;
  private static BlockingQueue<Channel> blockingQueue = null;
  private Connection connection;
  private ConnectionFactory connectionFactory;


  public void init() {
    if (blockingQueue != null) {
      return;
    }

    blockingQueue = new LinkedBlockingDeque<>();
    try {
      connectionFactory = RabbitMQDriver.getConnectionFactory();
      connection = connectionFactory.newConnection();
      for (int i = 0; i < CHANNELS; i++) {
        blockingQueue.add(connection.createChannel());
      }
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }


  public Channel take() throws InterruptedException {
    return blockingQueue.take();
  }


  public boolean add(Channel channel) {
    return blockingQueue.add(channel);
  }
}