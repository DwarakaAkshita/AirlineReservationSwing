
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;
import com.skywings.dao.AdminDAO;

public class AdminLoginFrame extends JFrame {

    public AdminLoginFrame() {

        setTitle("SkyWings - Admin Login");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color navy = new Color(15, 35, 75);
        Color blue = new Color(30, 100, 200);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 244, 250));

        JLabel heading = new JLabel("ADMIN LOGIN", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(navy);
        heading.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 15, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(blue);
        loginButton.setForeground(Color.WHITE);

        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel(""));
        form.add(loginButton);

        mainPanel.add(form, BorderLayout.CENTER);

        add(mainPanel);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            AdminDAO dao = new AdminDAO();

            if (dao.validateAdmin(username, password)) {
                JOptionPane.showMessageDialog(this, "Admin Login Successful!");
                dispose();
                new AdminDashboardFrame();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Admin Credentials!");
            }
        });

        setVisible(true);
    }
}
