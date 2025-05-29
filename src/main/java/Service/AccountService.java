package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    // Initialize our DAO upon class instantiation. 
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        // Validate the username and password both have valid values
        if(validateUsernameHelper(account.username) == false) {
            return null;
        }

        if(validatePasswordHelper(account.password) == false) {
            return null;
        }

        // Check to see if there is an existing account.
        if(this.accountDAO.queryUsername(account)){
            // if there is, we can't add a new one.
            return null;
        }

        // If there is not, then add the new account;
        return this.accountDAO.registerAccount(account);
    }

    public Account loginAccount(Account account) {
        return this.accountDAO.validateAccount(account);
    }

    public boolean validateUserHelper(int act_id){
        if(this.accountDAO.queryAccountID(act_id)){
            return true;
        } else {
            return false;
        }
    }

    public boolean validateUsernameHelper(String username) {
        // Check if username is blank or null 
        if(username == null || username == "") {
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePasswordHelper(String password) {
        // Check to make sure its not null
        if(password == null) {
            return false;
        } else if (password.length() >= 4) {
            // Here we check to make sure it meets minimum length requirements.
            return true;
        }
        return false;
    }
}
