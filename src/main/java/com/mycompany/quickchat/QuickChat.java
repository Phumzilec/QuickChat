package com.mycompany.quickchat;

import java.util.Scanner;

/**
 * Runs registration, login, and the QuickChat console menu.
 */
public class QuickChat {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("Welcome to QuickChat");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.println("Hello " + firstName + " " + lastName);

        String username;

        do {
            System.out.print("Enter a username: ");
            username = input.nextLine();

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

        String cellPhoneNumber;

        do {
            System.out.print("Enter your cellphone number with +27: ");
            cellPhoneNumber = input.nextLine();

            System.out.println(
                    login.returnCellPhoneStatus(cellPhoneNumber));

        } while (!login.checkCellPhoneNumber(cellPhoneNumber));

        String registrationMessage = login.registerUser(
                firstName,
                lastName,
                username,
                password,
                cellPhoneNumber);

        System.out.println(registrationMessage);

        String loginUsername;
        String loginPassword;
        boolean loggedIn;

        System.out.println("Please log in.");

        do {
            System.out.print("Enter your username: ");
            loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            loginPassword = input.nextLine();

            loggedIn = login.loginUser(loginUsername, loginPassword);

            System.out.println(
                    login.returnLoginStatus(loginUsername, loginPassword));

        } while (!loggedIn);

        System.out.println("\nWelcome to QuickChat.");

        int messageLimit = 0;

        while (messageLimit <= 0) {
            System.out.print("How many messages would you like to enter? ");

            try {
                messageLimit = Integer.parseInt(input.nextLine().trim());

                if (messageLimit <= 0) {
                    System.out.println(
                            "Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid whole number.");
            }
        }

        System.out.println(
                "You may enter " + messageLimit + " messages.");

        String choice;

        do {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice: ");

            choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println(
                            "Message entry will be added in the next step.");
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please enter 1, 2, or 3.");
            }

        } while (!choice.equals("3"));

        input.close();
    }
}