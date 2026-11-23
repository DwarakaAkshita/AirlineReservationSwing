
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;
import com.skywings.dao.AdminDAO;

public class AddFlightFrame extends JFrame {

    public AddFlightFrame() {

        setTitle("SkyWings - Add Flight");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color navy = new Color(15, 35, 75);
        Color blue = new Color(30, 100, 200);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(240, 244, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel("ADD NEW FLIGHT", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);
        heading.setBackground(navy);
        heading.setOpaque(true);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(8, 2, 15, 15));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JTextField airlineField = new JTextField();
        JTextField sourceField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField seatsField = new JTextField();

        form.add(new JLabel("Airline Name:"));
        form.add(airlineField);

        form.add(new JLabel("Source:"));
        form.add(sourceField);

        form.add(new JLabel("Destination:"));
        form.add(destinationField);

        form.add(new JLabel("Flight Date (YYYY-MM-DD):"));
        form.add(dateField);

        form.add(new JLabel("Departure (HH:MM:SS):"));
        form.add(departureField);

        form.add(new JLabel("Arrival (HH:MM:SS):"));
        form.add(arrivalField);

        form.add(new JLabel("Price:"));
        form.add(priceField);

        form.add(new JLabel("Total Seats:"));
        form.add(seatsField);

        mainPanel.add(form, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Flight");
        addButton.setBackground(blue);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 244, 250));
        bottomPanel.add(addButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        addButton.addActionListener(e -> {

            try {
                String airline = airlineField.getText().trim();
                String source = sourceField.getText().trim();
                String destination = destinationField.getText().trim();
                String date = dateField.getText().trim();
                String departure = departureField.getText().trim();
                String arrival = arrivalField.getText().trim();

                double price = Double.parseDouble(priceField.getText());
                int seats = Integer.parseInt(seatsField.getText());

                if (airline.isEmpty() || source.isEmpty()
                        || destination.isEmpty() || date.isEmpty()
                        || departure.isEmpty() || arrival.isEmpty()
                        || price <= 0 || seats <= 0) {

                    JOptionPane.showMessageDialog(this,
                            "Please fill all fields correctly!");
                    return;
                }

                AdminDAO dao = new AdminDAO();

                if (dao.addFlight(airline, source, destination,
                        date, departure, arrival, price, seats)) {

                    JOptionPane.showMessageDialog(this,
                            "Flight added successfully!");

                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to add flight!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input! Check date, time, price and seats.");
            }
        });

        setVisible(true);
    }
}
