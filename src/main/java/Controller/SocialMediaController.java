package Controller;

import Service.UserRegistrationService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    UserRegistrationService userRegistrationService;

    public SocialMediaController() {
        this.userRegistrationService = new UserRegistrationService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        return app;
    }

    /**
     * POST Account handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) throws JsonProcessingException {
        // Map the account
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // Utilize service to add the account
        Account accountToAdd = userRegistrationService.addAccount(account);

        // Convert the HTTP body to JSON if successful
        if(accountToAdd != null)
        {
            context.json(mapper.writeValueAsString(accountToAdd));
        }
        // Set the HTTP response status to 400 if unsuccessful (Client error)
        else
        {
            context.status(400);
        }
    }


}