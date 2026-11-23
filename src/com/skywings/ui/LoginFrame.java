
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;
import com.skywings.dao.UserDAO;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("SkyWings Airlines - Login");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color navy = new Color(15, 35, 75);
        Color blue = new Color(30, 100, 200);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 244, 250));

        JLabel heading = new JLabel("SKYWINGS AIRLINES", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);
        heading.setBackground(navy);
        heading.setOpaque(true);
        heading.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 15, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton adminButton = new JButton("Admin Login");

        loginButton.setBackground(blue);
        loginButton.setForeground(Color.WHITE);

        registerButton.setBackground(navy);
        registerButton.setForeground(Color.WHITE);

        adminButton.setBackground(new Color(80, 100, 130));
        adminButton.setForeground(Color.WHITE);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Password:"));
        form.add(passwordField);

        form.add(loginButton);
        form.add(registerButton);

        form.add(new JLabel(""));
        form.add(adminButton);

        mainPanel.add(form, BorderLayout.CENTER);

        add(mainPanel);

        loginButton.addActionListener(e -> {

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            UserDAO dao = new UserDAO();

            if (dao.validateLogin(email, password)) {

                int userId = dao.getUserId(email);

                JOptionPane.showMessageDialog(this, "Login Successful!");

                dispose();
                new MainFrame(userId);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password!");
            }
        });

        registerButton.addActionListener(e -> new RegisterFrame());

        adminButton.addActionListener(e -> new AdminLoginFrame());

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}

