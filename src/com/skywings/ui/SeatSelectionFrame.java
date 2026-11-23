
package com.skywings.ui;

import com.skywings.connection.DBConnection;
import com.skywings.dao.BookingDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.*;

public class SeatSelectionFrame extends JFrame {

    private int userId;
    private int flightId;

    private AircraftPanel cabinPanel;
    private JLabel selectedLabel;
    private JLabel priceLabel;

    private Map<String, JButton> seatButtons = new HashMap<>();
    private Map<String, Double> seatPrices = new HashMap<>();
    private Map<String, String> seatTypes = new HashMap<>();

    private Set<String> selectedSeats = new LinkedHashSet<>();

    public SeatSelectionFrame(int userId, int flightId) {

        this.userId = userId;
        this.flightId = flightId;

        setTitle("SkyWings - Select Your Seats");
        setSize(1000, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // HEADER
        JLabel title = new JLabel("SELECT YOUR SEATS", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(35, 40, 120));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        add(title, BorderLayout.NORTH);

        // AIRCRAFT CABIN
        cabinPanel = new AircraftPanel();

        loadSeats();

        JScrollPane scrollPane = new JScrollPane(cabinPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        selectedLabel = new JLabel("Selected Seats: None", JLabel.CENTER);
        priceLabel = new JLabel("Additional Charges: ₹0", JLabel.CENTER);

        selectedLabel.setFont(new Font("Arial", Font.BOLD, 15));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setBackground(new Color(40, 65, 180));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));

        confirmButton.addActionListener(e -> confirmSelection());

        bottomPanel.add(selectedLabel);
        bottomPanel.add(priceLabel);
        bottomPanel.add(confirmButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // AIRCRAFT SEAT LAYOUT
    private void loadSeats() {

        cabinPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        // Column headings
        gbc.gridy = 0;

        gbc.gridx = 0;
        cabinPanel.add(new JLabel("ROW"), gbc);

        String[] letters = {"A", "B", "C", "D", "E", "F"};

        for (int i = 0; i < 6; i++) {

            gbc.gridx = i < 3 ? i + 1 : i + 2;

            JLabel label = new JLabel(letters[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(new Color(30, 40, 120));

            cabinPanel.add(label, gbc);
        }

        // Load seats from database
        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT seat_number, seat_type, seat_price, is_booked "
                       + "FROM flight_seats WHERE flight_id = ? "
                       + "ORDER BY seat_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, flightId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String seatNumber = rs.getString("seat_number");
                String seatType = rs.getString("seat_type");
                double price = rs.getDouble("seat_price");
                boolean booked = rs.getBoolean("is_booked");

                seatPrices.put(seatNumber, price);
                seatTypes.put(seatNumber, seatType);

                JButton seatButton = new JButton(seatNumber);

                seatButton.setPreferredSize(new Dimension(58, 35));
                seatButton.setFont(new Font("Arial", Font.BOLD, 11));
                seatButton.setFocusPainted(false);
                seatButton.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 230), 1));
                seatButton.setOpaque(true);

                if (booked) {

                    seatButton.setBackground(Color.GRAY);
                    seatButton.setForeground(Color.WHITE);
                    seatButton.setEnabled(false);

                } else {

                    seatButton.setBackground(getSeatColor(seatType));
                    seatButton.setForeground(Color.WHITE);

                    seatButton.addActionListener(
                            e -> toggleSeat(seatNumber, seatButton));
                }

                seatButton.setToolTipText(
                        seatType + " - ₹" + price + " extra");

                seatButtons.put(seatNumber, seatButton);

                int row = Integer.parseInt(
                        seatNumber.substring(0, seatNumber.length() - 1));

                String letter = seatNumber.substring(
                        seatNumber.length() - 1);

                int column = letter.charAt(0) - 'A';

                gbc.gridx = column < 3 ? column + 1 : column + 2;
                gbc.gridy = row;

                cabinPanel.add(seatButton, gbc);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load seats: " + e.getMessage());
        }

        cabinPanel.revalidate();
        cabinPanel.repaint();
    }

    // SEAT CATEGORY COLORS
    private Color getSeatColor(String seatType) {

        if (seatType.equalsIgnoreCase("Premium")) {
            return new Color(125, 70, 200);
        }

        if (seatType.equalsIgnoreCase("Exit")) {
            return new Color(220, 65, 85);
        }

        return new Color(40, 175, 100);
    }

    // SELECT / DESELECT SEAT
    private void toggleSeat(String seatNumber, JButton button) {

        if (selectedSeats.contains(seatNumber)) {

            selectedSeats.remove(seatNumber);
            button.setBackground(
                    getSeatColor(seatTypes.get(seatNumber)));

        } else {

            selectedSeats.add(seatNumber);
            button.setBackground(new Color(45, 100, 235));
        }

        updateSelection();
    }

    // UPDATE PRICE
    private void updateSelection() {

        double total = 0;

        for (String seat : selectedSeats) {
            total += seatPrices.get(seat);
        }

        selectedLabel.setText(
                "Selected Seats: " +
                (selectedSeats.isEmpty() ? "None" : selectedSeats));

        priceLabel.setText("Additional Charges: ₹" + total);
    }

    // CONFIRM BOOKING
    private void confirmSelection() {

        if (selectedSeats.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one seat.");

            return;
        }

        double total = selectedSeats.stream()
                .mapToDouble(seat -> seatPrices.get(seat))
                .sum();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Selected Seats: " + selectedSeats +
                "\nAdditional Charges: ₹" + total +
                "\n\nConfirm booking?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        BookingDAO dao = new BookingDAO();

        boolean success = dao.bookSelectedSeats(
                userId,
                flightId,
                new ArrayList<>(selectedSeats)
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seats booked successfully!");

            dispose();

            new MyBookingsFrame(userId);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed! Some seats may already be booked.");

            selectedSeats.clear();

            seatButtons.clear();
            seatPrices.clear();
            seatTypes.clear();

            updateSelection();
            loadSeats();
        }
    }

    // AIRCRAFT BACKGROUND PANEL
    private class AircraftPanel extends JPanel {

        public AircraftPanel() {

            setLayout(new GridBagLayout());

            setPreferredSize(new Dimension(800, 1150));

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Aircraft body
            RoundRectangle2D body = new RoundRectangle2D.Double(
                    w * 0.28, 10,
                    w * 0.44, h - 20,
                    180, 180);

            g2.setColor(new Color(225, 232, 245));
            g2.fill(body);

            g2.setColor(new Color(170, 185, 215));
            g2.setStroke(new BasicStroke(3));
            g2.draw(body);

            // Aircraft nose
            Polygon nose = new Polygon();

            nose.addPoint((int)(w * 0.28), 80);
            nose.addPoint((int)(w * 0.50), 5);
            nose.addPoint((int)(w * 0.72), 80);

            g2.setColor(new Color(205, 215, 235));
            g2.fillPolygon(nose);

            g2.setColor(new Color(170, 185, 215));
            g2.drawPolygon(nose);

            // LEFT WING
            Polygon leftWing = new Polygon();

            leftWing.addPoint((int)(w * 0.28), (int)(h * 0.35));
            leftWing.addPoint((int)(w * 0.04), (int)(h * 0.58));
            leftWing.addPoint((int)(w * 0.08), (int)(h * 0.66));
            leftWing.addPoint((int)(w * 0.28), (int)(h * 0.52));

            g2.setColor(new Color(195, 210, 235));
            g2.fillPolygon(leftWing);

            g2.setColor(new Color(155, 175, 210));
            g2.drawPolygon(leftWing);

            // RIGHT WING
            Polygon rightWing = new Polygon();

            rightWing.addPoint((int)(w * 0.72), (int)(h * 0.35));
            rightWing.addPoint((int)(w * 0.96), (int)(h * 0.58));
            rightWing.addPoint((int)(w * 0.92), (int)(h * 0.66));
            rightWing.addPoint((int)(w * 0.72), (int)(h * 0.52));

            g2.setColor(new Color(195, 210, 235));
            g2.fillPolygon(rightWing);

            g2.setColor(new Color(155, 175, 210));
            g2.drawPolygon(rightWing);

            // TAIL
            Polygon tail = new Polygon();

            tail.addPoint((int)(w * 0.28), (int)(h * 0.88));
            tail.addPoint((int)(w * 0.15), (int)(h * 0.98));
            tail.addPoint((int)(w * 0.42), (int)(h * 0.95));
            tail.addPoint((int)(w * 0.50), (int)(h * 0.91));
            tail.addPoint((int)(w * 0.58), (int)(h * 0.95));
            tail.addPoint((int)(w * 0.85), (int)(h * 0.98));
            tail.addPoint((int)(w * 0.72), (int)(h * 0.88));

            g2.setColor(new Color(195, 210, 235));
            g2.fillPolygon(tail);

            g2.setColor(new Color(155, 175, 210));
            g2.drawPolygon(tail);

            g2.dispose();
        }
    }
}



