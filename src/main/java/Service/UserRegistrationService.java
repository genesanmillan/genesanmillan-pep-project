package Service;

import java.util.ArrayList;

import Model.Account;
import DAO.UserRegistrationDAO;

public class UserRegistrationService {

    // DAO to be utilized for this service
    private UserRegistrationDAO dao;

    // Constructor for this Service
    public UserRegistrationService() {
        dao = new UserRegistrationDAO();
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
     * This method retrieves and prints all accounts from the database using the DAO.
     */
    public void printAllAccounts() {
        dao.printAllAccounts();
    }
}
