import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
@WebServlet(name="SkierServlet")
public class SkierServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // check we have a URL!
//    if (urlPath == null || urlPath.isEmpty()) {
//      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//      response.getWriter().write("missing paramterers");
//      return;
//    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

//    if (!isUrlValid(urlParts)) {
//      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//    } else {
//      response.setStatus(HttpServletResponse.SC_OK);
//      // do any sophisticated processing with urlParts which contains all the url params
//      // TODO: process url params in `urlParts`
      response.getWriter().write("It works!");
//    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String[] urlPathList = request.getPathInfo().split("/");
    if (!isSkierPostUrlValid(urlPathList)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      String responseJSON = new Gson().toJson(new String("The URL path is invalid"));
      response.getWriter().write(responseJSON);
      return;
    }

    try {
      // push the request to the queue
      int resortID = Integer.parseInt(urlPathList[1]);
      int season = Integer.parseInt(urlPathList[3]);
      int day = Integer.parseInt(urlPathList[5]);
      int skierID = Integer.parseInt(urlPathList[7]);
      SkierServletPostResponse postResponse = new SkierServletPostResponse(resortID, season, day, skierID);
      BlockingChannelPool channelPool = new BlockingChannelPool();
      channelPool.init();
      Channel channel = channelPool.take();
      channel.queueDeclare("post", false, false, false, null);
      channel.basicPublish("", "post", null, new Gson().toJson(postResponse).getBytes());
      channelPool.add(channel);

      // response to the user
      response.setStatus(HttpServletResponse.SC_OK);
      LiftRide liftRide = new Gson().fromJson(getBodyContent(request), LiftRide.class);
      response.getWriter().write(new Gson().toJson(liftRide));
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      String responseJSON = new Gson().toJson(new String("The type of the input parameter doesn't match"));
      response.getWriter().write(responseJSON);
    } catch (IllegalArgumentException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      String responseJSON = new Gson().toJson(new String(e.getMessage()));
      response.getWriter().write(responseJSON);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return true;
  }

  private boolean isSkierPostUrlValid(String[] urlPathList) {
    if (urlPathList.length != 8) {
      return false;
    }

    return urlPathList[2].equals("seasons") && urlPathList[4].equals("days")
        && urlPathList[6].equals("skiers");
  }

  private String getBodyContent(HttpServletRequest req) throws IOException {
    BufferedReader reqBodyBuffer = req.getReader();
    StringBuilder reqBody = new StringBuilder();
    String line;
    while ((line = reqBodyBuffer.readLine()) != null) {
      reqBody.append(line);
    }

    return reqBody.toString();
  }
}
