

package com.skywings.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

import com.skywings.dao.BookingDAO;

public class MyBookingsFrame extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public MyBookingsFrame(int userId) {

        this.userId = userId;

        setTitle("SkyWings - My Bookings");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color navy = new Color(15, 35, 75);
        Color blue = new Color(30, 100, 200);
        Color background = new Color(240, 244, 250);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(background);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navy);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel logo = new JLabel("SKYWINGS AIRLINES");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        header.add(logo, BorderLayout.WEST);
        mainPanel.add(header, BorderLayout.NORTH);

        // Heading
        JLabel heading = new JLabel("MY BOOKINGS");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(navy);

        // Updated Table
        model = new DefaultTableModel(
            new String[]{
                "Booking ID",
                "Flight ID",
                "Seats",
                "Seat Numbers",
                "Booking Date"
            }, 0
        );

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setForeground(navy);
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setSelectionForeground(navy);
        table.setGridColor(new Color(220, 230, 245));

        table.getTableHeader().setBackground(navy);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.setBackground(background);
        centerPanel.add(heading, BorderLayout.NORTH);
        centerPanel.add(tableCard, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(background);

        JButton refreshButton = new JButton("Refresh");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton backButton = new JButton("Back to Home");

        styleButton(refreshButton, blue);
        styleButton(cancelButton, navy);
        styleButton(backButton, new Color(80, 100, 130));

        bottomPanel.add(refreshButton);
        bottomPanel.add(cancelButton);
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadBookings();

        refreshButton.addActionListener(e -> loadBookings());
        cancelButton.addActionListener(e -> cancelBooking());

        backButton.addActionListener(e -> {
            dispose();
            new MainFrame(userId);
        });

        setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 42));
    }

    // Load bookings with actual seat numbers
    private void loadBookings() {

        model.setRowCount(0);

        BookingDAO dao = new BookingDAO();
        ResultSet rs = dao.getBookings(userId);

        try {

            if (rs != null) {

                while (rs.next()) {

                    int bookingId = rs.getInt("booking_id");
                    int flightId = rs.getInt("flight_id");
                    int seats = rs.getInt("seats");

                    String seatNumbers = getSeatNumbers(bookingId);

                    model.addRow(new Object[]{
                        bookingId,
                        flightId,
                        seats,
                        seatNumbers,
                        rs.getTimestamp("booking_date")
                    });
                }

                rs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get booked seat numbers
    private String getSeatNumbers(int bookingId) {

        StringBuilder seats = new StringBuilder();

        String sql =
            "SELECT fs.seat_number "
            + "FROM booking_seats bs "
            + "JOIN flight_seats fs ON bs.seat_id = fs.seat_id "
            + "WHERE bs.booking_id = ? "
            + "ORDER BY fs.seat_id";

        try {

            java.sql.Connection con =
                com.skywings.connection.DBConnection.getConnection();

            java.sql.PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                if (seats.length() > 0) {
                    seats.append(", ");
                }

                seats.append(rs.getString("seat_number"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (seats.length() == 0) {
            return "Not assigned";
        }

        return seats.toString();
    }

    // Cancel booking
    private void cancelBooking() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(this,
                "Please select a booking first!");

            return;
        }

        int bookingId = Integer.parseInt(
            model.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this booking?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            BookingDAO dao = new BookingDAO();

            if (dao.cancelBooking(bookingId)) {

                JOptionPane.showMessageDialog(this,
                    "Booking cancelled successfully!");

                loadBookings();

            } else {

                JOptionPane.showMessageDialog(this,
                    "Cancellation failed!");
            }
        }
    }
}
