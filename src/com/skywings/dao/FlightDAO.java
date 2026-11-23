
package com.skywings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.skywings.connection.DBConnection;

public class FlightDAO {

    public ResultSet searchFlights(String source, String destination) {

        String sql = "SELECT * FROM flights "
                   + "WHERE LOWER(TRIM(source)) = LOWER(TRIM(?)) "
                   + "AND LOWER(TRIM(destination)) = LOWER(TRIM(?))";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, source);
            ps.setString(2, destination);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
