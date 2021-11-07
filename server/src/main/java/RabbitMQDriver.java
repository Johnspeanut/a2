import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQDriver {
  private static String ipAddress = "ec2-100-26-208-245.compute-1.amazonaws.com";
  private static String userName = "admin";
  private static String passwd = "admin";
//  private static String vhost = System.getenv("rabbitmq_vhost");

  public static ConnectionFactory getConnectionFactory() {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(ipAddress);
    factory.setUsername(userName);
    factory.setPassword(passwd);
//    factory.setVirtualHost(vhost);
    factory.setPort(5672);

    return factory;
  }
}