package com.andrewjones.logingui;

import com.andrewjones.logingui.auth.LoginAuth;
import com.andrewjones.logingui.utils.UI;

import java.awt.event.ActionEvent;
import java.net.PasswordAuthentication;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.*;

public final class Login extends JFrame {

    private final JFrame frame;

    private final JTextField userText;

    private final JPasswordField passwordText;

    private final Pattern emailValidation;

    public Login() {
        setTitle("Login to Andruid929");
        setSize(300, 150);
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

    private void processLoginInfo() {
        String emailAddress = userText.getText().toLowerCase();

        if (!emailValidation.matcher(emailAddress).matches()) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email", "Warning", JOptionPane.ERROR_MESSAGE);

            return;
        }

        char[] password = passwordText.getPassword();

        if (password.length < 8 || password.length > 32) {
            JOptionPane.showMessageDialog(frame, "Invalid password length, most likely incorrect", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        try {
            var loginVerification = new LoginAuth(new PasswordAuthentication(emailAddress, password));

            if (!loginVerification.isUserFound()) {
                JOptionPane.showMessageDialog(frame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (!loginVerification.isUserValidated()) {
                JOptionPane.showMessageDialog(frame, "Invalid password, check your input", "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }

            String firstName = loginVerification.getFirstName();
            String lastName = loginVerification.getLastName();

            String welcomeMessage = "Welcome, ".concat(firstName).concat(" ").concat(lastName);

            JOptionPane.showMessageDialog(frame, welcomeMessage, "Welcome", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
