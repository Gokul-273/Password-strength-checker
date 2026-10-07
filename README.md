# Password Strength Checker

A simple Java Swing desktop application that evaluates password strength in real time. It provides a visual progress bar, requirement checklist, and a show/hide password option.

## Features

- Real-time password strength evaluation
- Password visibility toggle
- Visual strength progress bar
- Requirement checklist for:
  - Minimum 6 characters
  - At least one uppercase letter
  - At least one number
  - At least one special character
- Strength levels: Weak, Medium, and Strong
- Uses the system’s native look and feel

## Password Strength Rules

| Strength | Rule |
|---|---|
| Weak | Password contains fewer than 6 characters |
| Medium | Password contains 6 or more characters but does not meet the strong-password condition |
| Strong | Password contains at least 10 characters and one special character |

## Requirements

- Java Development Kit (JDK) 8 or later

## How to Run

1. Save the source file as `PasswordStrengthChecker.java`.
2. Open a terminal in the folder containing the file.
3. Compile the program:

```bash
javac PasswordStrengthChecker.java
```

4. Run the application:

```bash
java PasswordStrengthChecker
```

## Technologies Used

- Java
- Java Swing
- AWT

## Project Structure

```text
PasswordStrengthChecker.java
README.md
```

## Author

Gokul