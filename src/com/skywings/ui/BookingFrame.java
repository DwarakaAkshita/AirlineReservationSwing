
package com.skywings.ui;

import javax.swing.*;
import java.awt.*;
import com.skywings.dao.BookingDAO;

public class BookingFrame extends JFrame {

    private int userId;
    private int flightId;

    public BookingFrame(int userId, int flightId) {

        this.userId = userId;
        this.flightId = flightId;

        setTitle("SkyWings - Book Flight");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel heading = new JLabel("Confirm Your Booking");
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel flightLabel = new JLabel("Flight ID: " + flightId);
        JLabel seatsLabel = new JLabel("Number of Seats:");

        JTextField seatsField = new JTextField();

        JButton bookButton = new JButton("Confirm Booking");

        panel.add(heading);
        panel.add(new JLabel(""));
        panel.add(flightLabel);
        panel.add(new JLabel(""));
        panel.add(seatsLabel);
        panel.add(seatsField);
        panel.add(new JLabel(""));
        panel.add(bookButton);

        add(panel);

        bookButton.addActionListener(e -> {

            try {
                int seats = Integer.parseInt(seatsField.getText());

                if (seats <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Enter a valid number of seats!");
                    return;
                }

                BookingDAO dao = new BookingDAO();

                if (dao.bookFlight(userId, flightId, seats)) {

                    JOptionPane.showMessageDialog(this,
                            "Flight booked successfully!");

                    dispose();

                    new MyBookingsFrame(userId);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Insufficient seats available!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid seat numbers!");
            }
        });

        setVisible(true);
    }
}
