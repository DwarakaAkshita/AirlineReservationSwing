

package com.skywings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.skywings.connection.DBConnection;

public class BookingDAO {

    // EXISTING: Book flights using number of seats
    public boolean bookFlight(int userId, int flightId, int seats) {

        if (seats <= 0) {
            return false;
        }

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String flightSQL =
                "SELECT total_seats FROM flights WHERE flight_id=? FOR UPDATE";

            int totalSeats;

            try (PreparedStatement ps = con.prepareStatement(flightSQL)) {
                ps.setInt(1, flightId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    totalSeats = rs.getInt("total_seats");
                }
            }

            String bookedSQL =
                "SELECT COALESCE(SUM(seats),0) AS booked "
                + "FROM bookings WHERE flight_id=?";

            int bookedSeats;

            try (PreparedStatement ps = con.prepareStatement(bookedSQL)) {
                ps.setInt(1, flightId);

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    bookedSeats = rs.getInt("booked");
                }
            }

            if (seats > totalSeats - bookedSeats) {
                con.rollback();
                return false;
            }

            String insertSQL =
                "INSERT INTO bookings(user_id,flight_id,seats) "
                + "VALUES(?,?,?)";

            try (PreparedStatement ps = con.prepareStatement(insertSQL)) {
                ps.setInt(1, userId);
                ps.setInt(2, flightId);
                ps.setInt(3, seats);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // NEW: Book specific selected seats
    public boolean bookSelectedSeats(
            int userId, int flightId, List<String> selectedSeats) {

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return false;
        }

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Lock and verify selected seats
            String checkSQL =
                "SELECT seat_id, is_booked FROM flight_seats "
                + "WHERE flight_id=? AND seat_number=? FOR UPDATE";

            java.util.Map<String, Integer> seatIds =
                new java.util.HashMap<>();

            try (PreparedStatement ps = con.prepareStatement(checkSQL)) {

                for (String seat : selectedSeats) {

                    ps.setInt(1, flightId);
                    ps.setString(2, seat);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next() || rs.getBoolean("is_booked")) {
                            con.rollback();
                            return false;
                        }

                        seatIds.put(seat, rs.getInt("seat_id"));
                    }
                }
            }

            // Create booking record
            String bookingSQL =
                "INSERT INTO bookings(user_id,flight_id,seats) VALUES(?,?,?)";

            int bookingId;

            try (PreparedStatement ps = con.prepareStatement(
                    bookingSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, userId);
                ps.setInt(2, flightId);
                ps.setInt(3, selectedSeats.size());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    bookingId = rs.getInt(1);
                }
            }

            // Mark seats as booked
            String updateSQL =
                "UPDATE flight_seats SET is_booked=TRUE "
                + "WHERE seat_id=? AND is_booked=FALSE";

            // Connect booking with its seats
            String mappingSQL =
                "INSERT INTO booking_seats(booking_id,seat_id) VALUES(?,?)";

            try (PreparedStatement updatePS =
                    con.prepareStatement(updateSQL);
                 PreparedStatement mappingPS =
                    con.prepareStatement(mappingSQL)) {

                for (String seat : selectedSeats) {

                    int seatId = seatIds.get(seat);

                    updatePS.setInt(1, seatId);

                    if (updatePS.executeUpdate() != 1) {
                        con.rollback();
                        return false;
                    }

                    mappingPS.setInt(1, bookingId);
                    mappingPS.setInt(2, seatId);
                    mappingPS.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {

            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // EXISTING: Get user's bookings
    public ResultSet getBookings(int userId) {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM bookings WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATED: Cancel booking and release its seats
    public boolean cancelBooking(int bookingId) {

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String releaseSQL =
                "UPDATE flight_seats SET is_booked=FALSE "
                + "WHERE seat_id IN "
                + "(SELECT seat_id FROM booking_seats WHERE booking_id=?)";

            try (PreparedStatement ps = con.prepareStatement(releaseSQL)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            String deleteSQL = "DELETE FROM bookings WHERE booking_id=?";

            try (PreparedStatement ps = con.prepareStatement(deleteSQL)) {
                ps.setInt(1, bookingId);

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {

            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
