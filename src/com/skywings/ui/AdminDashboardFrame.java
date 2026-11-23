
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame() {

        setTitle("SkyWings - Admin Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color navy = new Color(15, 35, 75);
        Color blue = new Color(30, 100, 200);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 244, 250));

        JLabel header = new JLabel("SKYWINGS ADMIN DASHBOARD");
        header.setFont(new Font("Arial", Font.BOLD, 25));
        header.setForeground(Color.WHITE);
        header.setBackground(navy);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 244, 250));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome, Administrator!");
        welcome.setFont(new Font("Arial", Font.BOLD, 30));
        welcome.setForeground(navy);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage airline flights");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addFlightButton = new JButton("Add New Flight");
        JButton logoutButton = new JButton("Logout");

        addFlightButton.setBackground(blue);
        addFlightButton.setForeground(Color.WHITE);

        logoutButton.setBackground(navy);
        logoutButton.setForeground(Color.WHITE);

        addFlightButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));

        addFlightButton.setMaximumSize(new Dimension(250, 50));
        logoutButton.setMaximumSize(new Dimension(250, 50));

        addFlightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(100));
        centerPanel.add(welcome);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(subtitle);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(addFlightButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(logoutButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        addFlightButton.addActionListener(e -> new AddFlightFrame());

        logoutButton.addActionListener(e -> {
            dispose();
            new AdminLoginFrame();
        });

        setVisible(true);
    }
}
