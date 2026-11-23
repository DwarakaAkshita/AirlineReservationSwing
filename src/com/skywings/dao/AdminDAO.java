
package com.skywings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.skywings.connection.DBConnection;

public class AdminDAO {

    public boolean validateAdmin(String username, String password) {

        if (username.equals("admin") && password.equals("admin123")) {
            return true;
        }

        return false;
    }

    public boolean addFlight(String airline, String source, String destination,
            String date, String departure, String arrival,
            double price, int seats) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO flights " +
                    "(airline_name, source, destination, flight_date, " +
                    "departure_time, arrival_time, price, total_seats) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, airline);
            ps.setString(2, source);
            ps.setString(3, destination);
            ps.setString(4, date);
            ps.setString(5, departure);
            ps.setString(6, arrival);
            ps.setDouble(7, price);
            ps.setInt(8, seats);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
