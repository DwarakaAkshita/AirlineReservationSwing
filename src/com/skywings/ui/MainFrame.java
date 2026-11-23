
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private int userId;

    public MainFrame(int userId) {

        this.userId = userId;

        setTitle("SkyWings Airlines");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 244, 250));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        header.setBackground(new Color(15, 35, 75));

        JLabel logo = new JLabel("SKYWINGS AIRLINES");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        header.add(logo);
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 244, 250));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome to SkyWings!");
        welcome.setFont(new Font("Arial", Font.BOLD, 32));
        welcome.setForeground(new Color(15, 35, 75));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your journey begins here.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton searchButton = new JButton("Search Flights");
        JButton bookingsButton = new JButton("My Bookings");
        JButton logoutButton = new JButton("Logout");

        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchButton.setMaximumSize(new Dimension(250, 45));
        bookingsButton.setMaximumSize(new Dimension(250, 45));
        logoutButton.setMaximumSize(new Dimension(250, 45));

        searchButton.setBackground(new Color(30, 100, 200));
        searchButton.setForeground(Color.WHITE);

        bookingsButton.setBackground(new Color(15, 35, 75));
        bookingsButton.setForeground(Color.WHITE);

        logoutButton.setBackground(new Color(220, 60, 60));
        logoutButton.setForeground(Color.WHITE);

        centerPanel.add(Box.createVerticalStrut(90));
        centerPanel.add(welcome);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(subtitle);
        centerPanel.add(Box.createVerticalStrut(45));
        centerPanel.add(searchButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(bookingsButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(logoutButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        searchButton.addActionListener(e -> new FlightFrame(userId));

        bookingsButton.addActionListener(e -> new MyBookingsFrame(userId));

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}


