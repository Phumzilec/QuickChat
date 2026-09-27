package com.mycompany.quickchat;

import java.util.Scanner;

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

            if (login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
            } else {
                System.out.println(
                        "Password is not correctly formatted; please ensure "
                        + "that the password contains at least eight "
                        + "characters, a capital letter, a number, "
                        + "and a special character.");
            }
        } while (!login.checkPasswordComplexity(password));
        
                String cellPhoneNumber;

        do {
            System.out.print("Enter your cellphone number with +27: ");
            cellPhoneNumber = input.nextLine();

            if (login.checkCellPhoneNumber(cellPhoneNumber)) {
                System.out.println("Cell phone number successfully added.");
            } else {
                System.out.println(
                        "Cell phone number incorrectly formatted "
                        + "or does not contain international code.");
            }
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

        input.close();
    }
}