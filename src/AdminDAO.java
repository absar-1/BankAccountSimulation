import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

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
                        rs.getString("admin_name"),
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

    // Add bank
    public static boolean addBank(String bankName){
        String query="SELECT * FROM Banks WHERE bank_name=?";
        try(
                Connection conn=DBConnection.getConnection();
                PreparedStatement stmt= conn.prepareStatement(query);)
        {
            stmt.setString(1, bankName);
            ResultSet rs=stmt.executeQuery();

            if(rs.next()){
                return false;
            }
            else{
                String addQuery="INSERT INTO Banks(bank_name) VALUES (?)";
                PreparedStatement stmt2= conn.prepareStatement(addQuery);
                stmt2.setString(1, bankName);
                return stmt2.executeUpdate()>0;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Remove Bank
    public static boolean removeBank(String bankName){
        String query="SELECT * FROM Banks WHERE bank_name=?";
        try(
                Connection conn= DBConnection.getConnection();
                PreparedStatement stmt= conn.prepareStatement(query);){
            stmt.setString(1, bankName);
            ResultSet rs=stmt.executeQuery();

            if(!rs.next()){
                return false;
            }
            else{
                String deleteQuery="DELETE FROM Banks WHERE bank_name=?";
                try(PreparedStatement stmt2= conn.prepareStatement(deleteQuery);){
                    stmt2.setString(1, bankName);
                    return stmt2.executeUpdate()>0;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add Account Holder (Open new account)
    public static boolean addAccountHolder(String name, String age, String gender, String accType, String address, String contact, String cnic, String email) {

        String countQuery = "SELECT COUNT(*) FROM AccountHolders";
        String countUserNameQuery = "SELECT COUNT(*) FROM AccountHolders WHERE account_holder_name = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(countQuery);
                PreparedStatement stmt2 = conn.prepareStatement(countUserNameQuery)
        ) {

            stmt2.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();

            if (rs.next() && rs2.next()) {

                int users = rs2.getInt(1);
                int count = rs.getInt(1);

                String id = "HBL(" + accType.substring(0, 1) + "A)-000" + (++count);

                String accountNumber = "011102220333" + String.format("%04d", count);

                String username = name.toLowerCase().replace(" ", "") + (++users);

                int password = new Random().nextInt(9000) + 1000;

                String insertQuery = "INSERT INTO AccountHolders VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmt3 = conn.prepareStatement(insertQuery);

                stmt3.setString(1, id);
                stmt3.setString(2, name);
                stmt3.setString(3, "0");
                stmt3.setString(4, age);
                stmt3.setString(5, gender);
                stmt3.setString(6, username);
                stmt3.setString(7, String.valueOf(password));
                stmt3.setString(8, accountNumber);
                stmt3.setString(9, accType);
                stmt3.setString(10, address);
                stmt3.setString(11, contact);
                stmt3.setString(12, cnic);
                stmt3.setString(13, email);

                return stmt3.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Add Admin
    public static boolean addAdmin(String name, String cnic, String contact, String age, String gender,String password,String email){
        String countQuery = "SELECT COUNT(*) FROM Admins";

        try(
            Connection conn= DBConnection.getConnection();
            PreparedStatement stmt=conn.prepareStatement(countQuery);
                ){
            ResultSet rs=stmt.executeQuery();
            if(rs.next()) {
                int assignId = rs.getInt(1);
                String id = "HBL(Admin)-000" + (++assignId);
                String countNameQuery = "SELECT COUNT(*) FROM Admins WHERE admin_name = ?";
                PreparedStatement stmt3 = conn.prepareStatement(countNameQuery);
                stmt3.setString(1, name);
                ResultSet rs3 = stmt3.executeQuery();
                if(rs3.next()) {
                    String username = name.replace(" ", "").toLowerCase() + (rs3.getInt(1) + 1);
                    String insertQuery = "INSERT INTO Admins VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt2 = conn.prepareStatement(insertQuery);
                    stmt2.setString(1, id);
                    stmt2.setString(2, name);
                    stmt2.setString(3, age);
                    stmt2.setString(4, gender);
                    stmt2.setString(5, username);
                    stmt2.setString(6, password);
                    stmt2.setString(7, cnic);
                    stmt2.setString(8, contact);
                    stmt2.setString(9, email);

                    return stmt2.executeUpdate() > 0;
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all admins
    public static ArrayList<String> getAllAdmins(){
        ArrayList<String> allAdmins=new ArrayList<>();
        String query="SELECT * FROM Admins";
        try(
                Connection conn=DBConnection.getConnection();
                PreparedStatement stmt=conn.prepareStatement(query);
                ){
            ResultSet rs=stmt.executeQuery();
            while(rs.next()){
                allAdmins.add(rs.getString("id")+","+rs.getString("admin_name")+","+rs.getString("age")+","+rs.getString("gender")+","+rs.getString("username")+","+rs.getString("cnic")+","+rs.getString("contact")+","+rs.getString("email"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return allAdmins;
    }

    // Remove Admin
    public static boolean removeAdmin(String ID){
        String query="DELETE FROM Admins WHERE id=?";
        try(
            Connection conn=DBConnection.getConnection();
            PreparedStatement stmt=conn.prepareStatement(query);
                ){
            stmt.setString(1, ID);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
