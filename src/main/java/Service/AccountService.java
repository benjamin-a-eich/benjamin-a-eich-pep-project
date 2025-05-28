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
        // Check to see if there is an existing account.
        if(this.accountDAO.queryUser(account)){
            // if there is, we can't add a new one.
            return null;
        }
        // If there is not, then add the new account;
        return this.accountDAO.registerAccount(account);
    }

    public Account loginAccount(Account account) {
        return this.accountDAO.validateAccount(account);
    }
}
