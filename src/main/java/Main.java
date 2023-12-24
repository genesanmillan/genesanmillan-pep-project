import Controller.SocialMediaController;
import DAO.UserRegistrationDAO;
import io.javalin.Javalin;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Service.UserRegistrationService;
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

        try {
            mainInstance.registerUserSuccessful();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Printing Accounts: ");
        (new UserRegistrationService()).printAllAccounts();
    }

    public void registerUserSuccessful() throws IOException, InterruptedException {
        HttpClient webClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"user\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();


        HttpResponse response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        System.out.println("Status: " + status);
        System.out.println("Response Body: " + response.body().toString());

        System.out.println("----------------------------- End of Response -----------------------------");
        
        Account expectedAccount = new Account(2, "user", "password");
        System.out.println("Expected Account: " + expectedAccount.toString());

        Account actualAccount = objectMapper.readValue(response.body().toString(), Account.class);
        System.out.println("Actual Account: " + actualAccount.toString());


    }
}
