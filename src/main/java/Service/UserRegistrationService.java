package Service;

import java.util.ArrayList;

import Model.Account;
import DAO.UserRegistrationDAO;

public class UserRegistrationService {

    // DAO to be utilized for this service
    private UserRegistrationDAO dao;

    // Constructor for this Service
    public UserRegistrationService()
    {
        dao = new UserRegistrationDAO();
    }

    /**
     * Checks if the username given is available to use
     * @param username string to be checked
     * @return the username availability
    */
    public boolean usernameAvailable(String username)
    {
        // Get usernames from DAO
        ArrayList<String> usernames = dao.getAllUsername();

        // Iterate through the usernames
        for(String i : usernames)
        {
            System.out.println("Comparing given username: " + username + " to " + i);
            // Return false if current string is equals the provided username
            if(i.equalsIgnoreCase(username))
            {
                return false;
            }
        }

        // Returns true if username is available
        return true;
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
        System.out.println("Checking username availability: " + usernameAvailable(account.getUsername()));
        System.out.println("account.getUsername().isBlank(): " + account.getUsername().isBlank());
        System.out.println("account.getPassword().length() >= 4 " + (account.getPassword().length() >= 4));


        // Check constraints in the if-statement
        if(!account.getUsername().isBlank() && usernameAvailable(account.getUsername()) && account.getPassword().length() >= 4)
        {
            System.out.println("CONSTRAINTS PASSED");
            // Return the account if successful
            return dao.insertAccount(account);
        }

        System.out.println("CONSTRAINTS DID NOT PASS");
        return null;
    }

    /**
     * This method retrieves and prints all accounts from the database using the DAO.
     */
    public void printAllAccounts() {
        dao.printAllAccounts();
    }
}
