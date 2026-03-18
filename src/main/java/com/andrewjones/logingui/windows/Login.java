package com.andrewjones.logingui.windows;

import com.andrewjones.logingui.auth.LoginAuth;
import com.andrewjones.logingui.dialogs.Dialogs;
import com.andrewjones.logingui.utils.UI;

import java.awt.event.ActionEvent;
import java.net.PasswordAuthentication;
import java.sql.SQLException;

import javax.swing.*;

public final class Login extends JFrame {

    private JFrame frame;

    private JTextField userText;

    private JPasswordField passwordText;

    private JButton button;

    public Login() {
        frame = this;

        populateUi();
        setListeners();

        setVisible(true);
    }

    private void populateUi() {
        setTitle("Login to Andruid929");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel label = new JLabel("Email");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        button = new JButton("Login");
        button.setBounds(125, 80, 80, 25);
        panel.add(button);

        JLabel success = new JLabel("");
        success.setBounds(120, 150, 300, 40);
        panel.add(success);
    }

    private void setListeners() {
        button.addActionListener(ignored -> processLoginInfo());

        UI.addKeybind(this, "processInfo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLoginInfo();
            }
        });
    }

    private void processLoginInfo() {
        String emailAddress = userText.getText().toLowerCase();

        char[] password = passwordText.getPassword();

        try {
            var loginVerification = new LoginAuth(new PasswordAuthentication(emailAddress, password));

            if (!loginVerification.isUserFound()) {
                Dialogs.showErrorDialog(frame, "User not found");

                return;
            }

            if (!loginVerification.isUserValidated()) {
                Dialogs.showErrorDialog(frame, "Invalid password, check your input");

                return;
            }

            String firstName = loginVerification.getFirstName();
            String lastName = loginVerification.getLastName();

            String welcomeMessage = "Welcome, ".concat(firstName).concat(" ").concat(lastName);

            Dialogs.showInfoDialog(frame, welcomeMessage, "Welcome");

        } catch (SQLException | IllegalArgumentException e) {
            Dialogs.showErrorDialog(frame, e.getMessage());
        }
    }

}
