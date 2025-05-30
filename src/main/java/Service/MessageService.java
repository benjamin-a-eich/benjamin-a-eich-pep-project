package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }
    
    public Message addMessage(Message message){
        // Frist validate the message text
        boolean validMsg = messageVaildationHelper(message.message_text);

        if(validMsg) {
            // Set epoch time nevermind this is already set in the incoming json
            // message.time_posted_epoch = epochTimeHelper();
            // run the actual create message command   
            return messageDAO.createMessage(message);
        } else {
            return null;
        }
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int msg_id) {
        Message msgToReturn = messageDAO.getMessageByID(msg_id);
        return msgToReturn;
    }

    public List<Message> getMessagesByAccount(int account_id) {
        // Making sure the user is valid is done through the Account service class. So handle it in the controller, then just pass the ID in here
        return messageDAO.getAllMessagesFromAccount(account_id);
    }

    public Message updateMessage(int msg_id, String newMsgText) {

        // Validate the new message text. Do this validation first as we don't havet to hit the DB if it fails\
        if(messageVaildationHelper(newMsgText)) {
            // Make sure the message actually exists before we update it
            Message msgToUpdate = messageDAO.getMessageByID(msg_id);

            if (msgToUpdate != null) {
            
                msgToUpdate.message_text = newMsgText;

                return messageDAO.updateMessage(msgToUpdate);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Message deleteMessage(int msg_id) {
        Message msgToDelete = messageDAO.getMessageByID(msg_id);

        // Make sure the message actually exists before we delete it
        if (msgToDelete != null) {
            return messageDAO.updateMessage(msgToDelete);
        } else {
            return null;
        }
    }

    // Helper function to calculate epochtime
    // private long epochTimeHelper() {
    //     return java.time.Instant.now().toEpochMilli();
    // }

    // helper function to make sure that the message text is valid
    private boolean messageVaildationHelper(String text){
        if(text == null || text == "") {
            return false;
        } else if (text.length() > 255) {
            return false;
        } else {
            return true;
        }
    }
}
