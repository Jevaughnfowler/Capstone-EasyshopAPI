package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlProfileDao implements ProfileDao {

    private final DataSource dataSource;

    public MySqlProfileDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, email, phone, address, city, state, zip) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, profile.getUserId());
            stmt.setString(2, profile.getFirstName());
            stmt.setString(3, profile.getLastName());
            stmt.setString(4, profile.getEmail());
            stmt.setString(5, profile.getPhone());
            stmt.setString(6, profile.getAddress());
            stmt.setString(7, profile.getCity());
            stmt.setString(8, profile.getState());
            stmt.setString(9, profile.getZip());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create profile", e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profile profile = new Profile();
                    profile.setUserId(rs.getInt("user_id"));
                    profile.setFirstName(rs.getString("first_name"));
                    profile.setLastName(rs.getString("last_name"));
                    profile.setEmail(rs.getString("email"));
                    profile.setPhone(rs.getString("phone"));
                    profile.setAddress(rs.getString("address"));
                    profile.setCity(rs.getString("city"));
                    profile.setState(rs.getString("state"));
                    profile.setZip(rs.getString("zip"));
                    return profile;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reading profile", e);
        }
    }

    @Override
    public void update(Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, city = ?, state = ?, zip = ? WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profile.getFirstName());
            stmt.setString(2, profile.getLastName());
            stmt.setString(3, profile.getEmail());
            stmt.setString(4, profile.getPhone());
            stmt.setString(5, profile.getAddress());
            stmt.setString(6, profile.getCity());
            stmt.setString(7, profile.getState());
            stmt.setString(8, profile.getZip());
            stmt.setInt(9, profile.getUserId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile", e);
        }
    }

    @Override
    public void delete(int userId) {
        String sql = "DELETE FROM profiles WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete profile", e);
        }
    }

}
