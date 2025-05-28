package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;

    public SocialMediaController(){
        accountService = new AccountService();
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
}