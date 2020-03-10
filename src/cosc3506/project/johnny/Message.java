package cosc3506.project.johnny;

import java.io.Serializable;

class Message implements Serializable {

    int id;
    String sender, recipient, subject, message, sent, status;

    Message(int id, String recipient, String sender, String subject,
            String message, String sent, String status) {

        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.sent = sent;
        this.status = status;


    }

}
