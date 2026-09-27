package com.mycompany.quickchat;

public class Login {
    private String firstName;
private String lastName;
private String registeredUsername;
private String registeredPassword;
private String registeredCellPhoneNumber;
private boolean registered = false;

    public boolean checkUserName(String username) {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)
                    && !Character.isWhitespace(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasUppercase && hasNumber && hasSpecialCharacter;
    }
    
        /**
     * Checks the international number format used in the POE example.
     * Regex syntax reference: Oracle, Java SE 21 Pattern documentation.
     * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
     * Accessed: 27 September 2026.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber != null
                && cellPhoneNumber.matches("[+]27[1-9][0-9]{8}");
    }
   
        public String registerUser(String firstName, String lastName,
            String username, String password, String cellPhoneNumber) {

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure "
                    + "that your username contains an underscore and "
                    + "is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure "
                    + "that the password contains at least eight "
                    + "characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does "
                    + "not contain international code.";
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhoneNumber = cellPhoneNumber;
        this.registered = true;

        return "User registered successfully.";
    }
}