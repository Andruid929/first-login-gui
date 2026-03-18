package com.andrewjones.logingui.auth;

import com.andrewjones.logingui.db.DB;
import com.andrewjones.logingui.encyption.EncryptionUtility;

import org.jetbrains.annotations.NotNull;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class LoginAuth {

    public static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_VALIDATION_PATTERN = Pattern.compile(EMAIL_VALIDATION_REGEX);

    private final boolean foundUser;
    private final boolean userValidated;

    private String firstName;
    private String lastName;


    public LoginAuth(@NotNull PasswordAuthentication credentials) throws SQLException {
        String emailAddress = credentials.getUserName();
        char[] password = credentials.getPassword();

        if (!EMAIL_VALIDATION_PATTERN.matcher(emailAddress).matches()) {
            throw new IllegalArgumentException("Please enter a valid email");
        }

        if (password.length < 8 || password.length > 32) {
            throw new IllegalArgumentException("Invalid password length");
        }

        try (Connection connection = DB.getDbConnection()) {

            String sql = "SELECT hashed_password, first_name, last_name, gender FROM app_user WHERE email_address = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, emailAddress);

            ResultSet results = statement.executeQuery();

            statement.closeOnCompletion();

            if (!results.next()) {
                foundUser = false;
                userValidated = false;

                return;
            }

            foundUser = true;

            String storedPassword = results.getString("hashed_password");

            BCrypt.Result verificationResult = EncryptionUtility.VERIFIER.verify(password, storedPassword);

            userValidated = verificationResult.verified;

            if (userValidated) {
                firstName = results.getString("first_name");
                lastName = results.getString("last_name");
            }
        }
    }

    public boolean isUserFound() {
        return foundUser;
    }

    public boolean isUserValidated() {
        return foundUser && userValidated;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
