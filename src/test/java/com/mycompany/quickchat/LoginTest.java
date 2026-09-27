package com.mycompany.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void validUsernameIsAccepted() {
        Login login = new Login();

        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void invalidUsernameIsRejected() {
        Login login = new Login();

        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }
    
        @Test
    public void validPasswordIsAccepted() {
        Login login = new Login();

        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void invalidPasswordIsRejected() {
        Login login = new Login();

        assertFalse(login.checkPasswordComplexity("password"));
    }
    
        @Test
    public void validCellPhoneNumberIsAccepted() {
        Login login = new Login();

        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void invalidCellPhoneNumberIsRejected() {
        Login login = new Login();

        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
    
        @Test
    public void correctDetailsAllowLogin() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1",
                "Ch&&sec@ke99!", "+27838968976");

        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void incorrectPasswordPreventsLogin() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1",
                "Ch&&sec@ke99!", "+27838968976");

        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
    }

    @Test
    public void incorrectUsernamePreventsLogin() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1",
                "Ch&&sec@ke99!", "+27838968976");

        assertFalse(login.loginUser("wrong", "Ch&&sec@ke99!"));
    }
    
        @Test
    public void successfulLoginReturnsWelcomeMessage() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1",
                "Ch&&sec@ke99!", "+27838968976");

        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void failedLoginReturnsErrorMessage() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith", "kyl_1",
                "Ch&&sec@ke99!", "+27838968976");

        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus("kyl_1", "wrongPassword"));
    }
    
        @Test
    public void invalidUsernameReturnsRegistrationError() {
        Login login = new Login();

        assertEquals(
                "Username is not correctly formatted; please ensure "
                + "that your username contains an underscore and "
                + "is no more than five characters in length.",
                login.registerUser("Kyle", "Smith", "kyle!!!!!!!",
                        "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void invalidPasswordReturnsRegistrationError() {
        Login login = new Login();

        assertEquals(
                "Password is not correctly formatted; please ensure "
                + "that the password contains at least eight "
                + "characters, a capital letter, a number, "
                + "and a special character.",
                login.registerUser("Kyle", "Smith", "kyl_1",
                        "password", "+27838968976"));
    }
    
        @Test
    public void validDetailsRegisterSuccessfully() {
        Login login = new Login();

        assertEquals(
                "User registered successfully.",
                login.registerUser("Kyle", "Smith", "kyl_1",
                        "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void invalidCellPhoneReturnsRegistrationError() {
        Login login = new Login();

        assertEquals(
                "Cell phone number incorrectly formatted or does "
                + "not contain international code.",
                login.registerUser("Kyle", "Smith", "kyl_1",
                        "Ch&&sec@ke99!", "08966553"));

        assertFalse(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
}