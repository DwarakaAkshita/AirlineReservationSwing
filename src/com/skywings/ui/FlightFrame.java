

package com.skywings.ui;

import com.skywings.dao.FlightDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class FlightFrame extends JFrame {

    private int userId;

    private JTextField sourceField;
    private JTextField destinationField;

    private JTable flightTable;
    private DefaultTableModel tableModel;

    public FlightFrame(int userId) {

        this.userId = userId;

        setTitle("SkyWings - Search Flights");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("SEARCH FLIGHTS", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(25, 55, 100));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        add(title, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20));

        sourceField = new JTextField();
        destinationField = new JTextField();

        JButton searchButton = new JButton("Search Flights");

        searchPanel.add(new JLabel("Source"));
        searchPanel.add(new JLabel("Destination"));
        searchPanel.add(new JLabel(""));

        searchPanel.add(sourceField);
        searchPanel.add(destinationField);
        searchPanel.add(searchButton);

        // Flight Table
        tableModel = new DefaultTableModel(
                new String[]{
                        "Flight ID",
                        "Airline",
                        "Source",
                        "Destination",
                        "Date",
                        "Departure",
                        "Arrival",
                        "Price"
                }, 0
        );

        flightTable = new JTable(tableModel);

        flightTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(flightTable);

        // Bottom Buttons
        JPanel bottomPanel = new JPanel();

        JButton selectButton = new JButton("Select Flight");
        JButton backButton = new JButton("Back");

        bottomPanel.add(selectButton);
        bottomPanel.add(backButton);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        // CORRECTED LAYOUT
        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Search Action
        searchButton.addActionListener(e -> searchFlights());

        // Select Flight Action
        selectButton.addActionListener(e -> selectFlight());

        // Back Action
        backButton.addActionListener(e -> {
            new MainFrame(userId);
            dispose();
        });

        setVisible(true);
    }

    private void searchFlights() {

        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        if (source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter source and destination.");
            return;
        }

        tableModel.setRowCount(0);

        try {

            FlightDAO dao = new FlightDAO();

            ResultSet rs = dao.searchFlights(source, destination);

            if (rs == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to retrieve flights.");
                return;
            }

            while (rs.next()) {

                tableModel.addRow(new Object[]{
                        rs.getInt("flight_id"),
                        rs.getString("airline_name"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getDate("flight_date"),
                        rs.getTime("departure_time"),
                        rs.getTime("arrival_time"),
                        rs.getDouble("price")
                });
            }

            rs.close();

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No flights found.");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error searching flights: " + e.getMessage());
        }
    }

    private void selectFlight() {

        int selectedRow = flightTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a flight.");
            return;
        }

        int flightId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString());

        new SeatSelectionFrame(userId, flightId);

        dispose();
    }
}
