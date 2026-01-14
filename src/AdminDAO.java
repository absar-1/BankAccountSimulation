import java.io.*;
import java.sql.*;
import java.util.ArrayList;
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
    public static boolean addIndAcc(String name, String age, String gender, String accType, String address, String contact, String cnic, String email) {

        String countQuery = "SELECT COUNT(*) FROM Accounts";
        String countUserNameQuery = "SELECT COUNT(*) FROM Accounts WHERE username LIKE ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(countQuery);
                PreparedStatement stmt2 = conn.prepareStatement(countUserNameQuery)
        ) {

            stmt2.setString(1, (name.toLowerCase().replace(" ", "")) + "%");

            ResultSet rs = stmt.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();

            if (rs.next() && rs2.next()) {

                int users = rs2.getInt(1);
                int count = rs.getInt(1);

                String id = "HBL(" + accType.substring(0, 1) + "A)-000" + (++count);

                String accountNumber = "011102220333" + String.format("%04d", count);

                String username = name.toLowerCase().replace(" ", "") + (++users);

                int password = new Random().nextInt(9000) + 1000;

                String insertCustomerQuery = "INSERT INTO Customers VALUES (?,?,?,?,?,?,?)";
                PreparedStatement stmt3 = conn.prepareStatement(insertCustomerQuery);

                stmt3.setString(1, name);
                stmt3.setString(2, age);
                stmt3.setString(3, gender);
                stmt3.setString(4, address);
                stmt3.setString(5, contact);
                stmt3.setString(6, cnic);
                stmt3.setString(7, email);

                ResultSet rs1=stmt3.executeQuery();
                if(rs1.next()){
                    String insertAccountQuery = "INSERT INTO Accounts VALUES (?,?,?,?,?,?,?)";
                    PreparedStatement stmt4 = conn.prepareStatement(insertAccountQuery);

                    stmt4.setString(1, id);
                    stmt4.setString(2, "0");
                    stmt4.setString(3, username);
                    stmt4.setString(4, String.valueOf(password));
                    stmt4.setString(5, accountNumber);
                    stmt4.setString(6, accType);
                    stmt4.setString(7, name.toUpperCase());

                    int res = stmt4.executeUpdate();

                    String insertCustomerAccountQuery = "INSERT INTO CustomerAccounts VALUES (?,?)";
                    PreparedStatement stmt5 = conn.prepareStatement(insertCustomerAccountQuery);

                    stmt5.setString(1, rs1.getString("customer_id"));
                    stmt5.setString(2, id);

                    int res2 = stmt5.executeUpdate();

                    return (res > 0 && res2 > 0);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addJointAcc(String name1, String age1, String gender1, String address1, String contact1, String cnic1, String email1,String name2, String age2, String gender2, String address2, String contact2, String cnic2, String email2,String accType,String accTitle){
        String insertCustomer1="INSERT INTO Customers VALUES (?,?,?,?,?,?,?)";
        String insertCustomer2="INSERT INTO Customers VALUES (?,?,?,?,?,?,?)";
        String insertAccount="INSERT INTO Accounts VALUES (?,?,?,?,?,?,?)";
        String countUserNameQuery = "SELECT COUNT(*) FROM Accounts WHERE username LIKE ?";
        String insertCustomerAccount1="INSERT INTO CustomerAccounts VALUES (?,?)";
        String insertCustomerAccount2="INSERT INTO CustomerAccounts VALUES (?,?)";
        String countQuery = "SELECT COUNT(*) FROM Accounts";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt1 = conn.prepareStatement(countQuery);
                PreparedStatement stmt2=conn.prepareStatement(countUserNameQuery);
        ) {
            stmt2.setString(1, (name1.toLowerCase().split(" ")[0] + "&" + name2.toLowerCase().split(" ")[0]) + "%");

            ResultSet rs = stmt1.executeQuery();
            ResultSet rs2=stmt2.executeQuery();
            if (rs.next() && rs2.next()) {

                int count = rs.getInt(1);
                int users = rs2.getInt(1);

                String accountId = "HBL(JA)-000" + (++count);
                String accountNumber = "011102220333" + String.format("%04d", count);
                String username= name1.toLowerCase().split(" ")[0] + "&" + name2.toLowerCase().split(" ")[0] + (++users);
                int password = new Random().nextInt(9000) + 1000;

                PreparedStatement stmt3 = conn.prepareStatement(insertCustomer1);
                stmt3.setString(1, name1);
                stmt3.setString(2, age1);
                stmt3.setString(3, gender1);
                stmt3.setString(4, address1);
                stmt3.setString(5, contact1);
                stmt3.setString(6, cnic1);
                stmt3.setString(7, email1);
                ResultSet rs3=stmt3.executeQuery();

                PreparedStatement stmt4 = conn.prepareStatement(insertCustomer2);
                stmt4.setString(1, name2);
                stmt4.setString(2, age2);
                stmt4.setString(3, gender2);
                stmt4.setString(4, address2);
                stmt4.setString(5, contact2);
                stmt4.setString(6, cnic2);
                stmt4.setString(7, email2);
                ResultSet rs4=stmt4.executeQuery();

                if(rs3.next() && rs4.next()){
                    PreparedStatement stmt5=conn.prepareStatement(insertAccount);
                    stmt5.setString(1, accountId);
                    stmt5.setString(2, "0");
                    stmt5.setString(3, username);
                    stmt5.setString(4, String.valueOf(password));
                    stmt5.setString(5, accountNumber);
                    stmt5.setString(6,accType);
                    stmt5.setString(7,accTitle);
                    int res=stmt5.executeUpdate();

                    PreparedStatement stmt6=conn.prepareStatement(insertCustomerAccount1);
                    stmt6.setString(1, rs3.getString("customer_id"));
                    stmt6.setString(2, accountId);
                    int res2=stmt6.executeUpdate();

                    PreparedStatement stmt7=conn.prepareStatement(insertCustomerAccount2);
                    stmt7.setString(1, rs4.getString("customer_id"));
                    stmt7.setString(2, accountId);
                    int res3=stmt7.executeUpdate();

                    return (res>0 && res2>0 && res3>0);
                }
            }
        }
        catch (SQLException e) {
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

    public static boolean removeAccount(String ID) throws IOException {
        String query2="DELETE FROM CustomerAccounts WHERE id=?";
        String query="DELETE FROM Accounts WHERE id=?";
        try(
                Connection conn=DBConnection.getConnection();
                PreparedStatement stmt=conn.prepareStatement(query);
                PreparedStatement stmt2=conn.prepareStatement(query2);
        ){
            stmt.setString(1, ID);
            stmt2.setString(1, ID);


            return stmt.executeUpdate() > 0 && stmt2.executeUpdate() > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
