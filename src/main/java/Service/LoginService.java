package Service;

import DAO.LoginDAO;
import Model.Account;

public class LoginService {
    // DAO to be utilized for this service
    private LoginDAO dao;

    // Constructor for this Service
    public LoginService() {
        dao = new LoginDAO();
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
