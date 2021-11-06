import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;


public class main {
  private static String ipAddress = "ec2-100-26-208-245.compute-1.amazonaws.com";
  private static String userName = "admin";
  private static String passwd = "admin";
//  private static String vhost = "";
  public static Map<Integer, List<SkierServletPostRequest>> map;
  private static int THREAD_NUMBER = 128;

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
    map = new ConcurrentHashMap<>();

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ipAddress);
    factory.setUsername(userName);
    factory.setPassword(passwd);
//    factory.setVirtualHost(vhost);

    factory.setPort(5672);
    Connection connection = factory.newConnection();

    for (int i = 0; i < THREAD_NUMBER; i++) {
      Thread thread = new Thread(new PostRequestHandler(connection));
      thread.start();
    }

//    CountDownLatch countDownLatch = new CountDownLatch(3);
//    countDownLatch.await();

  }

}