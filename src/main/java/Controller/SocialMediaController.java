package Controller;

import Service.MessageService;
import Service.AccountService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    // Declare and initialize services with the constructor
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getOneMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllUserMessagesHandler);

        return app;
    }

    /**
     * POST: Account handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) throws JsonProcessingException {
        // Map the account
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // Utilize service to add the account
        Account accountToAdd = accountService.addAccount(account);

        // Convert the HTTP body to JSON if successful
        if(accountToAdd != null) {
            context.json(accountToAdd);
        }
        // Set the HTTP response status to 400 if unsuccessful (Client error)
        else {
            context.status(400);
        }
    }

    /**
     * POST: Login handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        // Map the account
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // Utilize service to login
        Account loginAccount = accountService.login(account);

        // Convert the HTTP body to JSON if successful
        if(loginAccount != null)
        {
            context.json(loginAccount);
        }
        // Set the HTTP response status to 401 if unsuccessful (Unauthorized)
        else
        {
            context.status(401);
        }
    }

    /**
     * POST: Create New Message handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createNewMessageHandler(Context context) throws JsonProcessingException {
        // Map the account
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        // Utilize service to login
        Message newMessage = messageService.createNewMessage(message);

        // Convert the HTTP body to JSON if successful
        if(newMessage != null) {
            context.json(newMessage);
        }
        // Set the HTTP response status to 400 if unsuccessful (Client error)
        else {
            context.status(400);
        }
    }

    /**
     * GET: Get all Messages Handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {
        // Get all messages from the service and convert the list into JSON
        List<Message> messages = messageService.getAllMessages();
        context.json(messages != null ? messages : "");
    }

    /**
     * GET: Get one Message Handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getOneMessageHandler(Context context) throws JsonProcessingException {
        // Get the parameter given by the request
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        // Utilize the service method and convert it to JSON
        context.json(messageService.getOneMessage(message_id) != null ? messageService.getOneMessage(message_id) : "");
    }

    /**
     * DELETE: Delete Message Handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        // Get the parameter given by the request
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        // Utilize the service method and convert it to JSON
        Message message = messageService.deleteMessage(message_id);
        context.json(message != null ? message : "");
    }

    /**
     * PATCH: Update Message Handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        // Get the parameter given by the request
        int message_id = Integer.parseInt(context.pathParam("message_id"));
    
        // Deserialize request body into a Message object
        ObjectMapper objectMapper = new ObjectMapper();
        Message requestBody;
    
        try {
            requestBody = objectMapper.readValue(context.body(), Message.class);
        } catch (JsonProcessingException e) {
            // If request body doesn't conform to the Message model, set the response status to 400 (Client error)
            context.status(400);
            return;
        }
    
        // Get the message_text from the request body
        String message_text = requestBody.getMessage_text();
    
        // Utilize the service to update the message
        Message message = messageService.updateMessage(message_id, message_text);
    
        // Convert the updated message to JSON if successful
        if (message != null) {
            context.json(message);
        } 
        // Set the HTTP response status to 400 if unsuccessful (Client error)
        else {
            context.status(400);
        }
    }
    
    /**
     * GET: Get all messages from a user handler
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllUserMessagesHandler(Context context) throws JsonProcessingException {
        // Get the parameter given by the request
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        // Convert the list to JSON
        List<Message> messages = messageService.getAllUserMessages(account_id);
        context.json(messages != null ? messages : "");
    }
}