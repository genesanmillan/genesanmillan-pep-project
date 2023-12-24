package DAO;

import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;

import Model.Account;

public class UserRegistrationDAO {

    /** 
        This method retrieves all the username.
        Note: Method only retrieves necessary data (username) for the user registration aspect of the program

        @return Arraylist<String> of usernames
    */
    public ArrayList<String> getAllUsername()
    {
        Connection connection = ConnectionUtil.getConnection();

        // Arraylist of string to store the usernames
        ArrayList<String> usernames = new ArrayList<>();

        try {
            // Get the username column from account
            String sql = "SELECT username FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the list
            while(rs.next()){
                usernames.add(rs.getString("username"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return usernames;
    }

    /**
     * This method inserts the provided account into the database
     * @param Account object to be added
     * @return Account object that is successfully added, null if unsuccessful
     */
    public Account insertAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Insert the Account into the account table
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Enter parameters into the ?s
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            // Return account if insertion is successful with the auto-generated account_id
            ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
            if(keyResultSet.next()){
                int generated_account_id = (int) keyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        // Returns null if insertion is unsuccessful
        return null;
    }

    /**
     * This method retrieves and prints all accounts from the database.
     */
    public void printAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Get all columns from the account table
            String sql = "SELECT * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the result set and print account details
            while (rs.next()) {
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                System.out.println("Account ID: " + accountId + ", Username: " + username + ", Password: " + password);
            }
        } catch (SQLException e) {
            // Log the exception
            e.printStackTrace();
        }
    }
}
