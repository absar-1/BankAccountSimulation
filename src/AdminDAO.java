import java.sql.*;
import java.util.ArrayList;

public class AdminDAO {
    // Login and get admin details
    public static String[] loginAndGetAdminDetails(String username, String password) {
        String query = "SELECT * FROM Admins WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt. executeQuery();

            if (rs.next()) {
                return new String[] {
                        rs. getString("id"),
                        rs.getString("adminName"),
                        String.valueOf(rs. getInt("age")),
                        rs.getString("gender"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs. getString("cnic"),
                        rs.getString("contact"),
                        rs.getString("email")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add new admin
    public static boolean addAdmin(String id, String name, int age, String gender,
       String username, String password, String cnic, String contact, String email) {

        String query = "INSERT INTO Admins (id, adminName, age, gender, username, password, cnic, contact, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn. prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt. setInt(3, age);
            stmt.setString(4, gender);
            stmt.setString(5, username);
            stmt.setString(6, password);
            stmt.setString(7, cnic);
            stmt.setString(8, contact);
            stmt.setString(9, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Remove admin
    public static boolean removeAdmin(String id) {
        String query = "DELETE FROM Admins WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all admins (except super admin)
    public static ArrayList<String> getAllAdmins(String superAdminId) {
        ArrayList<String> admins = new ArrayList<>();
        String query = "SELECT * FROM Admins WHERE id != ? ";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn. prepareStatement(query)) {

            stmt.setString(1, superAdminId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String adminData = rs.getString("id") + "," +
                        rs.getString("adminName") + "," +
                        rs.getInt("age") + "," +
                        rs.getString("gender") + "," +
                        rs. getString("username") + "," +
                        rs.getString("cnic") + "," +
                        rs.getString("contact") + "," +
                        rs.getString("email");
                admins. add(adminData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    // Get admin count (for generating new ID)
    public static int getAdminCount() {
        String query = "SELECT COUNT(*) FROM Admins";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs. getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
