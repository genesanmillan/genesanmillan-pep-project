package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    /** 
        This method retrieves all the posted IDs.
        @return Integer list of posted IDs
    */
    public ArrayList<Integer> getAllPostedID()
    {
        // Arraylist of integers to store the posted IDs
        ArrayList<Integer> messages = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();
        try {
            // Get the account_id column from account table
            String sql = "SELECT account_id FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the list
            while(rs.next()){
                messages.add(rs.getInt("account_id"));
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return messages;
    }

    /**
     * This method inserts a new message into the table
     * @param message Message object to be inserted into the table
     * @return Message object inserted if successful, null otherwise
     */
    public Message createNewMessage(Message message)
    {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Insert Message into the table 
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Enter parameters into the ?s
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            // Return message if insertion is successful with the auto-generated message_id
            ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
            if(keyResultSet.next()) {
                int generated_message_id = keyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Returns null if creation is unsuccessful
        return null;
    }

    /**
     * 
     * @return all messages as a list
     */
    public List<Message> getAllMessages()
    {
        // List to store the messages
        List<Message> messages = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();
        try {
            // Get all the messages from the table
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the result set
            while(rs.next()){
                // Get parameters from the columns
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");

                // Instantiate new message with the parameters
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);

                // Add new message to the list
                messages.add(message);
            }   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
    
    /**
     * This method returns the Message object with the given message_id
     * @param message_id message_id of the Message to look for
     * @return Message object with the given message_id, null if not found
     */
    public Message getOneMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Enter proper sql to get the data with the given message_id
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Enter parameter into the ?
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            // If found in the result set
            while(rs.next()){
                // Get parameters from the columns
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");

                // Return the Message object created with the parameters
                return new Message(message_id, posted_by, message_text, time_posted_epoch);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Return null if Message not found
        return null;
    }

    /**
     * This method deletes the Message object with the given message_id
     * @param message_id message_id of the Message to delete
     * @return Deleted Message object, null if no deletion
     */
    public Message deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getOneMessage(message_id);

        if(message != null) {
            try {
                // Enter proper sql to get the data with the given message_id
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                // Enter parameter into the ?
                preparedStatement.setInt(1, message_id);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // Returns the original message found if deleted, returns null if not
        return message;
    }

    /**
     * This method updates the message in the database with the given message_id
     * @param message_id message_id of the Message to update
     * @param message_text string to be used in replacing the message_text
     * @return Message object of the message updated
     */
    public Message updateMessage(int message_id, String message_text){
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Enter proper sql to update the data with the given message_id and message_text
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Enter parameters into the ?s
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();

            // Return the updated Message object
            return getOneMessage(message_id);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Return null if no message is updated
        return null;
    }

    /**
     * This method gets all the messages posted by the specified user
     * @param account_id user to get the messages from
     * @return list of all the user's messages, null if empty
     */
    public List<Message> getAllUserMessages(int account_id) {
        // List to store the messages
        List<Message> messages = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();
        try {
            // Enter proper sql to get the messages with the given account_id
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Enter parameter into ?
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the result set
            while(rs.next()){
                // Get parameters from the columns
                int message_id = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");

                // Add the new message to the list
                messages.add(new Message(message_id, account_id, message_text, time_posted_epoch));
            }

            // Return the list of messages
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
