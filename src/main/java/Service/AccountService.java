package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    // DAO to be utilized for this service
    private AccountDAO dao;

    // Constructor for this Service
    public AccountService() {
        dao = new AccountDAO();
    }

    /**
     * This method adds an account to the database
     * 
     * Account Constraints:
     * username is not blank and doesn't exist in the database
     * password is atleast 4 characters long
     * 
     * @param account account to be added
     * @return Account object if successful, null otherwise
     * 
     */
    public Account addAccount(Account account)
    {
        // Checks if username is available to use
        boolean usernameAvailable = !dao.getAllUsername().contains(account.getUsername());
        boolean usernameNotBlank = !account.getUsername().isBlank();
        boolean validPassword = account.getPassword().length() >= 4;

        // Check constraints
        if(usernameNotBlank && usernameAvailable && validPassword)
        {
            // Return the account if successful
            return dao.insertAccount(account);
        }

        // Return null if unsuccessful
        return null;
    }

    /**
     * This method checks the login credentials and retrieves the account if successful.
     *
     * @param account Account object containing username and password
     * @return Account object if login is successful, null otherwise
     */

     public Account login(Account account) {
        return dao.login(account);
    }
}
