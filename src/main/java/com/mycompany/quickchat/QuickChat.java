package com.mycompany.quickchat;

public class QuickChat {

    public static void main(String[] args) {
        Login login = new Login();

        System.out.println("Valid username: "
                + login.checkUserName("kyl_1"));

        System.out.println("Invalid username: "
                + login.checkUserName("kyle!!!!!!!"));

        System.out.println("Valid password: "
                + login.checkPasswordComplexity("Ch&&sec@ke99!"));

        System.out.println("Invalid password: "
                + login.checkPasswordComplexity("password"));
    }
}