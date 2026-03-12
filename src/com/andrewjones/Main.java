package com.andrewjones;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {
    private static JLabel success;
    private static JLabel label;
    private static JPanel panel;
    private static JFrame frame;
    private static JTextField userText;
    private static JLabel passwordLabel;
    private static JPasswordField passwordText;
    private static JButton button;

    public static void main(String[] args) {
        //Coded by Andrew using IntelliJ IDE
        panel = new JPanel();
        frame = new JFrame("Login to Andruid929");
        frame.setSize(350, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        label = new JLabel("User");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        userText = new JTextField();
        //Adds a text field
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        button = new JButton("Login");
        button.setBounds(125, 80, 80, 25);
        button.addActionListener(new Main());
        panel.add(button);

        success = new JLabel("");
        success.setBounds(120, 150, 300, 40);
        panel.add(success);

        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

            String user = userText.getText();
            String password = passwordText.getText();
            System.out.println(user + ", " + password);

            if (user.equals("Andrew") && password.equals("Daxxy"))
                success.setText("Welcome Andrew Jones");

            else if (user.equals("Bless B") && password.equals("1379"))
                success.setText("Welcome Blessings Phale");

            else if (user.equals("Leena") && password.equals("Hinami"))
                success.setText("Welcome Leena Munshi");
            else
                success.setText("Unknown user");

    }
}
