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
        
        System.out.println("Valid cellphone: "
        + login.checkCellPhoneNumber("+27838968976"));

System.out.println("Invalid cellphone: "
        + login.checkCellPhoneNumber("08966553"));

        System.out.println(login.registerUser(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976"));
        
                System.out.println(login.registerUser(
                "Kyle",
                "Smith",
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976"));
                
    }
}