import Controller.SocialMediaController;
import DAO.UserRegistrationDAO;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.stop();
        app.start(8080);

        ConnectionUtil.resetTestDatabase();

        Main mainInstance = new Main();

        // try {
        //     mainInstance.createMessageSuccessful();
        // } catch (IOException | InterruptedException e) {
        //     e.printStackTrace();
        // }
    }

    public void createMessageSuccessful() throws IOException, InterruptedException {

        HttpClient webClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest postMessageRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":1, " +
                        "\"message_text\": \"hello message\", " +
                        "\"time_posted_epoch\": 1669947792}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse response = webClient.send(postMessageRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        System.out.println("Status: " + status);     
        System.out.println(response.body().toString());

        ObjectMapper om = new ObjectMapper();
        Message expectedResult = new Message(2, 1, "hello message", 1669947792);
        
        System.out.println(expectedResult.toString());

        Message actualResult = om.readValue(response.body().toString(), Message.class);
        
        System.out.println(actualResult.toString());
    }
}
