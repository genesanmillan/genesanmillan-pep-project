package DAO;

import java.sql.*;

import Util.ConnectionUtil;
import Model.Account;

public class LoginDAO {

    /**
     * This method checks the login credentials and retrieves the account if successful.
     *
     * @param account Account object containing username and password
     * @return Account object if login is successful, null otherwise
     */
    public Account login(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Select the given username and password from account
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Enter parameters from the account object
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();

            // If there's a data with the proper credentials
            if (rs.next()) {
                int account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                // Return the account object with the account ID
                return new Account(account_id, username, password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        // If unsuccessful, return null
        return null;
    }
}
