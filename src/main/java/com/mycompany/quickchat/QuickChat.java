package com.mycompany.quickchat;

import java.io.IOException;
import java.util.Scanner;

/**
 * Runs the console registration, login and message-processing features.
 */
public class QuickChat {

    public static void main(String[] args) {
        Message.resetSession();

        try (Scanner input = new Scanner(System.in)) {
            Login login = new Login();

            registerAndLogin(input, login);

            System.out.println("\nWelcome to QuickChat.");

            int messageLimit = readMessageLimit(input);
            boolean limitReached = false;
            String choice;

            do {
                System.out.println("\nPlease choose an option:");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");

                choice = read(input, "Enter your choice: ");

                switch (choice) {
                    case "1":
                        if (limitReached) {
                            System.out.println(
                                    "You have reached your message limit.");
                        } else {
                            for (int number = 0;
                                    number < messageLimit;
                                    number++) {

                                System.out.println(
                                        "\nMessage " + (number + 1)
                                        + " of " + messageLimit);

                                Message message =
                                        readMessage(input, number);

                                processMessage(input, message);
                            }

                            limitReached = true;

                            System.out.println(
                                    "Total messages sent: "
                                    + Message.returnTotalMessagess());
                        }
                        break;

                    case "2":
                        System.out.println("Coming Soon.");
                        break;

                    case "3":
                        System.out.println("\nSent messages:");
                        System.out.println(Message.printMessages());

                        System.out.println(
                                "Total messages sent: "
                                + Message.returnTotalMessagess());

                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println(
                                "Invalid option. Please enter 1, 2, or 3.");
                }

            } while (!"3".equals(choice));
        }
    }

    private static String read(Scanner input, String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private static void registerAndLogin(
            Scanner input, Login login) {

        System.out.println("QuickChat registration");

        String firstName = read(input, "Enter your first name: ");
        String lastName = read(input, "Enter your last name: ");

        String username;

        do {
            username = read(input, "Enter a username: ");

            if (login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println(
                        "Username is not correctly formatted; please ensure "
                        + "that your username contains an underscore and "
                        + "is no more than five characters in length.");
            }

        } while (!login.checkUserName(username));

        String password;

        do {
            System.out.print("Enter a password: ");
            password = input.nextLine();

            System.out.println(login.returnPasswordStatus(password));

        } while (!login.checkPasswordComplexity(password));

        String phone;

        do {
            phone = read(
                    input, "Enter your cellphone number with +27: ");

            System.out.println(login.returnCellPhoneStatus(phone));

        } while (!login.checkCellPhoneNumber(phone));

        System.out.println(login.registerUser(
                firstName, lastName, username, password, phone));

        boolean loggedIn;

        System.out.println("Please log in.");

        do {
            String user = read(input, "Enter your username: ");

            System.out.print("Enter your password: ");
            String pass = input.nextLine();

            loggedIn = login.loginUser(user, pass);

            System.out.println(login.returnLoginStatus(user, pass));

        } while (!loggedIn);
    }

    private static int readMessageLimit(Scanner input) {
        while (true) {
            try {
                int limit = Integer.parseInt(read(
                        input,
                        "How many messages would you like to enter? "));

                if (limit > 0) {
                    return limit;
                }

            } catch (NumberFormatException e) {
                // Invalid input is handled by the message below.
            }

            System.out.println(
                    "Please enter a valid whole number greater than 0.");
        }
    }

    private static Message readMessage(
            Scanner input, int number) {

        String recipient;
        Message recipientCheck;

        do {
            recipient = read(
                    input,
                    "Enter the recipient's cellphone number with +27: ");

            recipientCheck = new Message(recipient, "", number);

            System.out.println(recipientCheck.checkRecipientCell());

        } while (!recipientCheck.isRecipientValid());

        Message message;

        do {
            System.out.print(
                    "Enter your message (maximum 250 characters): ");

            message = new Message(
                    recipient, input.nextLine(), number);

            System.out.println(message.checkMessageLength());

        } while (!message.isMessageValid());

        return message;
    }

    private static void processMessage(
            Scanner input, Message message) {

        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");

            String action = read(input, "Enter your choice: ");

            if (!"1".equals(action)
                    && !"2".equals(action)
                    && !"3".equals(action)) {

                System.out.println(
                        "Invalid option. Please choose 1, 2, or 3.");
                continue;
            }

            if ("2".equals(action)) {
                System.out.println("Press 0 to delete the message.");

                while (!"0".equals(read(
                        input, "Enter 0 to confirm deletion: "))) {

                    System.out.println(
                            "Please enter 0 to confirm deletion.");
                }
            }

            try {
                String result = message.SentMessage(action);

                if ("2".equals(action)) {
                    System.out.println("Message disregarded.");
                } else {
                    System.out.println(result);
                }

                if ("1".equals(action)) {
                    System.out.println(message.details());

                } else if ("3".equals(action)) {
                    System.out.println(
                            "Saved to: "
                            + Message.STORAGE_FILE.toAbsolutePath());
                }

                return;

            } catch (IOException e) {
                System.out.println(
                        "The message could not be stored: "
                        + e.getMessage());

                System.out.println(
                        "Choose another action or retry storage.");
            }
        }
    }
}