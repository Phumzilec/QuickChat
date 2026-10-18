package com.mycompany.quickchat;

public class Message {

    private final String recipient;
    private final String messageText;

    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        int extraCharacters = messageText.length() - 250;

        return "Message exceeds 250 characters by "
                + extraCharacters
                + "; please reduce the size.";
    }
}