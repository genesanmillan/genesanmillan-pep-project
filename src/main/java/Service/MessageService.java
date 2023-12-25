package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    // DAO to be utilized for this service
    MessageDAO dao;

    // Constructor for this Service
    public MessageService() {
        dao = new MessageDAO();
    }

    /**
     * This method adds a message into the database
     * 
     * Constraints:
     * message_text is not blank and under 255 characters
     * posted_by is an existing user
     * 
     * @param message Message object to be used
     * @return Message object if successful, null otherwise
     */
    public Message createNewMessage(Message message) {
        boolean validMessageText = !message.getMessage_text().isBlank() && message.getMessage_text().length() < 255;
        boolean userExists = dao.getAllPostedID().contains(message.getPosted_by());


        // Check constraints
        if(validMessageText && userExists)
        {
            // Return the message object if successful
            return dao.createNewMessage(message);
        }

        // Return null if unsuccessful
        return null;
    }

    /**
     * 
     * @return all messages as a list
     */
    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    /**
     * This method returns the Message object with the given message_id
     * @param message_id message_id of the message to look for
     * @return Message object with the given message_id, null if not found
     */
    public Message getOneMessage(int message_id){
        return dao.getOneMessage(message_id);
    }

    /**
     * This method deletes the Message object with the given message_id
     * @param message_id message_id of the message to delete
     * @return Deleted Message object, null if no deletion
     */
    public Message deleteMessage(int message_id){
        return dao.deleteMessage(message_id);
    }

    /**
     * This method updates the message in the database with the given message_id
     * 
     * Constraints:
     * message_id exists in the database
     * message_text is not blank and not over 255 characters
     * 
     * @param message_id message_id of the Message to update
     * @param message_text string to be used in replacing the message_text
     * @return Message object of the message updated
     */
    public Message updateMessage(int message_id, String message_text){
        // Add constraints
        boolean validID = getOneMessage(message_id) != null;
        boolean validText = !message_text.isBlank() && message_text.length() <= 255;

        System.out.println("validID: " + validID);
        System.out.println("validText: " + validText);

        // Return the updated Message object if parameters are valid
        if(validID && validText){
            return dao.updateMessage(message_id, message_text);
        }

        // Return null if no object is returned or parameters didn't pass the constraints
        return null;
    }

    /**
     * This method gets all the messages posted by the specified user
     * @param account_id user to get the messages from
     * @return list of all the user's messages, null if empty
     */
    public List<Message> getAllUserMessages(int account_id) {
        return dao.getAllUserMessages(account_id);
    }
}
