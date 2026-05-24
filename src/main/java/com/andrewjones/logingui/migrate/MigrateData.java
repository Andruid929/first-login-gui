package com.andrewjones.logingui.migrate;

import static java.nio.file.StandardOpenOption.APPEND;

import com.andrewjones.logingui.db.DB;
import com.andrewjones.logingui.utils.AppUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class MigrateData {
    public static final Path PATH_TO_FILE = Path.of("C:\\Users\\Andrew\\Desktop\\aad3_3e3d_39j4.temp");

    public static void exportUserData() throws SQLException, IOException {

        if (Files.exists(PATH_TO_FILE)) {
            Files.delete(PATH_TO_FILE);
        }

        Files.createFile(PATH_TO_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(PATH_TO_FILE, StandardCharsets.UTF_8, APPEND)) {

            List<AppUser> appUsers = new ArrayList<>();

            try (Connection connection = DB.getDbConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM app_user")) {


                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String gender = resultSet.getString("gender");
                    String dateOfBirth = resultSet.getString("date_of_birth");
                    String emailAddress = resultSet.getString("email_address");
                    String maritalStatus = resultSet.getString("marital_status");
                    String password = resultSet.getString("password");

                    AppUser user = new AppUser(firstName, lastName, gender, dateOfBirth, emailAddress, maritalStatus, password);

                    appUsers.add(user);
                }

            }

            new ObjectMapper().writeValue(writer, appUsers);
        }
    }

    private static List<AppUser> importUserData() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(PATH_TO_FILE, StandardCharsets.UTF_8)) {

            return new ObjectMapper().readValue(reader, new TypeReference<>() {
            });
        }
    }

    @Contract(pure = true)
    public static void uploadData(@NotNull Connection connection) throws IOException, SQLException {
        List<AppUser> users = importUserData();

        String sql = "INSERT INTO app_user(first_name, last_name, gender,"
                + " date_of_birth, email_address, marital_status, password)"
                + " values (?, ?, ?, ?, ?, ?, ?)";

        for (AppUser appUser : users) {
            Date date = Date.valueOf(appUser.dateOfBirth());

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, appUser.firstName());
            statement.setString(2, appUser.lastName());
            statement.setString(3, appUser.gender());
            statement.setDate(4, date);
            statement.setString(5, appUser.emailAddress());
            statement.setString(6, appUser.maritalStatus());
            statement.setString(7, appUser.password());

            statement.executeUpdate();
        }

    }

}
