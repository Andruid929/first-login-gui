package com.andrewjones.logingui.auth;

import static com.andrewjones.logingui.validation.Validate.*;

import com.andrewjones.logingui.db.DB;
import com.andrewjones.logingui.encyption.EncryptionUtility;
import com.andrewjones.logingui.form.Gender;
import com.andrewjones.logingui.form.MaritalStatus;
import com.andrewjones.logingui.form.SignUpForm;
import com.andrewjones.logingui.normalise.Normalise;

import org.jetbrains.annotations.NotNull;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public final class SignUpAuth {

    public SignUpAuth(@NotNull PasswordAuthentication auth, @NotNull SignUpForm formData) throws SQLException, IllegalArgumentException {
        String emailAddress = formData.email().trim();
        String dateOfBirth = formData.dateOfBirth().trim();
        String maritalStatus = formData.maritalStatus().trim();
        String gender = formData.gender().trim();

        String[] fullName = auth.getUserName().split("#");

        char[] password = auth.getPassword();
        char[] confirmation = formData.confirmation();

        String firstName;
        String lastName;

        try {
            firstName = Normalise.normaliseName(fullName[0]);
            lastName = Normalise.normaliseName(fullName[1]);

        } catch (IndexOutOfBoundsException ignored) {
            throw new IllegalArgumentException("Invalid last name given");
        }

        if (isTooLong(firstName) || isTooLong(lastName)) {
            throw new IllegalArgumentException("Name must be, at most, 30 characters");
        }

        if (invalidEmail(emailAddress)) {
            throw new IllegalArgumentException("Please enter a valid email");
        }

        if (weakPassword(password)) {
            throw new IllegalArgumentException("Consider a stronger password");
        }

        if (!Arrays.equals(password, confirmation)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Gender validGender = validGender(gender);

        MaritalStatus validStatus = validMaritalStatus(maritalStatus);

        validateDateOfBirth(dateOfBirth);

        String hashedPassword = EncryptionUtility.HASHER.hashToString(EncryptionUtility.HASH_COST, password);

        try (Connection connection = DB.getDbConnection()) {

            String sql = "INSERT INTO app_user(first_name, last_name, gender,"
                    + " date_of_birth, email_address, marital_status, password)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, validGender.getAlternativeValue());
            statement.setDate(4, Date.valueOf(dateOfBirth));
            statement.setString(5, emailAddress.toLowerCase());
            statement.setString(6, validStatus.getStatus());
            statement.setString(7, hashedPassword);

            statement.execute();
        }
    }


}
