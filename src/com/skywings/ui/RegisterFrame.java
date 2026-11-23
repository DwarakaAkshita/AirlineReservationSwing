
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;

import com.skywings.dao.UserDAO;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {

        setTitle("SkyWings Airlines - Register");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color navy = new Color(25, 48, 85);
        Color blue = new Color(45, 105, 190);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(navy);
        header.setPreferredSize(new Dimension(650, 100));

        JLabel logo = new JLabel("✈ SkyWings Airlines", JLabel.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 27));
        logo.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Join us and start your journey!", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);

        header.add(logo);
        header.add(subtitle);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(242, 246, 251));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 390));
        card.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        card.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Your Account");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(navy);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));
        nameField.setMaximumSize(new Dimension(350, 55));
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        emailField.setMaximumSize(new Dimension(350, 55));
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setMaximumSize(new Dimension(350, 55));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton registerButton = new JButton("Create Account");
        registerButton.setBackground(blue);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(350, 45));

        JButton backButton = new JButton("Back to Login");
        backButton.setForeground(blue);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(350, 40));

        registerButton.addActionListener(e -> {

            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            } else {

                UserDAO userDAO = new UserDAO();

                if (userDAO.registerUser(name, email, password)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful!");
                    dispose();
                    new LoginFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed! Email may already exist.");
                }
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        card.add(heading);
        card.add(Box.createVerticalStrut(25));
        card.add(nameField);
        card.add(Box.createVerticalStrut(15));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(registerButton);
        card.add(Box.createVerticalStrut(10));
        card.add(backButton);

        background.add(card);

        add(header, BorderLayout.NORTH);
        add(background, BorderLayout.CENTER);

        setVisible(true);
    }
}
