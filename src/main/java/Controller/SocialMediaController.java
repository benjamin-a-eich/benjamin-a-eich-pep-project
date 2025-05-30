package Controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
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
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageUpdateHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);

        return app;
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account newAccount = om.readValue(ctx.body(), Account.class);
        // call account service next
        Account addedAccount = accountService.addAccount(newAccount);
        if(addedAccount == null) {
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(addedAccount));
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account acct = om.readValue(ctx.body(), Account.class);
        Account validAcct = accountService.loginAccount(acct);

        if(validAcct == null) {
            ctx.status(401);
        } else {
            ctx.status(200);
            ctx.json(om.writeValueAsString(validAcct));
        }
    }

    private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
        // Get data first
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);

        // Validate that an account exists with the given id
        boolean acctExists = accountService.validateUserHelper(message.posted_by);

        // If it does exist call the method to post it
        if(acctExists) {
            Message postedMsg = messageService.addMessage(message);
            if(postedMsg != null) {
                ctx.status(200);
                ctx.json(om.writeValueAsString(postedMsg));
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        
        ctx.json(om.writeValueAsString(messageService.getAllMessages()));
        ctx.status(200);
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        // Get the path parameter
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message returnedMsg = messageService.getMessageByID(messageID);
        
        if (returnedMsg != null) {
            ctx.json(om.writeValueAsString(returnedMsg));
        }
        
        ctx.status(200);
    }
    
    private void getAllMessagesByAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));

        ctx.json(om.writeValueAsString(messageService.getMessagesByAccount(accountID)));
        ctx.status(200);
    }

    private void patchMessageUpdateHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        // Get the path parameter
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));

        // Get the body, which is just supposed to have the updated message text
        Map<String, Object> bodyMap = om.readValue(ctx.body(), new TypeReference<Map<String, Object>>() {});
        String newText = bodyMap.get("message_text").toString();

        Message updatedMessage = messageService.updateMessage(messageID , newText);

        if(updatedMessage != null) {
            ctx.json(om.writeValueAsString(updatedMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        // Get the path parameter
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message deletedMessage = messageService.deleteMessage(messageID);
        if(deletedMessage != null) {
            ctx.json(om.writeValueAsString(deletedMessage));
        }
        
        ctx.status(200);
    }
}