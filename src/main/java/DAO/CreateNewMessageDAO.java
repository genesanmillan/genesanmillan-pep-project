package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

import Model.Message;
import Util.ConnectionUtil;

public class CreateNewMessageDAO {
    public ArrayList<Integer> getAllPostedID()
    {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Integer> messages = new ArrayList<>();

        try {
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
}
