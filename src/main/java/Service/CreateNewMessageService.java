package Service;

import DAO.CreateNewMessageDAO;
import Model.Message;

public class CreateNewMessageService {

    // DAO to be utilized for this service
    CreateNewMessageDAO dao;

    // Constructor for this Service
    public CreateNewMessageService() {
        dao = new CreateNewMessageDAO();
    }

    /**
     * This method adds a message into the database
     * 
     * Message constraints:
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
}
