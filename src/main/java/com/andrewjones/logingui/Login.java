package com.andrewjones.logingui;

import static com.andrewjones.logingui.db.DB.DATA_SOURCE;

import com.andrewjones.logingui.encyption.Encryption;
import com.andrewjones.logingui.utils.UI;

import org.jetbrains.annotations.Contract;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class Login extends JFrame {

    private final JFrame frame;

    private final JTextField userText;

    private final JPasswordField passwordText;

    private final Pattern emailValidation;

    public Login() {
        setTitle("Login to Andruid929");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        frame = this;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel label = new JLabel("Email");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        userText = new JTextField();
        //Adds a text field
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        emailValidation = Pattern.compile(EMAIL_VALIDATION_REGEX);

        JButton button = new JButton("Login");
        button.setBounds(125, 80, 80, 25);
        button.addActionListener(ignored -> processLoginInfo());
        panel.add(button);

        JLabel success = new JLabel("");
        success.setBounds(120, 150, 300, 40);
        panel.add(success);

        UI.addKeybind(this, "processInfo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLoginInfo();
            }
        });

        setVisible(true);

    }

    private Connection getDbConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    private void processLoginInfo() {
        String emailAddress = userText.getText();

        if (!emailValidation.matcher(emailAddress).matches()) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email", "Warning", JOptionPane.ERROR_MESSAGE);

            return;
        }

        char[] password = passwordText.getPassword();

        if (password.length < 8 || password.length > 32) {
            JOptionPane.showMessageDialog(frame, "Invalid password length, most likely incorrect", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        try (Connection connection = getDbConnection();) {

            String sql = "select hashedpassword, first_name, last_name from app_user where email_address = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, emailAddress);

            ResultSet results = statement.executeQuery();

            statement.closeOnCompletion();

            boolean foundUser = false;

            while (results.next()) {
                foundUser = true;

                String storedPassword = results.getString("hashedpassword");

                BCrypt.Result verificationResult = Encryption.VERIFIER.verify(password, storedPassword);

                if (verificationResult.verified) {
                    String firstName = results.getString("first_name");
                    String lastName = results.getString("last_name");

                    String welcomeMessage = "Welcome, ".concat(firstName).concat(" ").concat(lastName);

                    JOptionPane.showMessageDialog(frame, welcomeMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

                    userText.setText("");
                    userText.grabFocus();

                    passwordText.setText("");

                    return;
                }
            }

            String errorMessage = foundUser ? "Invalid password" : "User not found, make sure email is valid";

            throw new SQLException(errorMessage);

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            passwordText.setText("");

            exception.printStackTrace(System.err);
        }
    }
}
