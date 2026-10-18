# QuickChat — Part 1

## Student details
- Name: Phumzile Clementine Shabalala
- Student number: ST10503068
- Module: PROG5121
- Assessment: POE Part 1 — Registration and Login

## Overview
QuickChat is a Java console application that allows a user to register
an account and then log in using the registered username and password.
https://youtu.be/S0HlD1ZC9jg

## Features
- Collects the user's first and last names.
- Checks that the username contains an underscore and is no more than
  five characters long.
- Checks that the password contains at least eight characters,
  an uppercase letter, a number and a special character.
- Checks cellphone numbers using a regular expression: +27 followed
  by nine digits, with no leading zero after the country code.
- Repeats prompts when invalid details are entered.
- Displays a personalised welcome message after successful login.

## Account storage
The application stores one account in memory while it runs.
Account details are not saved after the application closes.
https://youtu.be/S0HlD1ZC9jg

## Requirements
- JDK 25
- Apache NetBeans with Maven support
- Internet access for the initial Maven dependency downloads

## How to run
1. Open the QuickChat project in NetBeans.
2. Right-click the project and select Run.
3. Enter your first name, last name, username, password and cellphone number.
4. Log in using the username and password you registered.

## How to run the tests
1. Right-click the QuickChat project in NetBeans.
2. Select Test.
3. Check the output for 19 tests with no failures or errors.

## Interpretation of the brief
Some wording differs between the feature requirements and test tables.

- Cellphone format: the application accepts +27 followed by nine digits,
  with no leading zero after +27. This matches the valid example
  +27838968976 supplied in the brief.
- Cellphone console messages follow the Part 1 test table:
  "Cell number successfully captured." and
  "Cell number is incorrectly formatted or does not contain an
  international code; please correct the number and try again."
- The registerUser method retains the cellphone error wording from
  the feature requirements.
- Successful login uses the feature requirement's welcome message:
  "Welcome <first name>, <last name> it is great to see you again."

These choices document how the conflicting instructions were interpreted;
any clarification from the lecturer takes precedence.

## References
1. The Independent Institute of Education, "Programming 1A
   (PROG5121/p/w): POE (Paper & Marking Rubrics)," 2026,
   pp. 5–10 and 20–21. Assessment brief supplied by the institution.
2. Oracle, "Pattern — Java Platform, Standard Edition 21 API Specification."
   [Online]. Available:
   https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
   [Accessed: 27 September 2026].
   Used as a reference for the regular-expression syntax in the
   cellphone validation method.



## Part 2 — Sending Messages

Part 2 extends the registration and login application with
console-based messaging. Users must log in successfully before
accessing the messaging menu.

### Features

- Numeric menu that repeats until the user chooses Quit.
- Message entry using a for loop and a user-defined message limit.
- Recipient validation using the Part 1 cellphone checker.
- Message validation with a maximum of 250 characters.
- Random ten-digit message IDs, unique within the current session.
- Automatically generated message hashes.
- Send, disregard and store message options.
- JSON storage that preserves previously stored messages.
- A sent-message report and a total excluding stored and
  disregarded messages.
- Automated JUnit tests through GitHub Actions.

Option 2 displays "Coming Soon." as required for Part 2.

### Requirements and execution

- JDK 25
- Apache Maven
- NetBeans or another Java IDE

Open the folder containing pom.xml in NetBeans and run QuickChat.java.
To execute all tests, right-click the project and select Test.

Stored messages are saved in stored-messages.json in the application's
working directory. The application displays the storage location.

### Testing

GitHub Actions successfully executed 37 tests:
18 Message tests and 19 Login tests.

Failures: 0
Errors: 0
Skipped: 0

The workflow is located at .github/workflows/TestJava.yml.

### Implementation decisions

- Message numbers start at 0.
- Messages of exactly 250 characters are accepted.
- Recipient validation follows the supplied South African
  international-format example: +27 followed by nine digits.
- IDs are stored as strings to preserve leading zeros.
- Message hashes remove punctuation from the first and last words.

### References

Google. Gson User Guide.
https://google.github.io/gson/UserGuide.html

JUnit Team. JUnit 5 User Guide, version 5.10.3.
https://junit.org/junit5/docs/5.10.3/user-guide/index.html

GitHub. Building and testing Java with Maven.
https://docs.github.com/en/actions/tutorials/build-and-test-code/java-with-maven

Accessed: 18 October 2026.