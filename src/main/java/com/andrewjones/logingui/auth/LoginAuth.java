package com.andrewjones.logingui.auth;

import com.andrewjones.logingui.db.DB;
import com.andrewjones.logingui.encyption.Encryption;

import org.jetbrains.annotations.NotNull;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class LoginAuth {

    private final boolean foundUser;

    private final boolean userValidated;

    private String firstName;
    private String lastName;

    public LoginAuth(@NotNull PasswordAuthentication credentials) throws SQLException {
        String emailAddress = credentials.getUserName();
        char[] password = credentials.getPassword();

        try (Connection connection = DB.getDbConnection()) {

            String sql = "select hashed_password, first_name, last_name, gender from app_user where email_address = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, emailAddress);

            ResultSet results = statement.executeQuery();

            statement.closeOnCompletion();

            if (results.next()) {
                foundUser = true;

                String storedPassword = results.getString("hashed_password");

                BCrypt.Result verificationResult = Encryption.VERIFIER.verify(password, storedPassword);

                if (verificationResult.verified) {
                    firstName = results.getString("first_name");
                    lastName = results.getString("last_name");

                    userValidated = true;
                } else {

                    userValidated = false;
                }

            } else {
                foundUser = false;

                userValidated = false;
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
