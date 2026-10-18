package com.mycompany.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Validates messages, records sent messages and stores deferred messages.
 */
public class Message {

    public static final Path STORAGE_FILE =
            Path.of("stored-messages.json");

    private static final Gson JSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final Set<String> generatedIDs = new HashSet<>();
    private static final StringBuilder sentMessages = new StringBuilder();

    private static int totalMessagesSent;

    private final String recipient;
    private final String messageText;
    private final String messageID;
    private final int messageNumber;

    private String status = "READY";

    public Message(String recipient, String messageText) {
        this(recipient, messageText, 0);
    }

    public Message(String recipient, String messageText,
            int messageNumber) {
        this(recipient, messageText, messageNumber, generateID());
    }

    /*
     * Allows tests in this package to supply a repeatable message ID.
     * Normal application code uses a randomly generated ID.
     */
    Message(String recipient, String messageText,
            int messageNumber, String messageID) {

        if (messageNumber < 0) {
            throw new IllegalArgumentException(
                    "Message number cannot be negative.");
        }

        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = messageID;
    }

    private static String generateID() {
        String id;

        do {
            id = String.format(
                    Locale.ROOT,
                    "%010d",
                    ThreadLocalRandom.current()
                            .nextLong(10_000_000_000L));

        } while (!generatedIDs.add(id));

        return id;
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

    public String getStatus() {
        return status;
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("[0-9]{10}");
    }

    public String messageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    public boolean isRecipientValid() {
        return new Login().checkCellPhoneNumber(recipient);
    }

    public String checkRecipientCell() {
        if (isRecipientValid()) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does "
                + "not contain an international code. Please correct "
                + "the number and try again.";
    }

    public boolean isMessageValid() {
        return messageText != null
                && !messageText.isBlank()
                && messageText.length() <= 250;
    }

    public String checkMessageLength() {
        if (messageText == null || messageText.isBlank()) {
            return "Please enter a message that is not blank.";
        }

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        return "Message exceeds 250 characters by "
                + (messageText.length() - 250)
                + "; please reduce the size.";
    }

    public String createMessageHash() {
        String text = messageText == null
                ? "" : messageText.trim();

        String firstAndLast = "";

        if (!text.isEmpty()) {
            String[] words = text.split("\\s+");

            String firstWord = words[0].replaceAll(
                    "[^\\p{L}\\p{N}]", "");

            String lastWord = words[words.length - 1].replaceAll(
                    "[^\\p{L}\\p{N}]", "");

            firstAndLast = firstWord + lastWord;
        }

        return (messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstAndLast)
                .toUpperCase(Locale.ROOT);
    }

    public String details() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public String SentMessage(String action) throws IOException {
        return SentMessage(action, STORAGE_FILE);
    }

    /*
     * The file parameter allows tests to use temporary storage.
     * The console confirms deletion before supplying action 2.
     */
    public String SentMessage(String action, Path file)
            throws IOException {

        if (!"1".equals(action)
                && !"2".equals(action)
                && !"3".equals(action)) {
            return "Invalid option. Please choose 1, 2, or 3.";
        }

        if (!"READY".equals(status)) {
            return "Message has already been processed.";
        }

        switch (action) {
            case "1":
                validateForDelivery();

                status = "SENT";
                totalMessagesSent++;

                sentMessages.append(details()).append("\n\n");

                return "Message successfully sent.";

            case "2":
                status = "DISREGARDED";
                return "Press 0 to delete the message.";

            case "3":
                storeMessage(file);
                return "Message successfully stored.";

            default:
                throw new IllegalStateException("Unexpected action.");
        }
    }

    private void validateForDelivery() {
        if (!checkMessageID()
                || !isRecipientValid()
                || !isMessageValid()) {
            throw new IllegalArgumentException(
                    "Message details are invalid.");
        }
    }

    public void storeMessage() throws IOException {
        storeMessage(STORAGE_FILE);
    }

    /**
     * JSON API reference:
     * Google, Gson User Guide, Serialization and Deserialization.
     * https://google.github.io/gson/UserGuide.html
     * Accessed: 18 October 2026.
     *
     * Adds a record to a JSON array, preserving existing records.
     * A temporary file reduces the risk of incomplete writes.
     */
    public void storeMessage(Path file) throws IOException {
        if (!"READY".equals(status)) {
            throw new IllegalStateException(
                    "Message has already been processed.");
        }

        validateForDelivery();

        JsonArray messages = new JsonArray();

        if (Files.exists(file)) {
            try {
                String existing = Files.readString(
                        file, StandardCharsets.UTF_8);

                messages = JsonParser.parseString(existing)
                        .getAsJsonArray();

            } catch (JsonParseException | IllegalStateException e) {
                throw new IOException(
                        "Existing storage is not a valid JSON array.", e);
            }
        }

        JsonObject record = new JsonObject();

        record.addProperty("messageID", messageID);
        record.addProperty("messageNumber", messageNumber);
        record.addProperty("messageHash", createMessageHash());
        record.addProperty("recipient", recipient);
        record.addProperty("message", messageText);
        record.addProperty("status", "STORED");

        messages.add(record);

        Path destination = file.toAbsolutePath();

        Path temporary = Files.createTempFile(
                destination.getParent(), "quickchat-", ".tmp");

        try {
            Files.writeString(
                    temporary,
                    JSON.toJson(messages),
                    StandardCharsets.UTF_8);

            try {
                Files.move(
                        temporary,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);

            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        temporary,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            status = "STORED";

        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public static int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public static String printMessages() {
        if (sentMessages.length() == 0) {
            return "No messages have been sent.";
        }

        return sentMessages.toString();
    }

    /**
     * Starts a fresh console session and isolates unit tests.
     * Stored JSON files remain available between sessions.
     */
    public static void resetSession() {
        totalMessagesSent = 0;
        sentMessages.setLength(0);
        generatedIDs.clear();
    }
}