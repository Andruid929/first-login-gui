package com.andrewjones.logingui.windows;

import com.andrewjones.logingui.auth.SignUpAuth;
import com.andrewjones.logingui.dialogs.Dialogs;
import com.andrewjones.logingui.form.SignUpForm;
import com.andrewjones.logingui.swing.MyTextField;
import com.andrewjones.logingui.utils.ActionKeys;
import com.andrewjones.logingui.utils.UI;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.PasswordAuthentication;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public final class SignUp extends JFrame implements WindowStep {

    private final JFrame frame;

    private JTextField firstNameText;
    private JTextField lastNameText;
    private JTextField emailAddressText;
    private JTextField genderText;
    private JTextField maritalStatusText;

    private MyTextField dateOfBirthText;
    private MyTextField monthOfBirthText;
    private MyTextField yearOfBirthText;

    private JPasswordField passwordText;
    private JPasswordField confirmPasswordText;

    private MyTextField[] birthInfoTrio;

    private JButton signUpButton;

    private final String NUMBERS_ONLY_REGEX = "^\\d+$";

    public SignUp() {
        this.frame = this;

        populateUi();
        setListeners();

        setVisible(true);
    }

    @Override
    public void populateUi() {
        setTitle("Sign up to Andruid929");
        setSize(320, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel firstNameLabel = new JLabel("First name");
        firstNameLabel.setBounds(10, 20, 110, 25);
        panel.add(firstNameLabel);

        firstNameText = new JTextField();
        firstNameText.setBounds(120, 20, 165, 25);
        panel.add(firstNameText);

        JLabel lastNameLabel = new JLabel("Last name");
        lastNameLabel.setBounds(10, 60, 110, 25);
        panel.add(lastNameLabel);

        lastNameText = new JTextField();
        lastNameText.setBounds(120, 60, 165, 25);
        panel.add(lastNameText);

        JLabel birthDay = new JLabel("Birthday");
        birthDay.setBounds(10, 100, 110, 25);
        panel.add(birthDay);

        dateOfBirthText = new MyTextField("DD");
        dateOfBirthText.setBounds(120, 100, 55, 25);
        panel.add(dateOfBirthText);

        monthOfBirthText = new MyTextField("MM");
        monthOfBirthText.setBounds(175, 100, 55, 25);
        panel.add(monthOfBirthText);

        yearOfBirthText = new MyTextField("YYYY");
        yearOfBirthText.setBounds(230, 100, 55, 25);
        panel.add(yearOfBirthText);

        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setBounds(10, 140, 110, 25);
        panel.add(genderLabel);

        genderText = new JTextField();
        genderText.setBounds(120, 140, 165, 25);
        panel.add(genderText);

        JLabel emailAddressLabel = new JLabel("Email address");
        emailAddressLabel.setBounds(10, 180, 110, 25);
        panel.add(emailAddressLabel);

        emailAddressText = new JTextField();
        emailAddressText.setBounds(120, 180, 165, 25);
        panel.add(emailAddressText);

        JLabel maritalStatusLabel = new JLabel("Marital status");
        maritalStatusLabel.setBounds(10, 220, 110, 25);
        panel.add(maritalStatusLabel);

        maritalStatusText = new JTextField();
        maritalStatusText.setBounds(120, 220, 165, 25);
        panel.add(maritalStatusText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 260, 110, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(120, 260, 165, 25);
        panel.add(passwordText);

        JLabel confirmPasswordLabel = new JLabel("Confirm password");
        confirmPasswordLabel.setBounds(10, 300, 110, 25);
        panel.add(confirmPasswordLabel);

        confirmPasswordText = new JPasswordField();
        confirmPasswordText.setBounds(120, 300, 165, 25);
        panel.add(confirmPasswordText);

        signUpButton = new JButton("Sign up");
        signUpButton.setBounds(90, 340, 120, 30);
        panel.add(signUpButton);

        firstNameText.grabFocus();

        birthInfoTrio = new MyTextField[]{dateOfBirthText, monthOfBirthText, yearOfBirthText};
    }

    private void processSignUpAttempt() {
        String firstName = firstNameText.getText();
        String lastName = lastNameText.getText();

        String emailAddress = emailAddressText.getText();

        String gender = genderText.getText();
        String maritalStatus = maritalStatusText.getText();

        String date = addPaddingIfNeeded(dateOfBirthText.getText());
        String month = addPaddingIfNeeded(monthOfBirthText.getText());
        String year = addPaddingIfNeeded(yearOfBirthText.getText());

        char[] password = passwordText.getPassword();
        char[] confirmation = confirmPasswordText.getPassword();

        String fullName = firstName.concat("#").concat(lastName);

        String fullDate = formatAsSqlDate(date, month, year);

        SignUpForm formData = new SignUpForm(emailAddress, fullDate, confirmation, gender, maritalStatus);

        try {
            new SignUpAuth(new PasswordAuthentication(fullName, password), formData);

            Dialogs.showInfoDialog(frame, "Welcome, ".concat(fullName.replace('#', ' ')).concat(". Sign in with your details"), "Sign up successful");

            new Login();

            dispose();
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace(System.err);
            Dialogs.showErrorDialog(frame, e.getMessage());
        }

    }

    @Override
    public void setListeners() {
        UI.addKeybind(frame, "processSignIn", ActionKeys.ENTER_KEYSTROKE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSignUpAttempt();
            }
        });

        UI.addKeybind(frame, "switchToLogin", ActionKeys.ALT_S_KEYSTROKE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();

                frame.dispose();
            }
        });

        signUpButton.addActionListener(e -> processSignUpAttempt());

        for (MyTextField textField : birthInfoTrio) {
            textField.addFocusListener(new FocusListener() {

                private final String PLACEHOLDER = textField.getPlaceholder();

                @Override
                public void focusGained(FocusEvent e) {
                    if (textField.hasFocus() && textField.getText().equals(PLACEHOLDER)) { //Clear the text field for the user and set the text colour to black

                        textField.setText("");

                        textField.setSelectedTextColor(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (textField.getText().equals(PLACEHOLDER)) { //Set the placeholder text as a hint
                        textField.setSelectedTextColor(Color.GRAY);

                    } else if (textField.getText().isBlank()) { //Reset blank text field to placeholder text
                        textField.setText(PLACEHOLDER);

                        textField.setSelectedTextColor(Color.GRAY);
                    }
                }
            });

            ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {

                private final String PLACEHOLDER = textField.getPlaceholder();

                @Override
                public void insertString(FilterBypass fb, int offset, String string,
                                         AttributeSet attr) throws BadLocationException {

                    if (string.matches(NUMBERS_ONLY_REGEX) || string.equals(PLACEHOLDER) || string.isBlank()) {
                        super.insertString(fb, offset, string.trim(), attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length,
                                    String string, AttributeSet attr) throws BadLocationException {

                    if (string.matches(NUMBERS_ONLY_REGEX) || string.equals(PLACEHOLDER) || string.isBlank()) {
                        super.replace(fb, offset, length, string.trim(), attr);
                    }
                }
            });
        }
    }

    private @NotNull String addPaddingIfNeeded(@NotNull String number) {
        if (number.length() == 1) {
            return "0".concat(number);
        }

        return number;
    }

    private @NotNull String formatAsSqlDate(String date, @NotNull String month, @NotNull String year) {
        return year.concat("-").concat(month).concat("-").concat(date);
    }
}
