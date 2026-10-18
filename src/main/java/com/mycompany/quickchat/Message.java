package com.mycompany.quickchat;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private final String recipient;
    private final String messageText;
    private final String messageID;
    private final int messageNumber;

    public Message(String recipient, String messageText) {
        this(recipient, messageText, 0);
    }

    public Message(String recipient, String messageText,
            int messageNumber) {

        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;

        this.messageID = Long.toString(
                ThreadLocalRandom.current().nextLong(
                        1_000_000_000L, 10_000_000_000L));
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public boolean checkMessageID() {
        return messageID.matches("[0-9]{10}");
    }

    public String createMessageHash() {
        String cleanedMessage = messageText.trim();

        if (cleanedMessage.isEmpty()) {
            return messageID.substring(0, 2)
                    + ":" + messageNumber + ":";
        }

        String[] words = cleanedMessage.split("\\s+");

        String firstWord = words[0].replaceAll(
                "[^\\p{L}\\p{N}]", "");

        String lastWord = words[words.length - 1].replaceAll(
                "[^\\p{L}\\p{N}]", "");

        return (messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord + lastWord)
                .toUpperCase(Locale.ROOT);
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

    public String checkRecipientCell() {
        Login validator = new Login();

        if (validator.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does "
                + "not contain an international code. Please correct "
                + "the number and try again.";
    }
}