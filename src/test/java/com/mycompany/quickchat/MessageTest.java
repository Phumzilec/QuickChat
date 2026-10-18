package com.mycompany.quickchat;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Part 2 examples, boundaries, message actions and JSON storage.
 */
class MessageTest {

    private static final String PHONE = "+27718693002";

    private static final String DINNER =
            "Hi Mike, can you join us for dinner tonight?";

    @TempDir
    Path directory;

    @BeforeEach
    void resetSession() {
        Message.resetSession();
    }

    @Test
    void acceptsBriefMessage() {
        assertEquals(
                "Message ready to send.",
                new Message(PHONE, DINNER).checkMessageLength());
    }

    @Test
    void acceptsExactly250Characters() {
        assertEquals(
                "Message ready to send.",
                new Message(PHONE, "A".repeat(250))
                        .checkMessageLength());
    }

    @Test
    void reportsExactExcessCharacters() {
        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                new Message(PHONE, "A".repeat(251))
                        .checkMessageLength());

        assertEquals(
                "Message exceeds 250 characters by 20; please reduce the size.",
                new Message(PHONE, "A".repeat(270))
                        .checkMessageLength());
    }

    @Test
    void rejectsBlankAndNullMessages() {
        assertFalse(new Message(PHONE, "   ").isMessageValid());
        assertFalse(new Message(PHONE, null).isMessageValid());
    }

    @Test
    void acceptsBriefRecipient() {
        assertEquals(
                "Cell phone number successfully captured.",
                new Message(PHONE, DINNER).checkRecipientCell());
    }

    @Test
    void rejectsBriefLocalRecipient() {
        Message message = new Message(
                "08575975889",
                "Hi Keegan, did you receive the payment?");

        assertEquals(
                "Cell phone number is incorrectly formatted or does "
                + "not contain an international code. Please correct "
                + "the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    void rejectsOtherInvalidRecipients() {
        String[] invalid = {
            null, "", "+270718693002", "+2771869300",
            "+277186930022", "+2771869300x"
        };

        for (String phone : invalid) {
            assertFalse(
                    new Message(phone, DINNER).isRecipientValid());
        }
    }

    @Test
    void generatesUniqueTenDigitIDsInLoop() {
        Set<String> ids = new HashSet<>();

        for (int number = 0; number < 200; number++) {
            Message message = new Message(PHONE, DINNER, number);

            assertTrue(message.checkMessageID());
            assertEquals(10, message.getMessageID().length());
            assertTrue(ids.add(message.getMessageID()));

            assertEquals(
                    "Message ID generated: " + message.getMessageID(),
                    message.messageIDStatus());
        }
    }

    @Test
    void rejectsInvalidIDs() {
        String[] invalid = {
            null, "123456789", "12345678901", "abcdefghij"
        };

        for (String id : invalid) {
            assertFalse(
                    new Message(PHONE, DINNER, 0, id)
                            .checkMessageID());
        }
    }

    @Test
    void checksBriefHashAndOtherHashesInLoop() {
        String[] texts = {
            DINNER,
            "Hi Keegan, did you receive the payment?",
            "  hello   world!  ",
            "Hello"
        };

        String[] expected = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT",
            "00:2:HELLOWORLD",
            "00:3:HELLOHELLO"
        };

        for (int number = 0; number < texts.length; number++) {
            Message message = new Message(
                    PHONE, texts[number], number, "0012345678");

            assertEquals(
                    expected[number], message.createMessageHash());
        }
    }

    @Test
    void sendsOnceAndReportsDetailsInOrder() throws IOException {
        Message message = new Message(
                PHONE, DINNER, 0, "0012345678");

        assertEquals(
                "Message successfully sent.",
                message.SentMessage("1"));

        assertEquals(1, Message.returnTotalMessagess());

        String expected =
                "Message ID: 0012345678"
                + "\nMessage Hash: 00:0:HITONIGHT"
                + "\nRecipient: " + PHONE
                + "\nMessage: " + DINNER;

        assertEquals(expected, message.details());
        assertEquals(expected + "\n\n", Message.printMessages());

        assertEquals(
                "Message has already been processed.",
                message.SentMessage("1"));

        assertEquals(1, Message.returnTotalMessagess());
    }

    @Test
    void disregardsWithoutIncreasingTotal() throws IOException {
        Message message = new Message(
                "08575975889",
                "Hi Keegan, did you receive the payment?",
                1);

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage("2"));

        assertEquals("DISREGARDED", message.getStatus());
        assertEquals(0, Message.returnTotalMessagess());

        assertEquals(
                "No messages have been sent.",
                Message.printMessages());
    }

    @Test
    void storesAndAppendsValidJSON() throws IOException {
        Path file = directory.resolve("messages.json");
        String text = "Hi \"Mike\"\\test\nDinner?";

        Message first = new Message(PHONE, text, 0);

        assertEquals(
                "Message successfully stored.",
                first.SentMessage("3", file));

        new Message(PHONE, "Second message", 1)
                .storeMessage(file);

        JsonArray records = JsonParser.parseString(
                Files.readString(file)).getAsJsonArray();

        assertEquals(2, records.size());

        assertEquals(
                text,
                records.get(0).getAsJsonObject()
                        .get("message").getAsString());

        assertEquals(
                first.getMessageID(),
                records.get(0).getAsJsonObject()
                        .get("messageID").getAsString());

        assertEquals(
                "STORED",
                records.get(0).getAsJsonObject()
                        .get("status").getAsString());

        assertEquals(0, Message.returnTotalMessagess());

        assertEquals(
                "No messages have been sent.",
                Message.printMessages());
    }

    @Test
    void preservesInvalidStorageAndAllowsRetry() throws IOException {
        Path file = directory.resolve("messages.json");
        Files.writeString(file, "not json");

        Message message = new Message(PHONE, DINNER);

        assertThrows(
                IOException.class,
                () -> message.SentMessage("3", file));

        assertEquals("not json", Files.readString(file));
        assertEquals("READY", message.getStatus());

        assertEquals(
                "Message successfully sent.",
                message.SentMessage("1"));
    }

    @Test
    void storageFailureDoesNotClaimSuccess() {
        Path file = directory.resolve("missing/messages.json");
        Message message = new Message(PHONE, DINNER);

        assertThrows(
                IOException.class,
                () -> message.SentMessage("3", file));

        assertEquals("READY", message.getStatus());
        assertEquals(0, Message.returnTotalMessagess());
    }

    @Test
    void invalidActionDoesNotChangeState() throws IOException {
        Message message = new Message(PHONE, DINNER);

        assertEquals(
                "Invalid option. Please choose 1, 2, or 3.",
                message.SentMessage("9"));

        assertEquals("READY", message.getStatus());
        assertEquals(0, Message.returnTotalMessagess());
    }

    @Test
    void refusesToSendInvalidDetails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Message("08575975889", DINNER)
                        .SentMessage("1"));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Message(PHONE, "A".repeat(251))
                        .SentMessage("1"));

        assertEquals(0, Message.returnTotalMessagess());
    }

    @Test
    void countsOnlySentMessagesAcrossAllActions() throws IOException {
        new Message(PHONE, DINNER, 0).SentMessage("1");

        new Message(PHONE, "Discard this", 1)
                .SentMessage("2");

        new Message(PHONE, "Store this", 2)
                .SentMessage("3", directory.resolve("stored.json"));

        assertEquals(1, Message.returnTotalMessagess());

        assertFalse(Message.printMessages().contains("Discard this"));
        assertFalse(Message.printMessages().contains("Store this"));
    }
}