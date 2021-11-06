import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.LinkedList;

public class PostRequestHandler implements Runnable {
  private Connection connection;
  private int basicQos = 1;

  public PostRequestHandler(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void run() {
    try {
      Channel channel = connection.createChannel();
      channel.basicQos(basicQos);
      channel.queueDeclare("post", false, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        consume(message);
//                System.out.println(" [x] Received '" + message + "'");
      };
      channel.basicConsume("post", true, deliverCallback, consumerTag -> {});
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void consume(String message) {
    SkierServletPostRequest request = new Gson().fromJson(message, SkierServletPostRequest.class);
    if (!main.map.containsKey(request.getSkier_id())) {
      main.map.put(request.getSkier_id(), new LinkedList<>());
    }

    main.map.get(request.getSkier_id()).add(request);
  }
}